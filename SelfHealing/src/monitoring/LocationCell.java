package monitoring;

import java.util.HashMap;

import interfaces.LocationElement;

public class LocationCell {
	private HashMap<Integer, LocationElement> locationElements;

	public LocationCell() {
		super();
		this.locationElements = new HashMap<Integer, LocationElement>();
	}

	public HashMap<Integer, LocationElement> getLocationElements() {
		return locationElements;
	}

	public void setLocationElements(HashMap<Integer, LocationElement> locationElements) {
		this.locationElements = locationElements;
	}

}
