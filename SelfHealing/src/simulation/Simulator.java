package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import monitoring.EnergyMonitor;
import monitoring.LatencyMonitor;

public class Simulator {

	public static void main(String[] args) {
		final int simulationDuration = 60000; // Set the simulationDuration in ms (eg. 1000ms == 1sec).

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
		String content = improvedRun.printStatistics() + "\n" + baselineRun.printStatistics();

		printLog(content, "FailureHandling_EndResult.txt");
	}

	private static void printLog(String content, String fileName) {
		File logDir = new File("logs");

		if (!logDir.exists()) {
			logDir.mkdirs();
		}

		File logFile = new File("logs/" + fileName);

		try (BufferedReader reader = new BufferedReader(new StringReader(content));
				PrintWriter writer = new PrintWriter(new FileWriter(logFile));) {
			reader.lines().forEach(line -> writer.println(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
