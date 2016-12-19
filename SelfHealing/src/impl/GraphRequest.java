package impl;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

public class GraphRequest extends JPanel {
	private Color c;
	public GraphRequest(Color c){
		super();
		this.c=c;
	}
	@Override
	public void paintComponent(Graphics g){
		g.drawOval(0,0,10,10);
		g.fillOval(0, 0, 10, 10);
	}
}
