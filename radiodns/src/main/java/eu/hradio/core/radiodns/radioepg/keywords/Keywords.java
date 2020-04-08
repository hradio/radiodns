package eu.hradio.core.radiodns.radioepg.keywords;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Keywords implements Serializable {

	private static final long serialVersionUID = 582624195273709567L;

	private final String[] mKeywords;
	private final String mLang;

	public Keywords(@NonNull String keywords, @NonNull String lang) {
		mKeywords = keywords.split(",");
		mLang = lang;
	}

	public Keywords(@NonNull String keywords) {
		this(keywords, "en");
	}

	public final String[] getKeywords() {
		return mKeywords;
	}
}
