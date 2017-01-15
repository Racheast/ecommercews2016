package infrastructure;
import java.util.Timer;
import java.util.TimerTask;

import interfaces.SpecificationElement;

public class VM {
	//
	private final int ID;
	private static int id_counter=1;
	
	private Request request;
	private Address address;
	
	private int size;
	private int consumed_cpu;
	private int consumed_memory;
	private int consumed_networkBandwith;  //depends on the consumed memory
	private int runtime;
	private double page_dirtying_rate;  //TODO: depends linearly on the combination of the utilized memory, cpu and nw_bandwidth
	private final double page_dirtying_threshold;
	
	public VM(VM vm){
		this.ID=id_counter++;
		this.request=vm.getRequest();
		this.size=vm.getSize();
		this.consumed_cpu=vm.getCpu();
		this.consumed_memory=vm.getMemory();
		this.consumed_networkBandwith=vm.getNetworkBandwidth();
		this.runtime=vm.getRuntime();
		this.page_dirtying_rate=2.0;
		this.page_dirtying_threshold=0.05;
	}
	
	public VM(Request request, int size, int consumed_cpu, int consumed_memory, int consumed_networkBandwith,
			int runtime) {
		this.ID=id_counter++;
		this.request = request;
		this.size = size;
		this.consumed_cpu = consumed_cpu;
		this.consumed_memory = consumed_memory;
		this.consumed_networkBandwith = consumed_networkBandwith;
		this.runtime = runtime;
		this.page_dirtying_rate=2.0;
		this.page_dirtying_threshold=0.05;
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
	public int getCpu() {
		return consumed_cpu;
	}
	public void setCpu(int consumed_cpu) {
		this.consumed_cpu = consumed_cpu;
	}
	public int getMemory() {
		return consumed_memory;
	}
	public void setMemory(int comsumed_memory) {
		this.consumed_memory= comsumed_memory;
	}
	public int getNetworkBandwidth() {
		return consumed_networkBandwith;
	}
	public void setNetworkBandwith(int consumed_networkBandwith) {
		this.consumed_networkBandwith = consumed_networkBandwith;
	}
	public int getID(){
		return this.ID;
	}
	public Request getRequest(){
		return this.request;
	}
	
	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
	
	public double getPage_dirtying_rate() {
		return page_dirtying_rate;
	}

	public double getPage_dirtying_threshold() {
		return page_dirtying_threshold;
	}

	public String compactString(){
		return "VM"+ID+"("+request.compactString()+")";
	}

	@Override
	public String toString() {
		return "VM [ID=" + ID + ", request=" + request + ", address=" + address + ", size=" + size + ", consumed_cpu="
				+ consumed_cpu + ", consumed_memory=" + consumed_memory + ", consumed_networkBandwith="
				+ consumed_networkBandwith + ", runtime=" + runtime + ", page_dirtying_rate=" + page_dirtying_rate
				+ ", page_dirtying_threshold=" + page_dirtying_threshold + "]";
	}
	
	

	
}
