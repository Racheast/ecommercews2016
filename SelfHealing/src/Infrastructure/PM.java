package Infrastructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import interfaces.SpecificationElement;

public class PM {
	private HashMap<Integer, VM> vms = new HashMap<Integer, VM>();
	private static int id_counter = 1;
	private final int ID;

	private double u0, u_cpu, u_mem, u_network;

	private double cpu;
	private double memory;
	private double size;
	private double network_bandwidth; // mbit per sec

	public PM(double u0, double u_cpu, double u_mem, double u_network, double cpu, double memory, double size) {
		this.ID = id_counter++;
		this.u0 = u0;
		this.u_cpu = u_cpu;
		this.u_mem = u_mem;
		this.u_network = u_network;
		this.cpu = cpu;
		this.memory = memory;
		this.size = size;
		this.network_bandwidth = 0;
	}

	/*
	 * public VM copyVM(VM vm, double r){ int v=vm.getMemory(); double t=v/r;
	 * 
	 * while(v > vm.getPage_dirtying_threshold()){ v = (int)
	 * Math.round(vm.getPage_dirtying_rate() * t); t = v/r; }
	 * 
	 * return startApplication(vm); }
	 */

	public double migrateVM(double dataVolume, double transmissionRate) {
		return dataVolume / transmissionRate;
	}

	public synchronized VM startApplication(VM vm) {
		VM newVM = new VM(vm);
		Address address = new Address();
		newVM.setAddress(address);
		newVM.getAddress().setPM_ID(this.ID);
		vms.put(newVM.getID(), newVM);
		System.out.println(
				this.compactString() + ": " + newVM.compactString() + " for " + vm.compactString() + " created!\n");
		return newVM;
	}

	public HashMap<Integer, VM> getVms() {
		return vms;
	}

	public void setVms(HashMap<Integer, VM> vms) {
		this.vms = vms;
	}

	public double getCpu() {
		return cpu;
	}

	public void setCpu(double cpu) {
		this.cpu = cpu;
	}

	public double getMemory() {
		return memory;
	}

	public void setMemory(double memory) {
		this.memory = memory;
	}

	public double getSize() {
		return size;
	}

	public void setSize(double size) {
		this.size = size;
	}

	public double getConsumed_cpu() {
		double consumed_cpu = 0;
		for (VM vm : getListOfVMs()) {
			consumed_cpu += vm.getCpu();
		}
		return consumed_cpu;
	}

	public double getConsumed_memory() {
		double consumed_memory = 0;
		for (VM vm : getListOfVMs()) {
			consumed_memory += vm.getMemory();
		}
		return consumed_memory;
	}

	public double getConsumed_networkBandwith() {
		double consumed_networkBandwith = 0;
		for (VM vm : getListOfVMs()) {
			consumed_networkBandwith += vm.getNetworkBandwidth();
		}
		return consumed_networkBandwith;
	}

	public int getID() {
		return this.ID;
	}

	public double getNetwork_bandwidth() {
		return network_bandwidth;
	}

	public void setNetwork_bandwidth(double network_bandwidth) {
		this.network_bandwidth = network_bandwidth;
	}

	public boolean shutdownVM(double vmID) {
		this.vms.remove(vmID);
		System.out.println(this.compactString() + ": VM" + vmID + " shut down.\n");
		return true;
	}

	private ArrayList<VM> getListOfVMs() {
		ArrayList<VM> vmList = new ArrayList<VM>();
		Set<Integer> keys = vms.keySet();
		for (int key : keys)
			vmList.add(vms.get(key));
		return vmList;
	}

	public double getEnergyUtilization() {
		double u_total = u0;
		Set<Integer> keys = vms.keySet();
		for (int k : keys) {
			VM vm = vms.get(k);
			u_total += u_cpu * (vm.getCpu() / cpu) + u_mem * (vm.getMemory() / memory)
					+ u_network * (vm.getNetworkBandwidth() / network_bandwidth);
		}
		System.out.println("PM.getEnergyUtil(), ID="+this.ID+", VM keys="+keys);
		System.out.println("PM.getEnergyUtil(), ID="+this.ID+", u0="+u0);
		System.out.println("PM.getEnertyUtil(), ID="+this.ID+", u_total="+u_total);
		return u_total;
	}

	public double getExpectedEnergyUtilization(VM vm) {
		return u_cpu * (vm.getCpu() / cpu) + u_mem * (vm.getMemory() / memory)
				+ u_network * (vm.getNetworkBandwidth() / network_bandwidth);
	}

	@Override
	public String toString() {
		return "PM [ID=" + ID + ", u0=" + u0 + ", u_cpu=" + u_cpu + ", u_mem=" + u_mem + ", u_network=" + u_network
				+ ", cpu=" + cpu + ", memory=" + memory + ", size=" + size + ", network_bandwidth=" + network_bandwidth
				+ "]";
	}

	public String compactString() {
		return "PM" + ID;
	}

}