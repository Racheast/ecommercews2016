package impl;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

import Interfaces.LocationElement;
import Interfaces.Remote;
import Interfaces.SpecificationElement;

public class Edge implements LocationElement{
	private final int ID;
	private static int id_counter=1;
	private HashMap<Integer,PM> pms=new HashMap<Integer,PM>();
	
	private int xCoordinate;
	private int yCoordinate;
	//private HashMap<Integer,Integer> bandwidths;  //key = ID of a targetedge
	private double u0;
	private int bandwidth;
	
	public Edge(int xCoordinate, int yCoordinate, double u0, int bandwidth){
		this.ID=id_counter++;
		this.xCoordinate=xCoordinate;
		this.yCoordinate=yCoordinate;
		//this.bandwidths=new HashMap<Integer,Integer>();
		this.u0=u0;
		this.bandwidth=bandwidth;
	}
	
	public int getID(){
		return this.ID;
	}
	
	public int getxCoordinate() {
		return xCoordinate;
	}

	public int getyCoordinate() {
		return yCoordinate;
	}
	
	public synchronized VM assignRequest(VM vm){
		ArrayList<PM> pmList=getListOfPMs();
		
		for(PM pm:pmList){
			if(pm.getVms().containsKey(vm.getID())){
				return vm;
			}
		}
		
		int bandwidthSum=getConsumedBandwidth();
		
		if((this.bandwidth - bandwidthSum) >= vm.getNetworkBandwidth()){
			for(PM pm:pmList){
				if((pm.getCpu() - pm.getConsumed_cpu()) >= vm.getCpu() && (pm.getMemory()-pm.getConsumed_memory()) >= vm.getMemory() && (pm.getConsumed_networkBandwith()==0 || (pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= vm.getNetworkBandwidth())){
					pm.setNetwork_bandwidth(vm.getNetworkBandwidth());
				
					System.out.println(this.compactString()+": Assigning "+vm.compactString()+" to "+pm.compactString()+"\n");
					VM newVM=pm.startApplication(vm);
					newVM.getAddress().setEdge_ID(this.ID);
					return newVM;
				}
			}
		}
		System.out.println("EDGE"+this.ID+": No available PM found!\n");
		return null;
	}
	
	public synchronized VM assignRequest(VM vm, double transmissionRate){
		//1. assign application to proper pm
		//2. allocate a certain amount of available bandwidth to the pm
		
		ArrayList<PM> pmList=getListOfPMs();
		
		for(PM pm:pmList){
			if(pm.getVms().containsKey(vm.getID())){
				return vm;
			}
		}
		/*
		for(PM pm:pmList){
			if(pm.getVms().containsKey(vm.getID())){
				System.out.println("if(pm.getVms().containsKey(vm.getID())) == TRUE");
				return vm;
			}
		}
		*/
		int bandwidthSum=getConsumedBandwidth();
		
		if((this.bandwidth - bandwidthSum) >= vm.getNetworkBandwidth()){
			for(PM pm:pmList){
				if((pm.getCpu() - pm.getConsumed_cpu()) >= vm.getCpu() && (pm.getMemory()-pm.getConsumed_memory()) >= vm.getMemory() && (pm.getConsumed_networkBandwith()==0 || (pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= vm.getNetworkBandwidth())){
					pm.setNetwork_bandwidth(vm.getNetworkBandwidth());
				
					System.out.println(this.compactString()+": Assigning "+vm.compactString()+" to "+pm.compactString()+"\n");
					VM newVM=pm.copyVM(vm, transmissionRate);
					newVM.getAddress().setEdge_ID(this.ID);
					return newVM;
				}
			}
		}
		System.out.println("EDGE"+this.ID+": No available PM found!\n");
		return null;
	}
	
	public synchronized void putPM(Integer key, PM pm){
		pms.put(key, pm);
	}
	
	public HashMap<Integer, PM> getPms() {
		return pms;
	}
	
	public synchronized void setPms(HashMap<Integer, PM> pms) {
		this.pms = pms;
	}
	
	public int getBandwidth(){
		return this.bandwidth;
	}
	
	public synchronized double getEnergyUtilization(){
		double u_total=u0;
		Set<Integer> keys=pms.keySet();
		for(int key:keys){
			u0+=pms.get(key).getEnergyUtilization();
		}
		return u_total;
	}
	
	private synchronized ArrayList<PM> getListOfPMs(){
		Set<Integer> keys=pms.keySet();
		ArrayList<PM> pmList=new ArrayList<PM>();
		for(int key:keys){
			pmList.add(pms.get(key));
		}
		return pmList;
	}
	
	private synchronized int getConsumedBandwidth(){
		ArrayList<PM> pmList=getListOfPMs();
		int bandwidthSum=0;
		for(PM pm:pmList){
			bandwidthSum+=pm.getNetwork_bandwidth();
		}
		return bandwidthSum;
	}
	
	@Override
	public String toString() {
		return "Edge [ID=" + ID + ", pms=" + pms + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate
				+ ", u0=" + u0 + ", bandwidth=" + bandwidth + "]";
	}
	
	public String compactString(){
		return "Edge"+ID;
	}
	
}
