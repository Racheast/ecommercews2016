package Interfaces;

public interface RemoteController {
	public boolean stop(int Edge_ID,int PM_ID, int VM_ID);
	public boolean move(int x, int y);
}
