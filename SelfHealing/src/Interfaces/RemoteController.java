package Interfaces;

import impl.Address;
import impl.RemoteClient;

public interface RemoteController {
	public boolean stop(Address currentAddress);
	public Address move(Address currentAddress,int x, int y);
}
