package eu.hradio.core.radiodns.radioepg.bearer;

public enum BearerType {

	BEARER_TYPE_UNKNOWN(""),
	BEARER_TYPE_FM("fm"),
	BEARER_TYPE_DAB("dab"),
	BEARER_TYPE_DRM("drm"),
	BEARER_TYPE_AMSS("amss"),
	BEARER_TYPE_IBOC("hd"),
	BEARER_TYPE_HTTP("http"),
	BEARER_TYPE_HTTPS("https");

	private final String mBearerId;

	private BearerType(String bearerId) {
		mBearerId = bearerId;
	}

	public String getBearerTypeId() {
		return mBearerId;
	}
}
