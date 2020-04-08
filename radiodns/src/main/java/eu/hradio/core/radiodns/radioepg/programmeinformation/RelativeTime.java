package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;

import eu.hradio.core.radiodns.radioepg.time.Duration;

public class RelativeTime implements Serializable {

	private static final long serialVersionUID = 8444950190550829120L;

	private final Duration mStartTime;
	private final Duration mDuration;
	private final Duration mActualStartTime;
	private final Duration mActualDuration;

	public RelativeTime(@NonNull String startTime, @NonNull String duration, String actualStartTime, String actualDuration) {
		mStartTime = new Duration(startTime);
		mDuration = new Duration(duration);

		if(actualStartTime != null && !actualStartTime.isEmpty()) {
			mActualStartTime = new Duration(actualStartTime);
		} else {
			mActualStartTime = null;
		}

		if(actualDuration != null && !actualDuration.isEmpty()) {
			mActualDuration = new Duration(actualDuration);
		} else {
			mActualDuration = null;
		}
	}

	public RelativeTime(@NonNull String startTime, @NonNull String duration, String actualStartTime) {
		this(startTime, duration, actualStartTime, null);
	}

	public RelativeTime(@NonNull String startTime, @NonNull String duration) {
		this(startTime, duration, null, null);
	}

	/**
	 * Billed start time offset of the programme event from
	 * the start of the programme
	 * @return the {@link Duration}
	 */
	public Duration getStartTime() {
		return mStartTime;
	}

	/**
	 * Billed duration of the programme event. This should
	 * be the duration as advertised.
	 * @return the {@link Duration}
	 */
	public Duration getDuration() {
		return mDuration;
	}

	/**
	 * May be defined if the actual start time offset of the
	 * programme event differs from the billed time offset.
	 * @return the {@link Duration} or {@code null}
	 */
	public Duration getActualStartTime() {
		return mActualStartTime;
	}

	/**
	 * May be defined if the actual duration of the
	 * programme event differs from the billed duration.
	 * @return the {@link Duration} or {@code null}
	 */
	public Duration getActualDuration() {
		return mActualDuration;
	}
}
