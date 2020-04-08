package eu.hradio.core.radiodns.radioepg.crid;

import java.io.Serializable;

public class ShortCrid implements Serializable {

	private static final long serialVersionUID = 955556841830610461L;

	private final int mShortId;

	public ShortCrid(int shortId) {
		mShortId = shortId;
	}

	public int getShortId() {
		return mShortId;
	}
}
