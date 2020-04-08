package eu.hradio.core.radiodns;

public enum RadioDnsServiceType {

	RADIO_VIS("radiovis"),
	RADIO_EPG("radioepg"),
	RADIO_TAG("radiotag"),
	RADIO_WEB("radioweb");

	private final String mAppName;

	private RadioDnsServiceType(String appName) {
		mAppName = appName;
	}

	public String getAppName() {
		return mAppName;
	}
}
