package impl;
import Interfaces.LocationElement;
import Interfaces.SpecificationElement;

public class Request implements LocationElement{
	private static int id_counter=1;
	private final int ID;
	
	private int xCoordinate;
	private int yCoordinate;
	
	public Request(int xCoordinate, int yCoordinate) {
		super();
		this.ID=id_counter++;
		this.xCoordinate = xCoordinate;
		this.yCoordinate = yCoordinate;
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

	@Override
	public String toString() {
		return "Request [ID=" + ID + ", xCoordinate=" + xCoordinate + ", yCoordinate=" + yCoordinate + "]";
	}
	
	public String compactString(){
		return "Request"+ID;
	}
}