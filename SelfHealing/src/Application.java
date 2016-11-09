
public class Application {
	private int needed_memory;
	private int needed_cpu;
	
	private int x_coordinate;
	private int y_coordinate;
	
	public Application(int needed_memory, int needed_cpu, int x_coordinate, int y_coordinate) {
		super();
		this.needed_memory = needed_memory;
		this.needed_cpu = needed_cpu;
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
