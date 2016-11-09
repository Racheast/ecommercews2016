import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
	
	public void sendRequest(Application application){
		//allocate application to proper edge
	}
	
	
	private int manhattanDistance(int x1, int y1, int x2, int y2){
		return Math.abs(x2-x1) + Math.abs(y2-y1);
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
	
}
