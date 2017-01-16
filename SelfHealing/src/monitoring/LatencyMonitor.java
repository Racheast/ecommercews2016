package monitoring;

import java.util.Timer;
import java.util.TimerTask;

import infrastructure.EdgeController;

public class LatencyMonitor {
	private final EdgeController edgeController1;
	private final EdgeController edgeController2;

	private TimeSeriesMonitor t;
	private final Timer timer;
	
	public LatencyMonitor(EdgeController edgeController1, EdgeController edgeController2) {
		super();
		this.timer=new Timer();
		this.edgeController1 = edgeController1;
		this.edgeController2 = edgeController2;
		this.t = new TimeSeriesMonitor("Average Latency");
	}

	public void start() {
		t.showChart();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				t.updateSeries(edgeController1.getAverageLatency(), edgeController2.getAverageLatency());
			}
		}, 0, 1000);
	}
	
	public void stop() {
		this.timer.cancel();
	}
}
