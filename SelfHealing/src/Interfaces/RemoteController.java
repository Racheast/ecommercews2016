package Interfaces;

import impl.Address;
import impl.RemoteClient;
import impl.VM;

public interface RemoteController {
	public boolean stop(VM vm);
	public VM move(VM vm,int x, int y);
}
