package monitoring;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import javax.imageio.ImageIO;

import infrastructure.EdgeController;

public class EnergyMonitor {
	private final EdgeController edgeController1;
	private final EdgeController edgeController2;
	private TimeSeriesMonitor t;
	private final Timer timer;
	private final int plotInterval;  //in ms
	
	public EnergyMonitor(EdgeController edgeController1, EdgeController edgeController2, int plotInterval) {
		super();
		this.timer=new Timer();
		this.edgeController1 = edgeController1;
		this.edgeController2 = edgeController2;
		this.t = new TimeSeriesMonitor("Total Energy Utilization", "time", "u_total");
		this.plotInterval=plotInterval;
	}

	public void start() {
		t.showChart();
		timer.schedule(new TimerTask() {
			@Override
			public void run() {
				t.updateSeries(edgeController1.getTotalEnergyUtilization(), edgeController2.getTotalEnergyUtilization());
			}
		}, 0, plotInterval);
	}
	
	public void stop() {
		this.timer.cancel();
	}
	
	public void printImage(){
		BufferedImage objBufferedImage=t.getChart().createBufferedImage(600,400);
		ByteArrayOutputStream bas = new ByteArrayOutputStream();
		        try {
		            ImageIO.write(objBufferedImage, "png", bas);
		        } catch (IOException e) {
		            e.printStackTrace();
		        }

		byte[] byteArray=bas.toByteArray();
		ByteArrayInputStream in = new ByteArrayInputStream(byteArray);
		BufferedImage image;
		try {
			image = ImageIO.read(in);
			File logDir = new File("logs");
			if(!logDir.exists()){
				logDir.mkdirs();
			}
			File outputfile = new File("logs/EnergyMonitor.png");
			ImageIO.write(image, "png", outputfile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
