package airport.gui;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;

import javax.swing.*;



// EXTRA
@SuppressWarnings("serial")
class WindowDraw extends JPanel {
	 final private Shape negyzet = new Rectangle2D.Double(10, 10, 100, 100);
	 
	 public WindowDraw() {
	    // Skip
		 
		 
		 addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	 super.mouseClicked(me);
            	 if(negyzet.contains(me.getPoint())) {
             		System.out.println("Muhaha");
             	}
            }
		 });
	 }
	 
	 @Override
	 protected void paintComponent(Graphics grphcs) {
		 super.paintComponent(grphcs);
	     Graphics2D g2d = (Graphics2D) grphcs;
	     g2d.setPaint(Color.BLACK);
		 
		 g2d.draw(negyzet);
	 }
	 
}



