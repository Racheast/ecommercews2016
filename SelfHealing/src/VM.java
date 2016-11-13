import java.util.Timer;
import java.util.TimerTask;

import Interfaces.SpecificationElement;

public class VM implements SpecificationElement{
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
		this((SpecificationElement)request);
		this.request=request;
		this.runtime=request.getRuntime(); //TODO: calculate real value via formula
		/*
		this.ID=id_counter++;
		this.size=request.getSize();
		this.consumed_cpu=request.getCpu();
		this.consumed_memory=request.getMemory();
		this.consumed_networkBandwith=request.getNetworkBandwidth();
		this.page_dirtying_rate=0.2; //TODO: calculate real value via formula
		*/
	}
	
	public VM(SpecificationElement specificationElement){
		this.ID=id_counter++;
		this.size=specificationElement.getSize();
		this.consumed_cpu=specificationElement.getCpu();
		this.consumed_memory=specificationElement.getMemory();
		this.consumed_networkBandwith=specificationElement.getNetworkBandwidth();
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

	@Override
	public String toString() {
		return "VM [ID=" + ID + ", request=" + request.getID() + ", size=" + size + ", consumed_cpu=" + consumed_cpu
				+ ", consumed_memory=" + consumed_memory + ", consumed_networkBandwith=" + consumed_networkBandwith
				+ ", runtime=" + runtime + ", page_dirtying_rate=" + page_dirtying_rate + "]";
	}



	
}
