package eu.hradio.core.radiodns.radioepg.bearer;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocation;

public class Bearer implements Serializable {

	private static final long serialVersionUID = -4369723785425543511L;

	private final String mBearerIdString;
	private final int mCost;
	private final String mMimeString;
	private final int mBitrate;
	private final int mOffset;
	private final BearerType mBearerType;
	private List<GeoLocation> mGeoLocations = new ArrayList<>();

	/**
	 * Creates a Bearer with all mandatory and optional values
	 * @param bearerId the complete string as given in the {@code id} attribute
	 * @param cost the cost given in the {@code cost} attribute
	 * @param mime the mime string given in the {@code mimeValue} attribute
	 * @param bitrate the bitrate given in the {@code bitrate} attribute
	 * @param offset the offset given in the {@code offset} attribute
	 */
	public Bearer(@NonNull String bearerId, int cost, String mime, int bitrate, int offset) {
		mBearerIdString = bearerId;
		mCost = cost;
		mMimeString = mime;
		mBitrate = bitrate;
		mOffset = offset;

		String[] bearerTypeId = bearerId.split(":");
		if(bearerTypeId.length > 0) {
			for(BearerType type : BearerType.values()) {
				if(type.getBearerTypeId().equalsIgnoreCase(bearerTypeId[0])) {
					mBearerType = type;
					return;
				}
			}
		}

		mBearerType = BearerType.BEARER_TYPE_UNKNOWN;
	}

	/**
	 * Creates a bearer without the additional offset value. offset defaults to zero
	 * @param bearerId the complete string as given in the {@code id} attribute
	 * @param cost the cost given in the {@code cost} attribute
	 * @param mime the mime string given in the {@code mimeValue} attribute
	 * @param bitrate the bitrate given in the {@code bitrate} attribute
	 */
	public Bearer(@NonNull String bearerId, int cost, String mime, int bitrate) {
		this(bearerId, cost, mime, bitrate, 0);
	}

	public void addGeoLocations(List<GeoLocation> geoLocations) {
		mGeoLocations.addAll(geoLocations);
	}

	public void addGeoLocation(GeoLocation geoLocation) {
		mGeoLocations.add(geoLocation);
	}

	public String getBearerIdString() {
		return mBearerIdString;
	}

	public int getCost() {
		return mCost;
	}

	public String getMimeType() {
		return mMimeString;
	}

	public int getBitrate() {
		return mBitrate;
	}

	public int getOffset() {
		return mOffset;
	}

	public BearerType getBearerType() {
		return mBearerType;
	}

	public List<GeoLocation> getGeoLocations() {
		return mGeoLocations;
	}
}
