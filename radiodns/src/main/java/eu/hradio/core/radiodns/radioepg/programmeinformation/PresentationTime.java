package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.hradio.core.radiodns.radioepg.time.Duration;
import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class PresentationTime implements Serializable {

	private static final long serialVersionUID = 8918595064635296812L;

	private final Duration mDuration;

	private TimePoint mStartTime;
	private TimePoint mEndTime;

	public PresentationTime(@NonNull Duration duration) {
		mDuration = duration;
	}

	public PresentationTime(@NonNull String duration) {
		mDuration = new Duration(duration);
	}

	public void setStartTime(@NonNull TimePoint timepoint) {
		mStartTime = timepoint;
	}

	public void setStartTime(@NonNull String timepoint) {
		mStartTime = new TimePoint(timepoint);
	}

	public void setEndTime(@NonNull TimePoint timepoint) {
		mEndTime = timepoint;
	}

	public void setEndTime(@NonNull String timepoint) {
		mEndTime = new TimePoint(timepoint);
	}

	public Duration getDuration() {
		return mDuration;
	}

	public TimePoint getStartTime() {
		return mStartTime;
	}

	public TimePoint getEndTime() {
		return mEndTime;
	}
}
