package eu.hradio.core.radiodns;

import android.util.Log;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.crid.Crid;
import eu.hradio.core.radiodns.radioepg.crid.ShortCrid;
import eu.hradio.core.radiodns.radioepg.genre.Genre;
import eu.hradio.core.radiodns.radioepg.keywords.Keywords;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.name.NameType;
import eu.hradio.core.radiodns.radioepg.programmeinformation.AcquisitionTime;
import eu.hradio.core.radiodns.radioepg.programmeinformation.BroadcastType;
import eu.hradio.core.radiodns.radioepg.programmeinformation.Location;
import eu.hradio.core.radiodns.radioepg.programmeinformation.OnDemand;
import eu.hradio.core.radiodns.radioepg.programmeinformation.PresentationTime;
import eu.hradio.core.radiodns.radioepg.programmeinformation.Programme;
import eu.hradio.core.radiodns.radioepg.programmeinformation.ProgrammeEvent;
import eu.hradio.core.radiodns.radioepg.programmeinformation.RelativeTime;
import eu.hradio.core.radiodns.radioepg.programmeinformation.Schedule;
import eu.hradio.core.radiodns.radioepg.programmeinformation.Time;
import eu.hradio.core.radiodns.radioepg.scope.Scope;
import eu.hradio.core.radiodns.radioepg.scope.ServiceScope;
import eu.hradio.core.radiodns.radioepg.time.Duration;
import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class RadioEpgPiParser extends RadioEpgParser {

	private static final String TAG = "REpgPiParser";

	private RadioEpgProgrammeInformation mParsedPi;

	private static final String EPG_TAG                 = "epg";
	private static final String SCHEDULE_TAG            = "schedule";
	private static final String SCOPE_TAG               = "scope";

	private static final String START_TIME_ATTR         = "startTime";
	private static final String STOP_TIME_ATTR          = "stopTime";

	private static final String PROGRAMME_TAG           = "programme";
	private static final String TIME_TAG                = "time";
	private static final String RELATIVE_TIME_TAG       = "relativeTime";
	private static final String PROGRAMME_EVENT_TAG     = "programmeEvent";
	private static final String LOCATION_TAG            = "location";
	private static final String ONDEMAND_TAG            = "onDemand";
	private static final String PRESENTATIONTIME_TAG    = "presentationTime";
	private static final String ACQUISITIONTIME_TAG     = "acquisitionTime";

	private static final String START_ATTR              = "start";
	private static final String END_ATTR                = "end";
	private static final String DURATION_ATTR           = "duration";
	private static final String ACTUALTIME_ATTR         = "actualTime";
	private static final String ACTUALDURATION_ATTR     = "actualDuration";

	private static final String TIME_ATTR               = "time";

	private static final String RECOMMENDATION_ATTR     = "recommendation";
	private static final String BROADCAST_ATTR          = "broadcast";

	RadioEpgProgrammeInformation parse(InputStream piDataStream) throws IOException {
		try {
			if(BuildConfig.DEBUG) Log.d(TAG, "Starting parser...");

			XmlPullParser parser = Xml.newPullParser();
			parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
			parser.setInput(piDataStream, NAMESPACE);

			parser.nextTag();

			if(BuildConfig.DEBUG)Log.d(TAG, "Starting parser nextTag: " + parser.getName());

			mParsedPi = new RadioEpgProgrammeInformation();

			while(parser.next() != XmlPullParser.END_TAG) {
				String tagName = parser.getName();
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					if(BuildConfig.DEBUG)Log.d(TAG, "NoStartTag: " + tagName + ", EventType: " + parser.getEventType());
					continue;
				}

				if(BuildConfig.DEBUG)Log.d(TAG, "TagName: " + tagName);

				if(tagName.equals(SCHEDULE_TAG)) {
					mParsedPi.addSchedule(parseSchedule(parser));
				} else {
					if(BuildConfig.DEBUG)Log.d(TAG, "Skiping Tag:" + tagName);
					skip(parser);
				}
			}


		} catch(XmlPullParserException ppExc) {
			if(BuildConfig.DEBUG)ppExc.printStackTrace();
		} finally {
			piDataStream.close();
		}

		return mParsedPi;
	}

	private Schedule parseSchedule(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Schedule...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SCHEDULE_TAG);

		String creationTimeString = parser.getAttributeValue(NAMESPACE, CREATIONTIME_ATTR);
		String originatorString = parser.getAttributeValue(NAMESPACE, ORIGINATOR_ATTR);
		String versionString = parser.getAttributeValue(NAMESPACE, VERSION_ATTR);
		String langString = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);

		if(creationTimeString == null) {
			creationTimeString = "";
		}
		if(originatorString == null) {
			originatorString = "";
		}

		int version = 1;
		if(versionString != null) {
			try {
				version = Integer.parseInt(versionString.trim());
			} catch(NumberFormatException numExc) {
				if(BuildConfig.DEBUG)numExc.printStackTrace();
			}
		}

		if(langString == null) {
			langString = mDocumentLanguage;
		}

		Schedule retSchedule = new Schedule(creationTimeString, originatorString, version, langString);

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String tagName = parser.getName();
			if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Schedule TagName: " + tagName);

			if(tagName.equals(SCOPE_TAG)) {

				Scope schedScope = parseScope(parser);
				if(schedScope != null) {
					retSchedule.setScope(schedScope);
				}
			} else if(tagName.equals(PROGRAMME_TAG)) {
				Programme prog = parseProgramme(parser);
				if(prog != null) {
					retSchedule.addProgramme(prog);
				}
			} else {
				if(BuildConfig.DEBUG)Log.d(TAG, "Skiping Tag within Schedule: " + tagName);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, SCHEDULE_TAG);

		return retSchedule;
	}

	private Scope parseScope(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Scope...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, SCOPE_TAG);

		String startTime = parser.getAttributeValue(NAMESPACE, START_TIME_ATTR);
		String stopTime = parser.getAttributeValue(NAMESPACE, STOP_TIME_ATTR);

		Scope retScope = null;

		if(startTime != null && stopTime != null) {
			retScope = new Scope(startTime, stopTime);

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}

				String tagName = parser.getName();
				if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Scope TagName: " + tagName);

				if(tagName.equals(SERVICESCOPE_TAG)) {
					ServiceScope srvScope = parseServiceScope(parser);
					if(srvScope != null) {
						retScope.addServiceScope(srvScope);
					}
				} else {
					if(BuildConfig.DEBUG)Log.w(TAG, "Skiping within Schedule-Scope: " + tagName);
					skip(parser);
				}
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, SCOPE_TAG);

		//parser.nextTag();

		return retScope;
	}

	private Programme parseProgramme(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Programme...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, PROGRAMME_TAG);

		Programme retProg = null;

		String cridIdString = parser.getAttributeValue(NAMESPACE, ID_ATTR);
		String shortCridIdString = parser.getAttributeValue(NAMESPACE, SHORT_ID_ATTR);

		int shortId = -1;
		try {
			shortId = Integer.parseInt(shortCridIdString.trim());
		} catch(NumberFormatException numExc) {
			if(BuildConfig.DEBUG)Log.w(TAG, "Error parsing ShortCridId: " + shortCridIdString); numExc.printStackTrace();
		}

		if(shortId != -1 && cridIdString != null) {
			String versionString = parser.getAttributeValue(NAMESPACE, VERSION_ATTR);
			String recommendationString = parser.getAttributeValue(NAMESPACE, RECOMMENDATION_ATTR);
			String broadcastString = parser.getAttributeValue(NAMESPACE, BROADCAST_ATTR);
			String langString = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);

			int version = 1;
			if(versionString != null) {
				try {
					version = Integer.parseInt(versionString.trim());
				} catch(NumberFormatException numExc) {
					if(BuildConfig.DEBUG)Log.w(TAG, "Error parsing Version: " + versionString); numExc.printStackTrace();
				}
			}

			boolean isRecomm = false;
			if(recommendationString != null) {
				if(recommendationString.trim().equals("yes")) {
					isRecomm = true;
				}
			}

			BroadcastType broadType = BroadcastType.BROADCAST_TYPE_ON_AIR;
			if(broadcastString != null) {
				BroadcastType broad = BroadcastType.fromString(broadcastString.trim());
			}

			if(langString == null) {
				langString = mDocumentLanguage;
			}

			retProg = new Programme(new ShortCrid(shortId), new Crid(cridIdString.trim()), version, isRecomm, broadType, langString);

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}

				String tagName = parser.getName();
				if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Programme TagName: " + tagName);

				if(tagName.equals(NAME_SHORT_TAG)) {
					retProg.addName(parseName(parser, NAME_SHORT_TAG, NameType.NAME_SHORT));
				} else if(tagName.equals(NAME_MEDIUM_TAG)) {
					retProg.addName(parseName(parser, NAME_MEDIUM_TAG, NameType.NAME_MEDIUM));
				} else if(tagName.equals(NAME_LONG_TAG)) {
					retProg.addName(parseName(parser, NAME_LONG_TAG, NameType.NAME_LONG));
				} else if(tagName.equals(MEDIADESCRIPTION_TAG)) {
					retProg.addMediaDescription(parseMediaDescription(parser));
				} else if(tagName.equals(GENRE_TAG)) {
					Genre genre = parseGenre(parser);
					if(genre != null) {
						retProg.addGenre(genre);
					}
				} else if(tagName.equals(KEYWORDS_TAG)) {
					retProg.addKeywords(new Keywords(readTagText(parser)));
				} else if(tagName.equals(LINK_TAG)) {
					Link link = parseLink(parser);
					if(link != null) {
						retProg.addLink(link);
					}
				} else if(tagName.equals(ONDEMAND_TAG)) {
					OnDemand onDemand = parseOndemand(parser);
					if(onDemand != null) {
						retProg.addOnDemand(onDemand);
					}
				} else if(tagName.equals(LOCATION_TAG)) {
					retProg.addLocation(parseLocation(parser));
				} else if(tagName.equals(PROGRAMME_EVENT_TAG)) {
					ProgrammeEvent progEvent = parseProgrammeEvent(parser);
					if(progEvent != null) {
						retProg.addProgrammeEvent(progEvent);
					}
				} else {
					if(BuildConfig.DEBUG)Log.w(TAG, "Skiping within Schedule-Programme: " + tagName);
					skip(parser);
				}
			}
		} else {
			if(BuildConfig.DEBUG)Log.w(TAG, "Error parsing required Programme Attributes: " + shortCridIdString + " : " + cridIdString + ", advancing parser");
			parser.nextTag();
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, PROGRAMME_TAG);

		return retProg;
	}

	private ProgrammeEvent parseProgrammeEvent(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing ProgrammeEvent...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, PROGRAMME_EVENT_TAG);

		ProgrammeEvent retProg = null;

		String cridIdString = parser.getAttributeValue(NAMESPACE, ID_ATTR);
		String shortCridIdString = parser.getAttributeValue(NAMESPACE, SHORT_ID_ATTR);

		int shortId = -1;
		try {
			shortId = Integer.parseInt(shortCridIdString.trim());
		} catch(NumberFormatException numExc) {
			if(BuildConfig.DEBUG)Log.w(TAG, "Error parsing ShortCridId: " + shortCridIdString); numExc.printStackTrace();
		}

		if(shortId != -1 && cridIdString != null) {
			String versionString = parser.getAttributeValue(NAMESPACE, VERSION_ATTR);
			String recommendationString = parser.getAttributeValue(NAMESPACE, RECOMMENDATION_ATTR);
			String broadcastString = parser.getAttributeValue(NAMESPACE, BROADCAST_ATTR);
			String langString = parser.getAttributeValue(NAMESPACE, XMLLANG_ATTR);

			int version = 1;
			if(versionString != null) {
				try {
					version = Integer.parseInt(versionString.trim());
				} catch(NumberFormatException numExc) {
					if(BuildConfig.DEBUG)Log.w(TAG, "Error parsing Version: " + versionString); numExc.printStackTrace();
				}
			}

			boolean isRecomm = false;
			if(recommendationString != null) {
				if(recommendationString.trim().equals("yes")) {
					isRecomm = true;
				}
			}

			BroadcastType broadType = BroadcastType.BROADCAST_TYPE_ON_AIR;
			if(broadcastString != null) {
				BroadcastType broad = BroadcastType.fromString(broadcastString.trim());
			}

			if(langString == null) {
				langString = mDocumentLanguage;
			}

			retProg = new ProgrammeEvent(new ShortCrid(shortId), new Crid(cridIdString.trim()), version, isRecomm, broadType, langString);

			while (parser.next() != XmlPullParser.END_TAG) {
				if (parser.getEventType() != XmlPullParser.START_TAG) {
					continue;
				}

				String tagName = parser.getName();
				if(BuildConfig.DEBUG)Log.d(TAG, "Parsing ProgrammeEvent TagName: " + tagName);

				if(tagName.equals(NAME_SHORT_TAG)) {
					retProg.addName(parseName(parser, NAME_SHORT_TAG, NameType.NAME_SHORT));
				} else if(tagName.equals(NAME_MEDIUM_TAG)) {
					retProg.addName(parseName(parser, NAME_MEDIUM_TAG, NameType.NAME_MEDIUM));
				} else if(tagName.equals(NAME_LONG_TAG)) {
					retProg.addName(parseName(parser, NAME_LONG_TAG, NameType.NAME_LONG));
				} else if(tagName.equals(MEDIADESCRIPTION_TAG)) {
					retProg.addMediaDescription(parseMediaDescription(parser));
				} else if(tagName.equals(GENRE_TAG)) {
					Genre genre = parseGenre(parser);
					if(genre != null) {
						retProg.addGenre(genre);
					}
				} else if(tagName.equals(KEYWORDS_TAG)) {
					retProg.addKeywords(new Keywords(readTagText(parser)));
				} else if(tagName.equals(LINK_TAG)) {
					Link link = parseLink(parser);
					if(link != null) {
						retProg.addLink(link);
					}
				} else if(tagName.equals(ONDEMAND_TAG)) {
					OnDemand onDemand = parseOndemand(parser);
					if(onDemand != null) {
						retProg.addOnDemand(onDemand);
					}
				} else if(tagName.equals(LOCATION_TAG)) {
					retProg.addLocation(parseLocation(parser));
				} else {
					if(BuildConfig.DEBUG)Log.w(TAG, "Skiping within ProgrammeEvent: " + tagName);
					skip(parser);
				}
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, PROGRAMME_EVENT_TAG);

		return retProg;
	}

	private Location parseLocation(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Location...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, LOCATION_TAG);

		Location retLoc = new Location();

		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String tagName = parser.getName();
			if (BuildConfig.DEBUG) Log.d(TAG, "Parsing Location TagName: " + tagName);

			if(tagName.equals(TIME_TAG)) {
				Time time = parseTime(parser);
				if(time != null) {
					retLoc.addTime(time);
				}
			} else if(tagName.equals(RELATIVE_TIME_TAG)) {
				RelativeTime relTime = parseRelativeTime(parser);
				if(relTime != null) {
					retLoc.addRelativeTime(relTime);
				}
			} else if(tagName.equals(BEARER_TAG)) {
				Bearer locBearer = parseBearer(parser);
				if(locBearer != null) {
					retLoc.addBearer(locBearer);
				}
			} else {
				if(BuildConfig.DEBUG)Log.w(TAG, "Skiping within Location: " + tagName);
				skip(parser);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, LOCATION_TAG);

		return retLoc;
	}

	private Time parseTime(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Time...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, TIME_TAG);

		Time retTime = null;

		String timeString = parser.getAttributeValue(NAMESPACE, TIME_ATTR);
		String durationString = parser.getAttributeValue(NAMESPACE, DURATION_ATTR);
		String actualTimeString = parser.getAttributeValue(NAMESPACE, ACTUALTIME_ATTR);
		String actualDurationString = parser.getAttributeValue(NAMESPACE, ACTUALDURATION_ATTR);

		if(timeString != null && durationString != null) {
			retTime = new Time(timeString.trim(), durationString.trim(), actualTimeString != null ? actualTimeString.trim() : null, actualDurationString != null ? actualDurationString.trim() : null);
		}

		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing Time, advancing parser");
		parser.next();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, TIME_TAG);

		return retTime;
	}

	private RelativeTime parseRelativeTime(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing RelativeTime...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, RELATIVE_TIME_TAG);

		RelativeTime retRelTime = null;

		String timeString = parser.getAttributeValue(NAMESPACE, TIME_ATTR);
		String durationString = parser.getAttributeValue(NAMESPACE, DURATION_ATTR);
		String actualTimeString = parser.getAttributeValue(NAMESPACE, ACTUALTIME_ATTR);
		String actualDurationString = parser.getAttributeValue(NAMESPACE, ACTUALDURATION_ATTR);

		if(timeString != null && durationString != null) {
			retRelTime = new RelativeTime(timeString.trim(), durationString.trim(), actualTimeString != null ? actualTimeString.trim() : null, actualDurationString != null ? actualDurationString.trim() : null);
		}

		parser.next();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, RELATIVE_TIME_TAG);

		return retRelTime;
	}

	private OnDemand parseOndemand(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing OnDemand...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, ONDEMAND_TAG);

		OnDemand retOd = null;

		PresentationTime presTime = null;
		List<Bearer> bearers = new ArrayList<>();
		List<AcquisitionTime> acquTimes = new ArrayList<>();
		while (parser.next() != XmlPullParser.END_TAG) {
			if (parser.getEventType() != XmlPullParser.START_TAG) {
				continue;
			}

			String tagName = parser.getName();
			if (BuildConfig.DEBUG) Log.d(TAG, "Parsing OnDemand TagName: " + tagName);

			if(tagName.equals(PRESENTATIONTIME_TAG)) {
				presTime = parsePresentationTime(parser);
			} else if(tagName.equals(BEARER_TAG)) {
				Bearer bearer = parseBearer(parser);
				if(bearer != null) {
					bearers.add(bearer);
				}
			} else if(tagName.equals(ACQUISITIONTIME_TAG)) {
				AcquisitionTime acquTime = parseAcquisitionTime(parser);
				if(acquTime != null) {
					acquTimes.add(acquTime);
				}
			} else {
				if(BuildConfig.DEBUG)Log.w(TAG, "Skiping within OnDemand: " + tagName);
				skip(parser);
			}
		}

		if(presTime != null && !bearers.isEmpty()) {
			retOd = new OnDemand(presTime, bearers);

			if(!acquTimes.isEmpty()) {
				retOd.addAcquisitionTimes(acquTimes);
			}
		}

		parser.require(XmlPullParser.END_TAG, NAMESPACE, ONDEMAND_TAG);

		return retOd;
	}

	private PresentationTime parsePresentationTime(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing PresentationTime...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, PRESENTATIONTIME_TAG);

		PresentationTime retTime = null;

		String durationString = parser.getAttributeValue(NAMESPACE, DURATION_ATTR);
		String startTimeString = parser.getAttributeValue(NAMESPACE, START_ATTR);
		String endTimeString = parser.getAttributeValue(NAMESPACE, END_ATTR);

		if(durationString != null) {
			Duration duration = new Duration(durationString.trim());

			TimePoint startTime = null;
			if(startTimeString != null) {
				startTime = new TimePoint(startTimeString.trim());
			}

			TimePoint endTime = null;
			if(endTimeString != null) {
				endTime = new TimePoint(endTimeString.trim());
			}

			retTime = new PresentationTime(duration);

			if(startTime != null) {
				retTime.setStartTime(startTime);
			}
			if(endTime != null) {
				retTime.setEndTime(endTime);
			}
		}

		parser.next();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, PRESENTATIONTIME_TAG);

		return retTime;
	}

	private AcquisitionTime parseAcquisitionTime(XmlPullParser parser) throws XmlPullParserException, IOException {
		if(BuildConfig.DEBUG)Log.d(TAG, "Parsing AcquisitionTime...");

		parser.require(XmlPullParser.START_TAG, NAMESPACE, ACQUISITIONTIME_TAG);

		AcquisitionTime acquTime = null;

		String startTimeString = parser.getAttributeValue(NAMESPACE, START_ATTR);
		String endTimeString = parser.getAttributeValue(NAMESPACE, END_ATTR);

		if(startTimeString != null && endTimeString != null) {
			acquTime = new AcquisitionTime(startTimeString.trim(), endTimeString.trim());
		}

		parser.next();

		parser.require(XmlPullParser.END_TAG, NAMESPACE, ACQUISITIONTIME_TAG);

		return acquTime;
	}
}
