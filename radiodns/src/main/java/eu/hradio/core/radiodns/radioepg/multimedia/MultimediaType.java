package eu.hradio.core.radiodns.radioepg.multimedia;

public enum MultimediaType {

	/** The image format is unerstricted **/
	MULTIMEDIA_LOGO_UNRESTRICTED(-1, -1, ""),

	/** PNG v1.1, 32x32, colour depth 256 **/
	MULTIMEDIA_LOGO_SQUARE(32, 32, "image/png"),

	/** PNG v1.1, 32x112, colour depth 256 **/
	MULTIMEDIA_LOGO_RECTANGLE(32, 112, "image/png");

	private final int mWidth;
	private final int mHeight;
	private final String mMime;

	private MultimediaType(int width, int height, String mime) {
		mWidth = width;
		mHeight = height;
		mMime = mime;
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

	public static MultimediaType fromTypeString(String typeString) {
		if(typeString != null) {
			if(typeString.equalsIgnoreCase("logo_colour_square")) {
				return MULTIMEDIA_LOGO_SQUARE;
			} else if(typeString.equalsIgnoreCase("logo_colour_rectangle")) {
				return MULTIMEDIA_LOGO_RECTANGLE;
			}
		}

		return MULTIMEDIA_LOGO_UNRESTRICTED;
	}
}
