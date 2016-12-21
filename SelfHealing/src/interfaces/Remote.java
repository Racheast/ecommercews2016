package interfaces;

import infrastructure.VM;

public interface Remote {
	public boolean stop();
	public void move(int x, int y);
	public VM getVM();
}
