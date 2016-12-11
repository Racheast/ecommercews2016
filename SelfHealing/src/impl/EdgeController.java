package impl;
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
	
	//constructor we don't use at the moment
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
	
	// FIFO realization for handling requests
	public synchronized RemoteClient sendRequest(VM vm){
		//TODO: allocate application to proper edge
		
		/*
		 * 1. generate sorted Edge list considering the distance between each edge-location and the request-location
		 * 2. run through sorted Edge list and check whether this edge has free specs
		 * 3. assign request to the nearest free edge and return true or return false
		 */
		ArrayList<Edge> sortedEdges=this.generateSortedDistanceList(this.getListOfEdges(), vm.getRequest());
		
		for(Edge e:sortedEdges){
			System.out.println("CONTROLLER: SEND REQUEST OPERATION: "+vm.compactString()+" forwarded to edge"+e.getID()+"\n");
			VM newVM=e.assignRequest(vm);
			
			if(newVM!=null){
				RemoteClient remoteClient=new RemoteClient();
				remoteClient.setVM(newVM);
				remoteClient.setController(this);
				return remoteClient;
			}
		}
		
		System.out.println("CONTROLLER: SEND REQUEST OPERATION: No proper edges found!\n");
		//add the counter of mistakes
		return null;
	}
	
	private synchronized ArrayList<Edge> generateSortedDistanceList(ArrayList<Edge> edges, Request request){
		return generateSortedDistanceList(edges,request.getxCoordinate(),request.getyCoordinate());
	}
	
	private synchronized ArrayList<Edge> generateSortedDistanceList(ArrayList<Edge> edges, int x, int y){
		ArrayList<ComparableEdge> comparableEdges=new ArrayList<ComparableEdge>();
		ArrayList<Edge> sortedEdges=new ArrayList<Edge>();
		
		for(Edge e:edges){
			ComparableEdge ce=new ComparableEdge(e, manhattanDistance(e.getxCoordinate(),e.getyCoordinate(),x,y));
			comparableEdges.add(ce);
		}
		
		Collections.sort(comparableEdges);
		
		for(ComparableEdge ce:comparableEdges){
			sortedEdges.add(ce.getEdge());
		}
		
		return sortedEdges;
		//sorted edges
	}
	
	private int manhattanDistance(int x1, int y1, int x2, int y2){
		return Math.abs(x2-x1) + Math.abs(y2-y1);
	}
	
	private int manhattanDistance(LocationElement e1, LocationElement e2){
		if(e1 == null || e2 == null)
			return -1;
		return manhattanDistance(e1.getxCoordinate(),e1.getyCoordinate(),e2.getxCoordinate(),e2.getyCoordinate());
	}
	
	
//we have different grids every time
	
	public String printMap(){
		String output="";
		for(int y=0; y<map[0].length; y++){
			for(int x=0; x<map.length; x++){
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
	public boolean stop(VM vm) {
		System.out.println("CONTROLLER: Shutting down "+vm.compactString()+"\n");
		Edge edge=edges.get(vm.getAddress().getEdge_ID());
		if(edge!=null){
			PM pm=edge.getPms().get(vm.getAddress().getPM_ID());
			if(pm!=null){
					return pm.shutdownVM(vm.getID());
			}
		}
		return false;
	}

	@Override
	public synchronized VM move(VM vm, int x, int y) {
		Edge edge=edges.get(vm.getAddress().getEdge_ID());
		if(edge!=null){
			PM pm=edge.getPms().get(vm.getAddress().getPM_ID());
			if(pm!=null){
				
					//System.out.println("CONTROLLER: Moving VM"+vm.getID()+" of Request"+vm.getRequest().getID()+" from ("+vm.getRequest().getxCoordinate()+"/"+vm.getRequest().getyCoordinate()+") to ("+x+"/"+y+")");
					System.out.println("CONTROLLER: Moving "+vm.compactString()+"\n");
					
					ArrayList<Edge> sortedEdges=generateSortedDistanceList(this.getListOfEdges(), x, y);
					
					for(Edge e:sortedEdges){
						System.out.println("CONTROLLER: MOVE OPERATION: "+vm.compactString()+" forwarded to "+e.compactString()+"\n");
						VM newVM=e.assignRequest(vm);
						if(newVM!=null){
							if(vm!=newVM){
								System.out.println("CONTROLLER: MOVE OPERATION:"+ vm.compactString() +" copied from "+vm.getAddress().compactString()+" to "+newVM.getAddress().compactString()+"\n");
								stop(vm);
							}else{
								System.out.println("CONTROLLER: MOVE OPERATION: "+ vm.compactString() +" remained on "+vm.getAddress().compactString()+"\n");
							}
							return newVM;
						}
					}
				
			}
		}
		System.out.println("MOVE OPERATION: No proper edges found! VM stayed on "+vm.getAddress().compactString()+"\n");
		return vm;  //Return the old vm if new edge could not be found
	}


	
}
