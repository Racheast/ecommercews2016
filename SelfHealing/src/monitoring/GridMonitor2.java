package monitoring;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import infrastructure.Edge;
import infrastructure.VM;

public class GridMonitor2 extends JFrame{
	private Edge[][] edges;
	private HashMap<Integer,VM> vms;
	
	//Swing components
	private JPanel panel;
	private final int max_x;
	private final int max_y;
	private final int windowWidth=750;
	private final int windowHeight=750;
	private final int widthLimit=100;
	private final int heightLimit=100;
	
	public GridMonitor2(Edge[][] edges, HashMap<Integer,VM> vms) {
		super();
		this.edges = edges;
		this.vms = vms;
		this.max_x=edges.length;
		this.max_y=edges[0].length;
		
		panel = new JPanel();
		panel.setBackground(Color.WHITE);
		panel.setLayout(new GridLayout(1,1));
		panel.setSize(this.windowWidth, this.windowHeight);
		//this.add(panel);
		this.add(new GraphEdge(100,100));
		this.add(new GraphEdge(100,200));
		this.add(new GraphEdge(100,300));

		/*
		for(int x=0; x<max_x; x++){
			for(int y=0; y<max_y;y++){
				Edge e=edges[x][y];
				if(e!=null){
					System.out.println("e != null");
					int graph_x=(int)Math.round((new Double(x)/new Double(max_x))*(windowWidth-widthLimit));
					int graph_y=(int)Math.round((new Double(y)/new Double(max_y))*(windowHeight-heightLimit));
					panel.add(new GraphEdge(graph_x,graph_y));
				}
			}
		}
		*/
		
		//panel.revalidate();
		//panel.repaint(0);

		//reprintMap();
	}
	
	public void reprintMap(){
		this.panel = (JPanel)this.panel;
		this.panel.removeAll();
		
		panel.revalidate();
		panel.repaint();
	}
	
	public void showMonitor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.windowWidth, this.windowHeight);
		this.setVisible(true);
	}
	
}
