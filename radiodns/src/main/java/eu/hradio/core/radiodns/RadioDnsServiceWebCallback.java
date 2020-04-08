package eu.hradio.core.radiodns;

import org.omri.radioservice.RadioService;

public interface RadioDnsServiceWebCallback extends RadioDnsCallback {

	void applicationInformationListRetrieved(RadioWebApplicationInformationList rwebAppList, RadioService service);
}
