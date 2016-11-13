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

	public synchronized RemoteClient assignRequest(SpecificationElement specificationElement){
		//1. assign application to proper pm
		//2. allocate a certain amount of available bandwidth to the pm

		ArrayList<PM> pmList=getListOfPMs();
		
		int bandwidthSum=0;
		for(PM pm:pmList){
			bandwidthSum+=pm.getNetwork_bandwidth();
		}
		
		if((this.bandwidth - bandwidthSum) >= specificationElement.getNetworkBandwidth()){
			for(PM pm:pmList){
				//(pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= request.getNeeded_bandwidth()
				if((pm.getCpu() - pm.getConsumed_cpu()) >= specificationElement.getCpu() && (pm.getMemory()-pm.getConsumed_memory()) >= specificationElement.getMemory() && (pm.getConsumed_networkBandwith()==0 || (pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= specificationElement.getNetworkBandwidth())){
					pm.setNetwork_bandwidth(specificationElement.getNetworkBandwidth());
					String s="";
					
					if(specificationElement instanceof Request)
						s="Request";
					else if(specificationElement instanceof VM)
						s="VM";
					
					System.out.println("Assigning "+s+specificationElement.getID()+" to PM"+pm.getID());
					RemoteClient remoteClient=pm.startApplication(specificationElement);
					remoteClient.setEdge_ID(this.ID);
					return remoteClient;
				}
			}
		}
		System.out.println("No available PM found in edge"+this.ID);
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
	
	@Override
	public String toString() {
		return "Edge [ID=" + ID + ", pms=" + pms + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate
				+ ", u0=" + u0 + ", bandwidth=" + bandwidth + "]";
	}
	
}
