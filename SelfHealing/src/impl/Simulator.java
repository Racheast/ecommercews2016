package impl;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JFrame;

import javafx.application.Application;
import javafx.stage.Stage;

public class Simulator {
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//SimulationRun simulationRun=new SimulationRun(10,10);
		//simulationRun.run();
		/*
		GridMonitor frame = new GridMonitor();
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 3
	    frame.setSize(400,400);
	    frame.setVisible(true);
	    */
	   
		
		Timer timer=new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				System.out.println("Before updateMatrix()");
				//frame.update();
				System.out.println("After updateMatrix()");
			}
			
		}, 0, 2000);
		
	}

}
