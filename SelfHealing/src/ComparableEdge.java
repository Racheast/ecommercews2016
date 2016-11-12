
public class ComparableEdge implements Comparable<ComparableEdge> {
	private final Edge edge;
	private int distance;
	
	public ComparableEdge(Edge edge, int distance){
		this.edge=edge;
		this.distance=distance;
	}
	
	public Edge getEdge(){
		return this.edge;
	}
	
	public int getDistance(){
		return this.distance;
	}
	
	@Override
	public int compareTo(ComparableEdge edge2) {
		if(this.distance > edge2.distance)
			return 1;
		else if(this.distance < edge2.distance)
			return -1;
		
		return 0;
	}

}
