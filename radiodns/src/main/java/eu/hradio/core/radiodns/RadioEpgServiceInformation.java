package eu.hradio.core.radiodns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.serviceinformation.Service;
import eu.hradio.core.radiodns.radioepg.serviceinformation.ServiceProvider;
import eu.hradio.core.radiodns.radioepg.time.TimePoint;

public class RadioEpgServiceInformation implements Serializable {

	private static final long serialVersionUID = -9110375812503863251L;

	private final TimePoint mCreationTime;
	private final String mOriginator;
	private final String mLanguage;
	private final int mVersion;
	private final String mTermsUrl;

	private ServiceProvider mServiceProvider;

	private List<Service> mServices = new ArrayList<>();

	RadioEpgServiceInformation(String creationTime, String originator, String language, int version, String termsUrl) {
		mCreationTime = new TimePoint(creationTime);
		mOriginator = originator;
		mLanguage = language;

		mVersion = version;
		mTermsUrl = termsUrl;
	}

	RadioEpgServiceInformation(String creationTime, String originator, String language, int version) {
		this(creationTime, originator, language, version, "");
	}

	RadioEpgServiceInformation(String creationTime, String originator, String language) {
		this(creationTime, originator, language, 1, "");
	}

	void setServiceProvider(ServiceProvider provider) {
		mServiceProvider = provider;
	}

	void addService(Service service) {
		mServices.add(service);
	}

	void addServices(List<Service> services) {
		mServices.addAll(services);
	}

	public TimePoint getCreationTime() {
		return mCreationTime;
	}

	public String getOriginator() {
		return mOriginator;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public int getVersion() {
		return mVersion;
	}

	public String getTermsUrl() {
		return mTermsUrl;
	}

	public ServiceProvider getServiceProvider() {
		return mServiceProvider;
	}

	public List<Service> getServices() {
		return mServices;
	}
}
