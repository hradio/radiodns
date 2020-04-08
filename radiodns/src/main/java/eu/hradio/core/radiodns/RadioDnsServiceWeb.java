package eu.hradio.core.radiodns;

import android.support.annotation.NonNull;
import android.util.Log;

import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static eu.hradio.core.radiodns.BuildConfig.DEBUG;

public class RadioDnsServiceWeb extends RadioDnsService {

	private final static String TAG = "RadioDnsServiceWeb";

	private final String mAilUrl;
	private RadioWebApplicationInformationList mAIL = null;
	private volatile boolean mAppParserRunning = false;
	private ExecutorService mCbExe;
	private ConcurrentLinkedQueue<RadioDnsServiceWebCallback> mCallbacks = new ConcurrentLinkedQueue<>();

	RadioDnsServiceWeb(SRV srvRecord, String rdnsSrvId, String bearerUri, RadioDnsServiceType srvType, RadioService lookupSrv) {
		super(srvRecord, rdnsSrvId, bearerUri, srvType, lookupSrv);

		mCbExe = Executors.newFixedThreadPool(10);

		mAilUrl = createRwebUrl();
	}

	public void getApplicationInformationList(@NonNull RadioDnsServiceWebCallback callback) {
		mCallbacks.offer(callback);

		if(mAIL == null) {
			if(!mAppParserRunning) {
				mAppParserRunning = true;

				Thread appParserThread = new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							if(BuildConfig.DEBUG)Log.d(TAG, "Starting ParserThread");

							RadioWebApplicationParser parser = new RadioWebApplicationParser();
							mAIL = parser.parse(getConnection(mAilUrl).getInputStream());

							callCallbacks();
						} catch(IOException ioE) {

						} finally {
							callCallbacks();

							mAppParserRunning = false;
						}
					}
				});

				appParserThread.start();
			}
		} else {
			callCallbacks();
		}
	}

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

	private void callCallbacks() {
		if(mCallbacks.size() > 0) {
			final Object[] cbs = mCallbacks.toArray();
			for(final Object cb : cbs) {
				mCbExe.execute(new Runnable() {
					@Override
					public void run() {
						((RadioDnsServiceWebCallback)cb).applicationInformationListRetrieved(mAIL, getRadioService());
					}
				});
			}
			mCallbacks.clear();
		}
	}

	private String createRwebUrl() {
		StringBuilder ailUrlBuilder = new StringBuilder();

		ailUrlBuilder.append("http://").append(this.getTarget());

		if(this.getPort() != 80) {
			ailUrlBuilder.append(":").append(Integer.toString(this.getPort()));
		}

		ailUrlBuilder.append("/radiodns/web/");
		ailUrlBuilder.append(this.getServiceIdentifier());
		ailUrlBuilder.append("/AIL.xml");


		if(DEBUG) Log.d(TAG, "AILUrl: " + ailUrlBuilder.toString());

		return ailUrlBuilder.toString();
	}
}
