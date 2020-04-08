package eu.hradio.core.radiodns.radioepg.programmeinformation;

import android.support.annotation.NonNull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.crid.Crid;
import eu.hradio.core.radiodns.radioepg.crid.ShortCrid;
import eu.hradio.core.radiodns.radioepg.genre.Genre;
import eu.hradio.core.radiodns.radioepg.keywords.Keywords;
import eu.hradio.core.radiodns.radioepg.link.Link;
import eu.hradio.core.radiodns.radioepg.mediadescription.MediaDescription;
import eu.hradio.core.radiodns.radioepg.name.Name;

public class Programme implements Serializable {

	private static final long serialVersionUID = 1044907069207230679L;

	private final ShortCrid mShortId;
	private final Crid mId;
	private final boolean mIsRecommendation;
	private final BroadcastType mBroadcastType;
	private final String mLanguage;
	private final int mVersion;

	private List<Name> mNames = new ArrayList<>();
	private List<Location> mLocations = new ArrayList<>();
	private List<OnDemand> mOndemands = new ArrayList<>();
	private List<MediaDescription> mMediaDescriptions = new ArrayList<>();
	private List<Genre> mGenres = new ArrayList<>();
	private List<Keywords> mKeywords = new ArrayList<>();
	private List<Link> mLinks = new ArrayList<>();
	private List<ProgrammeEvent> mProgrammeEvents = new ArrayList<>();

	public Programme(@NonNull ShortCrid shortId, @NonNull Crid id, int version, boolean recommendation, @NonNull BroadcastType bcType, @NonNull String language) {
		mShortId = shortId;
		mId = id;
		mVersion = version;
		mIsRecommendation = recommendation;
		mBroadcastType = bcType;
		mLanguage = language;
	}

	public Programme(@NonNull ShortCrid shortId, @NonNull Crid id, int version, boolean recommendation, @NonNull BroadcastType bcType) {
		this(shortId, id, version, recommendation, bcType, "en");
	}

	public Programme(@NonNull ShortCrid shortId, @NonNull Crid id, int version, boolean recommendation) {
		this(shortId, id, version, recommendation, BroadcastType.BROADCAST_TYPE_ON_AIR);
	}

	public Programme(@NonNull ShortCrid shortId, @NonNull Crid id, int version) {
		this(shortId, id, version, false);
	}

	public Programme(@NonNull ShortCrid shortId, @NonNull Crid id) {
		this(shortId, id, 1);
	}

	public void addName(Name name) {
		mNames.add(name);
	}

	public void addLocation(Location location) {
		mLocations.add(location);
	}

	public void addOnDemand(OnDemand ondemand) {
		mOndemands.add(ondemand);
	}

	public void addMediaDescription(MediaDescription md) {
		mMediaDescriptions.add(md);
	}

	public void addGenre(Genre genre) {
		mGenres.add(genre);
	}

	public void addKeywords(Keywords kw) {
		mKeywords.add(kw);
	}

	public void addLink(Link link) {
		mLinks.add(link);
	}

	public void addProgrammeEvent(ProgrammeEvent event) {
		mProgrammeEvents.add(event);
	}

	//TODO memberOf


	public ShortCrid getShortId() {
		return mShortId;
	}

	public Crid getId() {
		return mId;
	}

	public boolean isRecommendation() {
		return mIsRecommendation;
	}

	public BroadcastType getBroadcastType() {
		return mBroadcastType;
	}

	public String getLanguage() {
		return mLanguage;
	}

	public int getVersion() {
		return mVersion;
	}

	public List<Name> getNames() {
		return mNames;
	}

	public List<Location> getLocations() {
		return mLocations;
	}

	public List<OnDemand> getOndemands() {
		return mOndemands;
	}

	public List<MediaDescription> getMediaDescriptions() {
		return mMediaDescriptions;
	}

	public List<Genre> getGenres() {
		return mGenres;
	}

	public List<Keywords> getKeywords() {
		return mKeywords;
	}

	public List<Link> getLinks() {
		return mLinks;
	}

	public List<ProgrammeEvent> getProgrammeEvents() {
		return mProgrammeEvents;
	}

	/* some convenience */
	public int getRemainingSeconds() {
		int remainingSeconds = -1;

		for(Location loc : mLocations) {
			for(Time progTime : loc.getTimes()) {
				long progStartTimeMillis = progTime.getStartTime().getTimePointCalendar().getTimeInMillis();
				long progStopTimeMillis = progStartTimeMillis + progTime.getDuration().getDurationSeconds()*1000;
				long curTime = System.currentTimeMillis();
				if(curTime < progStartTimeMillis) {
					remainingSeconds = (int)((progStopTimeMillis - progStartTimeMillis) / 1000);
				} else if(curTime >= progStopTimeMillis){
					remainingSeconds = 0;
				} else {
					remainingSeconds = (int)((progStopTimeMillis - curTime) / 1000);
				}
			}
		}

		return remainingSeconds;
	}
}
