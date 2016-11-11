
public class Request {
	private int needed_memory;
	private int needed_cpu;
	private int needed_bandwidth;
	private int needed_size;
	private int runtime;
	
	private int x_coordinate;
	private int y_coordinate;
	
	public Request(int needed_memory, int needed_cpu, int needed_bandwidth, int needed_size, int runtime,
			int x_coordinate, int y_coordinate) {
		super();
		this.needed_memory = needed_memory;
		this.needed_cpu = needed_cpu;
		this.needed_bandwidth = needed_bandwidth;
		this.needed_size = needed_size;
		this.runtime = runtime;
		this.x_coordinate = x_coordinate;
		this.y_coordinate = y_coordinate;
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
	public int getX_coordinate() {
		return x_coordinate;
	}
	public void setX_coordinate(int x_coordinate) {
		this.x_coordinate = x_coordinate;
	}
	public int getY_coordinate() {
		return y_coordinate;
	}
	public void setY_coordinate(int y_coordinate) {
		this.y_coordinate = y_coordinate;
	}
	
	
}
