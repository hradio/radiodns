package eu.hradio.core.radiodns.radioepg.radiodns;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class RadioDns implements Serializable {

	private static final long serialVersionUID = -8106508207939090256L;

	private final String mFqdn;
	private final String mSid;

	public RadioDns(@NonNull String fqdn, @NonNull String serviceIdentifier) {
		mFqdn = fqdn;
		mSid = serviceIdentifier;
	}

	public String getFqdn() {
		return mFqdn;
	}

	public String getServiceIdentifier() {
		return mSid;
	}
}
