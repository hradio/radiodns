package eu.hradio.core.radiodns;

public enum RadioWebApplicationControl {

	APPLICATION_CONTROL_UNDEFINED,

	/** Receiver shall start the signalled application immediately after the signalling has been discovered **/
	APPLICATION_CONTROL_AUTOSTART,

	/** Receiver shall start the signalled application on user request only **/
	APPLICATION_CONTROL_ENABLED,

	/** Receiver shall not start the application at all. But shall keep running instances alive until they are quit by the user **/
	APPLICATION_CONTROL_DISABLED;

	public static RadioWebApplicationControl getControl(String controlString) {
		if(controlString != null) {
			if(controlString.trim().equalsIgnoreCase("autostart")) {
				return APPLICATION_CONTROL_AUTOSTART;
			}
			if(controlString.trim().equalsIgnoreCase("enabled")) {
				return APPLICATION_CONTROL_ENABLED;
			}
			if(controlString.trim().equalsIgnoreCase("disabled")) {
				return APPLICATION_CONTROL_DISABLED;
			}
		}

		return APPLICATION_CONTROL_UNDEFINED;
	}
}
