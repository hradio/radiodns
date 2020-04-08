package eu.hradio.core.radiodns.radioepg.time;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Duration implements Serializable {

	private static final long serialVersionUID = 5563294957799955046L;

	private final String mDurationString;
	private final int mDurationSeconds;

	public Duration(@NonNull String duration) {
		mDurationString = duration.trim();

		String durString = "";
		if(mDurationString.length() > 0) {
			durString = mDurationString.substring(2);
		}

		int hours = 0;
		int minutes = 0;
		int seconds = 0;

		//check if hours are present
		if(durString.length() > 0 && mDurationString.contains("H")) {
			String hoursString = durString.split("H")[0];
			hours = Integer.parseInt(hoursString) * 60 * 60;
			durString = mDurationString.substring(mDurationString.indexOf("H") + 1);
		}

		if(durString.length() > 0 && mDurationString.contains("M")) {
			String minutesString = durString.split("M")[0];
			minutes = Integer.parseInt(minutesString) * 60;
			durString = mDurationString.substring(mDurationString.indexOf("M") + 1);
		}

		if(durString.length() > 0 && mDurationString.contains("S")) {
			String secondsString = durString.split("S")[0];
			seconds = Integer.parseInt(secondsString);
		}

		mDurationSeconds = hours + minutes + seconds;
	}

	public int getDurationSeconds() {
		return mDurationSeconds;
	}
}
