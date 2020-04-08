package eu.hradio.core.radiodns;

import org.minidns.record.SRV;
import org.omri.radioservice.RadioService;

public class RadioDnsServiceTag extends RadioDnsService {

	private final static String TAG = "RadioDnsServiceTag";

	RadioDnsServiceTag(SRV srvRecord, String rdnsSrvId, String bearerUri, RadioDnsServiceType srvType, RadioService lookupSrv) {
		super(srvRecord, rdnsSrvId, bearerUri, srvType, lookupSrv);
	}
}
