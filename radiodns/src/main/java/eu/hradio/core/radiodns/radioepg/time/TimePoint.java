package eu.hradio.core.radiodns.radioepg.time;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import static eu.hradio.core.radiodns.BuildConfig.DEBUG;

public class TimePoint implements Serializable {

	private static final long serialVersionUID = 9050635464076149559L;

	private final String mTimePoint;
	private TimeZone mTimeOffset;
	private Calendar mTimePointCalendar;

	public TimePoint(@NonNull String timepoint) {
		mTimePoint = timepoint.trim();

		if(!mTimePoint.isEmpty()) {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZZZ", Locale.getDefault());
			try {
				dateFormat.parse(mTimePoint.replaceAll("Z$", "+00:00"));
				mTimeOffset = dateFormat.getTimeZone();
				mTimePointCalendar = dateFormat.getCalendar();

			} catch (ParseException e) {
				if (DEBUG) e.printStackTrace();

				mTimePointCalendar = Calendar.getInstance();
				mTimePointCalendar.set(1970, 1, 1, 0, 0, 0);
				mTimeOffset = mTimePointCalendar.getTimeZone();
			}
		} else {
			mTimePointCalendar = Calendar.getInstance();
			mTimePointCalendar.set(1970, 1, 1, 0, 0, 0);
			mTimeOffset = mTimePointCalendar.getTimeZone();
		}
	}

	public String getTimePoint() {
		return mTimePoint;
	}

	public TimeZone getTimeOffset() {
		return mTimeOffset;
	}

	public Calendar getTimePointCalendar() {
		return mTimePointCalendar;
	}
}
