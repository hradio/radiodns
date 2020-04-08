package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaIntendedAudienceCs {

	public static String getIntentedAudience(String termId) {
		String ret = mIntentedAudienceCs2011.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String> mIntentedAudienceCs2011 = new HashMap<String, String>() {
		{
			put("4.0", "Proprietary");
			put("4.1", "GENERAL AUDIENCE");
			put("4.2", "AGE GROUPS");
			put("4.2.1", "Children");
			put("4.2.1.0", "specific single age");
			put("4.2.1.1", "age 4-7");
			put("4.2.1.2", "age 8-13");
			put("4.2.1.3", "age 14-15");
			put("4.2.1.4", "age 0-3");
			put("4.2.2", "Adults");
			put("4.2.2.1", "age 16-17");
			put("4.2.2.2", "age 18-24");
			put("4.2.2.3", "age 25-34");
			put("4.2.2.4", "age 35-44");
			put("4.2.2.5", "age 45-54");
			put("4.2.2.6", "age 55-64");
			put("4.2.2.7", "age 65+");
			put("4.2.2.8", "specific single age");
			put("4.2.3", "Teenager");
			put("4.2.4", "Pre-teens");
			put("4.3", "SOCIAL/REGIONAL/MINORITY GROUPS");
			put("4.3.1", "Ethnic");
			put("4.3.1.1", "Immigrant groups");
			put("4.3.1.2", "Indigenous");
			put("4.3.2", "Religious");
			put("4.3.3", "Disabled");
			put("4.4", "OCCUPATIONAL GROUPS");
			put("4.4.1", "AB");
			put("4.4.1.1", "A");
			put("4.4.1.2", "B");
			put("4.4.2", "C1C2");
			put("4.4.2.1", "White Collar worker");
			put("4.4.2.2", "Skilled manual labourer");
			put("4.4.3", "DE");
			put("4.4.3.1", "D");
			put("4.4.3.2", "E");
			put("4.5", "OTHER SPECIAL INTEREST/OCCUPATIONAL GROUPS");
			put("4.6", "GENDER");
			put("4.6.1", "Primarily for males");
			put("4.6.2", "Primarily for females");
			put("4.7", "GEOGRAPHICAL");
			put("4.7.1", "Universal");
			put("4.7.2", "Continental");
			put("4.7.3", "National");
			put("4.7.4", "Regional");
			put("4.7.5", "Local");
			put("4.7.6", "Multinational");
			put("4.8", "EDUCATION STANDARD");
			put("4.8.1", "Primary");
			put("4.8.2", "Secondary");
			put("4.8.3", "Tertiary");
			put("4.8.4", "Post Graduate/Life Long Learning");
			put("4.9", "LIFESTYLE STAGES");
			put("4.9.1", "Single");
			put("4.9.2", "Couple");
			put("4.9.3", "Family with Children 0-3");
			put("4.9.4", "Family with Children 4-7");
			put("4.9.5", "Family with Children 8-15");
			put("4.9.6", "Family with Children 16+");
			put("4.9.7", "Empty Nester");
			put("4.9.8", "Retired");
			put("4.9.9", "Family (mixed ages)");

			put("4.11", "LANGUAGE OF TARGET AUDIENCE");
		}
	};

}
