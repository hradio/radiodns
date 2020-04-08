package eu.hradio.core.radiodns.radioepg.scope;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class Scope implements Serializable {

	private static final long serialVersionUID = -3684242241886246262L;

	private final TimePoint mStartTime;
	private final TimePoint mStopTime;

	private List<ServiceScope> mServiceScopes = new ArrayList<>();

	public Scope(@NonNull String startTime, @NonNull String stopTime) {
		mStartTime = new TimePoint(startTime);
		mStopTime = new TimePoint(stopTime);
	}

	public void addServiceScope(ServiceScope scope) {
		mServiceScopes.add(scope);
	}

	public TimePoint getStartTime() {
		return mStartTime;
	}

	public TimePoint getStopTime() {
		return mStopTime;
	}

	public List<ServiceScope> getServiceScopes() {
		return mServiceScopes;
	}
}
