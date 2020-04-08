package eu.hradio.core.radiodns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.programmeinformation.Schedule;

public class RadioEpgProgrammeInformation implements Serializable {

	private static final long serialVersionUID = -4841305937162561477L;

	private List<Schedule> mSchedules = new ArrayList<>();

	public void addSchedule(Schedule schedules) {
		mSchedules.add(schedules);
	}

	public void addSchedules(List<Schedule> schedules) {
		mSchedules.addAll(schedules);
	}

	public List<Schedule> getSchedules() {
		return mSchedules;
	}
}
