package impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import Comparables.ComparablePM;
import Interfaces.LocationElement;
import Interfaces.Remote;
import Interfaces.SpecificationElement;

public class Edge implements LocationElement {
	private final int ID;
	private static int id_counter = 1;
	private HashMap<Integer, PM> pms = new HashMap<Integer, PM>();

	private int xCoordinate;
	private int yCoordinate;
	private double u0;
	private int bandwidth;

	public Edge(int xCoordinate, int yCoordinate, double u0, int bandwidth) {
		this.ID = id_counter++;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.u0 = u0;
		this.bandwidth = bandwidth;
	}

	public int getID() {
		return this.ID;
	}

	public int getxCoordinate() {
		return xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}

	public synchronized VM assignRequest(VM vm) {
		PM pm = findPMforVM(vm);
		if (pm != null) {
			// pm.addNetwork_bandwidth(vm.getNetworkBandwidth());
			System.out.println(
					this.compactString() + ": Assigning " + vm.compactString() + " to " + pm.compactString() + "\n");
			VM newVM = pm.startApplication(vm);
			newVM.getAddress().setEdge_ID(this.ID);
			return newVM;
		}

		System.out.println("EDGE" + this.ID + ": No available PM found!\n");
		return null;
	}

	/*
	 * Searches for a PM that has enough free specs to handle the vm. If vm is
	 * already located in this edge then the hosting pm is returned. otherwise a
	 * pm with enough free specs is returned. If no pm available then null is
	 * returned.
	 */
	public synchronized PM findPMforVM(VM vm) {
		ArrayList<PM> sortedPMs = generateSortedEnergyList(vm, getListOfPMs());

		// Check if vm is already located in this edge. If yes return pm that
		// contains vm
		for (PM pm : sortedPMs) {
			if (pm.getVms().containsKey(vm.getID())) {
				return pm;
			}
		}

		for (PM pm : sortedPMs) {
			if ((pm.getCpu() - pm.getConsumed_cpu()) >= vm.getCpu()
					&& (pm.getMemory() - pm.getConsumed_memory()) >= vm.getMemory() && (pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= vm.getNetworkBandwidth()) {
				return pm;
			}
		}

		return null;
	}

	/*
	 * public synchronized VM assignRequest(VM vm, double transmissionRate){
	 * //1. assign application to proper pm //2. allocate a certain amount of
	 * available bandwidth to the pm
	 * 
	 * ArrayList<PM> pmList=getListOfPMs();
	 * 
	 * for(PM pm:pmList){ if(pm.getVms().containsKey(vm.getID())){ return vm; }
	 * }
	 * 
	 * for(PM pm:pmList){ if(pm.getVms().containsKey(vm.getID())){
	 * System.out.println("if(pm.getVms().containsKey(vm.getID())) == TRUE");
	 * return vm; } }
	 * 
	 * int bandwidthSum=getConsumedBandwidth();
	 * 
	 * if((this.bandwidth - bandwidthSum) >= vm.getNetworkBandwidth()){ for(PM
	 * pm:pmList){ if((pm.getCpu() - pm.getConsumed_cpu()) >= vm.getCpu() &&
	 * (pm.getMemory()-pm.getConsumed_memory()) >= vm.getMemory() ){
	 * pm.addNetwork_bandwidth(vm.getNetworkBandwidth());
	 * 
	 * System.out.println(this.compactString()+": Assigning "+vm.compactString()
	 * +" to "+pm.compactString()+"\n"); VM newVM=pm.copyVM(vm,
	 * transmissionRate); newVM.getAddress().setEdge_ID(this.ID); return newVM;
	 * } } } System.out.println("EDGE"+this.ID+": No available PM found!\n");
	 * return null; }
	 */

	public synchronized void putPM(Integer key, PM pm) {
		pms.put(key, pm);
	}

	public HashMap<Integer, PM> getPms() {
		return pms;
	}

	public synchronized void setPms(HashMap<Integer, PM> pms) {
		this.pms = pms;
	}

	public synchronized void installPms(HashMap<Integer, PM> pms) {
		this.pms = pms;
		// Distribute the Networkbandwidth of this Edge uniformly among its PMs
		Set<Integer> keys = pms.keySet();
		for (int key : keys) {
			pms.get(key).setNetwork_bandwidth((int) Math.floor(this.bandwidth / pms.size()));
		}
	}

	public int getBandwidth() {
		return this.bandwidth;
	}

	public synchronized double getEnergyUtilization() {
		double u_total = u0;
		Set<Integer> keys = pms.keySet();
		for (int key : keys) {
			u_total += pms.get(key).getEnergyUtilization();
		}
		return u_total;
	}

	private synchronized ArrayList<PM> getListOfPMs() {
		Set<Integer> keys = pms.keySet();
		ArrayList<PM> pmList = new ArrayList<PM>();
		for (int key : keys) {
			pmList.add(pms.get(key));
		}
		return pmList;
	}

	private synchronized int getConsumedBandwidth() {
		ArrayList<PM> pmList = getListOfPMs();
		int bandwidthSum = 0;
		for (PM pm : pmList) {
			bandwidthSum += pm.getNetwork_bandwidth();
		}
		return bandwidthSum;
	}

	@Override
	public String toString() {
		return "Edge [ID=" + ID + ", pms=" + pms + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate
				+ ", u0=" + u0 + ", bandwidth=" + bandwidth + "]";
	}

	public String compactString() {
		return "Edge" + ID;
	}

	private ArrayList<PM> generateSortedEnergyList(VM vm, ArrayList<PM> pms) {
		ArrayList<ComparablePM> comparablePMs = new ArrayList<ComparablePM>();
		ArrayList<PM> sortedPMs = new ArrayList<PM>();
		for (PM pm : pms) {
			comparablePMs.add(new ComparablePM(pm, pm.getExpectedEnergyUtilization(vm)));
		}
		Collections.sort(comparablePMs);
		for (ComparablePM cpm : comparablePMs) {
			sortedPMs.add(cpm.getPm());
		}
		return sortedPMs;
	}

}
