package interfaces;

import Infrastructure.Address;
import Infrastructure.RemoteClient;
import Infrastructure.VM;

public interface RemoteController {
	public boolean stop(VM vm);
	public VM move(VM vm,int x, int y);
}
