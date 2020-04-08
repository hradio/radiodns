package eu.hradio.core.radiodns.radioepg.geolocation;

import java.io.Serializable;
import java.util.List;

public class GeoLocationPolygon implements Serializable {

	private static final long serialVersionUID = -1825858425186748395L;

	private final List<GeoLocationPoint> mPolygonEntries;

	public GeoLocationPolygon(List<GeoLocationPoint> polygonEntries) {
		mPolygonEntries = polygonEntries;
	}
}
