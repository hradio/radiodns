package eu.hradio.core.radiodns.radioepg.crid;

import android.support.annotation.NonNull;

import java.io.Serializable;

public class Crid implements Serializable {

	private static final long serialVersionUID = 3142336311505710334L;

	private final String mCridString;
	private final String mCridAuthority;
	private final String mCridData;

	public Crid(@NonNull String cridId) {
		mCridString = cridId.trim();

		String[] split = mCridString.split("/");
		if(split.length >= 3) {
			mCridAuthority = split[2];

			StringBuilder crdBuilder = new StringBuilder();
			for(int rem = 3; rem < split.length; rem++) {
				crdBuilder.append(split[rem]);
				crdBuilder.append("/");
			}
			crdBuilder.deleteCharAt(crdBuilder.length()-1);

			mCridData = crdBuilder.toString();

			return;
		}

		mCridAuthority = "";
		mCridData = "";
	}

	public String getCridString() {
		return mCridString;
	}

	public String getCridAuthority() {
		return mCridAuthority;
	}

	public String getCridData() {
		return mCridData;
	}
}
