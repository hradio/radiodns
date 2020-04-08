package eu.hradio.core.radiodns.radioepg.genre;

import java.util.HashMap;

public class TvaMediaTypeCs {

	public static String getMediaType(String termId) {
		String ret = mMediaTypeCs2010.get(termId);
		if(ret != null) {
			return ret;
		}

		return "";
	}

	private final static HashMap<String, String> mMediaTypeCs2010 = new HashMap<String, String>() {
		{
			put("7.0", "Proprietary");
			put("7.1", "Linear");
			put("7.1.1", "Audio only");
			put("7.1.2", "Video only");
			put("7.1.3", "Audio and video");
			put("7.1.4", "Multimedia");
			put("7.1.4.1", "Text");
			put("7.1.4.2", "Graphics");
			put("7.1.4.3", "Application");
			put("7.1.5", "Data");
			put("7.2", "Non Linear");
			put("7.2.1", "Audio only");
			put("7.2.2", "Video only");
			put("7.2.3", "Audio and video");
			put("7.2.4", "Multimedia");
			put("7.2.4.1", "Text");
			put("7.2.4.2", "Graphics");
			put("7.2.4.3", "Application");
			put("7.2.5", "Data");
			put("7.3", "AUDIO VIDEO ENHANCEMENTS");
			put("7.3.1", "Linear with non-sync");
			put("7.3.2", "Linear with sync");
			put("7.3.3", "Multi stream audio");
			put("7.3.4", "Multi stream video");
			put("7.3.5", "Non-linear single video/audio stream");
			put("7.3.6", "Non-linear multi stream");
			put("7.3.7", "Hybrid NVOD");
			put("7.3.8", "Mix and match");
			put("7.3.9", "Parallel \'layer controlled\' audio or video support");
			put("7.3.10", "Linear broadcast with online insertions");
			put("7.3.11", "Multimedia MashUp");
		}
	};
}
