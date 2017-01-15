package infrastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Set;

import comparables.ComparableEdge;
import exceptions.EdgeFailureException;
import exceptions.PMFailureException;
import interfaces.LocationElement;
import interfaces.Remote;
import interfaces.RemoteController;
import sla.SLAError;
import sla.SLAField;

public class EdgeController implements RemoteController {
	private HashMap<Integer, Edge> edges;
	private Edge[][] map;
	
	private final boolean improved;
	
	// constructor we don't use at the moment
	
	
	public EdgeController(Edge[][] edgeMap, boolean improved){
		edges = new HashMap<Integer, Edge>();
		this.addEdges(edgeMap);
		this.improved=improved;
	}
	
	public Edge[][] getMap() {
		return map;
	}

	public void addEdge(int x, int y, Edge edge) {
		edges.put(edge.getID(), edge);
		this.map[x][y] = edge;
	}
	
	public void addEdges(Edge[][] edges){
		this.map=edges;
		for(int x=0; x<edges.length; x++){
			for(int y=0; y<edges[0].length; y++){
				Edge e=edges[x][y];
				if(e!=null){
					this.edges.put(e.getID(), e);
				}
			}
		}
	}

	public synchronized RemoteClient sendVM(VM vm) {
		ArrayList<SLAError> slaErrors = new ArrayList<SLAError>();
		// check if vm-specs don't violate the SLAs which are assigned to the
		// request
		if (vm.getCpu() > vm.getRequest().getSla().getAgreedCPU()) {
			slaErrors.add(new SLAError(SLAField.agreedCPU, "Requested more than specified in the SLA. [requested=" + vm.getCpu()+", sla="+vm.getRequest().getSla().getAgreedCPU()+"]"));
		}

		if (vm.getMemory() > vm.getRequest().getSla().getAgreedMemory()) {
			slaErrors.add(new SLAError(SLAField.agreedMemory, "Requested more than specified in the SLA. [requested=" + vm.getMemory()+", sla="+vm.getRequest().getSla().getAgreedMemory()+"]"));
		}

		if (vm.getSize() > vm.getRequest().getSla().getAgreedSize()) {
			slaErrors.add(new SLAError(SLAField.agreedSize, "Requested more than specified in the SLA. [requested=" + vm.getSize()+", sla="+vm.getRequest().getSla().getAgreedSize()+"]" ));
		}

		if (vm.getNetworkBandwidth() > vm.getRequest().getSla().getAgreedNetworkBandwidth()) {
			slaErrors.add(new SLAError(SLAField.agreedNetworkBandwidth, "Requested more than specified in the SLA.[requested=" + vm.getNetworkBandwidth()+", sla="+vm.getRequest().getSla().getAgreedNetworkBandwidth()+"]"));
		}

		if (slaErrors.size() == 0) {

			ArrayList<Edge> sortedEdges = this.generateSortedDistanceList(this.getListOfEdges(), vm.getRequest());

			for (Edge e : sortedEdges) {
				System.out.println("CONTROLLER: SEND REQUEST OPERATION: " + vm.compactString() + " forwarded to edge"
						+ e.getID() + "\n");
				VM newVM = e.assignRequest(vm);

				if (newVM != null) {
					RemoteClient remoteClient = new RemoteClient();
					remoteClient.setVM(newVM);
					remoteClient.setController(this);
					return remoteClient;
				}
			}

			System.out.println("CONTROLLER: SEND REQUEST OPERATION: No proper edges found!\n");
			// add the counter of mistakes
		} else {
			System.out.println("CONTROLLER: SEND REQUEST OPERATION: Following SLA violations occured in " + vm.compactString() + ": ");
			for (SLAError e : slaErrors) {
				System.out.println(e);
			}
			System.out.println("CONTROLLER: " + vm.compactString() + " could not be forwarded\n");
		}
		return null;
	}

	private synchronized ArrayList<Edge> generateSortedDistanceList(ArrayList<Edge> edges, Request request) {
		return generateSortedDistanceList(edges, request.getxCoordinate(), request.getyCoordinate());
	}

	private synchronized ArrayList<Edge> generateSortedDistanceList(ArrayList<Edge> edges, int x, int y) {
		ArrayList<ComparableEdge> comparableEdges = new ArrayList<ComparableEdge>();
		ArrayList<Edge> sortedEdges = new ArrayList<Edge>();

		for (Edge e : edges) {
			ComparableEdge ce = new ComparableEdge(e, manhattanDistance(e.getxCoordinate(), e.getyCoordinate(), x, y));
			comparableEdges.add(ce);
		}

		Collections.sort(comparableEdges);

		for (ComparableEdge ce : comparableEdges) {
			sortedEdges.add(ce.getEdge());
		}

		return sortedEdges;
		// sorted edges
	}

	private int manhattanDistance(int x1, int y1, int x2, int y2) {
		return Math.abs(x2 - x1) + Math.abs(y2 - y1);
	}

	private int manhattanDistance(LocationElement e1, LocationElement e2) {
		if (e1 == null || e2 == null)
			return -1;
		return manhattanDistance(e1.getxCoordinate(), e1.getyCoordinate(), e2.getxCoordinate(), e2.getyCoordinate());
	}

	// we have different grids every time

	public String printMap() {
		String output = "";
		for (int y = 0; y < map[0].length; y++) {
			for (int x = 0; x < map.length; x++) {
				if (map[x][y] != null) {
					output += "X";
				} else {
					output += ".";
				}
			}
			output += "\n";
		}
		return output;
	}

	public ArrayList<Edge> getListOfEdges() {
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (int x = 0; x < map.length; x++) {
			for (int y = 0; y < map[0].length; y++) {
				if (map[x][y] != null) {
					Edge edge = map[x][y];
					edges.add(edge);
				}
			}
		}
		return edges;
	}

	private double getMemoryTransmissionRate(Edge sourceEdge, Edge targetEdge) {

		int distance = manhattanDistance(sourceEdge, targetEdge);
		int max_distance = map.length + map[0].length;

		if (distance == max_distance)
			distance--; // avoid that r==0

		double r = (1 - (distance / max_distance)) * sourceEdge.getBandwidth();

		return r;
	}

	@Override
	public boolean stop(VM vm) {
		System.out.println("CONTROLLER: Shutting down " + vm.compactString() + "\n");
		Edge edge = edges.get(vm.getAddress().getEdge_ID());
		if (edge != null) {
			PM pm = edge.getPms().get(vm.getAddress().getPM_ID());
			if (pm != null) {
				return pm.shutdownVM(vm.getID());
			}
		}
		return false;
	}

	@Override
	public synchronized VM move(VM vm, int x, int y) {
		System.out.println("CONTROLLER: Moving " + vm.compactString() + "\n");
		Edge sourceEdge = edges.get(vm.getAddress().getEdge_ID());
		ArrayList<Edge> sortedEdges = generateSortedDistanceList(this.getListOfEdges(), x, y);

		for (Edge targetEdge : sortedEdges) {
			System.out.println("CONTROLLER: MOVE OPERATION: " + vm.compactString() + " forwarded to "
					+ targetEdge.compactString() + "\n");
			int dataVolume = vm.getMemory();
			PM pm = targetEdge.findPMforVM(vm);

			if (pm != null) {
				if (pm.getID() != vm.getAddress().getPM_ID()) {
					double v_mig = migrateVM(vm, pm, getMemoryTransmissionRate(sourceEdge, targetEdge));
					double u_mig = 0.9 * v_mig + 0.1; // u_mig = alpha * v_mig +
														// beta
					VM newVM = pm.startApplication(vm);
					newVM.getAddress().setEdge_ID(targetEdge.getID());
					System.out.println("CONTROLLER: MOVE OPERATION:" + vm.compactString() + " copied from "
							+ vm.getAddress().compactString() + " to " + newVM.getAddress().compactString() + ". Migration energy utilization="+u_mig+"\n");
					stop(vm);
					return newVM;
				} else {
					System.out.println("CONTROLLER: MOVE OPERATION: " + vm.compactString() + " remained on "
							+ vm.getAddress().compactString() + "\n");
				}
			}

		}

		System.out.println("CONTROLLER: MOVE OPERATION: No proper edges found! VM remained on "
				+ vm.getAddress().compactString() + "\n");
		return vm; // Return the old vm if new edge could not be found
	}

	/*
	 * Iterratively "transmits" vm to pm Returns the total amount of datavolume
	 * that was transmitted during the migration process
	 */
	private double migrateVM(VM vm, PM pm, double transmissionRate) {
		double dataVolume = vm.getMemory();
		double totalDataVolume = dataVolume;
		while (dataVolume > vm.getMemory() * vm.getPage_dirtying_threshold()) {
			double t = pm.migrateVM(dataVolume, transmissionRate);
			dataVolume = vm.getPage_dirtying_rate() * t;
			totalDataVolume += dataVolume;
		}
		return totalDataVolume;
	}
	
	public double getTotalEnergyUtilization(){
		double u_total=0;
		Set<Integer> keys=edges.keySet();
		for(int key:keys){
			u_total+=edges.get(key).getEnergyUtilization();
		}
		System.out.println("CONTROLLER: u_total="+u_total);
		return u_total;
	}
	
	public synchronized void simulatePMFailure(){
		Random r=new Random();
		ArrayList<Integer> pm_ids=getAllPMIDs();
		int numberFails=(int)Math.abs(Math.round(r.nextGaussian()*2 + pm_ids.size()*0.1));
		
		for(int i=0; i<numberFails; i++){
			int i2=r.nextInt(pm_ids.size());
			PM pm=getPMbyID(pm_ids.get(i2));
			pm_ids.remove(i2);
			if(pm!=null){
				try{
					pm.simulatePMFailure();
				}catch(PMFailureException e){
					//TODO: Exceptionhandling (maybe generate Error-Erray and return it after completion)
				}
			}
		}
	}
	
	public synchronized void simulateEdgeFailure(){
		ArrayList<Integer> edge_ids=new ArrayList<Integer>(edges.keySet());
		Random r=new Random();
		int numberFails=(int)Math.abs(Math.round(r.nextGaussian()*2 + edge_ids.size()*0.17));
		
		for(int i=0; i<numberFails; i++){
			int i2=r.nextInt(edge_ids.size());
			Edge edge=edges.get(edge_ids.get(i2));
			edge_ids.remove(i2);
			if(edge!=null){
				try{	
					edge.simulateEdgeFailure();
				}catch(EdgeFailureException e){
					//TODO: Exceptionhandling (maybe generate Error-Array and return it after completion)					
				}
			}
		}
	}
	
	private ArrayList<Integer> getAllPMIDs(){
		ArrayList<Integer> pm_ids=new ArrayList<Integer>();
		for(Edge e:getListOfEdges()){
			for(int pm_id:e.getPms().keySet()){
				pm_ids.add(pm_id);
			}
		}
		return pm_ids;
	}
	
	private PM getPMbyID(int id){
		for(Edge e:getListOfEdges()){
			PM p=e.getPms().get(id);
			if(p!=null){
				return p;
			}
		}
		return null;
	}

}
