package impl;

import java.util.Timer;
import java.util.TimerTask;

public class Monitor {
	private EdgeController edgeController;
	private TimeSeriesMonitor t;

	public Monitor(EdgeController edgeController) {
		super();
		this.edgeController = edgeController;
		this.t = new TimeSeriesMonitor("Total Energy Utilization");
	}

	public void start() {
		t.showChart();
		Timer timer = new Timer();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				System.out.println("MONITOR: getTotalEnergyUtil()="+edgeController.getTotalEnergyUtilization());
				t.updateChart(edgeController.getTotalEnergyUtilization());
			}
		}, 0, 1000);
	}
}
