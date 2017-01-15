package infrastructure;
import interfaces.Remote;
import interfaces.RemoteController;

public class RemoteClient implements Remote{
	private RemoteController controller;
	private VM vm;
	
	public RemoteClient(){
		this.controller=null;
		this.vm=null;
	}
	
	public RemoteClient(VM vm) {
		this.vm=vm;
	}
	
	
	public boolean stop(){
		return controller.stop(vm);
	}

	public void move(int x, int y) {
		this.vm=controller.move(vm, x, y);
	}

	public void setController(RemoteController controller) {
		this.controller = controller;
	}
	
	public void setVM(VM vm){
		this.vm=vm;
	}
	
	public VM getVM(){
		return this.vm;
	}
		
}
