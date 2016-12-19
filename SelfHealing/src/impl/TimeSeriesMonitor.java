package impl;

import java.awt.BorderLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
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

    /** The most recent value added. */
    private double lastValue = 100.0;
    
    private Second currentSecond;
    
    private final JFreeChart chart;
    /**
     * Constructs a new demonstration application.
     *
     * @param title  the frame title.
     */
    public TimeSeriesMonitor(String title) {
        super(title);
        this.currentSecond=new Second();
        this.series = new TimeSeries("Random Data", Second.class);
        final TimeSeriesCollection dataset = new TimeSeriesCollection(this.series);
        //final JFreeChart chart = createChart(dataset);
        this.chart=createChart(dataset);

        final ChartPanel chartPanel = new ChartPanel(chart);
       
        final JPanel content = new JPanel(new BorderLayout());
        content.add(chartPanel);
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);

    }

    /**
     * Creates a sample chart.
     * 
     * @param dataset  the dataset.
     * 
     * @return A sample chart.
     */
    private JFreeChart createChart(final XYDataset dataset) {
        final JFreeChart result = ChartFactory.createTimeSeriesChart(
            "Dynamic Data Demo", 
            "Time", 
            "Value",
            dataset, 
            true, 
            true, 
            false
        );
        final XYPlot plot = result.getXYPlot();
        ValueAxis axis = plot.getDomainAxis();
        axis.setAutoRange(true);
        axis.setFixedAutoRange(60000.0);  // 60 seconds
        axis = plot.getRangeAxis();
        axis.setRange(0.0, 20000); 
        //axis.setAutoRangeMinimumSize(100000.0);
        return result;
    }
    
  

	
	public void updateChart(double value) {
		this.currentSecond =(Second) this.currentSecond.next();
		ValueAxis axis=this.chart.getXYPlot().getDomainAxis();
		axis.resizeRange(0.0, 100000);
		series.add(currentSecond, value);	
	}

	public void showChart() {
		this.pack();
		RefineryUtilities.positionFrameRandomly(this);
		this.setVisible(true);
	}

}
