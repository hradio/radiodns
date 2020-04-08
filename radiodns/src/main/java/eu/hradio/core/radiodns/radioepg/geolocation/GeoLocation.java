package eu.hradio.core.radiodns.radioepg.geolocation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GeoLocation implements Serializable {

	private static final long serialVersionUID = -7788123122934098187L;

	private List<String> mCountryStrings = new ArrayList<>();
	private List<GeoLocationPoint> mLocationPoints = new ArrayList<>();
	private List<GeoLocationPolygon> mLocationPolygons = new ArrayList<>();

	public void addCountryString(String countryString) {
		mCountryStrings.add(countryString);
	}

	public void addLocationPolygon(GeoLocationPolygon locationPolygon) {
		mLocationPolygons.add(locationPolygon);
	}

	public void addLocationPoint(GeoLocationPoint locationPoint) {
		mLocationPoints.add(locationPoint);
	}

	public void addCountryStrings(List<String> countryStrings) {
		mCountryStrings.addAll(countryStrings);
	}

	public void addLocationPolygons(List<GeoLocationPolygon> locationPolygons) {
		mLocationPolygons.addAll(locationPolygons);
	}

	public void addLocationPoints(List<GeoLocationPoint> locationPoints) {
		mLocationPoints.addAll(locationPoints);
	}

	public List<String> getCountryStrings() {
		return mCountryStrings;
	}

	public List<GeoLocationPoint> getLocationPoints() {
		return mLocationPoints;
	}

	public List<GeoLocationPolygon> getLocationPolygons() {
		return mLocationPolygons;
	}

}
