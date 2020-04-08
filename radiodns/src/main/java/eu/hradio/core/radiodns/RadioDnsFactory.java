package eu.hradio.core.radiodns;

import android.support.annotation.NonNull;
import android.util.Log;

import org.omri.radioservice.RadioService;

import java.util.concurrent.ConcurrentHashMap;

public class RadioDnsFactory {

	private static final String TAG = "RadioDnsFactory";

	private static ConcurrentHashMap<RadioService, RadioDnsCore> mLookupCache = new ConcurrentHashMap<>();

	/**
	 * Creates a {@link RadioDnsCore} for the core RadioDNS lookup
	 * @param lookupSrv the {@link RadioService} to create the {@link RadioDnsCore} for
	 * @param noCache {@code false} if the {@link RadioDnsCore} shall be cached for further lookups {@code true} otherwise
	 * @return a {@link RadioDnsCore} for the core RadioDNS lookup
	 */
	public static RadioDnsCore createCoreLookup(@NonNull RadioService lookupSrv, boolean noCache) {
		if(noCache) {
			if(BuildConfig.DEBUG) Log.d(TAG, "Creating non-cached LookupService for: " + lookupSrv.getServiceLabel());
			return new RadioDnsCore(lookupSrv);
		}

		if(mLookupCache.containsKey(lookupSrv)) {
			if(BuildConfig.DEBUG) Log.d(TAG, "Returning cached LookupService for: " + lookupSrv.getServiceLabel());
			return mLookupCache.get(lookupSrv);
		}

		if(BuildConfig.DEBUG) Log.d(TAG, "Creating cached LookupService");
		RadioDnsCore rdns = new RadioDnsCore(lookupSrv);
		mLookupCache.put(lookupSrv, rdns);

		return rdns;
	}

	public static void clearCache() {
		mLookupCache.clear();
	}
}
