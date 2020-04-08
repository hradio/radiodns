package eu.hradio.core.radiodns;

import android.support.annotation.NonNull;
import android.util.Log;

import net.ser1.stomp.Client;
import net.ser1.stomp.Listener;

import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;
import org.omri.radioservice.RadioServiceListener;
import org.omri.radioservice.metadata.Textual;
import org.omri.radioservice.metadata.TextualMetadataListener;
import org.omri.radioservice.metadata.TextualType;
import org.omri.radioservice.metadata.VisualIpRdnsRadioVis;
import org.omri.radioservice.metadata.VisualMetadataListener;
import org.omri.radioservice.metadata.VisualMimeType;
import org.omri.radioservice.metadata.VisualType;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RadioDnsServiceVis extends RadioDnsService {

	private final static String TAG = "RadioDnsServiceVis";

	private ExecutorService mCbExe;
	private CopyOnWriteArrayList<VisualMetadataListener> mVisualSubscibers = new CopyOnWriteArrayList<>();
	private CopyOnWriteArrayList<TextualMetadataListener> mTextualSubscibers = new CopyOnWriteArrayList<>();

	private Client mStompClient;

	private final String mImageTopicString;
	private final String mTextTopicString;

	private Timer mConnectedTimer = null;

	RadioDnsServiceVis(SRV srvRecord, String rdnsSrvId, String bearerUri, RadioDnsServiceType srvType, RadioService lookupSrv) {
		super(srvRecord, rdnsSrvId, bearerUri, srvType, lookupSrv);

		mCbExe = Executors.newFixedThreadPool(10);

		mImageTopicString = createImageTopic();
		mTextTopicString = createTextTopic();
	}

	public void subscribe(@NonNull RadioServiceListener callback) {
		if(callback instanceof TextualMetadataListener) {
			mTextualSubscibers.add((TextualMetadataListener)callback);
		}
		if(callback instanceof VisualMetadataListener) {
			mVisualSubscibers.add((VisualMetadataListener)callback);
		}

		if(!mVisualSubscibers.isEmpty() && !mTextualSubscibers.isEmpty()) {
			if (mStompClient == null) {
				connectStompClient();

				if (mConnectedTimer == null) {
					mConnectedTimer = new Timer();
					mConnectedTimer.scheduleAtFixedRate(new TimerTask() {
						@Override
						public void run() {
							if (!mStompClient.isConnected()) {
								if(BuildConfig.DEBUG)Log.w(TAG, "STOMP client seems to be disconnected reconnecting...");
								mStompClient = null;
								connectStompClient();
							} else {
								if(BuildConfig.DEBUG)Log.d(TAG, "STOMP client isConnected: " + mStompClient.isConnected() + ", isClosed: " + mStompClient.isClosed());
							}
						}
					}, 0, 1000);
				}
			}
		}
	}

	private void connectStompClient() {
		if(mStompClient == null) {
			try {
				if(BuildConfig.DEBUG)Log.d(TAG, "Connecting STOMP client to: " + this.getTarget() + " at port: " + this.getPort());
				mStompClient = new Client(this.getTarget(), this.getPort(), "", "");

				mStompClient.addErrorListener(mStompErrorListener);

				if(mStompClient.isConnected()) {
					if(BuildConfig.DEBUG)Log.d(TAG, "STOMP client connected");
				} else {
					if (BuildConfig.DEBUG) Log.d(TAG, "STOMP client not connected");
				}
				if (BuildConfig.DEBUG) Log.d(TAG, "Subscribing STOMP to: " + mTextTopicString);
				mStompClient.subscribe(mTextTopicString, mStompTextListener);
				if (BuildConfig.DEBUG) Log.d(TAG, "Subscribing STOMP to: " + mImageTopicString);
				mStompClient.subscribe(mImageTopicString, mStompImagesListener);
			} catch(javax.security.auth.login.LoginException loginExc) {
				if(BuildConfig.DEBUG)Log.e(TAG, "Login exception occured");
			} catch(IOException ioExc) {
				if(BuildConfig.DEBUG)Log.e(TAG, "IO exception occured");
			}
		}
	}

	public void unsubscribe(@NonNull RadioServiceListener callback) {
		if(callback instanceof TextualMetadataListener) {
			mTextualSubscibers.remove((TextualMetadataListener)callback);
		}
		if(callback instanceof VisualMetadataListener) {
			mVisualSubscibers.remove((VisualMetadataListener)callback);
		}

		if(mVisualSubscibers.isEmpty() && mTextualSubscibers.isEmpty()) {
			if(mStompClient != null) {
				if(mConnectedTimer != null) {
					if(BuildConfig.DEBUG)Log.d(TAG, "Cancelling STOMP alive timer");
					mConnectedTimer.cancel();
					mConnectedTimer = null;
				}

				if(mStompClient.isConnected()) {
					if(BuildConfig.DEBUG) Log.d(TAG, "Last callback unsubscribed, disconnecting STOMP client");

					mStompClient.unsubscribe(mImageTopicString);
					mStompClient.unsubscribe(mTextTopicString);

					mStompClient.disconnect();
					mStompClient = null;
				}
			}
		}
	}

	/**/
	private Listener mStompErrorListener = new Listener() {
		@Override
		public void message(Map headers, String body) {
			if(BuildConfig.DEBUG) {
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					Log.d(TAG, "STOMP Error Header: " + pair.getKey() + " : " + pair.getValue());
				}
			}
			if(BuildConfig.DEBUG)Log.d(TAG, "STOMP Error Body: " + body);
		}
	};

	private Listener mStompImagesListener = new Listener() {
		@Override
		public void message(Map headers, String body) {
			if(BuildConfig.DEBUG) {
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					Log.d(TAG, "STOMP Image Header: " + pair.getKey() + " : " + pair.getValue());
				}
			}
			if(BuildConfig.DEBUG)Log.d(TAG, "STOMP Image Body: " + body);

			mCbExe.execute(new VisDownload(headers, body));
		}
	};

	private Listener mStompTextListener = new Listener() {
		@Override
		public void message(Map headers, String body) {
			if(BuildConfig.DEBUG) {
				Iterator it = headers.entrySet().iterator();
				while (it.hasNext()) {
					Map.Entry pair = (Map.Entry)it.next();
					Log.d(TAG, "STOMP Text Header: " + pair.getKey() + " : " + pair.getValue());
				}
			}
			if(BuildConfig.DEBUG)Log.d(TAG, "STOMP Text Body " + body);

			if(body.length() > 5) {
				final RadioVisTextual textual = new RadioVisTextual(body.substring(5));
				for (final TextualMetadataListener cb : mTextualSubscibers) {
					mCbExe.execute(new Runnable() {
						@Override
						public void run() {
							cb.newTextualMetadata(textual);
						}
					});
				}
			}
		}
	};

	/* Topic Ids */
	private String createImageTopic() {
		return "/topic/" + this.getServiceIdentifier() + "/image";
	}

	private String createTextTopic() {
		return "/topic/" + this.getServiceIdentifier() + "/text";
	}

	private class RadioVisTextual implements Textual {

		private final String mText;

		RadioVisTextual(String text) {
			mText = text;
		}

		@Override
		public TextualType getType() {
			return TextualType.METADATA_TEXTUAL_TYPE_RADIODNS_RADIOVIS;
		}

		@Override
		public String getText() {
			return mText;
		}
	}

	private final static String RADIOVIS_TRIGGERTIME = "trigger-time";
	private final static String RADIOVIS_CATEGORY_ID = "CategoryId";
	private final static String RADIOVIS_SLIDE_ID = "SlideId";
	private final static String RADIOVIS_LINK = "link";
	private final static String RADIOVIS_CATEGORY_TITLE = "CategoryTitle";

	private class RadioVisVisual implements VisualIpRdnsRadioVis {

		private Calendar mTriggerCal = null;
		private int mCatId = -1;
		private int mSlideId = 0;
		private String mCatTitle = "";
		private String mLink = "";
		private VisualMimeType mVisType = VisualMimeType.METADATA_VISUAL_MIMETYPE_UNKNOWN;
		private int mVisWidth = -1;
		private int mVisHeight = -1;
		private final byte[] mVisData;

		RadioVisVisual(Map headers, byte[] visData, VisualMimeType mime) {
			String triggerTime = (String)headers.get(RADIOVIS_TRIGGERTIME);

			SimpleDateFormat iso8601format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());
			if(triggerTime != null && !triggerTime.equals("NOW")) {
				try {
					iso8601format.parse(triggerTime.replaceAll("Z$", "+00:00"));
					mTriggerCal = iso8601format.getCalendar();
				} catch (ParseException e) {
					if (BuildConfig.DEBUG) e.printStackTrace();
				}
			}

			String catIdString = (String)headers.get(RADIOVIS_CATEGORY_ID);
			if(catIdString != null) {
				try {
					mCatId = Integer.parseInt(catIdString);
				} catch(NumberFormatException numExc) {
					if(BuildConfig.DEBUG)Log.e(TAG, "Error parsing CatgoryID string: " + catIdString);
				}
			}

			String slideIdString = (String)headers.get(RADIOVIS_SLIDE_ID);
			if(slideIdString != null) {
				try {
					mSlideId = Integer.parseInt(slideIdString);
				} catch(NumberFormatException numExc) {
					if(BuildConfig.DEBUG)Log.e(TAG, "Error parsing SlideID string: " + slideIdString);
				}
			}

			String linkString = (String)headers.get(RADIOVIS_LINK);
			if(linkString != null) {
				mLink = linkString;
			}

			String catTitleString = (String)headers.get(RADIOVIS_CATEGORY_TITLE);
			if(catTitleString != null) {
				mCatTitle = catTitleString;
			}

			mVisData = visData;
		}

		@Override
		public Calendar getTriggerTime() {
			return mTriggerCal;
		}

		@Override
		public VisualType getVisualType() {
			return VisualType.METADATA_VISUAL_TYPE_RADIODNS_RADIOVIS;
		}

		@Override
		public VisualMimeType getVisualMimeType() {
			return mVisType;
		}

		@Override
		public byte[] getVisualData() {
			return mVisData;
		}

		@Override
		public int getVisualWidth() {
			return mVisWidth;
		}

		@Override
		public int getVisualHeight() {
			return mVisHeight;
		}

		@Override
		public String getLink() {
			return mLink;
		}

		@Override
		public int getCategoryId() {
			return mCatId;
		}

		@Override
		public int getSlideId() {
			return mSlideId;
		}

		@Override
		public String getCategoryTitle() {
			return mCatTitle;
		}
	}

	private final static String HTTP_HEADER_CONTENT_LENGTH = "Content-Length";
	private final static String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
	private class VisDownload implements Runnable {

		private final Map mHeaders;
		private final String mBody;

		private VisDownload(Map headers, String body) {
			if(BuildConfig.DEBUG)Log.d(TAG, "VisDownload: " + body);
			mHeaders = headers;
			if(body.startsWith("SHOW ")) {
				mBody = body.substring(5);
			} else {
				mBody = body;
			}
		}

		@Override
		public void run() {
			try {
				URL visUrl = new URL(mBody);
				HttpURLConnection visConn = (HttpURLConnection)visUrl.openConnection();
				visConn.setInstanceFollowRedirects(true);
				visConn.setConnectTimeout(5000);
				visConn.setReadTimeout(5000);

				if(visConn.getResponseCode() == HttpURLConnection.HTTP_OK) {
					String contentLengthString = visConn.getHeaderField(HTTP_HEADER_CONTENT_LENGTH);
					String contentTypeString = visConn.getHeaderField(HTTP_HEADER_CONTENT_TYPE);

					int contentLength = 0;
					if(contentLengthString != null) {
						contentLength = Integer.parseInt(contentLengthString.trim());
					}

					VisualMimeType visMime = VisualMimeType.METADATA_VISUAL_MIMETYPE_UNKNOWN;
					if(contentTypeString != null) {
						switch (contentTypeString.trim()) {
							case "image/jpeg":
							case "image/jpg": {
								visMime = VisualMimeType.METADATA_VISUAL_MIMETYPE_JPEG;
								break;
							}
							case "image/bmp": {
								visMime = VisualMimeType.METADATA_VISUAL_MIMETYPE_BMP;
								break;
							}
							case "image/gif": {
								visMime = VisualMimeType.METADATA_VISUAL_MIMETYPE_GIF;
								break;
							}
							case "image/png": {
								visMime = VisualMimeType.METADATA_VISUAL_MIMETYPE_PNG;
								break;
							}
						}
					}

					if(BuildConfig.DEBUG)Log.d(TAG, "VisDownload ContentLength: " + contentLengthString + ", ContentType: " + contentTypeString);

					InputStream visInputStream = visConn.getInputStream();
					ByteArrayOutputStream visOutputStream = new ByteArrayOutputStream();
					byte[] inBuff = new byte[1024];
					int readLength;
					int bytesred = 0;
					while ((readLength = visInputStream.read(inBuff)) > 0) {
						visOutputStream.write(inBuff, 0, readLength);
						bytesred += readLength;
					}

					if(BuildConfig.DEBUG)Log.d(TAG, "VisDownload finished: " + bytesred);

					if(bytesred > 0 && bytesred == contentLength) {
						final RadioVisVisual vis = new RadioVisVisual(mHeaders, visOutputStream.toByteArray(), visMime);
						for(final VisualMetadataListener cb : mVisualSubscibers) {
							mCbExe.execute(new Runnable() {
								@Override
								public void run() {
									cb.newVisualMetadata(vis);
								}
							});
						}
					}
				}
			} catch(MalformedURLException malUrlExc) {
				if(BuildConfig.DEBUG)Log.e(TAG, "MalformedURL: " + mBody);
			} catch(SocketTimeoutException sockExc) {
				if(BuildConfig.DEBUG)Log.e(TAG, "SocketrtimeoutException: " + mBody);
			} catch(IOException ioExc) {
				if(BuildConfig.DEBUG)Log.e(TAG, "IOException: " + mBody);
				if(BuildConfig.DEBUG)ioExc.printStackTrace();
			}
		}
	}
}
