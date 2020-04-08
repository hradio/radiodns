package eu.hradio.core.radiodns;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.name.NameType;
import eu.hradio.core.radiodns.radioepg.scope.ServiceScope;

class RadioWebApplicationParser extends RadioEpgParser {

	private static final String TAG = "RWebAppParser";

	private RadioWebApplicationInformationList mParsedAil;

	private static final String NAMESPACE = null;

	private static final String APPLICATIONINFORMATION_TAG =          "applicationInformation";

	RadioWebApplicationInformationList parse(InputStream rwebDataStream) throws IOException {
		try {
			if(BuildConfig.DEBUG)Log.d(TAG, "Starting parser...");

			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(rwebDataStream, null);

			parser.nextTag();

			if(BuildConfig.DEBUG)Log.d(TAG, "Starting parser nextTag: " + parser.getName());

			mParsedAil = parseRoot(parser);

			while(parser.next() != XmlPullParser.END_TAG) {
				String tagName = parser.getName();
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					if(BuildConfig.DEBUG)Log.d(TAG, "NoStartTag: " + tagName + ", EventType: " + parser.getEventType());
					continue;
				}

				if(BuildConfig.DEBUG)Log.d(TAG, "TagName: " + tagName);

				if(tagName.equals(APPLICATION_TAG)) {
					mParsedAil.addApplication(parseApplication(parser));
				} else {
					skip(parser);
				}
			}


		} catch(XmlPullParserException ppExc) {
			if(BuildConfig.DEBUG)ppExc.printStackTrace();
		} finally {
			rwebDataStream.close();
		}

		return mParsedAil;
	}

	private RadioWebApplicationInformationList parseRoot(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing root...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, APPLICATIONINFORMATION_TAG);

		String creationTime = parser.getAttributeValue(NAMESPACE, CREATIONTIME_ATTR);
		String originator = parser.getAttributeValue(NAMESPACE, ORIGINATOR_ATTR);
		String xmlLang = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);

		if(xmlLang != null) {
			mDocumentLanguage = xmlLang;
		}

		return new RadioWebApplicationInformationList(creationTime == null ? "" : creationTime.trim(), originator == null ? "" : originator.trim(), xmlLang == null ? "en" : xmlLang.trim());
	}

	private RadioWebApplication parseApplication(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Application");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, APPLICATION_TAG);

		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Application TagName: " + parser.getName());

		String controlCode = parser.getAttributeValue(NAMESPACE, CONTROL_ATTR);
		String appId = parser.getAttributeValue(NAMESPACE, APPID_ATTR);
		String appPrio = parser.getAttributeValue(NAMESPACE, APPPRIO_ATTR);

		int appIdInt = -1;
		if(appId != null) {
			try {
				appIdInt = Integer.parseInt(appId.trim());
			} catch(NumberFormatException numExc) {
				if(BuildConfig.DEBUG)numExc.printStackTrace();
			}
		}

		int appPrioInt = -1;
		if(appPrio != null) {
			try {
				appPrioInt = Integer.parseInt(appPrio.trim());
			} catch(NumberFormatException numExc) {
				if(BuildConfig.DEBUG)numExc.printStackTrace();
			}
		}

		RadioWebApplication radioWebApp = new RadioWebApplication(RadioWebApplicationControl.getControl(controlCode), appIdInt, appPrioInt);
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String name = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Application TagName: " + name);

			if(name.equals(APPSCOPE_TAG)) {
				ServiceScope appScope = parseApplicationScope(parser);
				if(appScope != null) {
					radioWebApp.addServiceScope(appScope);
				}
			} else if(name.equals(NAME_SHORT_TAG)) {
				radioWebApp.addName(parseName(parser, NAME_SHORT_TAG, NameType.NAME_SHORT));
			} else if(name.equals(NAME_MEDIUM_TAG)) {
				radioWebApp.addName(parseName(parser, NAME_MEDIUM_TAG, NameType.NAME_MEDIUM));
			} else if(name.equals(NAME_LONG_TAG)) {
				radioWebApp.addName(parseName(parser, NAME_LONG_TAG, NameType.NAME_LONG));
			} else if(name.equals(MEDIADESCRIPTION_TAG)) {
				radioWebApp.addMediaDescription(parseMediaDescription(parser));
			}  else if(name.equals(BEARER_TAG)) {
				Bearer bearer = parseBearer(parser);
				if(bearer != null) {
					radioWebApp.addBearer(bearer);
				}
			} else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skipping Tag: " + name);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, APPLICATION_TAG);

		if(BuildConfig.DEBUG) Log.d(TAG, "Parsing finished");

		return radioWebApp;
	}
}
