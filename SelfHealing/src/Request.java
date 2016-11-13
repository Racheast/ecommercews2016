import Interfaces.LocationElement;

public class Request implements LocationElement{
	private static int id_counter=1;
	private final int ID;
	private int needed_memory;
	private int needed_cpu;
	private int needed_bandwidth;
	private int needed_size;
	private int runtime;
	
	private int xCoordinate;
	private int yCoordinate;
	
	public Request(int needed_memory, int needed_cpu, int needed_bandwidth, int needed_size, int runtime,
			int xCoordinate, int yCoordinate) {
		super();
		this.ID=id_counter++;
		this.needed_memory = needed_memory;
		this.needed_cpu = needed_cpu;
		this.needed_bandwidth = needed_bandwidth;
		this.needed_size = needed_size;
		this.runtime = runtime;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
	}
	public int getNeeded_memory() {
		return needed_memory;
	}
	public void setNeeded_memory(int needed_memory) {
		this.needed_memory = needed_memory;
	}
	public int getNeeded_cpu() {
		return needed_cpu;
	}
	public void setNeeded_cpu(int needed_cpu) {
		this.needed_cpu = needed_cpu;
	}
	public int getNeeded_bandwidth() {
		return needed_bandwidth;
	}
	public void setNeeded_bandwidth(int needed_bandwidth) {
		this.needed_bandwidth = needed_bandwidth;
	}
	
	public int getNeeded_size() {
		return needed_size;
	}
	public void setNeeded_size(int needed_size) {
		this.needed_size = needed_size;
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
	public String toString() {
		return "Request [ID=" + ID + ", needed_memory=" + needed_memory + ", needed_cpu=" + needed_cpu
				+ ", needed_bandwidth=" + needed_bandwidth + ", needed_size=" + needed_size + ", runtime=" + runtime
				+ ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}
	
	
	
	
}
