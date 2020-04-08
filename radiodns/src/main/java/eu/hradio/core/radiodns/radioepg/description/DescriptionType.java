package eu.hradio.core.radiodns.radioepg.description;

public enum DescriptionType {

	DESCRIPTION_SHORT(180),
	DESCRIPTION_LONG(1200);

	private final int mMaxChars;

	private DescriptionType(int maxChars) {
		mMaxChars = maxChars;
	}

	public int getMaxCharacters() {
		return mMaxChars;
	}
}
