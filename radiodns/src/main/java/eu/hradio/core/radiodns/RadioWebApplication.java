package eu.hradio.core.radiodns;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.bearer.Bearer;
import eu.hradio.core.radiodns.radioepg.mediadescription.MediaDescription;
import eu.hradio.core.radiodns.radioepg.name.Name;
import eu.hradio.core.radiodns.radioepg.scope.ServiceScope;

public class RadioWebApplication implements Serializable {

	private static final long serialVersionUID = -8142697515025657146L;

	private final static String TAG = "RadioWebApplication";

	private final RadioWebApplicationControl mControlCode;
	private final int mAppId;
	private final int mAppPrio;
	private List<Bearer> mBearers = new ArrayList<>();
	private List<Name> mAppNames = new ArrayList<>();
	private List<MediaDescription> mDescs = new ArrayList<>();
	private List<ServiceScope> mScopes = new ArrayList<>();

	RadioWebApplication(RadioWebApplicationControl controlCode, int appId, int appPrio) {
		mControlCode = controlCode;
		mAppId = appId;
		mAppPrio = appPrio;
	}

	void addBearer(Bearer bearer) {
		mBearers.add(bearer);
	}

	void addName(Name name) {
		mAppNames.add(name);
	}

	void addMediaDescription(MediaDescription md) {
		mDescs.add(md);
	}

	void addServiceScope(ServiceScope scope) {
		mScopes.add(scope);
	}

	public RadioWebApplicationControl getControlCode() {
		return mControlCode;
	}

	public int getApplicationId() {
		return mAppId;
	}

	public int getApplicationPriority() {
		return mAppPrio;
	}

	public List<ServiceScope> getScopes() {
		return mScopes;
	}

	public List<Bearer> getBearers() {
		return mBearers;
	}

	public List<Name> getNames() {
		return mAppNames;
	}

	public List<MediaDescription> getMediaDescriptions() {
		return mDescs;
	}
}
