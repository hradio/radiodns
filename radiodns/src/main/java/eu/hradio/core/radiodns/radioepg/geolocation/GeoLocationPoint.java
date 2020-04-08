package eu.hradio.core.radiodns.radioepg.geolocation;

import java.io.Serializable;

public class GeoLocationPoint implements Serializable {

	private static final long serialVersionUID = 7803586796620543333L;

	private final double mLatitude;
	private final double mLongitude;

	public GeoLocationPoint(double latitude, double longitude) {
		mLatitude = latitude;
		mLongitude = longitude;
	}

	public double getLatitude() {
		return mLatitude;
	}

	public double getLongitude() {
		return mLongitude;
	}
}
