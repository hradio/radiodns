package eu.hradio.core.radiodns;

import org.omri.radioservice.RadioService;

import java.util.List;

public interface RadioDnsCoreLookupCallback extends RadioDnsCallback {

	void coreLookupFinished(RadioService lookupSrv, List<RadioDnsService> foundServices);
}
