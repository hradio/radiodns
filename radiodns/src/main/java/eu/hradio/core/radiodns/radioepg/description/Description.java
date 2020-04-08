package eu.hradio.core.radiodns.radioepg.description;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Description implements Serializable {

	private static final long serialVersionUID = -1774662507842244772L;

	private final DescriptionType mType;
	private final String mLang;
	private final String mDesc;

	public Description(@NonNull DescriptionType type, @NonNull String lang, @NonNull String description) {
		mType = type;
		mLang = lang;

		if(description.length() > mType.getMaxCharacters()) {
			description = description.substring(0, mType.getMaxCharacters());
		}

		mDesc = description;
	}

	public Description(@NonNull DescriptionType type, @NonNull String description) {
		this(type, "en", description);
	}

	public DescriptionType getType() {
		return mType;
	}

	public String getLanguage() {
		return mLang;
	}

	public String getDescription() {
		return mDesc;
	}
}
