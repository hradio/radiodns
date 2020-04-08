package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.scope.Scope;
import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class Schedule implements Serializable {

	private static final long serialVersionUID = -8949115745750085336L;

	private final TimePoint mCreationTime;
	private final String mOriginator;
	private final int mVersion;
	private final String mLanguage;

	private Scope mScope;
	private List<Programme> mProgrammes = new ArrayList<>();

	public Schedule(@NonNull String creationTime, @NonNull String originator, int version, @NonNull String language) {
		mCreationTime = new TimePoint(creationTime);
		mOriginator = originator;
		mVersion = version;
		mLanguage = language;
	}

	public Schedule(@NonNull String creationTime, @NonNull String originator, int version) {
		this(creationTime, originator, version, "en");
	}

	public Schedule(@NonNull String creationTime, @NonNull String originator) {
		this(creationTime, originator, 1, "en");
	}

	public Schedule(@NonNull String creationTime) {
		this(creationTime, "", 1, "en");
	}

	public Schedule() {
		this("", "", 1, "en");
	}

	public void setScope(Scope scope) {
		mScope = scope;
	}

	public void addProgramme(Programme programme) {
		mProgrammes.add(programme);
	}

	public TimePoint getCreationTime() {
		return mCreationTime;
	}

	public String getOriginator() {
		return mOriginator;
	}

	public int getVersion() {
		return mVersion;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public Scope getScope() {
		return mScope;
	}

	public List<Programme> getProgrammes() {
		return mProgrammes;
	}

	/* some convenience */

	/**
	 * Returns the currently running {@link Programme} based on the systems time
	 * @return the currently running {@link Programme} or {@code null} if no {@link Programme} is currently running
	 */
	public Programme getCurrentRunningProgramme() {
		Programme retProg = null;

		for(Programme prog : mProgrammes) {
			for(Location loc : prog.getLocations()) {
				for(Time progTime : loc.getTimes()) {
					long progStartTimeMillis = progTime.getStartTime().getTimePointCalendar().getTimeInMillis();
					long progStopTimeMillis = progStartTimeMillis + progTime.getDuration().getDurationSeconds()*1000;
					long curTime = System.currentTimeMillis();
					if(curTime >= progStartTimeMillis && curTime < progStopTimeMillis) {
						retProg = prog;
						break;
					}
				}
			}
		}

		return retProg;
	}
}
