package eu.hradio.core.radiodns;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import org.minidns.dnsserverlookup.android21.AndroidUsingLinkProperties;
import org.minidns.hla.ResolverApi;
import org.minidns.hla.ResolverResult;
import org.minidns.record.CNAME;
import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;
import org.omri.radioservice.RadioServiceDab;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static eu.hradio.core.radiodns.BuildConfig.DEBUG;

public class RadioDnsCore {

	private final static String TAG = "RadioDnsCore";

	private final RadioService mLookupSrv;
	private final String mFqdn;
	private final String mRdnsSrvId;
	private final String mBearerUri;

	private volatile boolean mLookupRunning = false;

	private ArrayList<RadioDnsService> mFoundServices = new ArrayList<>();

	private ExecutorService mCbExe;
	private ConcurrentLinkedQueue<RadioDnsCoreLookupCallback> mCallbacks = new ConcurrentLinkedQueue<>();

	RadioDnsCore(@NonNull RadioService lookupSrv) {
		mLookupSrv = lookupSrv;

		mCbExe = Executors.newFixedThreadPool(10);

		switch(mLookupSrv.getRadioServiceType()) {
			case RADIOSERVICE_TYPE_DAB:
			case RADIOSERVICE_TYPE_EDI: {
				RadioServiceDab dabSrv = (RadioServiceDab)mLookupSrv;
				mFqdn = "0." +
						Integer.toHexString(dabSrv.getServiceId()) +
						"." +
						Integer.toHexString(dabSrv.getEnsembleId()) +
						"." +
						Integer.toHexString(dabSrv.getServiceId()).charAt(0) +
						Integer.toHexString(dabSrv.getEnsembleEcc()) +
						".dab.radiodns.org";

				mRdnsSrvId = "dab/" +
						Integer.toHexString(dabSrv.getServiceId()).charAt(0) +
						Integer.toHexString(dabSrv.getEnsembleEcc()) +
						"/" +
						Integer.toHexString(dabSrv.getEnsembleId()) +
						"/" +
						Integer.toHexString(dabSrv.getServiceId()) +
						"/" +
						"0";

				mBearerUri = "dab:" +
						Integer.toHexString(dabSrv.getServiceId()).charAt(0) +
						Integer.toHexString(dabSrv.getEnsembleEcc()) +
						"." +
						Integer.toHexString(dabSrv.getEnsembleId()) +
						"." +
						Integer.toHexString(dabSrv.getServiceId()) +
						"." +
						"0";

				if(DEBUG)Log.d(TAG, "Constructed FQDN: " + mFqdn);
				if(DEBUG)Log.d(TAG, "Constructed SrID: " + mRdnsSrvId);

				break;
			}
			case RADIOSERVICE_TYPE_FM:
			case RADIOSERVICE_TYPE_IP:
			case RADIOSERVICE_TYPE_HDRADIO:
			case RADIOSERVICE_TYPE_SIRIUS:
			default: {
				if(DEBUG)Log.e(TAG, "RDNS lookup for RadioServiceType " + mLookupSrv.getRadioServiceType().toString() + " not yet implemented!");
				mFqdn = null;
				mRdnsSrvId = null;
				mBearerUri = null;
				break;
			}
		}
	}

	/**
	 * Initiates the core RadioDNS lookup
	 * @param callback the {@link RadioDnsCoreLookupCallback}
	 */
	public void coreLookup(@NonNull RadioDnsCoreLookupCallback callback, @NonNull final Context context) {
		mCallbacks.offer(callback);

		if(mFoundServices.isEmpty() && mFqdn != null && mRdnsSrvId != null && mBearerUri != null) {
			if(!mLookupRunning) {
				mLookupRunning = true;

				if(DEBUG)Log.d(TAG, "Starting lookup thread");

				Thread lookupThread = new Thread() {
					@Override
					public void run() {
						if(DEBUG)Log.d(TAG, "Resolving for: " + mLookupSrv.getServiceLabel());

						try {
							AndroidUsingLinkProperties.setup(context);

							ResolverResult<CNAME> result = ResolverApi.INSTANCE.resolve(mFqdn, CNAME.class);
							if(result.wasSuccessful()) {
								if(DEBUG)Log.d(TAG, "CNAME result success");
								for(CNAME cname : result.getAnswers()) {
									if(DEBUG)Log.d(TAG, "MiniDNS CNAME: " + cname.target.getDomainpart() + " : " + cname.target.getHostpart() + " : " + cname.toString());
									resolveServiceRecords(cname, context);
								}
							} else {
								if(DEBUG)Log.d(TAG, "CNAME resolution failed");
							}
						} catch(IOException ioExc) {
							ioExc.printStackTrace();
						} finally {
							if(DEBUG)Log.d(TAG, "Calling callback for: " + mLookupSrv.getServiceLabel());
							callCallbacks();

							mLookupRunning = false;
						}
					}
				};

				lookupThread.start();
			}
		} else {
			callCallbacks();
		}
	}

	private void callCallbacks() {
		if(mCallbacks.size() > 0) {
			final Object[] cbs = mCallbacks.toArray();
			for(final Object cb : cbs) {
				mCbExe.execute(new Runnable() {
					@Override
					public void run() {
						((RadioDnsCoreLookupCallback)cb).coreLookupFinished(mLookupSrv, mFoundServices);
					}
				});
			}
			mCallbacks.clear();
		}
	}

	private void resolveServiceRecords(CNAME cname, @NonNull final Context context) {
		for(RadioDnsServiceType rdnsType : RadioDnsServiceType.values()) {

			try {
				AndroidUsingLinkProperties.setup(context);

				String appFqdn = String.format("_%s._%s.%s", rdnsType.getAppName(), "tcp", cname.getTarget().toString());

				ResolverResult<SRV> result = ResolverApi.INSTANCE.resolve(appFqdn, SRV.class);
				if (result.wasSuccessful()) {
					if(DEBUG)Log.d(TAG, "SRV result success");
					for (SRV serviceRecord : result.getAnswers()) {
						if(DEBUG)Log.d(TAG, "SRV: " + serviceRecord.target.toString());

						if (DEBUG)Log.d(TAG, rdnsType.toString() + " SrvRecord found, Target: " + serviceRecord.target.toString() + ", Port: " + serviceRecord.port + ", Weight: " + serviceRecord.weight + ", Prio: " + serviceRecord.priority);

						switch (rdnsType) {
							case RADIO_VIS: {
								mFoundServices.add(new RadioDnsServiceVis(serviceRecord, mRdnsSrvId, mBearerUri, rdnsType, mLookupSrv));
								break;
							}
							case RADIO_EPG: {
								mFoundServices.add(new RadioDnsServiceEpg(serviceRecord, mRdnsSrvId, mBearerUri, rdnsType, mLookupSrv));
								break;
							}
							case RADIO_TAG: {
								mFoundServices.add(new RadioDnsServiceTag(serviceRecord, mRdnsSrvId, mBearerUri, rdnsType, mLookupSrv));
								break;
							}
							case RADIO_WEB: {
								mFoundServices.add(new RadioDnsServiceWeb(serviceRecord, mRdnsSrvId, mBearerUri, rdnsType, mLookupSrv));
								break;
							}
						}
					}
				}
			} catch (IOException ioExc) {
				ioExc.printStackTrace();
			}
		}
	}
}
