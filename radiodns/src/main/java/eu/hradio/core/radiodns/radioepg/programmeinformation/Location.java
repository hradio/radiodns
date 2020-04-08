package eu.hradio.core.radiodns.radioepg.programmeinformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;

public class Location implements Serializable {

	private static final long serialVersionUID = 6722895495494377335L;

	private List<Time> mTimes = new ArrayList<>();
	private List<RelativeTime> mRelativeTimes = new ArrayList<>();
	private List<Bearer> mBearers = new ArrayList<>();

	public void addTime(Time time) {
		mTimes.add(time);
	}

	public void addRelativeTime(RelativeTime relTime) {
		mRelativeTimes.add(relTime);
	}

	public void addBearer(Bearer bearer) {
		mBearers.add(bearer);
	}

	public List<Time> getTimes() {
		return mTimes;
	}

	public List<RelativeTime> getRelativeTimes() {
		return mRelativeTimes;
	}

	public List<Bearer> getBearers() {
		return mBearers;
	}
}
