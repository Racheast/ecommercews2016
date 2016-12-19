package impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class GraphEdge extends JPanel{
	private int x,y;
	public GraphEdge(int x, int y){
		super();
		this.x=x;
		this.y=y;
	}
	@Override
	public void paintComponent(Graphics g){
		//g.drawOval(0,0,10,10);
		//g.fillOval(0, 0, 10, 10);
		/*
		BufferedImage img;
		try {
			img = ImageIO.read(getClass().getResource("/images/server_icon.png"));			
			g.drawImage(img, 0, 0, 30,30,this);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/

		System.out.println("GraphEdge: Drawing at x="+x+", y="+y);
		g.drawRect(x, y, 20, 40);
		g.setColor(Color.BLACK);
	}
	
}
