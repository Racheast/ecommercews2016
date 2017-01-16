package simulation;

import monitoring.EnergyMonitor;
import monitoring.LatencyMonitor;

public class Simulator {
	
	
	public static void main(String[] args) {
		SimulationRun improvedRun=new SimulationRun(true);
		SimulationRun baselineRun=new SimulationRun(false);
		improvedRun.start();
		baselineRun.start();
		EnergyMonitor energyMonitor=new EnergyMonitor(improvedRun.getController(), baselineRun.getController());
		energyMonitor.start();
		LatencyMonitor latencyMonitor=new LatencyMonitor(improvedRun.getController(), baselineRun.getController());
		latencyMonitor.start();

	}

}
