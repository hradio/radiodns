package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaOriginationCs {

	public static String getOrigination(String termId) {
		String ret = mOriginationCs2011.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String> mOriginationCs2011 = new HashMap<String, String>() {
		{
			put("5.7", "Cinema"); //Made originally for viewing in the cinema
			put("5.7.1", "Made on location"); //.
			put("5.7.2", "Made in studio");
			put("5.7.3", "Made by the consumer");
			put("5.7.4", "Produced by major studio");
			put("5.7.5", "Produced by independent studio");
			put("5.8", "TV"); //Made originally for viewing on TV screens
			put("5.8.1", "Made on location"); //made at an event in an unstructured environment - where the action is based
			put("5.8.1.1", "Live"); //eg live sporting coverage, Live concerts, Operas etc from theatres
			put("5.8.1.2", "As Live"); //delayed relay of events - eg olympics frrom different time zone
			put("5.8.1.3", "Edited"); //edited highlights from event such as Match of the Day, NFL highlights
			put("5.8.2", "Made in studio"); //made in a purpose built studio or controlled environment such as quiz show from village hall where the action comes to the equipment required to make the programme
			put("5.8.2.1", "Live"); //News, Weather. Lottery etc
			put("5.8.2.2", "As Live"); //CNN rebroadcasts, Question Time,
			put("5.8.2.3", "Edited"); //Sitcoms, most entertainment shows, Dramas etc
			put("5.8.3", "Made by the consumer"); //e.g. home video shared between consumer
			put("5.9", "Radio"); //Made originally for listening on Radio
			put("5.9.1", "Made on location"); //made at an event in an unstructured environment - where the action is based
			put("5.9.1.1", "Live"); //eg live sporting coverage, Live concerts, Operas etc from theatres
			put("5.9.1.2", "As Live"); //delayed relay of events - eg olympics from different time zone
			put("5.9.1.3", "Edited"); //edited highlights from events such as Down Your Way,
			put("5.9.2", "Made in studio"); //made in a puropse built studio or controlled environment such as quiz show from village hall where the action comes to the equipment required to make the programme
			put("5.9.2.1", "Live"); //DJ hosted programme, live chat show
			put("5.9.2.2", "As Live"); //repeats of whole previous programmes, deferred relays for rights
			put("5.9.2.3", "Edited"); //radio drama, documentary etc
			put("5.9.3", "Made on consumer equipment (home audio)");
			put("5.9.3.1", "Live"); //live streaming etc
			put("5.9.3.2", "As Live"); //deferred live stream
			put("5.9.3.3", "Edited"); //home audio content
			put("5.10", "Online Distribution"); //Made originally for distribution over non broadcast networks (streaming or download)
			put("5.10.1", "Made on location"); //made at an event in an unstructured environment - where the action is based
			put("5.10.1.1", "Live"); //eg live sporting coverage, Live concerts, Operas etc from theatres
			put("5.10.1.2", "As Live"); //delayed relay of events - eg olympics from different time zone
			put("5.10.1.3", "Edited"); //edited highlights from event.
			put("5.10.2", "Made in studio"); //made in a purpose built studio or controlled environment
			put("5.10.2.1", "Live"); //News, Weather. Lottery etc
			put("5.10.2.2", "As Live"); //,
			put("5.10.2.3", "Edited");
			put("5.10.3", "Made on consumer equipment"); //eg home video shared between consumer
			put("5.10.3.1", "Live");
			put("5.10.3.2", "As Live");
			put("5.10.3.3", "Edited");
			put("5.11", "Offline Distribution"); //CDs DVDs Other removable media
		}
	};
}
