package impl;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class GraphNode extends JPanel{
	private Color c;
	public GraphNode(Color c){
		super();
		this.c=c;
	}
	@Override
	public void paintComponent(Graphics g){
		g.drawOval(5,5,10,10);
		g.setColor(c);
		g.fillOval(5, 5, 10, 10);
	}
	
}
