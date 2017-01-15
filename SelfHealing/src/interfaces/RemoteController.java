package interfaces;

import infrastructure.Address;
import infrastructure.RemoteClient;
import infrastructure.VM;

public interface RemoteController {
	public boolean stop(VM vm);
	public VM move(VM vm,int x, int y);
}
