package airport.gui;


import airport.xmodel.Plane;
import airport.xmodel.Tower;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;

import javax.swing.*;



// EXTRA
@SuppressWarnings("serial")
class WindowMap extends JPanel {

	 
	 private ArrayList<Tower> allTower;
	 private ArrayList<JLabel> towerIds;
	 private ArrayList<Shape> allTowerCorner;
	 private ArrayList<Shape> towerOvals;
	 
	 
	 public WindowMap() {
	    // Skip
		 allTower = new ArrayList<Tower>();
		 towerIds = new ArrayList<JLabel>();
		 allTowerCorner = new ArrayList<Shape>();
		 towerOvals = new ArrayList<Shape>();
		 
		 for(int i = 0; i < Main.getTowerCount(); i++) {
	    	 Tower tower = Main.getTower(Main.getTowerId(i));
	    	 //Image towerImg = Toolkit.getDefaultToolkit().getImage(WindowMap.class.getResource("/airport/imgs/towerRed.png"));
		     
	    	 allTower.add(tower);
	    	 towerIds.add(new JLabel(Integer.toString(tower.getId())));
	    	 int radius = tower.getRadius();
	    	 towerOvals.add(new Ellipse2D.Double(tower.getX()-radius/2+7.5, tower.getY()-radius/2+7.5, radius, radius));
	    	 allTowerCorner.add(new Rectangle2D.Double(tower.getX()-3, tower.getY()-3, 20, 40));
	    	 
	     }
		 
		 // Add image
		 addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent me) {
            	 super.mouseClicked(me);
            	 for(int i = 0; i < allTower.size(); i++) {
        			 if((allTowerCorner.get(i)).contains(me.getPoint())) {
                  		System.out.println("Tower id: " + allTower.get(i).getId());
                  	 }
            	 }
            	 
            	 for(int i = 0; i < Main.getPlaneIdsSize(); i++) {
     	     		int id = Main.getPlaneId(i);
     	     		Plane plane = Main.getPlane(id);
     	     		
     	     		double planeX = plane.getX();
     	     		double planeY = plane.getY();
     	     		
     	     		int planeWidth = 15;
     	     		int planeHeight = 15;
     	     		
     	     		int meX = me.getPoint().x;
     	     		int meY = me.getPoint().y;
     	     		
     	     		
     	     		if(meX >= planeX && meX <= planeX+planeWidth && meY >= planeY && meY <= planeY+planeHeight) {
     	     			Main.openPlane(plane.getId());
     	     		}
     	     	}
            	
            }
		 });
		 
		 
		 addMouseMotionListener(new MouseAdapter() {
	        @Override
	        public void mouseMoved(MouseEvent me) {
	        		boolean l = false;
	        		
	            	 super.mouseMoved(me);
	            	 for(int i = 0; i < allTower.size(); i++) {
	        			 if((allTowerCorner.get(i)).contains(me.getPoint())) {
	        				 l = true;
	                  	 }
	            	 }
	            	 
	            	 for(int i = 0; i < Main.getPlaneIdsSize(); i++) {
	            		 int id = Main.getPlaneId(i);
	      	     		Plane plane = Main.getPlane(id);
	      	     		
	      	     		double planeX = plane.getX();
	      	     		double planeY = plane.getY();
	      	     		
	      	     		int planeWidth = 15;
	      	     		int planeHeight = 15;
	      	     		
	      	     		int meX = me.getPoint().x;
	      	     		int meY = me.getPoint().y;
	      	     		
	      	     		
	      	     		if(meX >= planeX && meX <= planeX+planeWidth && meY >= planeY && meY <= planeY+planeHeight) {
	      	     			l = true;
	      	     		}
	            	 }
	            	 
	            	 if(l) {
      	     			setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
	            	 } else {
      	     			setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	            	 }
	            }
	     });
	 }
	 
	 @Override
	 protected void paintComponent(Graphics grphcs) {
		 super.paintComponent(grphcs);
	     Graphics2D g2d = (Graphics2D) grphcs;
	     Image img1 = Toolkit.getDefaultToolkit().getImage(WindowMap.class.getResource("/airport/imgs/map.jpg"));
	     g2d.drawImage(img1, 0, 0, this);
	     g2d.finalize();
	     
	     
	     
	     
	     // Planes
	     	for(int i = 0; i < Main.getPlaneIdsSize(); i++) {
	     		int id = Main.getPlaneId(i);
	     		Plane plane = Main.getPlane(id);
	     		
	     		Image planeImg = Toolkit.getDefaultToolkit().getImage(WindowMap.class.getResource("/airport/imgs/plane.png"));

	     		int angle = plane.getAngle();
	     		
	     		/*
	     		g2d.rotate(Math.toRadians(+(angle)), 7.5, 7.5);

	     	    g2d.drawImage(planeImg, plane.getX(), plane.getY(), this);
	     	    g2d.rotate(Math.toRadians(-(angle)), 7.5, 7.5);   
	     		*/
	     		
	     		
	            g2d.rotate(Math.toRadians(angle), plane.getX() + 7.5, plane.getY() + 7.5);
	            g2d.drawImage(planeImg, (int) plane.getX(), (int) plane.getY(), this);

	            g2d.rotate(Math.toRadians(-angle), plane.getX() + 7.5, plane.getY() + 7.5);
	            
	            
	            
	            String planeId = Integer.toString(plane.getId());
	            

			    g2d.setPaint(Color.BLACK);
	            g2d.setFont(new Font("default", Font.BOLD, 12));
	            
		    	g2d.drawString(planeId, (int) plane.getX()+5, (int) plane.getY()+15+13);
		    	
	     	    //System.out.println("X: " + (plane.getX()-dx) + ", Y: " + (plane.getY()-dy));
	     		
	     		//g2d.drawImage(planeImg, plane.getX(), plane.getY(), this);
	     		
	     	}
	     
	     
	     // Towers
	     for(int i = 0; i < allTower.size(); i++) {
	    	 Tower tower = allTower.get(i);
	    	 
	    	 Image towerImg = Toolkit.getDefaultToolkit().getImage(WindowMap.class.getResource("/airport/imgs/tower.png"));
		     g2d.drawImage(towerImg, tower.getX(), tower.getY(), this);
		     g2d.finalize();
		     
		     add(towerIds.get(i));
		     towerIds.get(i).setForeground(Color.BLACK);
			 towerIds.get(i).setBounds(tower.getX()+5, tower.getY()+13, 60, 25);
			 

			 Color darkGray = new Color(70, 70, 70);
		     g2d.setPaint(darkGray);
			 g2d.draw(towerOvals.get(i));

		     g2d.setPaint(Color.BLACK);
			 
	     }
	 }
	 
	 public int isInCircle(Point point) {
		 int i = towerIds.size()-1;
		 while(i >= 0) {
			 if(towerOvals.get(i).contains(point)) {
          		return i;
          	 }
			 i--;
		 }
		 return i;
	 }
}



