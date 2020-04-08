package eu.hradio.core.radiodns.radioepg.multimedia;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Multimedia implements Serializable {

	private static final long serialVersionUID = 1717605927046874600L;

	private final MultimediaType mType;
	private final String mLang;
	private final String mUrl;
	private final int mWidth;
	private final int mHeight;
	private final MultimediaTransferType mTransferType;
	private final String mMime;

	public Multimedia(@NonNull MultimediaType type, @NonNull String lang, @NonNull String url, @NonNull String mime, int width, int height) {
		mType = type;
		mLang = lang;
		mUrl = url;

		mWidth = width;
		mHeight = height;

		mMime = mime;

		if(url.startsWith(MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_MOT.getTransferSchema())) {
			mTransferType = MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_MOT;
		} else if(url.startsWith(MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_HTTP.getTransferSchema())) {
			mTransferType = MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_HTTP;
		} else if(url.startsWith(MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_HTTPS.getTransferSchema())) {
			mTransferType = MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_HTTPS;
		} else {
			mTransferType = MultimediaTransferType.MULTIMEDIA_TRANSFER_TYPE_UNKNOWN;
		}
	}

	public Multimedia(@NonNull MultimediaType type, @NonNull String lang, @NonNull String url) {
		this(type, lang, url, type.getMime(), type.getWidth(), type.getHeight());
	}

	public Multimedia(@NonNull MultimediaType type, @NonNull String url) {
		this(type, "en", url, type.getMime(), type.getWidth(), type.getHeight());
	}

	public MultimediaType getType() {
		return mType;
	}

	public String getLanguage() {
		return mLang;
	}

	public String getUrl() {
		return mUrl;
	}

	public int getWidth() {
		return mWidth;
	}

	public int getHeight() {
		return mHeight;
	}

	public String getMime() {
		return mMime;
	}

	public MultimediaTransferType getTranferType() {
		return mTransferType;
	}
}
