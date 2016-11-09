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
	
	private int consumed_cpu;
	private int consumed_memory;
	private int consumed_networkBandwith;
	
	
	
	public PM(double u0, double u_cpu, double u_mem, double u_network, int cpu, int memory, int size, int network_bandwidth) {
		this.ID=id_counter++;
		this.u0=u0;
		this.u_cpu=u_cpu;
		this.u_mem=u_mem;
		this.u_network=u_network;
		this.cpu = cpu;
		this.memory = memory;
		this.size = size;
		this.network_bandwidth=network_bandwidth;
	}

	private void assignApplication(Application application){
		//distribute application among proper VMs
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
		return consumed_cpu;
	}
	public void setConsumed_cpu(int consumed_cpu) {
		this.consumed_cpu = consumed_cpu;
	}
	public int getConsumed_memory() {
		return consumed_memory;
	}
	public void setConsumed_memory(int consumed_memory) {
		this.consumed_memory = consumed_memory;
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
	
	public void removeVM(int id){
		this.vms.remove(id);
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
	
}