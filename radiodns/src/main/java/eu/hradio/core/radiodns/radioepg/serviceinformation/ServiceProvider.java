package eu.hradio.core.radiodns.radioepg.serviceinformation;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocation;
import eu.hradio.core.radiodns.radioepg.keywords.Keywords;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.mediadescription.MediaDescription;
import eu.hradio.core.radiodns.radioepg.name.Name;

public class ServiceProvider implements Serializable {

	private static final long serialVersionUID = 5657446199968357120L;

	private List<Name> mNames = new ArrayList<>();
	private List<MediaDescription> mMediaDescriptions = new ArrayList<>();
	private Keywords mKeywords;
	private List<Link> mLinks = new ArrayList<>();
	private GeoLocation mGeoLocation;

	public void addName(Name name) {
		mNames.add(name);
	}

	public void addMediaDescription(MediaDescription description) {
		mMediaDescriptions.add(description);
	}

	public void setKeywords(String kw) {
		mKeywords = new Keywords(kw);
	}

	public void addLink(Link link) {
		mLinks.add(link);
	}

	public void setGeolocation(GeoLocation geo) {
		mGeoLocation = geo;
	}

	public List<Name> getNames() {
		return mNames;
	}

	public List<MediaDescription> getMediaDescriptions() {
		return mMediaDescriptions;
	}

	public Keywords getKeywords() {
		return mKeywords;
	}

	public List<Link> getLinks() {
		return mLinks;
	}

	public GeoLocation getGeoLocation() {
		return mGeoLocation;
	}
}
