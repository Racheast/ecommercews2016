package simulation;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import infrastructure.Request;
import infrastructure.VM;
import interfaces.Remote;

public class MoveSimulator {
	private final Remote remote;
	private final int x_max, y_max;
	private final Timer timer;

	public MoveSimulator(Remote remote, int x_max, int y_max) {
		super();
		this.remote = remote;
		this.x_max=x_max;
		this.y_max=y_max;
		this.timer=new Timer();
	}
	
	public void start(){
		if (remote != null) {
			long end = System.currentTimeMillis() + remote.getVM().getRuntime();

			timer.schedule(new TimerTask() {

				@Override
				public void run() {
					if (System.currentTimeMillis() < end) {
						int old_x = remote.getVM().getRequest().getxCoordinate();
						int old_y = remote.getVM().getRequest().getyCoordinate();
						randomizeRequestCoordinates(remote.getVM().getRequest());
						
						System.out.println("MOVE_SIMULATOR: Moving " + remote.getVM().getRequest().compactString() + " from ("
								+ old_x + "/" + old_y + ") to (" + remote.getVM().getRequest().getxCoordinate() + "/"
								+ remote.getVM().getRequest().getyCoordinate() + ")\n");
						remote.move(remote.getVM().getRequest().getxCoordinate(), remote.getVM().getRequest().getyCoordinate());
					} else {
						System.out.println("MOVE_SIMULATOR: Cancelling " + remote.getVM().getRequest().compactString() + "\n");
						remote.stop();
						this.cancel();
					}
				}
			}, 2000, 1000); // moving requests (=users) across the map
		}
	}
	
	public void stop(){
		this.timer.cancel();
	}
	
	private void randomizeRequestCoordinates(Request request){
		request.setxCoordinate(randomStep(request.getxCoordinate(), x_max));
		request.setyCoordinate(randomStep(request.getyCoordinate(), y_max));
	}
	
	private int randomStep(int c, int max_c) {
		Random rand = new Random();
		int step = rand.nextInt(3) - 1; // step out of [-1,0,+1]
		int new_c = c + step;
		if (new_c >= max_c) {
			new_c = max_c - 1;
		} else if (new_c < 0) {
			new_c = 0;
		}
		return new_c;
	}
}
