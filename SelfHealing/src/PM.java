import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PM{
	private HashMap<Integer,VM> vms=new HashMap<Integer,VM>();
	private static int id_counter=1;
	private final int ID;
	
	private double u0, u_cpu, u_mem, u_network;  
	
	private int cpu;
	private int memory;
	private int size;
	private int network_bandwidth; //mbit per sec
	/*
	private int consumed_cpu;
	private int consumed_memory;
	private int consumed_networkBandwith;
	*/
	
	
	public PM(double u0, double u_cpu, double u_mem, double u_network, int cpu, int memory, int size) {
		this.ID=id_counter++;
		this.u0=u0;
		this.u_cpu=u_cpu;
		this.u_mem=u_mem;
		this.u_network=u_network;
		this.cpu = cpu;
		this.memory = memory;
		this.size = size;
	}

	public VM startApplication(Request request){
		VM vm=new VM(request);
		vms.put(vm.getID(),vm);
		System.out.println("VM"+vm.getID()+" created!");
		return vm;
	}
	
	public HashMap<Integer, VM> getVms() {
		return vms;
	}
	public void setVms(HashMap<Integer, VM> vms) {
		this.vms = vms;
	}
	public int getCpu() {
		return cpu;
	}
	public void setCpu(int cpu) {
		this.cpu = cpu;
	}
	public int getMemory() {
		return memory;
	}
	public void setMemory(int memory) {
		this.memory = memory;
	}
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public int getConsumed_cpu() {
		int consumed_cpu=0;
		for(VM vm:getListOfVMs()){
			consumed_cpu+=vm.getConsumed_cpu();
		}
		return consumed_cpu;
	}
	
	public int getConsumed_memory() {
		int consumed_memory=0;
		for(VM vm:getListOfVMs()){
			consumed_memory+=vm.getConsumed_memory();
		}
		return consumed_memory;
	}
	
	public int getConsumed_networkBandwith() {
		int consumed_networkBandwith=0;
		for(VM vm:getListOfVMs()){
			consumed_networkBandwith+=vm.getConsumed_networkBandwith();
		}
		return consumed_networkBandwith;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public int getNetwork_bandwidth() {
		return network_bandwidth;
	}

	public void setNetwork_bandwidth(int network_bandwidth) {
		this.network_bandwidth = network_bandwidth;
	}

	public void shutdownVM(int id){
		System.out.println("shutting down VM"+this.getID());
		this.vms.remove(id);
	}
	
	private ArrayList<VM> getListOfVMs(){
		ArrayList<VM> vmList=new ArrayList<VM>();
		Set<Integer> keys=vms.keySet();
		for(int key:keys)
			vmList.add(vms.get(key));
		return vmList;
	}
	
	public double getEnergyUtilization(){
		double u_total=u0;
		
		Set<Integer> keys=vms.keySet();
		
		for(int k:keys){
			VM vm=vms.get(k);
			u_total+=u_cpu*(vm.getConsumed_cpu()/cpu) + u_mem*(vm.getConsumed_memory()/memory) + u_network*(vm.getConsumed_networkBandwith()/network_bandwidth);
		}
		
		return u_total;
	}

	@Override
	public String toString() {
		return "PM [vms=" + vms + ", ID=" + ID + ", u0=" + u0 + ", u_cpu=" + u_cpu + ", u_mem=" + u_mem + ", u_network="
				+ u_network + ", cpu=" + cpu + ", memory=" + memory + ", size=" + size + ", network_bandwidth="
				+ network_bandwidth + "]";
	}
	
	
	
}