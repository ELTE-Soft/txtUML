package airport.gui;

import javax.swing.*;

import airport.xmodel.Plane;

import java.awt.Color;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


public class WindowOnePlane extends JPanel implements KeyListener {
	private static final long serialVersionUID = -8489860060227576592L;

	private int id;
	
	private JLabel _status;
	private JLabel _x;
	private JLabel _y;
	private JLabel _angle;
	private JLabel _tower;
	
	public WindowOnePlane(int planeId) {
		id = planeId;
		
		setLayout(null);
		
		JLabel label;
		label = new JLabel("Plane - " + id);
		//String name = "Plane - " + id;
		label.setFont(new Font("H1", Font.BOLD, 20));
		
		add(label);
		label.setBounds(10, 10, 200, 25);
		
		
		
		JLabel status = new JLabel("Status: ");
		JLabel x = new JLabel("X: ");
		JLabel y = new JLabel("Y: ");
		JLabel angle = new JLabel("Angle: ");
		JLabel tower = new JLabel("Tower: ");
		
		add(status);
		add(x);
		add(y);
		add(angle);
		add(tower);
		
		status.setBounds(20, 40, 50, 25);
		x.setBounds(20, 72, 50, 25);
		y.setBounds(20, 104, 50, 25);
		angle.setBounds(20, 136, 50, 25);
		tower.setBounds(20, 168, 50, 25);
		
		status.setHorizontalAlignment(SwingConstants.RIGHT);
	    x.setHorizontalAlignment(SwingConstants.RIGHT);
	    y.setHorizontalAlignment(SwingConstants.RIGHT);
	    angle.setHorizontalAlignment(SwingConstants.RIGHT);
	    tower.setHorizontalAlignment(SwingConstants.RIGHT);
	    
	    _status = new JLabel("0");
		_x = new JLabel("1");
		_y = new JLabel("2");
		_angle = new JLabel("3");
		_tower = new JLabel("4");
		
		add(_status);
		add(_x);
		add(_y);
		add(_angle);
		add(_tower);
		
		_status.setBounds(75, 40, 50, 25);
		_x.setBounds(75, 72, 50, 25);
		_y.setBounds(75, 104, 50, 25);
		_angle.setBounds(75, 136, 50, 25);
		_tower.setBounds(75, 168, 50, 25);
		
		addKeyListener(this);
		refresh();
	}
	
	public void refresh() {
		
		
		Plane plane = Main.getPlane(id);
		
		_status.setText(WindowPlanes.status(plane.getStatus()));
		_x.setText(Integer.toString((int) plane.getX()));
		_y.setText(Integer.toString((int) plane.getY()));
		_angle.setText(Integer.toString(plane.getAngle()));
		_tower.setText(Integer.toString(plane.getTowerId()));
			
		revalidate();
		repaint();
		

		setFocusable(true);

		Main._planesRefresh();
		// Is in an Circle
			Point myPoint = new Point((int) plane.getX(), (int) plane.getY());
			
			int towerId = Main.isInAroundTower(id, myPoint);
			
			if(plane.getTowerId() != towerId) {
				Main.connectTowerAndPlane(towerId, plane.getId());
			}
	}


	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int pressed = e.getKeyCode();
		
		// TODO Auto-generated method stub
		
				if(pressed == 37) {
					Main.minus15(id);
				}
				if(pressed == 38) {
					Main.go(id);
				}
				if(pressed == 39) {
					Main.plus15(id);
				}
				
				refresh();
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}


