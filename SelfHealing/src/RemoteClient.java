import Interfaces.Remote;
import Interfaces.RemoteController;

public class RemoteClient implements Remote{
	private RemoteController controller;
	private int Edge_ID;
	private int PM_ID;
	private int VM_ID;
	
	
	/*
	public RemoteClient(RemoteController controller,int edge_ID, int pM_ID, int vM_ID) {
		this.controller=controller;
		Edge_ID = edge_ID;
		PM_ID = pM_ID;
		VM_ID = vM_ID;
	}
	*/
	public boolean stop(){
		return controller.stop(Edge_ID, PM_ID, VM_ID);
	}

	public Remote move(int x, int y) {
		return controller.move(Edge_ID, PM_ID, VM_ID, x, y);
	}

	public void setController(RemoteController controller) {
		this.controller = controller;
	}

	public void setEdge_ID(int edge_ID) {
		Edge_ID = edge_ID;
	}

	public void setPM_ID(int pM_ID) {
		PM_ID = pM_ID;
	}

	public void setVM_ID(int vM_ID) {
		VM_ID = vM_ID;
	}
	
	
	
}
