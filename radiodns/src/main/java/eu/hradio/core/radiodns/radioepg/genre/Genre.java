package eu.hradio.core.radiodns.radioepg.genre;

import android.util.Log;

import java.io.Serializable;

import eu.hradio.core.radiodns.BuildConfig;

public class Genre  implements Serializable {

	private static final long serialVersionUID = 7289375598021779167L;
	
	private final static String TAG = "EpgGenre";

	private final String mGenreHref;
	private final String mGenreTypeString;

	private TvaClassificationSchemeType mTvaCs = TvaClassificationSchemeType.TVA_CS_UNKNOWN;
	private final GenreType mGenreType;

	private final String mGenre;

	public Genre(String genreHref, String genreType) {
		mGenreHref = genreHref.trim();
		mGenreTypeString = genreType.trim();

		//SampleString "urn:tva:metadata:cs:ContentCS:2002:3.6.4.1"
		//              [0]:[1]:  [2]   :[3]:  [4]   :[5] :  [6]
		String[] hrefSplit = mGenreHref.split(":");
		//TODO TvaIntendedAudienceCs, TvaOriginationCs steps parsing

		if(hrefSplit.length == 7) {

			mTvaCs = TvaClassificationSchemeType.fromSchemeString(hrefSplit[4]);

			switch (hrefSplit[4]) {
				case "IntentionCS":
					mGenre = TvaIntentionCs.getIntention(hrefSplit[6]);
					break;
				case "IntentionCSFormatCS":
					mGenre = TvaFormatCs.getFormat(hrefSplit[6]);
					break;
				case "ContentCS":
					mGenre = TvaContentCs.getContent(hrefSplit[6]);
					break;
				case "OriginationCS":
					mGenre = TvaOriginationCs.getOrigination(hrefSplit[6]);
					break;
				case "IntendedAudienceCS":
					mGenre = TvaIntendedAudienceCs.getIntentedAudience(hrefSplit[6]);
					break;
				case "ContentAlertCS":
					mGenre = TvaContentAlertCs.getContentAlert(hrefSplit[6]);
					break;
				case "MediaTypeCS":
					mGenre = TvaMediaTypeCs.getMediaType(hrefSplit[6]);
					break;
				case "AtmosphereCS":
					mGenre = TvaAtmosphereCs.getAtmosphere(hrefSplit[6]);
					break;
				default:
					mGenre = "";
					break;
			}
		} else {
			if(BuildConfig.DEBUG) Log.w(TAG, "TVA href length is only: " + hrefSplit.length + " : " + mGenreHref);
			mGenre = "";
		}

		for(GenreType type : GenreType.values()) {
			if(type.getGenreType().equalsIgnoreCase(mGenreTypeString)) {
				mGenreType = type;
				return;
			}
		}

		mGenreType = GenreType.GENRE_TYPE_OTHER;
	}

	/**
	 * Constructs a Genre defaulting the GenreType to main
	 * @param genreHref the string given in genre href attribute
	 */
	public Genre(String genreHref) {
		this(genreHref, GenreType.GENRE_TYPE_MAIN.getGenreType());
	}

	public String getGenreHref() {
		return mGenreHref;
	}

	public GenreType getGenreType() {
		return mGenreType;
	}

	public TvaClassificationSchemeType getTvaCs() {
		return mTvaCs;
	}

	public String getGenre() {
		return mGenre;
	}
}
