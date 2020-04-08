package eu.hradio.core.radiodns.radioepg.mediadescription;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.hradio.core.radiodns.radioepg.description.Description;
import eu.hradio.core.radiodns.radioepg.multimedia.Multimedia;

public class MediaDescription implements Serializable {

	private static final long serialVersionUID = -2538642707369698324L;

	private Multimedia mMultimedia = null;
	private List<Description> mDescriptions = new ArrayList<>();

	public MediaDescription() {

	}

	public MediaDescription(Multimedia multimedia, Description... descriptions) {
		mMultimedia = multimedia;
		mDescriptions.addAll(Arrays.asList(descriptions));
	}

	public void setMultimedia(Multimedia multimedia) {
		mMultimedia = multimedia;
	}

	public void addDescription(Description description) {
		mDescriptions.add(description);
	}

	public void addDescription(List<Description> descriptions) {
		mDescriptions.addAll(descriptions);
	}

	public Multimedia getMultimedia() {
		return mMultimedia;
	}

	public List<Description> getDescriptions() {
		return mDescriptions;
	}
}
