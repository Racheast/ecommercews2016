package monitoring;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.SeriesException;
import org.jfree.data.time.DynamicTimeSeriesCollection;
import org.jfree.data.time.Millisecond;
import org.jfree.data.time.Second;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.data.xy.XYDataset;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class TimeSeriesMonitor extends ApplicationFrame {

	 /** The time series data. */
    private TimeSeries series;
    private TimeSeries series2;

    /** The most recent value added. */
    private double lastValue = 100.0;
    
    private Second currentSecond;
    
    private final JFreeChart chart;
    
    private final XYPlot plot;
    
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public TimeSeriesMonitor(String title) {
        super(title);
        this.currentSecond=new Second();
        this.series = new TimeSeries("Improved", Second.class);
        this.series2 = new TimeSeries("Baseline", Second.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        final TimeSeriesCollection dataset2 = new TimeSeriesCollection(this.series2);

        //final JFreeChart chart = createChart(dataset);
        //this.chart=createChart(dataset);
        
        this.chart=ChartFactory.createTimeSeriesChart(
                "Total Energy Utilization", 
                "Time", 
                "Value",
                dataset, 
                true, 
                true, 
                false
            );
        
        this.plot = chart.getXYPlot();
        this.plot.setDataset(1,dataset2);
        
        //Scale the plot axis
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        //axis.setRange(0.0, 15000);  
        axis.setAutoRange(true);
        //Set the graph colors
        XYLineAndShapeRenderer renderer0 = new XYLineAndShapeRenderer(); 
        XYLineAndShapeRenderer renderer1 = new XYLineAndShapeRenderer(); 
        plot.setRenderer(0, renderer0); 
        plot.setRenderer(1, renderer1); 
        renderer0.setShapesVisible(false);
        renderer1.setShapesVisible(false);
        plot.getRendererForDataset(plot.getDataset(0)).setSeriesPaint(0, Color.red); 
        plot.getRendererForDataset(plot.getDataset(1)).setSeriesPaint(0, Color.blue);
       
        final ChartPanel chartPanel = new ChartPanel(chart);
       
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }


	public void updateSeries(double value1, double value2) {
		this.currentSecond =(Second) this.currentSecond.next();
		ValueAxis axis=this.chart.getXYPlot().getDomainAxis();
		axis.resizeRange(0.0, 100000);
		series.add(currentSecond, value1);
		series2.add(currentSecond, value2);
	}

	public void showChart() {
		this.pack();
		RefineryUtilities.positionFrameRandomly(this);
		this.setVisible(true);
	}

}
