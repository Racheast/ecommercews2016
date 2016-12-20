package monitoring;

import java.util.Timer;
import java.util.TimerTask;

import Infrastructure.EdgeController;

public class Monitor {
	private final EdgeController edgeController1;
	private final EdgeController edgeController2;

	private TimeSeriesMonitor t;
	private final Timer timer;
	
	public Monitor(EdgeController edgeController1, EdgeController edgeController2) {
		super();
		this.timer=new Timer();
		this.edgeController1 = edgeController1;
		this.edgeController2 = edgeController2;
		this.t = new TimeSeriesMonitor("Total Energy Utilization");
	}

	public void start() {
		t.showChart();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				t.updateSeries(edgeController1.getTotalEnergyUtilization(), edgeController2.getTotalEnergyUtilization());
			}
		}, 0, 1000);
	}
	
	public void stop() {
		this.timer.cancel();
	}
}
