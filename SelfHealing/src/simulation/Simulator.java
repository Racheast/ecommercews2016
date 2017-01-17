package simulation;

import monitoring.EnergyMonitor;
import monitoring.LatencyMonitor;

public class Simulator {

	public static void main(String[] args) {
		final int simulationDuration = 70000;  //Set the simulationDuration in ms (eg. 1000ms == 1sec).
		
		SimulationRun improvedRun = new SimulationRun(true);
		SimulationRun baselineRun = new SimulationRun(false);
		improvedRun.start();
		baselineRun.start();
		EnergyMonitor energyMonitor = new EnergyMonitor(improvedRun.getController(), baselineRun.getController());
		energyMonitor.start();
		LatencyMonitor latencyMonitor = new LatencyMonitor(improvedRun.getController(), baselineRun.getController());
		latencyMonitor.start();

		try {
			Thread.sleep(simulationDuration);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		improvedRun.stop();
		baselineRun.stop();
		energyMonitor.stop();
		latencyMonitor.stop();
		improvedRun.printStatistics();
		baselineRun.printStatistics();

	}

}
