package eu.hradio.core.radiodns;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.genre.Genre;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.name.NameType;
import eu.hradio.core.radiodns.radioepg.radiodns.RadioDns;
import eu.hradio.core.radiodns.radioepg.serviceinformation.Service;
import eu.hradio.core.radiodns.radioepg.serviceinformation.ServiceProvider;

public class RadioEpgSiParser extends RadioEpgParser {

	private static final String TAG = "REpgSiParser";

	private RadioEpgServiceInformation mParsedSi;

	RadioEpgServiceInformation parse(InputStream siDataStream) throws IOException {
		try {
			if(BuildConfig.DEBUG) Log.d(TAG, "Starting parser...");

			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(siDataStream, NAMESPACE);

			parser.nextTag();

			if(BuildConfig.DEBUG)Log.d(TAG, "Starting parser nextTag: " + parser.getName());

			mParsedSi = parseRoot(parser);

			while(parser.next() != XmlPullParser.END_TAG) {
				String tagName = parser.getName();
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					if(BuildConfig.DEBUG)Log.d(TAG, "NoStartTag: " + tagName + ", EventType: " + parser.getEventType());
					continue;
				}

				if(BuildConfig.DEBUG)Log.d(TAG, "TagName: " + tagName);

				if(tagName.equals(SERVICES_TAG)) {
					parseServices(parser);
				} else {
					if(BuildConfig.DEBUG)Log.d(TAG, "Skipping TagName: " + tagName);
					skip(parser);
				}
			}


		} catch(XmlPullParserException ppExc) {
			if(BuildConfig.DEBUG)ppExc.printStackTrace();
		} finally {
			siDataStream.close();
		}

		return mParsedSi;
	}

	private void parseServices(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing seervices...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SERVICES_TAG);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String tagName = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Services TagName: " + tagName);

			if(tagName.equals(SERVICEPROVIDER_TAG)) {
				mParsedSi.setServiceProvider(parseServiceProvider(parser));
			} else if(tagName.equals(SERVICE_TAG)) {
				mParsedSi.addService(parseService(parser));
			} else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skipping Tag within ServiceS: " + tagName);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, SERVICES_TAG);
	}

	private RadioEpgServiceInformation parseRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing root...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SERVICEINFORMATION_TAG);

		String creationTime = parser.getAttributeValue(NAMESPACE, CREATIONTIME_ATTR);
		String originator = parser.getAttributeValue(NAMESPACE, ORIGINATOR_ATTR);
		String xmlLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);
		String version = parser.getAttributeValue(NAMESPACE, VERSION_ATTR);
		String termsUrl = parser.getAttributeValue(NAMESPACE, TERMS_ATTR);

		if(xmlLang != null) {
			mDocumentLanguage = xmlLang;
		}

		int siVersion = 1;
		if(version != null) {
			try {
				siVersion = Integer.parseInt(version.trim());
			} catch (NumberFormatException numExc) {
				if(BuildConfig.DEBUG)numExc.printStackTrace();
			}
		}

		return new RadioEpgServiceInformation(creationTime == null ? "" : creationTime.trim(), originator == null ? "" : originator.trim(), xmlLang == null ? "en" : xmlLang.trim(), siVersion, termsUrl != null ? termsUrl : "");
	}

	private Service parseService(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, SERVICE_TAG);

		Service retService = new Service();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Services TagName: " + name);

			if(name.equals(NAME_SHORT_TAG)) {
				retService.addName(parseName(parser, NAME_SHORT_TAG, NameType.NAME_SHORT));
			} else if(name.equals(NAME_MEDIUM_TAG)) {
				retService.addName(parseName(parser, NAME_MEDIUM_TAG, NameType.NAME_MEDIUM));
			} else if(name.equals(NAME_LONG_TAG)) {
				retService.addName(parseName(parser, NAME_LONG_TAG, NameType.NAME_LONG));
			} else if(name.equals(MEDIADESCRIPTION_TAG)) {
				retService.addMediaDescription(parseMediaDescription(parser));
			} else if(name.equals(KEYWORDS_TAG)) {
				retService.setKeywords(readTagText(parser));
			} else if(name.equals(LINK_TAG)) {
				Link provLink = parseLink(parser);
				if(provLink != null) {
					retService.addLink(provLink);
				}
			} else if(name.equals(BEARER_TAG)) {
				Bearer bearer = parseBearer(parser);
				if(bearer != null) {
					retService.addBearer(bearer);
				}
			} else if(name.equals(RADIODNS_TAG)) {
				RadioDns rdns = parseRadioDns(parser);
				if(rdns != null) {
					retService.setRadioDns(rdns);
				}
			} else if(name.equals(GEOLOCATION_TAG)) {
				retService.setGeoLocation(parseGeoLocation(parser));
			} else if(name.equals(GENRE_TAG)) {
				Genre genre = parseGenre(parser);
				if(genre != null) {
					retService.addGenre(genre);
				}
			} else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skipping Tag within ServiceS: " + name);
				skip(parser);
			}
		}


		parser.require(XmlPullParser.END_TAG, NAMESPACE, SERVICE_TAG);

		return retService;
	}

	private RadioDns parseRadioDns(XmlPullParser parser) throws XmlPullParserException, IOException {
		parser.require(XmlPullParser.START_TAG, NAMESPACE, RADIODNS_TAG);

		RadioDns retDns = null;

		String fqdn = parser.getAttributeValue(NAMESPACE, RADIODNS_FQDN_ATTR);
		String sid = parser.getAttributeValue(NAMESPACE, RADIODNS_SID_ATTR);
		if(fqdn != null && sid != null) {
			retDns = new RadioDns(fqdn, sid);
		}

		parser.nextTag();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, RADIODNS_TAG);

		return retDns;
	}

	private ServiceProvider parseServiceProvider(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing ServiceProvider...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SERVICEPROVIDER_TAG);

		ServiceProvider provider = new ServiceProvider();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing ServiceProvider TagName: " + name);

			if(name.equals(NAME_SHORT_TAG)) {
				provider.addName(parseName(parser, NAME_SHORT_TAG, NameType.NAME_SHORT));
			} else if(name.equals(NAME_MEDIUM_TAG)) {
				provider.addName(parseName(parser, NAME_MEDIUM_TAG, NameType.NAME_MEDIUM));
			} else if(name.equals(NAME_LONG_TAG)) {
				provider.addName(parseName(parser, NAME_LONG_TAG, NameType.NAME_LONG));
			} else if(name.equals(MEDIADESCRIPTION_TAG)) {
				provider.addMediaDescription(parseMediaDescription(parser));
			} else if(name.equals(KEYWORDS_TAG)) {
				provider.setKeywords(readTagText(parser));
			} else if(name.equals(LINK_TAG)) {
				Link provLink = parseLink(parser);
				if(provLink != null) {
					provider.addLink(provLink);
				}
			} else if(name.equals(GEOLOCATION_TAG)) {
				provider.setGeolocation(parseGeoLocation(parser));
			}  else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skipping Tag within ServiceProvider: " + name);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, SERVICEPROVIDER_TAG);

		return provider;
	}

}
