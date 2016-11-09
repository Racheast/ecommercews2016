import java.util.HashMap;

public class Edge{
	private final int ID;
	private static int id_counter=1;
	private HashMap<Integer,PM> pms=new HashMap<Integer,PM>();
	
	private int xCoordinate;
	private int yCoordinate;
	private HashMap<Integer,Integer> bandwidths;  //key = ID of another edge
	
	public Edge(int xCoordinate, int yCoordinate){
		this.ID=id_counter++;
		this.xCoordinate=xCoordinate;
		this.yCoordinate=yCoordinate;
		this.bandwidths=new HashMap<Integer,Integer>();
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

	public void assignApplication(Application application){
		//assign application to proper pm
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
	
	public int getBandwith(Edge targetEdge){
		return bandwidths.get(targetEdge);
	}
	
	public void setBandwith(Edge targetEdge, int bandwith){
		bandwidths.put(targetEdge.getID(), bandwith);
	}
	
	public void setBandwith(int targetEdgeID, int bandwith){
		bandwidths.put(targetEdgeID,bandwith);
	}
	
}
