package eu.hradio.core.radiodns;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.description.Description;
import eu.hradio.core.radiodns.radioepg.description.DescriptionType;
import eu.hradio.core.radiodns.radioepg.genre.Genre;
import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocation;
import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocationPoint;
import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocationPolygon;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.mediadescription.MediaDescription;
import eu.hradio.core.radiodns.radioepg.multimedia.Multimedia;
import eu.hradio.core.radiodns.radioepg.multimedia.MultimediaType;
import eu.hradio.core.radiodns.radioepg.name.Name;
import eu.hradio.core.radiodns.radioepg.name.NameType;
import eu.hradio.core.radiodns.radioepg.scope.ServiceScope;

abstract class RadioEpgParser {

	static final String TAG = "REpgParser";

	static final String NAMESPACE   = null;
	static final String SERVICEINFORMATION_TAG  = "serviceInformation";

	static final String CREATIONTIME_ATTR       = "creationTime";
	static final String ORIGINATOR_ATTR         = "originator";
	static final String XMLLANG_ATTR            = "xml:lang";
	static final String VERSION_ATTR            = "version";
	static final String TERMS_ATTR              = "terms";

	static final String SERVICES_TAG            = "services";
	static final String SERVICEPROVIDER_TAG     = "serviceProvider";
	static final String SERVICE_TAG             = "service";

	static final String DESCRIPTION_SHORT_TAG       = "shortDescription";
	static final String DESCRIPTION_LONG_TAG        = "longDescription";
	static final String MULTIMEDIA_TAG              = "multimedia";
	static final String NAME_SHORT_TAG              = "shortName";
	static final String NAME_MEDIUM_TAG             = "mediumName";
	static final String NAME_LONG_TAG               = "longName";

	static final String MEDIADESCRIPTION_TAG        = "mediaDescription";
	static final String BEARER_TAG                  = "bearer";

	static final String TYPE_ATTR                   = "type";
	static final String MIMEVALUE_ATTR              = "mimeValue";
	static final String URL_ATTR                    = "url";
	static final String URI_ATTR                    = "uri";
	static final String WIDTH_ATTR                  = "width";
	static final String HEIGHT_ATTR                 = "height";

	static final String ID_ATTR                     = "id";
	static final String SHORT_ID_ATTR               = "shortId";
	static final String MIME_ATTR                   = "mime";
	static final String COST_ATTR                   = "cost";

	static final String DESCRIPTION_ATTR            = "description";
	static final String EXPIRYTIME_ATTR             = "expiryTime";

	static final String KEYWORDS_TAG                = "keywords";
	static final String LINK_TAG                    = "link";
	static final String GEOLOCATION_TAG             = "geolocation";
	static final String GEOLOCATION_COUNTRY_TAG     = "country";
	static final String GEOLOCATION_POINT_TAG       = "point";
	static final String GEOLOCATION_POLYGON_TAG     = "polygon";

	static final String RADIODNS_TAG                = "radiodns";
	static final String RADIODNS_FQDN_ATTR          = "fqdn";
	static final String RADIODNS_SID_ATTR           = "serviceIdentifier";

	static final String GENRE_TAG                   = "genre";
	static final String HREF_ATTR                   = "href";

	static final String APPLICATION_TAG             = "application";
	static final String CONTROL_ATTR                = "control";
	static final String APPID_ATTR                  = "applicationID";
	static final String APPPRIO_ATTR                = "applicationPriority";
	static final String APPSCOPE_TAG                = "applicationScope";

	static final String SERVICESCOPE_TAG                = "serviceScope";

	String mDocumentLanguage = "en";

	Name parseName(XmlPullParser parser, String nameTag, NameType type) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing name");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, nameTag);

		String nameLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);
		if(nameLang == null) {
			nameLang = mDocumentLanguage;
		}

		Name retName = new Name(type, nameLang, readTagText(parser));

		parser.require(XmlPullParser.END_TAG, NAMESPACE, nameTag);

		return retName;
	}

	MediaDescription parseMediaDescription(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing MediaDescription");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, MEDIADESCRIPTION_TAG);

		MediaDescription desc = new MediaDescription();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if(name.equals(DESCRIPTION_SHORT_TAG) || name.equals(DESCRIPTION_LONG_TAG)) {
				desc.addDescription(parseDescription(parser, name, DescriptionType.DESCRIPTION_SHORT));
			} else if(name.equals(MULTIMEDIA_TAG)) {
				Multimedia mm = parseMultimedia(parser);
				if(mm != null) {
					desc.setMultimedia(mm);
				}
			}
		}

		//parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, MEDIADESCRIPTION_TAG);

		return desc;
	}

	Description parseDescription(XmlPullParser parser, String descTag, DescriptionType type) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Description: " + descTag);

		parser.require(XmlPullParser.START_TAG, NAMESPACE, descTag);

		String descriptionLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);
		if(descriptionLang == null) {
			descriptionLang = mDocumentLanguage;
		}

		String descriptionText = readTagText(parser);

		parser.require(XmlPullParser.END_TAG, NAMESPACE, descTag);

		return new Description(type, descriptionLang, descriptionText);
	}

	Bearer parseBearer(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Bearer");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, BEARER_TAG);

		String bearerIdString = parser.getAttributeValue(NAMESPACE, ID_ATTR);
		String bearerMimeString = parser.getAttributeValue(NAMESPACE, MIMEVALUE_ATTR);
		String bearerCostString = parser.getAttributeValue(NAMESPACE, COST_ATTR);

		Bearer bearer = null;

		int bearerCost = -1;

		if(bearerMimeString == null) {
			bearerMimeString = "";
		}
		if(bearerCostString != null) {
			try {
				bearerCost = Integer.parseInt(bearerCostString.trim());
			} catch (NumberFormatException numExc) {
				if(BuildConfig.DEBUG)numExc.printStackTrace();
			}
		}

		if(bearerIdString != null) {
			if(BuildConfig.DEBUG)Log.d(TAG, "Creating Bearer from: " + bearerIdString);
			bearer = new Bearer(bearerIdString, bearerCost, bearerMimeString, 0);
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, BEARER_TAG);

		return bearer;
	}

	Link parseLink(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Link...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, LINK_TAG);

		Link retLink = null;

		String linkUri = parser.getAttributeValue(NAMESPACE, URI_ATTR);
		if(linkUri != null) {
			String linkMimeVal = parser.getAttributeValue(NAMESPACE, MIMEVALUE_ATTR);
			String linkLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);
			String linkDesc = parser.getAttributeValue(NAMESPACE, DESCRIPTION_ATTR);
			String linkExpiry = parser.getAttributeValue(NAMESPACE, EXPIRYTIME_ATTR);

			retLink =  new Link(linkUri.trim(), linkMimeVal != null ? linkMimeVal.trim() : "", linkLang != null ? linkLang.trim() : mDocumentLanguage, linkDesc != null ? linkDesc.trim() : "", linkExpiry != null ? linkExpiry.trim() : "");
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, LINK_TAG);

		return retLink;
	}

	GeoLocation parseGeoLocation(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, GEOLOCATION_TAG);

		GeoLocation retLoc = new GeoLocation();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing GeoLocation TagName: " + name);

			if(name.equals(GEOLOCATION_COUNTRY_TAG)) {
				retLoc.addCountryString(readTagText(parser));
			} else if(name.equals(GEOLOCATION_POINT_TAG)) {
				GeoLocationPoint point = parseGeoLocationPoint(parser);
				if(point != null) {
					retLoc.addLocationPoint(point);
				}
			}  else if(name.equals(GEOLOCATION_POLYGON_TAG)) {
				GeoLocationPolygon poly = parseGeoLocationPolygon(parser);
				if(poly != null) {
					retLoc.addLocationPolygon(poly);
				}
			} else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skipping Tag within ServiceProvider: " + name);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, GEOLOCATION_TAG);

		return retLoc;
	}

	GeoLocationPolygon parseGeoLocationPolygon(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, GEOLOCATION_POLYGON_TAG);

		GeoLocationPolygon retPoly = null;
		String[] polySplit = readTagText(parser).split(",");
		ArrayList<GeoLocationPoint> polyPoints = new ArrayList<>();
		for(String latLong : polySplit) {
			String[] pointSplit = latLong.split("\\s+");
			if(pointSplit.length == 2) {
				polyPoints.add(new GeoLocationPoint(Double.parseDouble(pointSplit[0].trim()), Double.parseDouble(pointSplit[1].trim())));
			}
		}

		if(!polyPoints.isEmpty()) {
			retPoly = new GeoLocationPolygon(polyPoints);
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, GEOLOCATION_POLYGON_TAG);

		return retPoly;
	}

	GeoLocationPoint parseGeoLocationPoint(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, GEOLOCATION_POINT_TAG);

		GeoLocationPoint retPoint = null;

		String[] pointSplit = readTagText(parser).split("\\s+");
		if(pointSplit.length == 2) {
			retPoint = new GeoLocationPoint(Double.parseDouble(pointSplit[0].trim()), Double.parseDouble(pointSplit[1].trim()));
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, GEOLOCATION_POINT_TAG);

		return retPoint;
	}

	Multimedia parseMultimedia(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Multimedia");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, MULTIMEDIA_TAG);

		String multimediaLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);
		if(multimediaLang == null) {
			multimediaLang = mDocumentLanguage;
		}

		MultimediaType mmType = MultimediaType.fromTypeString(parser.getAttributeValue(NAMESPACE, TYPE_ATTR));
		String multimediaUrl = parser.getAttributeValue(NAMESPACE, URL_ATTR);
		String multimediaMime = parser.getAttributeValue(NAMESPACE, MIMEVALUE_ATTR);
		String multimediaWidth = parser.getAttributeValue(NAMESPACE, WIDTH_ATTR);
		String multimediaHeight = parser.getAttributeValue(NAMESPACE, HEIGHT_ATTR);

		Multimedia mmRet = null;

		if(multimediaUrl != null) {
			if(mmType != MultimediaType.MULTIMEDIA_LOGO_UNRESTRICTED && multimediaMime == null) {
				if(BuildConfig.DEBUG)Log.w(TAG, "Multimedia MIME is null for: " + multimediaUrl);
				multimediaMime = "";
			}

			if(BuildConfig.DEBUG)Log.d(TAG, "Multimedia Type: " + mmType.toString());
			if(mmType == MultimediaType.MULTIMEDIA_LOGO_UNRESTRICTED) {
				int mmWidth = -1;
				int mmHeight = -1;
				if (multimediaWidth != null) {
					try {
						mmWidth = Integer.parseInt(multimediaWidth.trim());
					} catch (NumberFormatException numExc) {
						if(BuildConfig.DEBUG)numExc.printStackTrace();
					}
				}
				if (multimediaHeight != null) {
					try {
						mmHeight = Integer.parseInt(multimediaHeight.trim());
					} catch (NumberFormatException numExc) {
						if(BuildConfig.DEBUG)numExc.printStackTrace();
					}
				}

				if(BuildConfig.DEBUG)Log.d(TAG, "Multimedia unrestricted MIME : " + multimediaMime + ", Width: " + mmWidth + ", Height: " + mmHeight);
				mmRet = new Multimedia(mmType, multimediaLang, multimediaUrl, multimediaMime, mmWidth, mmHeight);
			} else {
				mmRet = new Multimedia(mmType, multimediaLang, multimediaUrl);
			}
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, MULTIMEDIA_TAG);

		return mmRet;
	}

	Genre parseGenre(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, GENRE_TAG);

		Genre retGenre = null;

		String hrefString = parser.getAttributeValue(NAMESPACE, HREF_ATTR);
		String typeString = parser.getAttributeValue(NAMESPACE, TYPE_ATTR);

		if(hrefString != null) {
			if(typeString != null) {
				retGenre = new Genre(hrefString.trim(), typeString.trim());
			} else {
				retGenre = new Genre(hrefString.trim());
			}

			if(retGenre.getGenre() == null || retGenre.getGenre().isEmpty()) {
				retGenre = null;
			}
		}

		if(BuildConfig.DEBUG) Log.d(TAG, "Advancing parser...");
		parser.next();
		if(parser.getEventType() == XmlPullParser.TEXT) {
			if(BuildConfig.DEBUG)Log.d(TAG, "Genre FreeText element");
			readTagText(parser);
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, GENRE_TAG);

		return retGenre;
	}

	ServiceScope parseApplicationScope(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Scope");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, APPSCOPE_TAG);

		String scopeString = parser.getAttributeValue(NAMESPACE, ID_ATTR);
		ServiceScope appScope = null;
		if(scopeString != null) {
			appScope = new ServiceScope(scopeString);
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, APPSCOPE_TAG);

		return appScope;
	}

	ServiceScope parseServiceScope(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Scope");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SERVICESCOPE_TAG);

		String scopeString = parser.getAttributeValue(NAMESPACE, ID_ATTR);
		ServiceScope appScope = null;
		if(scopeString != null) {
			appScope = new ServiceScope(scopeString);
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, SERVICESCOPE_TAG);

		return appScope;
	}

	String readTagText(XmlPullParser parser) throws IOException, XmlPullParserException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Reading text");

		String redtext = "";
		if (parser.next() == XmlPullParser.TEXT) {
			redtext = parser.getText().trim();

			parser.nextTag();
		}

		return redtext;
	}

	void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
		if (parser.getEventType() != XmlPullParser.START_TAG) {
			throw new IllegalStateException();
		}

		int depth = 1;
		while (depth != 0) {
			switch (parser.next()) {
				case XmlPullParser.END_TAG:
					depth--;
					break;
				case XmlPullParser.START_TAG:
					depth++;
					break;
			}
		}
	}
}
