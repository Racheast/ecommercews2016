package impl;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
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

		
		g.drawRect(0, 0, 20, 40);
		g.setColor(c);
	}
	
}
