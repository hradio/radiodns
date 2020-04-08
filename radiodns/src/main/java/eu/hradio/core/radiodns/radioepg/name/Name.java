package eu.hradio.core.radiodns.radioepg.name;

import android.support.annotation.NonNull;

import java.io.Serializable;

/**
 * Group of elements for adding names to programmes, programme events, programme groups and services
 * Three variants are supported: shortName, mediumName, longName
 */
public class Name implements Serializable {

	private static final long serialVersionUID = -7811004977698611113L;

	private final NameType mNameType;
	private final String mLang;
	private final String mName;

	public Name(@NonNull NameType type, @NonNull String lang, @NonNull String name) {
		mNameType = type;
		mLang = lang;

		if(name.length() > mNameType.getMaxCharacters()) {
			name = name.substring(0, mNameType.getMaxCharacters());
		}

		mName = name;
	}

	public Name(@NonNull NameType type, @NonNull String name) {
		this(type, "en", name);
	}

	/**
	 * Returns the type of this name element
	 * @return the type of this name element
	 */
	public NameType getType() {
		return mNameType;
	}

	/**
	 * Returns the language used for this name element
	 * @return the language used for this name element
	 */
	public String getLanguage() {
		return mLang;
	}

	/**
	 * Returns the name value
	 * @return the name value
	 */
	public  String getName() {
		return mName;
	}
}
