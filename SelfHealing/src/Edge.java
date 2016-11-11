import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Set;

public class Edge{
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
	
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}

	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}

	public boolean assignRequest(Request request){
		//1. assign application to proper pm
		//2. allocate a certain amount of available bandwidth to the pm

		//only for test purposes, remove this random selection afterwards
		ArrayList<PM> pmList=getListOfPMs();
		
		int bandwidthSum=0;
		for(PM pm:pmList){
			bandwidthSum+=pm.getNetwork_bandwidth();
		}
		if((this.bandwidth - bandwidthSum) >= request.getNeeded_bandwidth()){
			for(PM pm:pmList){
				if((pm.getCpu() - pm.getConsumed_cpu()) >= request.getNeeded_cpu() && (pm.getMemory()-pm.getConsumed_memory()) >= request.getNeeded_memory() && (pm.getNetwork_bandwidth() - pm.getConsumed_networkBandwith()) >= request.getNeeded_bandwidth()){
					pm.setNetwork_bandwidth(request.getNeeded_bandwidth());
					System.out.println("Assigning request to PM"+pm.getID());
					return true;
				}
			}
		}
		System.out.println("No available PM found in edge"+this.ID);
		return false;
	}
	
	public void putPM(Integer key, PM pm){
		pms.put(key, pm);
	}
	
	public HashMap<Integer, PM> getPms() {
		return pms;
	}
	
	public void setPms(HashMap<Integer, PM> pms) {
		this.pms = pms;
	}
	
	public int getBandwidth(){
		return this.bandwidth;
	}
	/*
	public int getBandwith(Edge targetEdge){
		return bandwidths.get(targetEdge);
	}
	
	public void setBandwith(Edge targetEdge, int bandwith){
		bandwidths.put(targetEdge.getID(), bandwith);
	}
	
	public void setBandwith(int targetEdgeID, int bandwith){
		bandwidths.put(targetEdgeID,bandwith);
	}
	*/
	public double getEnergyUtilization(){
		double u_total=u0;
		Set<Integer> keys=pms.keySet();
		for(int key:keys){
			u0+=pms.get(key).getEnergyUtilization();
		}
		return u_total;
	}
	
	private ArrayList<PM> getListOfPMs(){
		Set<Integer> keys=pms.keySet();
		ArrayList<PM> pmList=new ArrayList<PM>();
		for(int key:keys){
			pmList.add(pms.get(key));
		}
		return pmList;
	}
	
}
