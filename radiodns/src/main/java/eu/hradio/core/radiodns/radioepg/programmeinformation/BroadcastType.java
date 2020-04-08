package eu.hradio.core.radiodns.radioepg.programmeinformation;

public enum BroadcastType {

	BROADCAST_TYPE_ON_AIR("on-air"),
	BROADCAST_TYPE_OFF_AIR("off-air");

	private final String mTypeString;

	private BroadcastType(String tyepString) {
		mTypeString = tyepString;
	}

	public static BroadcastType fromString(String typeString) {
		if(typeString.equalsIgnoreCase(BROADCAST_TYPE_OFF_AIR.mTypeString)) {
			return BROADCAST_TYPE_OFF_AIR;
		}

		//Defaulting to on-air
		return BROADCAST_TYPE_ON_AIR;
	}
}
