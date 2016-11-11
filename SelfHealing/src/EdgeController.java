import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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
	
	public boolean sendRequest(Request request){
		//TODO: allocate application to proper edge
		
		/*
		 * only for test purposes! remove this afterwards!
		 * iteratively picks an edge that has enough free specs
		 * NO DISTANCE CONSIDERATION
		 */
		ArrayList<Edge> edges=this.getEdges();
		for(Edge e:getEdges()){
			if(e.assignRequest(request)==true){
				System.out.println("Request forwarded to edge"+e.getID());
				return true;
			}
		}
		System.out.println("No proper edges found!");
		return false;
	}
	
	
	private int manhattanDistance(int x1, int y1, int x2, int y2){
		return Math.abs(x2-x1) + Math.abs(y2-y1);
	}
	
	private int manhattanDistance(Edge e1, Edge e2){
		if(e1 == null || e2 == null)
			return -1;
		return manhattanDistance(e1.getxCoordinate(),e2.getxCoordinate(),e1.getyCoordinate(),e2.getyCoordinate());
	}
	
	public String printMap(){
		String output="";
		for(int x=0; x<map.length; x++){
			for(int y=0; y<map[0].length; y++){
				if(map[x][y]!=null){
					output+="X";
				}else{
					output+=" ";
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
