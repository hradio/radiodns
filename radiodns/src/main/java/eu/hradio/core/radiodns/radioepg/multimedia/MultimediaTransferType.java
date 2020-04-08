package eu.hradio.core.radiodns.radioepg.multimedia;

public enum MultimediaTransferType {

	MULTIMEDIA_TRANSFER_TYPE_UNKNOWN(""),
	MULTIMEDIA_TRANSFER_TYPE_HTTP("http"),
	MULTIMEDIA_TRANSFER_TYPE_HTTPS("https"),
	MULTIMEDIA_TRANSFER_TYPE_MOT("dab:");

	private final String mTransferSchema;

	private MultimediaTransferType(String type) {
		mTransferSchema = type;
	}

	public String getTransferSchema() {
		return mTransferSchema;
	}
}
