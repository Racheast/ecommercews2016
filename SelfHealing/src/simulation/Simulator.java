package simulation;

import monitoring.Monitor;

public class Simulator {
	
	
	public static void main(String[] args) {
		//SimulationRun simulationRun=new SimulationRun(10,10, true);
		SimulationRun improvedRun=new SimulationRun(true);
		SimulationRun baselineRun=new SimulationRun(false);
		improvedRun.start();
		baselineRun.start();
		Monitor monitor=new Monitor(improvedRun.getController(), baselineRun.getController());
		monitor.start();

	}

}
