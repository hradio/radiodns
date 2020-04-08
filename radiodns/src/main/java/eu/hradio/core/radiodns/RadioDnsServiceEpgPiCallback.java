package eu.hradio.core.radiodns;

public interface RadioDnsServiceEpgPiCallback extends RadioDnsCallback {

	/**
	 * Notifies the implementer about the finished Programme Information parsing
	 * @param pi the parsed {@link RadioEpgServiceInformation} or {@code null}
	 */
	void programmeInformationRetrieved(RadioEpgProgrammeInformation pi, RadioDnsServiceEpg rdnsService);
}
