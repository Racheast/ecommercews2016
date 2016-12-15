package impl;

import javax.swing.*; // JFrame, JPanel, ...

import Interfaces.LocationElement;

import java.awt.*; // GridLayout
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class GridMonitor extends JFrame { // This is the window class
	//Swing Components
	private JLayeredPane lp;
	private JPanel gridPanel;
	private JPanel glassPanel;
	
	private final int max_x;
	private final int max_y;
	private final int windowWidth=750;
	private final int windowHeight=750;
	
	private Edge[][] edgeArray;
	private HashMap<Integer, VM> vms;

	private LocationCell[][] locationCells;
		
	
	GraphNode gn1=new GraphNode(Color.BLUE);
	GraphNode gn2=new GraphNode(Color.GREEN);
	
	public GridMonitor(Edge[][] edgeArray) {
		this.max_x = edgeArray.length;
		this.max_y = edgeArray[0].length;
		
		this.vms = new HashMap<Integer, VM>();
		this.edgeArray = edgeArray;
		
		//Initialized Swing Components
		lp=getLayeredPane();
		
		gridPanel = new JPanel();
		gridPanel.setLayout(new GridLayout(max_y, max_x));
		gridPanel.setBackground(Color.WHITE);
		gridPanel.setSize(this.windowWidth, this.windowHeight);
		
		glassPanel = new JPanel();
		glassPanel.setLayout(new BorderLayout());
		glassPanel.setSize(this.windowWidth, this.windowHeight);
		glassPanel.setBackground(Color.GREEN);
		glassPanel.setOpaque(false);
		JLabel[][] labels = new JLabel[max_y][max_x];

		
		this.locationCells = new LocationCell[max_x][max_y];
		for(int y=0; y<max_y; y++){
			for(int x=0; x<max_x; x++){
				this.locationCells[x][y] = new LocationCell();
			}
		}
		
		for (int y = 0; y < max_y; y++) {
			for (int x = 0; x < max_x; x++) {
				LocationElement l = this.edgeArray[x][y];
				if(l!=null){	
					locationCells[x][y].getLocationElements().put(l.getID(), l);
				}	
			}
		}
		
		//this.add(gridPanel);
		lp.add(gridPanel, Integer.valueOf(1));
		lp.add(glassPanel, Integer.valueOf(2));
		this.reprintMap();

	}

	public void addVM(VM vm) {
		vms.put(vm.getID(), vm);
	}

	public void reprintMap() {
		this.gridPanel = (JPanel)this.gridPanel;
		this.gridPanel.removeAll();
		
		JLabel[][] labels = new JLabel[max_y][max_x];

		for (int y = 0; y < max_y; y++) {
			for (int x = 0; x < max_x; x++) {
				HashMap<Integer, LocationElement> locationElements = locationCells[x][y].getLocationElements();
				Set<Integer> keys = locationElements.keySet();
				
				String cellString = "";
				
				for (int key : keys) {
					LocationElement l = locationElements.get(key);
					if (l != null) {
						if (l instanceof Edge) {
							Edge e = (Edge) l;
							cellString += e.compactString() + "(" + e.getxCoordinate() + "," + e.getyCoordinate() + ")"
									+ "\n";
							
						} else if (l instanceof Request) {
							Request r = (Request) l;
							cellString += r.compactString() + "(" + r.getxCoordinate() + "," + r.getyCoordinate() + ")"
									+ "\n";
							
						}

					}

				} // end of for keys
				//labels[y][x] = new JLabel(cellString);
				//gridPanel.add(labels[y][x]);
				
				if(x==2 && y==2){
					gridPanel.add(gn1);
				}
				else if(x==5 && y==5){
					gridPanel.add(gn2);
				}else{
					gridPanel.add(new GraphNode(Color.WHITE));
				}
				
			} // end of for x
		} // end of for y
		GraphEdge ge=new GraphEdge(2,2,5,5);
		System.out.println("gn1 coordinates: "+gn1.getX()+", "+gn1.getY());
		System.out.println("gn2 coordinates: "+gn2.getX()+", "+gn2.getY());
		glassPanel.add(ge);
		gridPanel.revalidate();
		gridPanel.repaint();
		
	}
	
	public void moveLocationElement(int old_x, int old_y, LocationElement l){
		int new_x=l.getxCoordinate();
		int new_y=l.getyCoordinate();
		System.out.println("GridMonitor.moveLocationElement(): old_x="+old_x+", old_y="+old_y+"\nnew_x="+new_x+", new_y="+new_y);
		if(locationCells[old_x][old_y].getLocationElements().containsKey(l.getID())){
			locationCells[old_x][old_y].getLocationElements().remove(l.getID());
			locationCells[new_x][new_y].getLocationElements().put(l.getID(), l);
		}
		
		this.reprintMap();
		
	}
	
	public void deleteLocationElement(LocationElement l){
		System.out.println("deleteLocationElement: "+l);
		int x=l.getxCoordinate();
		int y=l.getyCoordinate();
		if(locationCells[x][y].getLocationElements().containsKey(l.getID())){
			locationCells[x][y].getLocationElements().remove(l.getID());
		}
		
		this.reprintMap();
	}
	
	public void addLocationElement(LocationElement l) {
		System.out.println("addLocationElement: "+l);
		this.locationCells[l.getxCoordinate()][l.getyCoordinate()].getLocationElements().put(l.getID(), l);
	}

	public void showMonitor() {
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setSize(this.windowWidth, this.windowHeight);
		this.setVisible(true);
	}
}
