package impl;
import Interfaces.LocationElement;
import Interfaces.SpecificationElement;

public class Request implements LocationElement, SpecificationElement{
	private static int id_counter=1;
	private final int ID;
	private int memory;
	private int cpu;
	private int bandwidth;
	private int size;
	private int runtime;
	
	private int xCoordinate;
	private int yCoordinate;
	
	public Request(int memory, int cpu, int bandwidth, int size, int runtime,
			int xCoordinate, int yCoordinate) {
		super();
		this.ID=id_counter++;
		this.memory = memory;
		this.cpu = cpu;
		this.bandwidth = bandwidth;
		this.size = size;
		this.runtime = runtime;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}

	public int getMemory() {
		return memory;
	}

	public void setMemory(int memory) {
		this.memory = memory;
	}

	public int getCpu() {
		return cpu;
	}

	public void setCpu(int cpu) {
		this.cpu = cpu;
	}

	public int getNetworkBandwidth() {
		return bandwidth;
	}

	public void setNetworkBandwidth(int bandwidth) {
		this.bandwidth = bandwidth;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getRuntime() {
		return runtime;
	}
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	public int getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	public int getID(){
		return this.ID;
	}
	
	@Override
	public Request getRequest() {
		return this;
	}
	
	@Override
	public String toString() {
		return "Request [ID=" + ID + ", memory=" + memory + ", cpu=" + cpu + ", bandwidth=" + bandwidth + ", size="
				+ size + ", runtime=" + runtime + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}
		
}