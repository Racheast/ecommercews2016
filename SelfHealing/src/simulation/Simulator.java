package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import monitoring.EnergyMonitor;
import monitoring.LatencyMonitor;

public class Simulator {

	public static void main(String[] args) {
		final int simulationDuration = 12000; // Set the simulationDuration in ms (eg. 1000ms == 1sec).

		SimulationRun improvedRun = new SimulationRun(true);
		SimulationRun baselineRun = new SimulationRun(false);
		improvedRun.start();
		baselineRun.start();
		EnergyMonitor energyMonitor = new EnergyMonitor(improvedRun.getController(), baselineRun.getController(), 500);
		energyMonitor.start();
		LatencyMonitor latencyMonitor = new LatencyMonitor(improvedRun.getController(), baselineRun.getController(), 500);
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
		energyMonitor.printImage();
		latencyMonitor.printImage();
		
		String content = improvedRun.printStatistics() + "\n" + baselineRun.printStatistics();

		printLog(content, "FailureHandling_EndResult.txt");
	}

	private static void printLog(String content, String fileName) {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");
		LocalDateTime now = LocalDateTime.now();
		String timestamp = dtf.format(now);
		
		File logDir = new File("logs");

		if (!logDir.exists()) {
			logDir.mkdirs();
		}

		File logFile = new File("logs/" + fileName);

		try (BufferedReader reader = new BufferedReader(new StringReader(timestamp +"\n\n" + content));
				PrintWriter writer = new PrintWriter(new FileWriter(logFile));) {
			reader.lines().forEach(line -> writer.println(line));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
