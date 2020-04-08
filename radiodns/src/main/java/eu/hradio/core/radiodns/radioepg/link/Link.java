package eu.hradio.core.radiodns.radioepg.link;

import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class Link implements Serializable {

	private static final long serialVersionUID = -4143887406286480645L;

	private final String mUri;
	private final String mMime;
	private final String mLang;
	private final String mDescription;
	private final TimePoint mExpiryTime;

	public Link(@NonNull String uri, @NonNull String mime, @NonNull String language, @NonNull String description, @NonNull String expiryTime) {
		mUri = uri;
		mMime = mime;
		mLang = language;
		if(description.length() > 180) {
			description = description.substring(0, 179);
		}
		mDescription = description;
		mExpiryTime = new TimePoint(expiryTime);
	}

	public Link(@NonNull String uri, @NonNull String mime, @NonNull String language, @NonNull String description) {
		this(uri, mime, language, description, "");
	}

	public Link(@NonNull String uri, @NonNull String mime, @NonNull String language) {
		this(uri, mime, language, "");
	}

	public Link(@NonNull String uri, @NonNull String mime) {
		this(uri, mime, "en");
	}

	public Link(@NonNull String uri) {
		this(uri, "");
	}

	public String getUri() {
		return mUri;
	}

	public String getMime() {
		return mMime;
	}

	public String getLanguage() {
		return mLang;
	}

	public String getDescription() {
		return mDescription;
	}

	public TimePoint getExpiryTime() {
		return mExpiryTime;
	}
}
