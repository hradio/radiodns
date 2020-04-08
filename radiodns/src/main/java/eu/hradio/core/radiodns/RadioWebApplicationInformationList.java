package eu.hradio.core.radiodns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class RadioWebApplicationInformationList implements Serializable {

	private static final long serialVersionUID = 8587869123685218943L;
	
	private final static String TAG = "RWebAIL";

	private final TimePoint mCreationTime;
	private final String mOriginator;
	private final String mLanguage;

	private List<RadioWebApplication> mApps = new ArrayList<>();

	RadioWebApplicationInformationList(String creationTime, String originator, String language) {
		mCreationTime = new TimePoint(creationTime);
		mOriginator = originator;
		mLanguage = language;
	}

	void addApplication(RadioWebApplication app) {
		mApps.add(app);
	}

	public TimePoint getCreationTime() {
		return mCreationTime;
	}

	public String getOriginator() {
		return mOriginator;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public List<RadioWebApplication> getRadioWebApps() {
		return mApps;
	}
}
