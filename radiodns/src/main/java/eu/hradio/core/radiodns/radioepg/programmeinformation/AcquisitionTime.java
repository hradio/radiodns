package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class AcquisitionTime implements Serializable {

	private static final long serialVersionUID = 133014625357314956L;

	private final TimePoint mStartTime;
	private final TimePoint mEndTime;

	public AcquisitionTime(@NonNull String startTime, @NonNull String endTime) {
		mStartTime = new TimePoint(startTime);
		mEndTime = new TimePoint(endTime);
	}

	public TimePoint getStartTime() {
		return mStartTime;
	}

	public TimePoint getEndTime() {
		return mEndTime;
	}
}
