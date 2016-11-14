package impl;

public class Address {
	private int Edge_ID;
	private int PM_ID;	
	public Address(){
		
	}
	
	public Address(int Edge_ID, int PM_ID) {
		super();
		this.Edge_ID = Edge_ID;
		this.PM_ID = PM_ID;
	}


	public int getEdge_ID() {
		return Edge_ID;
	}

	public void setEdge_ID(int edge_ID) {
		Edge_ID = edge_ID;
	}

	public int getPM_ID() {
		return PM_ID;
	}

	public void setPM_ID(int pM_ID) {
		PM_ID = pM_ID;
	}

	

	@Override
	public String toString() {
		return "Address [Edge_ID=" + Edge_ID + ", PM_ID=" + PM_ID + "]";
	}

	public String compactString(){
		return Edge_ID+"/"+PM_ID;
	}
	
	
	
}
