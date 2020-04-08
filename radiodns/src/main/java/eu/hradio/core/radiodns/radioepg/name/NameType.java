package eu.hradio.core.radiodns.radioepg.name;

public enum NameType {

	/** Maximum 8 characters**/
	NAME_SHORT(8),
	/** Maximum 16 characters**/
	NAME_MEDIUM(16),
	/** Maximum 128 characters**/
	NAME_LONG(128);

	private final int mMaxChars;

	private NameType(int maxChars) {
		mMaxChars = maxChars;
	}

	public int getMaxCharacters() {
		return mMaxChars;
	}
}
