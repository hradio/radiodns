package eu.hradio.core.radiodns.radioepg.genre;

public enum GenreType {

	GENRE_TYPE_MAIN("main"),
	GENRE_TYPE_SECONDARY("secondary"),
	GENRE_TYPE_OTHER("other");

	private final String mGenreType;

	private GenreType(String genreType) {
		mGenreType = genreType;
	}

	public String getGenreType() {
		return mGenreType;
	}
}
