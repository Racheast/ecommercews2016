package simulation;

import java.util.ArrayList;
import java.util.Random;
import java.util.Set;
import java.util.Timer;

import infrastructure.Edge;
import infrastructure.EdgeController;
import infrastructure.PM;

public class FailureSimulator implements Runnable {
	private final EdgeController controller;
	private volatile Thread t;

	public FailureSimulator(EdgeController controller) {
		this.controller = controller;
	}

	@Override
	public void run() {
		Thread thisThread = Thread.currentThread();
		Random rand=new Random();
		t=thisThread;
		while (t == thisThread) {
            //simulateFailure()
			System.out.println("FailureSimulator: BUMM!");
			try {
				Edge edge=controller.getListOfEdges().get(rand.nextInt(controller.getListOfEdges().size()));
				PM pm=edge.getListOfPMs().get(rand.nextInt(edge.getListOfPMs().size()));
				
				long sleep=(long)Math.abs(Math.round(rand.nextGaussian() * 2000 + 2000));
				thisThread.sleep(sleep); 
            } catch (InterruptedException e){
            	//doSmthng?
            }
        }
	}
	
	public void stop() {
        t = null;
    }

}
