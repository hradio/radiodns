package eu.hradio.core.radiodns.radioepg.serviceinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.genre.Genre;
import eu.hradio.core.radiodns.radioepg.geolocation.GeoLocation;
import eu.hradio.core.radiodns.radioepg.keywords.Keywords;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.mediadescription.MediaDescription;
import eu.hradio.core.radiodns.radioepg.name.Name;
import eu.hradio.core.radiodns.radioepg.radiodns.RadioDns;

public class Service implements Serializable {

	private static final long serialVersionUID = -4223533420411915065L;

	/* Optional, defaults to 1*/
	private final int mVersion;
	private List<Name> mNames = new ArrayList<>();
	private List<MediaDescription> mMediaDescriptions = new ArrayList<>();
	private List<Genre> mGenres = new ArrayList<>();
	private Keywords mKeywords = new Keywords("");
	private List<Link> mLinks = new ArrayList<>();
	private List<Bearer> mBearers = new ArrayList<>();
	private RadioDns mRadioDns;
	private GeoLocation mGeoLocation;

	public Service(int version) {
		mVersion = version;
	}

	public Service() {
		this(1);
	}

	public void addName(@NonNull Name name) {
		mNames.add(name);
	}

	public void addMediaDescription(@NonNull MediaDescription md) {
		mMediaDescriptions.add(md);
	}

	public void addGenre(@NonNull Genre genre) {
		mGenres.add(genre);
	}

	public void setKeywords(String kw) {
		mKeywords = new Keywords(kw);
	}

	public void addLink(Link link) {
		mLinks.add(link);
	}

	public void addBearer(Bearer bearer) {
		mBearers.add(bearer);
	}

	public void setRadioDns(RadioDns rdns) {
		mRadioDns = rdns;
	}

	public void setGeoLocation(GeoLocation geo) {
		mGeoLocation = geo;
	}

	//TODO serviceGroupMember


	public int getVersion() {
		return mVersion;
	}

	public List<Name> getNames() {
		return mNames;
	}

	public List<MediaDescription> getMediaDescriptions() {
		return mMediaDescriptions;
	}

	public List<Genre> getGenres() {
		return mGenres;
	}

	public Keywords getKeywords() {
		return mKeywords;
	}

	public List<Link> getLinks() {
		return mLinks;
	}

	public List<Bearer> getBearers() {
		return mBearers;
	}

	public RadioDns getRadioDns() {
		return mRadioDns;
	}

	public GeoLocation getGeoLocation() {
		return mGeoLocation;
	}
}
