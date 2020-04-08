package eu.hradio.core.radiodns;

import android.support.annotation.NonNull;
import android.util.Log;

import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static eu.hradio.core.radiodns.BuildConfig.DEBUG;

public class RadioDnsServiceEpg extends RadioDnsService {

	private final static String TAG = "RadioDnsServiceEpg";

	private final String mSiUrl;

	private volatile boolean mSiParserRunning = false;

	private ExecutorService mCbExe;
	private ConcurrentLinkedQueue<RadioDnsServiceEpgSiCallback> mSiCallbacks = new ConcurrentLinkedQueue<>();
	//private ConcurrentHashMap<String, ArrayList<RadioDnsServiceEpgPiCallback>> mPiCallbacks = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, CopyOnWriteArrayList<RadioDnsServiceEpgPiCallback>> mPiCallbacks = new ConcurrentHashMap<>();
	private ConcurrentHashMap<String, Boolean> mPiThreadsRunning = new ConcurrentHashMap<>();

	private RadioEpgServiceInformation mSi = null;
	private HashMap<String, RadioEpgProgrammeInformation> mPiMap = new HashMap<>();

	RadioDnsServiceEpg(SRV srvRecord, String rdnsSrvId, String bearerUri, RadioDnsServiceType srvType, RadioService lookupSrv) {
		super(srvRecord, rdnsSrvId, bearerUri, srvType, lookupSrv);

		mCbExe = Executors.newFixedThreadPool(10);

		mSiUrl = creatEpgSiUrl();
	}

	/**
	 * Retrieves and parses Service Information (SI)
	 * @param cb the {@link RadioDnsServiceEpgSiCallback} to be called after retrieval
	 */
	public void getServiceInformation(@NonNull RadioDnsServiceEpgSiCallback cb) {
		mSiCallbacks.offer(cb);

		if(mSi == null) {
			if(!mSiParserRunning) {
				mSiParserRunning = true;

				Thread piParserThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if(BuildConfig.DEBUG)Log.d(TAG, "Starting SI ParserThread");

							RadioEpgSiParser parser = new RadioEpgSiParser();
							mSi = parser.parse(getConnection(mSiUrl).getInputStream());

							callSiCallbacks(RadioDnsServiceEpg.this);
						} catch(IOException ioE) {
							if(BuildConfig.DEBUG)ioE.printStackTrace();
						} finally {
							callSiCallbacks(RadioDnsServiceEpg.this);
							mSiParserRunning = false;
						}
					}
				});

				piParserThread.start();
			}
		} else {
			if(BuildConfig.DEBUG)Log.d(TAG, "SI file already cached");
			callSiCallbacks(RadioDnsServiceEpg.this);
		}
	}

	//TODO handle a maximum of redirects to avoid endless redirects
	private HttpURLConnection getConnection(String connUrl) throws IOException {
		URL url = new URL(connUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setReadTimeout(10000 /* milliseconds */);
		conn.setConnectTimeout(15000 /* milliseconds */);
		conn.setRequestMethod("GET");
		conn.setDoInput(true);
		conn.connect();

		int httpResponseCode = conn.getResponseCode();
		if(BuildConfig.DEBUG)Log.d(TAG, "GetConnection HTTP responseCode: " + httpResponseCode);
		if(httpResponseCode == HttpURLConnection.HTTP_MOVED_PERM || httpResponseCode == HttpURLConnection.HTTP_MOVED_TEMP) {
			String redirectUrl = conn.getHeaderField("Location");
			if(redirectUrl != null && !redirectUrl.isEmpty()) {
				if(BuildConfig.DEBUG)Log.d(TAG, "GetConnection Following redirect to: " + redirectUrl);
				conn.disconnect();

				return getConnection(redirectUrl);
			}
		}

		return conn;
	}

	/**
	 * Retrieves and parses todays programme information
	 * @param cb the callback to be called after finishing parsing
	 */
	public void getProgrammeInformation(@NonNull RadioDnsServiceEpgPiCallback cb) {
		getProgrammeInformation(0, cb);
	}

	/**
	 * Retrieves and parses programme information for a relative day offset from today
	 * @param dayOffset the relative from today, e.g. 0 for today, -1 for yesterday, 1 for tomorrow
	 * @param cb
	 */
	public void getProgrammeInformation(int dayOffset, @NonNull final RadioDnsServiceEpgPiCallback cb) {
		final String dateString = createPiDate(dayOffset);
		final String piUrl = creatEpgPiUrl(dateString);

		if(BuildConfig.DEBUG)Log.d(TAG, "PI_URL: " + piUrl);

		if(!mPiCallbacks.containsKey(dateString)) {
			//mPiCallbacks.put(dateString, new ArrayList<RadioDnsServiceEpgPiCallback>(){{add(cb);}});
			mPiCallbacks.put(dateString, new CopyOnWriteArrayList<RadioDnsServiceEpgPiCallback>(){{add(cb);}});
		} else {
			//ArrayList<RadioDnsServiceEpgPiCallback> cbs = mPiCallbacks.get(dateString);
			CopyOnWriteArrayList<RadioDnsServiceEpgPiCallback> cbs = mPiCallbacks.get(dateString);
			if(cbs != null) {
				cbs.add(cb);
			}
		}

		if(!mPiMap.containsKey(dateString)) {
			Boolean running = mPiThreadsRunning.get(dateString);
			if(running == null || !running) {
				mPiThreadsRunning.put(dateString, true);

				Thread piParserThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if(BuildConfig.DEBUG)Log.d(TAG, "Starting ParserThread");

							RadioEpgPiParser parser = new RadioEpgPiParser();
							mPiMap.put(dateString, parser.parse(getConnection(piUrl).getInputStream()));

							callPiCallbacks(dateString, RadioDnsServiceEpg.this);
						} catch(IOException ioE) {
							if(BuildConfig.DEBUG)ioE.printStackTrace();
						} finally {
							callPiCallbacks(dateString, RadioDnsServiceEpg.this);

							mPiThreadsRunning.put(dateString, false);
						}
					}
				});

				piParserThread.start();
			}
		} else {
			if(BuildConfig.DEBUG)Log.d(TAG, "PI file already cached");
			callPiCallbacks(dateString, this);
		}
	}

	private void callSiCallbacks(final RadioDnsServiceEpg rdnsService) {
		if(mSiCallbacks.size() > 0) {
			final Object[] cbs = mSiCallbacks.toArray();
			for(final Object cb : cbs) {
				if(cb != null) {
					mCbExe.execute(new Runnable() {
						@Override
						public void run() {
							((RadioDnsServiceEpgSiCallback) cb).serviceInformationRetrieved(mSi, rdnsService);
						}
					});
				}
			}
			mSiCallbacks.clear();
		}
	}

	private void callPiCallbacks(final String date, final RadioDnsServiceEpg rdnsService) {
		if(mPiCallbacks.containsKey(date)) {
			synchronized (this) {
				//ArrayList<RadioDnsServiceEpgPiCallback> cbs = mPiCallbacks.get(date);
				CopyOnWriteArrayList<RadioDnsServiceEpgPiCallback> cbs = mPiCallbacks.get(date);
				if (cbs != null) {
					for (final RadioDnsServiceEpgPiCallback cb : cbs) {
						if (cb != null) {
							mCbExe.execute(new Runnable() {
								@Override
								public void run() {
									cb.programmeInformationRetrieved(mPiMap.get(date), rdnsService);
								}
							});
						}
					}
				}
				mPiCallbacks.remove(date);
			}
		}
	}

	private String creatEpgSiUrl() {
		StringBuilder silUrlBuilder = new StringBuilder();

		silUrlBuilder.append("http://").append(this.getTarget());

		if(this.getPort() != 80) {
			silUrlBuilder.append(":").append(Integer.toString(this.getPort()));
		}

		silUrlBuilder.append("/radiodns/spi/3.1/SI.xml");

		if(DEBUG) Log.d(TAG, "SiUrl: " + silUrlBuilder.toString());

		return silUrlBuilder.toString();
	}

	private String creatEpgPiUrl(String dateString) {
		StringBuilder pilUrlBuilder = new StringBuilder();

		pilUrlBuilder.append("http://").append(this.getTarget());

		if(this.getPort() != 80) {
			pilUrlBuilder.append(":").append(Integer.toString(this.getPort()));
		}

		pilUrlBuilder.append("/radiodns/spi/3.1/").append(this.getServiceIdentifier()).append("/");
		//pilUrlBuilder.append("/").append(this.getServiceIdentifier()).append("/");
		pilUrlBuilder.append(dateString);
		pilUrlBuilder.append("_PI.xml");

		return pilUrlBuilder.toString();
	}

	private String createPiDate(int dayOffset) {
		Calendar cal = Calendar.getInstance();

		int year = cal.get(Calendar.YEAR);
		int month = cal.get(Calendar.MONTH);
		int day = cal.get(Calendar.DAY_OF_MONTH);
		cal.set(year, month, day + (dayOffset));

		DateFormat df = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
		Date dateOffset = new Date(cal.getTimeInMillis());
		return df.format(dateOffset);
	}
}
