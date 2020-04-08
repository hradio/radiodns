package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaIntentionCs {

	/**
	 * Returns the associated IntentionCs value for the given TermID or {@code null}
	 * @param termId the TermId to lookup
	 * @return the Intention or {@code null} if the TermId is unknown
	 */
	public static String getIntention(String termId) {
		String ret = mIntentionCs2005.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String> mIntentionCs2005 = new HashMap<String, String>() {
		{
			put("1.0", "Proprietary");
			put("1.1", "ENTERTAIN");
			put("1.1.1", "Pure entertainment");
			put("1.1.2", "Informative Entertainment");
			put("1.2", "INFORM");
			put("1.2.1", "Government");
			put("1.2.2", "Pure information");
			put("1.2.3", "Infotainment");
			put("1.2.4", "Advice");
			put("1.3", "EDUCATE");
			put("1.3.1", "School Programmes");
			put("1.3.1.1", "Primary");
			put("1.3.1.2", "Secondary");
			put("1.3.2", "Lifelong / further education");
			put("1.4", "PROMOTE");
			put("1.5", "ADVERTISE");
			put("1.6", "RETAIL");
			put("1.6.1", "Gambling");
			put("1.6.2", "Home Shopping");
			put("1.7", "FUND RAISE/SOCIAL ACTION");
			put("1.7.1", "Fund Raising");
			put("1.7.2", "Social Action");
			put("1.8", "ENRICH");
			put("1.8.1", "General enrichment");
			put("1.8.2", "Inspirational enrichment");
			put("1.9", "EDUCATIONAL DIFFICULTY");
			put("1.9.1", "Very Easy");
			put("1.9.2", "Easy");
			put("1.9.3", "Medium");
			put("1.9.4", "Difficult");
			put("1.9.5", "Very Difficult");
		}
	};
}
