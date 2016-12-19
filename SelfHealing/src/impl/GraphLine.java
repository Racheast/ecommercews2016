package impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.JComponent;

public class GraphLine extends JComponent {
	private final int x1,x2,y1,y2;

	public GraphLine(int x1, int x2, int y1, int y2){
		super();
		this.x1=x1;
		this.y1=y1;
		this.x2=x2;
		this.y2=y2;
	}
	@Override
	public void paintComponent(Graphics g){
		g.setColor(Color.RED);
		g.drawLine(x1,y1,x2,y2);
	}
}
