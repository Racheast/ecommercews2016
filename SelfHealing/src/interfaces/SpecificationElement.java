package interfaces;

import Infrastructure.Request;

public interface SpecificationElement {	
	public int getMemory();
	public int getCpu();
	public int getNetworkBandwidth();
	public int getSize();
	public int getID();
	public Request getRequest();
}