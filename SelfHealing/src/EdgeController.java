import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Interfaces.LocationElement;
import Interfaces.Remote;
import Interfaces.RemoteController;

public class EdgeController implements RemoteController{
	private HashMap<Integer, Edge> edges;
	private Edge[][] map;
	
	public EdgeController(){
		map=new Edge[100][100];
		edges=new HashMap<Integer,Edge>();
	}
	
	public EdgeController(int x_max, int y_max){
		map=new Edge[x_max][y_max];
		edges=new HashMap<Integer,Edge>();
	}

	public Edge[][] getMap() {
		return map;
	}

	public void setMap(Edge[][] map) {
		this.map = map;
	}
	
	public void addEdge(int x, int y, Edge edge){
		edges.put(edge.getID(), edge);
		this.map[x][y]=edge;
	}
	
	public Remote sendRequest(Request request){
		//TODO: allocate application to proper edge
		
		/*
		 * 1. generate sorted Edge list considering the distance between each edge-location and the request-location
		 * 2. run through sorted Edge list and check whether this edge has free specs
		 * 3. assign request to the nearest free edge and return true or return false
		 */
		RemoteClient remoteClient=null;
		ArrayList<Edge> sortedEdges=this.generateSortedDistanceList(this.getListOfEdges(), request);
		
		for(Edge e:sortedEdges){
			System.out.println("Request"+request.getID()+" forwarded to edge"+e.getID());
			remoteClient=e.assignRequest(request);
			remoteClient.setController(this);
			return remoteClient;
		}
		
		if(remoteClient==null)
			System.out.println("No proper edges found!");
		
		return remoteClient;
	}
	
	private ArrayList<Edge> generateSortedDistanceList(ArrayList<Edge> edges, Request request){
		ArrayList<ComparableEdge> comparableEdges=new ArrayList<ComparableEdge>();
		ArrayList<Edge> sortedEdges=new ArrayList<Edge>();
		
		for(Edge e:edges){
			ComparableEdge ce=new ComparableEdge(e, manhattanDistance(e,request));
			comparableEdges.add(ce);
		}
		Collections.sort(comparableEdges);
		
		for(ComparableEdge ce:comparableEdges){
			//System.out.println("C.Edge"+ce.getEdge().getID()+"("+ce.getEdge().getxCoordinate()+","+ce.getEdge().getyCoordinate()+"), distance="+ce.getDistance());
			sortedEdges.add(ce.getEdge());
		}
		return sortedEdges;
	}
	
	private int manhattanDistance(int x1, int y1, int x2, int y2){
		return Math.abs(x2-x1) + Math.abs(y2-y1);
	}
	
	private int manhattanDistance(LocationElement e1, LocationElement e2){
		if(e1 == null || e2 == null)
			return -1;
		return manhattanDistance(e1.getxCoordinate(),e1.getyCoordinate(),e2.getxCoordinate(),e2.getyCoordinate());
	}
	
	public String printMap(){
		String output="";
		for(int x=0; x<map.length; x++){
			for(int y=0; y<map[0].length; y++){
				if(map[x][y]!=null){
					output+="X";
				}else{
					output+=".";
				}
			}
			output+="\n";
		}
		return output;
	}
	
	public ArrayList<Edge> getListOfEdges(){
		ArrayList<Edge> edges=new ArrayList<Edge>();
		for(int x=0; x<map.length; x++){
			for(int y=0; y<map[0].length; y++){
				if(map[x][y]!=null){
					Edge edge=map[x][y];
					edges.add(edge);
				}
			}
		}
		return edges;
	}
	
	private double getMemoryTransmissionRate(Edge sourceEdge, Edge targetEdge){
		
		int distance=manhattanDistance(sourceEdge,targetEdge);
		int max_distance=map.length+map[0].length;
		
		if(distance==max_distance)
			distance--;  //avoid that r==0
		
		double r=(1- distance/max_distance)*sourceEdge.getBandwidth();
			
		return r;
	}
	
	@Override
	public boolean stop(int Edge_ID, int PM_ID, int VM_ID) {
		Edge edge=edges.get(Edge_ID);
		if(edge!=null){
			PM pm=edge.getPms().get(PM_ID);
			if(pm!=null){
				if(pm.getVms().get(VM_ID)!=null){
					pm.shutdownVM(VM_ID);
				}
			}
		}
		return false;
	}

	@Override
	public boolean move(int x, int y) {
		// TODO Auto-generated method stub
		return false;
	}


	
}
