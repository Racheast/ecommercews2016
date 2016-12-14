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
		SimulationRun simulationRun=new SimulationRun(10,10);
		simulationRun.run();
		
		/*
		Edge[][] map=new Edge[4][8];  //x=4, y=8
		GridMonitor frame = new GridMonitor(map);
	    frame.showMonitor();
		*/
		/**
		Timer timer=new Timer();
		timer.schedule(new TimerTask(){

			@Override
			public void run() {
				System.out.println("Before updateMatrix()");
				//frame.update();
				System.out.println("After updateMatrix()");
			}
			
		}, 0, 2000);
		*/
	}

}
