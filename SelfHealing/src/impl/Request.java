package impl;
import Interfaces.LocationElement;
import Interfaces.SpecificationElement;

public class Request implements LocationElement{
	private static int id_counter=1;
	private final int ID;
	
	private SLA sla;
	
	private int xCoordinate;
	private int yCoordinate;
	
	public Request(int xCoordinate, int yCoordinate, SLA sla) {
		super();
		this.ID=id_counter++;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
		this.sla = sla;
	}
	
	public int getxCoordinate() {
		return xCoordinate;
	}
	public void setxCoordinate(int xCoordinate) {
		this.xCoordinate = xCoordinate;
	}
	public int getyCoordinate() {
		return yCoordinate;
	}
	public void setyCoordinate(int yCoordinate) {
		this.yCoordinate = yCoordinate;
	}
	
	public int getID(){
		return this.ID;
	}
	

	public SLA getSla() {
		return sla;
	}

	public void setSla(SLA sla) {
		this.sla = sla;
	}

	@Override
	public String toString() {
		return "Request [ID=" + ID + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}
	
	public String compactString(){
		return "Request"+ID;
	}
}