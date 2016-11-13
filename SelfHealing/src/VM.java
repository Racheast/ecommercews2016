import java.util.Timer;
import java.util.TimerTask;

public class VM{
	//
	private final int ID;
	private static int id_counter=1;
	
	private Request request;
	private int size;
	private int consumed_cpu;
	private int consumed_memory;
	private int consumed_networkBandwith;  //depends on the consumed memory
	private int runtime;
	private double page_dirtying_rate;  //depends linearly on the combination of the utilized memory, cpu and nw_bandwidth
		
	public VM(Request request){
		this.ID=id_counter++;
		this.request=request;
		this.size=request.getNeeded_size();
		this.consumed_cpu=request.getNeeded_cpu();
		this.consumed_memory=request.getNeeded_memory();
		this.consumed_networkBandwith=request.getNeeded_bandwidth();
		this.runtime=request.getRuntime(); //TODO: calculate real value via formula
		this.page_dirtying_rate=0.2; //TODO: calculate real value via formula
	}
	
	public VM(int size, int consumed_cpu, int consumed_memory, int runtime) {
		super();
		this.ID=id_counter++;
		this.size = size;
		this.consumed_cpu = consumed_cpu;
		this.consumed_memory = consumed_memory;
		this.runtime = runtime;
		this.consumed_networkBandwith=10;  //TODO: calculate real value via formula
		this.page_dirtying_rate=0.2;  //TODO: calculate real value via formula
	}

	public int getRuntime() {
		return runtime;
	}
	public void setRuntime(int runtime) {
		this.runtime = runtime;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getConsumed_cpu() {
		return consumed_cpu;
	}
	public void setConsumed_cpu(int consumed_cpu) {
		this.consumed_cpu = consumed_cpu;
	}
	public int getConsumed_memory() {
		return consumed_memory;
	}
	public void setConsumed_memory(int comsumed_memory) {
		this.consumed_memory= comsumed_memory;
	}
	public int getConsumed_networkBandwith() {
		return consumed_networkBandwith;
	}
	public void setConsumed_networkBandwith(int consumed_networkBandwith) {
		this.consumed_networkBandwith = consumed_networkBandwith;
	}
	public int getID(){
		return this.ID;
	}
	public Request getRequest(){
		return this.request;
	}

	
}
