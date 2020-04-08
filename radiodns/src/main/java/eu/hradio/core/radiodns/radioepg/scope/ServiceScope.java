package eu.hradio.core.radiodns.radioepg.scope;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class ServiceScope implements Serializable {

	private static final long serialVersionUID = 1332589507616948018L;

	private final String mServiceScope;

	public ServiceScope(@NonNull String serviceScopeString) {
		mServiceScope = serviceScopeString;
	}
}
