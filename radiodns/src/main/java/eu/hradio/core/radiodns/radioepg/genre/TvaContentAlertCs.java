package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaContentAlertCs {

	public static String getContentAlert(String termId) {
		String ret = mContentAlertCs2005.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String>  mContentAlertCs2005 = new HashMap<String, String>() {
		{
			put("6.0", "ALERT NOT REQUIRED");
			put("6.0.1", "No content that requires alerting in any of the categories below");
			put("6.1", "SEX");
			put("6.1.1", "No sex descriptors");
			put("6.1.2", "Obscured or implied sexual activity");
			put("6.1.3", "Frank portrayal of sex and sexuality");
			put("6.1.4", "Scenes of explicit sexual behaviour suitable for adults only");
			put("6.1.4.1", "One scene of explicit sexual behaviour suitable for adults only");
			put("6.1.4.2", "Occasional scenes of explicit sexual behaviour suitable for adults only");
			put("6.1.4.3", "Frequent scenes of explicit sexual behaviour suitable for adults only");
			put("6.1.5", "Sexual Violence");
			put("6.1.5.1", "One scene of sexual violence");
			put("6.1.5.2", "Occasional scenes of sexual violence");
			put("6.1.5.3", "Frequent scenes of sexual Violence");
			put("6.1.6", "Verbal sexual References");
			put("6.1.6.1", "One verbal sexual reference");
			put("6.1.6.2", "Occasional verbal sexual references");
			put("6.1.6.3", "Frequent verbal sexual references");
			put("6.2", "NUDITY");
			put("6.2.1", "No nudity descriptors");
			put("6.2.2", "Partial nudity");
			put("6.2.2.1", "One scene of partial nudity");
			put("6.2.2.2", "Occasional scenes of partial nudity");
			put("6.2.2.3", "Frequent scenes of partial nudity");
			put("6.2.3", "Full frontal nudity");put("6.2.3.1", "One scene of full frontal nudity");
			put("6.2.3.2", "Occasional scenes of full frontal nudity");
			put("6.2.3.3", "Frequent scenes of full frontal nudity");
			put("6.3", "VIOLENCE - HUMAN BEINGS");
			put("6.3.1", "No violence descriptors human beings");
			put("6.3.2", "Deliberate infliction of pain to human beings"); //Mild psychological or physical violence to human beings (psychological pressure, punching, slapping, knocking down.)
			put("6.3.2.1", "One Scene of deliberate infliction of pain to human beings"); //Mild psychological or physical violence to human beings (psychological pressure, punching, slapping, knocking down.)
			put("6.3.2.2", "Occasional deliberate infliction of pain to human beings"); //Mild psychological or physical violence to human beings (psychological pressure, punching, slapping, knocking down.)
			put("6.3.2.3", "Frequent deliberate infliction of pain to human beings"); //Mild psychological or physical violence to human beings (psychological pressure, punching, slapping, knocking down.)
			put("6.3.3", "Infliction of strong psychological or physical pain to human beings"); //heavy intimidation, torture, bloody scenes, accidental killing of human beings)
			put("6.3.3.1", "One scene of infliction of strong psychological or physical pain to human beings"); //heavy intimidation, torture, bloody scenes, accidental killing of human beings)
			put("6.3.3.2", "Occasional scenes of infliction of strong psychological or physical pain to human beings"); //heavy intimidation, torture, bloody scenes, accidental killing of human beings)
			put("6.3.3.3", "Frequent scenes of infliction of strong psychological or physical pain to human beings"); //heavy intimidation, torture, bloody scenes, accidental killing of human beings)
			put("6.3.4", "Deliberate killing of human beings");put("6.3.4.1", "One scene of deliberate killing of human beings");
			put("6.3.4.2", "Occasional deliberate killing of human beings");
			put("6.3.4.3", "Frequent deliberate killing of human beings");
			put("6.4", "VIOLENCE - ANIMALS");put("6.4.1", "No violence descriptors animals");
			put("6.4.2", "Deliberate infliction of pain to animals");put("6.4.2.1", "One scene of deliberate infliction of pain to animals");
			put("6.4.2.2", "Occasional deliberate infliction of pain to animals");
			put("6.4.2.3", "Frequent deliberate infliction of pain to animals");
			put("6.4.3", "Deliberate killing of animals");put("6.4.3.1", "One scene of deliberate killing of animals");
			put("6.4.3.2", "Occasional deliberate killing of animals");
			put("6.4.3.1", "Frequent deliberate killing of animals");
			put("6.5", "VIOLENCE - FANTASY CHARACTERS");
			put("6.5.1", "No violence descriptors");
			put("6.5.2", "Deliberate infliction of pain to fantasy characters (including animation)");
			put("6.5.2.1", "One scene of deliberate infliction of pain to fantasy characters (including animation)");
			put("6.5.2.2", "Occasional deliberate infliction of pain to fantasy characters (including animation)");
			put("6.5.2.3", "Frequent deliberate infliction of pain to fantasy characters (including animation)");
			put("6.5.3", "Deliberate killing of fantasy characters (including animation)");
			put("6.5.3.1", "One scene of deliberate killing of fantasy characters (including animation)");
			put("6.5.3.2", "Occasional deliberate killing of fantasy characters (including animation)");
			put("6.5.3.3", "Frequent deliberate killing of fantasy characters (including animation)");
			put("6.6", "LANGUAGE");
			put("6.6.1", "No language descriptors");
			put("6.6.2", "Occasional use of mild swear words and profanities");
			put("6.6.3", "Frequent use of mild swear words and profanities");
			put("6.6.4", "Occasional use of very strong language");
			put("6.6.5", "Frequent use of very strong language");
			put("6.6.6", "One use of very strong language");
			put("6.6.7", "Occasional use of strong language");
			put("6.6.8", "Frequent use of strong language");
			put("6.6.9", "One use of strong language");
			put("6.6.10", "Occasional use of offensive language (racist, homophobic, sexist)");
			put("6.6.11", "Frequent use of offensive language (racist, homophobic, sexist)");
			put("6.6.12", "One use of offensive language (racist, homophobic, sexist)");
			put("6.7", "DISTURBING SCENES");
			put("6.7.1", "No disturbing scenes descriptors");
			put("6.7.2", "Factual material that may cause distress, including verbal descriptions of traumatic events and the telling of sensitive human interest stories.");
			put("6.7.3", "Mild scenes of blood and gore (including medical procedures, injuries from accidents, terrorists attack, murder, disaster, war)");
			put("6.7.3.1", "One mild scene of blood and gore"); //including medical procedures, injuries from accidents, terrorists attack, murder, disaster, war)
			put("6.7.3.2", "Occasional mild scenes of blood and gore"); //including medical procedures, injuries from accidents, terrorists attack, murder, disaster, war)
			put("6.7.3.3", "Frequent mild scenes of blood and gore"); //including medical procedures, injuries from accidents, terrorists attack, murder, disaster, war)
			put("6.7.4", "Severe scenes of blood and gore (as 6.7.3 above)");
			put("6.7.4.1", "One severe scene of blood and gore");
			put("6.7.4.2", "Occasional severe scenes of blood and gore (as 6.7.3 above)");
			put("6.7.4.3", "Frequent severe scenes of blood and gore (as 6.7.3 above)");
			put("6.7.5", "Scenes with extreme horror effects");
			put("6.7.5.1", "One scene with extreme horror effects");
			put("6.7.5.2", "Occasional scenes with extreme horror effects");
			put("6.7.5.3", "Frequent scenes with extreme horror effects");
			put("6.8", "DISCRIMINATION");
			put("6.8.1", "No discrimination descriptors");
			put("6.8.2", "Deliberate discrimination or the portrayal of deliberate discrimination"); //including discrimination on the basis of gender, sexual orientation, race, religion, colour, nationality or ethnic background)
			put("6.9", "ILLEGAL DRUGS");
			put("6.9.1", "No illegal drugs descriptors");
			put("6.9.2", "Portrayal of illegal drug use");
			put("6.9.2.1", "One scene of illegal drug use");
			put("6.9.2.2", "Occasional portrayal of illegal drug use");
			put("6.9.2.3", "Frequent portrayal of illegal drug use");
			put("6.9.3", "Portrayal of illegal drug use with instructive detail");
			put("6.9.3.1", "One scene of illegal drug use with instructive detail");
			put("6.9.3.2", "Occasional portrayal of illegal drug use with instructive detail");
			put("6.9.3.3", "Frequent portrayal of illegal drug use with instructive detail");
			put("6.10", "STROBING");
			put("6.10.1", "No strobing");
			put("6.10.2", "Strobing that could impact on those suffering from Photosensitive epilepsy");
			put("6.10.2.1", "One scene of strobing that could impact on those suffering from photosensitive epilepsy");
			put("6.10.2.2", "Occasional strobing that could impact on those suffering from photosensitive epilepsy");
			put("6.10.2.3", "Frequent strobing that could impact on those suffering from photosensitive epilepsy");
		}
	};
}
