package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import eu.hradio.core.radiodns.radioepg.time.Duration;
import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class Time implements Serializable {

	private static final long serialVersionUID = -5494181602001913657L;

	private final TimePoint mStartTime;
	private final Duration mDuration;
	private final TimePoint mActualStartTime;
	private final Duration mActualDuration;

	private final TimePoint mEndTime;

	public Time(@NonNull String startTime, @NonNull String duration, String actualStartTime, String actualDuration) {
		mStartTime = new TimePoint(startTime);
		mDuration = new Duration(duration);

		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());
		long endTimeMillis = mStartTime.getTimePointCalendar().getTimeInMillis() + mDuration.getDurationSeconds()*1000;
		Date endDate = new Date(endTimeMillis);
		mEndTime = new TimePoint(dateFormat.format(endDate));

		if(actualStartTime != null && !actualStartTime.isEmpty()) {
			mActualStartTime = new TimePoint(actualStartTime);
		} else {
			mActualStartTime = null;
		}

		if(actualDuration != null && !actualDuration.isEmpty()) {
			mActualDuration = new Duration(actualDuration);
		} else {
			mActualDuration = null;
		}
	}

	public Time(@NonNull String startTime, @NonNull String duration, String actualStartTime) {
		this(startTime, duration, actualStartTime, null);
	}

	public Time(@NonNull String startTime, @NonNull String duration) {
		this(startTime, duration, null, null);
	}

	public TimePoint getStartTime() {
		return mStartTime;
	}

	public Duration getDuration() {
		return mDuration;
	}

	public TimePoint getEndTime() {
		return mEndTime;
	}

	public TimePoint getActualStartTime() {
		return mActualStartTime;
	}

	public Duration getActualDuration() {
		return mActualDuration;
	}
}
