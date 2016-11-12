import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import Interfaces.LocationElement;

public class EdgeController{
	private Edge[][] map;
	
	public EdgeController(){
		map=new Edge[100][100];
	}
	
	public EdgeController(int x_max, int y_max){
		map=new Edge[x_max][y_max];		
	}

	public Edge[][] getMap() {
		return map;
	}

	public void setMap(Edge[][] map) {
		this.map = map;
	}
	
	public void addEdge(int x, int y, Edge edge){
		this.map[x][y]=edge;
	}
	
	public VM sendRequest(Request request){
		//TODO: allocate application to proper edge
		
		/*
		 * 1. generate sorted Edge list considering the distance between each edge-location and the request-location
		 * 2. run through sorted Edge list and check whether this edge has free specs
		 * 3. assign request to the nearest free edge and return true or return false
		 */
		VM vm=null;
		ArrayList<Edge> sortedEdges=this.generateSortedDistanceList(this.getEdges(), request);
		
		for(Edge e:sortedEdges){
			System.out.println("Request forwarded to edge"+e.getID());
			vm=e.assignRequest(request);
		}
		
		if(vm==null)
			System.out.println("No proper edges found!");
		
		return vm;
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
			System.out.println("C.Edge"+ce.getEdge().getID()+"("+ce.getEdge().getxCoordinate()+","+ce.getEdge().getyCoordinate()+"), distance="+ce.getDistance());
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
	
	public ArrayList<Edge> getEdges(){
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
	
}
