package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;

public class OnDemand implements Serializable {

	private static final long serialVersionUID = 1008687064185484577L;

	private final PresentationTime mPresentationTime;
	private List<Bearer> mBearers = new ArrayList<>();
	private List<AcquisitionTime> mAcquisitionTimes = new ArrayList<>();

	public OnDemand(@NonNull PresentationTime preTime, @NonNull Bearer bearer) {
		mPresentationTime = preTime;
		mBearers.add(bearer);
	}

	public OnDemand(@NonNull PresentationTime preTime, @NonNull List<Bearer> bearers) {
		mPresentationTime = preTime;
		mBearers.addAll(bearers);
	}

	public void addAcquisitionTime(AcquisitionTime acqTime) {
		mAcquisitionTimes.add(acqTime);
	}

	public void addAcquisitionTimes(List<AcquisitionTime> acqTimes) {
		mAcquisitionTimes.addAll(acqTimes);
	}

	public PresentationTime getPresentationTime() {
		return mPresentationTime;
	}

	public List<Bearer> getBearers() {
		return mBearers;
	}

	public List<AcquisitionTime> getAcquisitionTimes() {
		return mAcquisitionTimes;
	}
}
