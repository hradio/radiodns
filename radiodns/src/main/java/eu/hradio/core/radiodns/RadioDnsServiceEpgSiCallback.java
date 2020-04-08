package eu.hradio.core.radiodns;

public interface RadioDnsServiceEpgSiCallback extends RadioDnsCallback {

	/**
	 * Notifies the implementer about the finished Service Information parsing
	 * @param si the parsed {@link RadioEpgServiceInformation} or {@code null}
	 */
	void serviceInformationRetrieved(RadioEpgServiceInformation si, RadioDnsServiceEpg rdnsService);
}
