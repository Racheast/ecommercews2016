package impl;
import Interfaces.Remote;
import Interfaces.RemoteController;

public class RemoteClient implements Remote{
	private RemoteController controller;
	private Address currentAddress;
	
	public RemoteClient(){
		
	}
	
	public RemoteClient(Address currentAddress) {
		this.currentAddress=currentAddress;
	}
	
	
	public boolean stop(){
		return controller.stop(currentAddress);
	}

	public void move(int x, int y) {
		this.currentAddress=controller.move(currentAddress, x, y);
	}

	public void setController(RemoteController controller) {
		this.controller = controller;
	}

	public void setCurrentAddress(Address currentAddress) {
		this.currentAddress = currentAddress;
	}	
	
}
