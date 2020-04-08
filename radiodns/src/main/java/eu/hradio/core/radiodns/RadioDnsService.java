package eu.hradio.core.radiodns;

import android.os.Build;
import android.util.Log;

import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;

import java.util.Objects;

public abstract class RadioDnsService {
	
	private final static String TAG = "RadioDnsService";

	private final String mTarget;
	private final int mPort;
	private final int mWeight;
	private final int mPriority;
	private final long mTimeToLive;
	private final RadioDnsServiceType mSrvType;
	private final String mSrvId;
	private final String mBearerUri;
	private final RadioService mLookupSrv;

	RadioDnsService(SRV srvRecord, String rdnsSrvId, String bearerUri, RadioDnsServiceType srvType, RadioService loopupSrv) {
		String target = srvRecord.target.toString();
		if(target.endsWith(".")) {
			target = target.substring(0, target.length()-1);
		}

		mTarget = target;
		mPort = srvRecord.port;
		mWeight = srvRecord.weight;
		mPriority = srvRecord.priority;
		mTimeToLive = 3600;

		if(BuildConfig.DEBUG) Log.d(TAG, "SrvRec TTL: " + mTimeToLive);

		mSrvType = srvType;
		mSrvId = rdnsSrvId;
		mBearerUri = bearerUri;

		mLookupSrv = loopupSrv;
	}

	/**
	 * Returns the canonical hostname of the machine providing the service, ending in a dot
	 * @return the canonical hostname of the machine providing the service, ending in a dot
	 */
	public String getTarget() {
		return mTarget;
	}

	/**
	 * Returns the TCP port on which the service is to be found
	 * @return the TCP port on which the service is to be found
	 */
	public int getPort() {
		return mPort;
	}

	/**
	 * Returns the relative weight for records with the same priority, higher value means more preferred
	 * @return the relative weight for records with the same priority, higher value means more preferred
	 */
	public int getWeight() {
		return mWeight;
	}

	/**
	 * Returns the priority of the target host, lower value means more preferred
	 * @return the priority of the target host, lower value means more preferred
	 */
	public int getPriority() {
		return mPriority;
	}

	/**
	 * Returns the {@link RadioDnsServiceType}
	 * @return the {@link RadioDnsServiceType}
	 */
	public RadioDnsServiceType getServiceType() {
		return mSrvType;
	}

	/**
	 * Returns the RadioDNS ServiceIdentifier
	 * @return the RadioDNS ServiceIdentifier
	 */
	public String getServiceIdentifier() {
		return mSrvId;
	}

	/**
	 * Returns the RadiODNS bearer URI
	 * @return the RadiODNS bearer URI
	 */
	public String getBearerUri() {
		return mBearerUri;
	}

	/**
	 * Returns the Time To Live value
	 * @return the Time To Live value
	 */
	public long getTtl() {
		return mTimeToLive;
	}

	/**
	 * Returns the {@link RadioService} this {@link RadioDnsService} belongs to
	 * @return the {@link RadioService} this {@link RadioDnsService} belongs to
	 */
	public RadioService getRadioService() {
		return mLookupSrv;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof RadioDnsService) {
			RadioDnsService compSrv = (RadioDnsService) obj;
			return ((compSrv.getTarget().equals(this.mTarget)) && (compSrv.getServiceType() == this.mSrvType) && (compSrv.getPort() == this.mPort) && (compSrv.getWeight() == this.mWeight) && (compSrv.getPriority() == this.mPriority));
		}

		return false;
	}

	@Override
	public int hashCode() {
		if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			return Objects.hash(mTarget, mSrvType, mPort, mWeight, mPriority);
		} else {
			int hash = 31;
			hash = 67 * hash + this.mTarget.hashCode();
			hash = 67 * hash + this.mSrvType.hashCode();
			hash = 67 * hash + (int)(this.mPort ^ (this.mPort >>> 32));
			hash = 67 * hash + (int)(this.mWeight ^ (this.mWeight >>> 32));
			hash = 67 * hash + (int)(this.mPriority ^ (this.mPriority >>> 32));

			return hash;
		}
	}
}
