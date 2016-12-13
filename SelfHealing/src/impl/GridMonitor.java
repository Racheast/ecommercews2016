package impl;

import javax.swing.*; // JFrame, JPanel, ...
import java.awt.*; // GridLayout

public class GridMonitor extends JFrame { // This is the window class
    private JPanel panel;
	
    public GridMonitor(int x, int y) {
    	panel = new JPanel();
        panel.setLayout(new GridLayout(x,y));
        panel.setBackground(Color.WHITE);
        JLabel[][] labels = new JLabel[x][y];
        for (int i = 0; i < y; i++)
        {
            for (int j = 0; j < x; j++)
            {
                labels[j][i] = new JLabel("("+j+", "+i+")");
                panel.add(labels[j][i]);
            }
        }
        this.add(panel);     
    }  
   
    public void update(){
    	/*
    	this.panel.removeAll();
    	JLabel[][] labels = new JLabel[5][5];
        for (int i = 0; i < 5; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                labels[j][i] = new JLabel("( "+number+" II "+j+", "+i+")");
                panel.add(labels[j][i]);
            }
        }
        panel.revalidate();
    	panel.repaint();
    	*/
    }
}
