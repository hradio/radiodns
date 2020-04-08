package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaAtmosphereCs {

	public static String getAtmosphere(String termId) {
		String ret = mAtmosphereCs2005.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String> mAtmosphereCs2005 = new HashMap<String, String>() {
		{
			put("8.0", "Proprietary"); put("8.1", "Alternative"); //Unconventional, not mainstream
			put("8.2",  "Analytical"); //Factual, in-depth, investigative, probing
			put("8.3",  "Astonishing"); //Amazing, surprising, breathtaking
			put("8.4", "Ambitious"); //Far reaching, high-aims, strongly determined
			put("8.5", "Black"); //Bleak, sinister, dark
			put("8.6", "Breathtaking");
			put("8.7", "Chilling"); //Hair-raising, spine-tingling
			put("8.8", "Coarse"); //Crude, lacking refinement, rough, lewd
			put("8.9", "Compelling"); //Gripping, rousing strong interest, conviction or admiration
			put("8.10", "Confrontational");
			put("8.11", "Contemporary"); //Modern in style or design, up-to-date
			put("8.12", "Crazy"); //Insane, mad, foolish
			put("8.13", "Cutting edge"); //Leading the way, in the vanguard
			put("8.14", "Eclectic"); //Mixed, collection, selecting ideas and styles from various sources
			put("8.15", "Edifying"); //Morally or intellectually improving
			put("8.16", "Exciting"); //Arousing great interest or enthusiasm, thrilling
			put("8.17", "Fast-moving"); //Rapid action, adrenaline-charged, dynamic, energetic
			put("8.18", "Frantic"); //Frenzied, hurried
			put("8.19", "Fun"); //Lively or playful amusement, enjoyable, not for a serious purpose
			put("8.20", "Gripping");
			put("8.21", "Gritty"); //Basic, no frills
			put("8.22", "Gutsy"); //Full-on, no holds barred, courageous
			put("8.23", "Happy"); //Feeling or showing pleasure or contentment, upbeat, uplifting
			put("8.24", "Heart-rending"); //Emotionally-charged, distressing, painful, tear-jerker
			put("8.25", "Heart-warming"); //Emotionally rewarding or uplifting, charming, delightful, enchanting
			put("8.26", "Hot"); //Fresh, recent, of the moment
			put("8.27", "Humorous"); //Amusing, hilarious, lighthearted, witty
			put("8.28", "Innovative"); //Ground-breaking, landmark, new ideas and methods
			put("8.29", "Insightful");
			put("8.30", "Inspirational"); //Uplifting, stimulating creative activity
			put("8.31", "Intriguing"); //Arousing/inspiring curiosity
			put("8.32", "Irreverent"); //Anti-establishment, lacking reverence for established principles and ways of behaving
			put("8.33", "Laid back"); //Calm, relaxed, easy-going
			put("8.34", "Outrageous"); //Shocking, in-your-face
			put("8.35", "Peaceful");
			put("8.36", "Powerful"); //Influential, emotionally-charged, strong
			put("8.37", "Practical");
			put("8.38", "Rollercoaster"); //Emotional up and downs, unpredictable, uncontrollable
			put("8.39", "Romantic"); //About love, being in love
			put("8.40", "Rousing"); //Stirring, energizing, exciting
			put("8.41", "Sad"); //Unhappy, causing sorrow, tragic, pitiful
			put("8.42", "Satirical"); //Irony, used to expose folly or vice, ridicule
			put("8.43", "Serious"); //Earnest, important, demanding consideration, not frivolous
			put("8.44", "Sexy"); //Racy, raunchy, steamy, sexually arousing, stimulating
			put("8.45", "Shocking"); //Causing shock or scandal
			put("8.46", "Silly"); //Foolish, imprudent, weak-minded
			put("8.47", "Spooky"); //Creepy, eerie, ghoulish
			put("8.48", "Stunning"); //Striking, visually impressive or attractive
			put("8.49", "Stylish"); //Fashionable, elegant
			put("8.50", "Terrifying"); //Scary, causing extreme fear
			put("8.51", "Thriller"); //Exciting or sensational story
			put("8.52", "Violent"); //Involving great physical force, violence
			put("8.53", "Wacky"); //Crazy, kooky, ridiculous, zany
		}
	};
}
