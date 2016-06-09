package airport;

/* README
 * 
 * The number next to the runway is the plane id, which is or will be in there
 * 
 * Rects in the left side are the runways
 * 		Green - free
 * 		Red - occipued
 * Circles are the airplanes
 * 
 * Texts above the buttons:
 * 	t - take off
 * 	l - land
 * 	ht - has taken off
 * 	hl - has landed
 * 	at - annul taking off
 * 	al - annul landind
 * 
 * 
 * Texts in the planes:
 * 	IA - In the Air
 * 	WL - Waiting for the Landing
 * 	AL - Able to Land
 *  OG - On the Ground
 *  WT - Waiting for the Taking off
 *  AT - Able to Take off
 *  
 */

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import airport.xmodel.*;

public class GUI_Tester {

	public GUI_Tester() throws FileNotFoundException {
		JFrame frame = new JFrame();
		frame.setTitle("Airport tester");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(true);

		initComponents(frame);

		frame.pack();
		frame.setVisible(true);

		frame.setLocationRelativeTo(null);
	}

	public static void main(String[] args) {

		ModelExecutor.create().run(() -> {
			try {
				new GUI_Tester();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		});
	}

	private void initComponents(JFrame frame) throws FileNotFoundException {
		frame.add(new txtUMLPanel());
	}
}

// custom panel
@SuppressWarnings("serial")
class txtUMLPanel extends JPanel {
	private static Scanner in;
	private static Scanner file;

	static private Vector<Plane> _planes;
	static private Vector<Runway> _runways;
	static private Tower _tower;
	static private Weather _weather;

	private int numOfRunways;
	private int numOfPlanes;

	private final ArrayList<Shape> pluses;
	private final ArrayList<Shape> minuses;

	private final ArrayList<Shape> runways;
	private final ArrayList<Shape> planes;
	private final ArrayList<Shape> t;
	private final ArrayList<Shape> l;

	private final ArrayList<Shape> ut;
	private final ArrayList<Shape> ht;
	private final ArrayList<Shape> ul;
	private final ArrayList<Shape> hl;
	private final ArrayList<Shape> at;
	private final ArrayList<Shape> al;

	private Dimension dim = new Dimension(880, 500);

	int paddingTop = 50;

	public txtUMLPanel() throws FileNotFoundException {
		boolean error = true;
		boolean withFile = false;
		while (error) {
			error = false;
			in = new Scanner(System.in);
			System.out.print("File: ");
			String fileName = in.nextLine();
			withFile = !fileName.toLowerCase().equals("null");
			if (withFile) {
				try {
					file = new Scanner(new File("test/" + fileName));
					numOfPlanes = file.nextInt();
					numOfRunways = file.nextInt();
				} catch (java.io.FileNotFoundException exp) {
					error = true;
				}
			} else {
				System.out.print("Number of runways: ");
				numOfRunways = in.nextInt();
				System.out.print("Number of planes: ");
				numOfPlanes = in.nextInt();
			}
		}

		// txtUML initializing
		// Initializing

		_planes = new Vector<Plane>();
		_runways = new Vector<Runway>();

		for (int i = 0; i < numOfPlanes; i++) {
			_planes.add(Action.create(Plane.class, i, 0, 0, 0));
		}

		// Runways
		for (int i = 0; i < numOfRunways; i++) {
			_runways.add(Action.create(Runway.class, i));
		}

		// Tower
		_tower = Action.create(Tower.class, 0, 0, 0, 0);

		// Weather
		_weather = Action.create(Weather.class, 10, 4, 4);

		// Link
		Action.link(TowerWeatherUsage.theTower.class, _tower, TowerWeatherUsage.theWeather.class, _weather);
		for (int i = 0; i < numOfPlanes; i++) {
			Action.link(PlaneTowerUsage.thePlane.class, _planes.get(i), PlaneTowerUsage.theTower.class, _tower);
		}
		for (int i = 0; i < numOfRunways; i++) {
			Action.link(TowerRunwayUsage.theTower.class, _tower, TowerRunwayUsage.theRunway.class, _runways.get(i));
		}

		// Actions start
		for (int i = 0; i < numOfPlanes; i++) {
			Action.start(_planes.get(i));
		}
		for (int i = 0; i < numOfRunways; i++) {
			Action.start(_runways.get(i));
		}

		Action.start(_tower);
		Action.start(_weather);

		pluses = new ArrayList<>();
		minuses = new ArrayList<>();

		runways = new ArrayList<>();
		planes = new ArrayList<>();

		t = new ArrayList<>();
		l = new ArrayList<>();
		ut = new ArrayList<>();
		ht = new ArrayList<>();
		ul = new ArrayList<>();
		hl = new ArrayList<>();
		at = new ArrayList<>();
		al = new ArrayList<>();

		// Pluses
		pluses.add(new Rectangle2D.Double(200, 40, 20, 20));
		pluses.add(new Rectangle2D.Double(450, 40, 20, 20));
		pluses.add(new Rectangle2D.Double(700, 40, 20, 20));
		// Minuses
		minuses.add(new Rectangle2D.Double(50, 40, 20, 20));
		minuses.add(new Rectangle2D.Double(300, 40, 20, 20));
		minuses.add(new Rectangle2D.Double(550, 40, 20, 20));

		for (int i = 0; i < numOfRunways; i++) {
			runways.add(new Rectangle2D.Double(50, paddingTop + 50 + i * 85, 300, 50));
		}
		for (int i = 0; i < numOfPlanes; i++) {
			planes.add(new Ellipse2D.Double(450, paddingTop + 50 + i * 85, 50, 50));
			t.add(new Rectangle2D.Double(532, paddingTop + 61 + i * 85, 25, 25));
			l.add(new Rectangle2D.Double(532 + 40, paddingTop + 61 + i * 85, 25, 25));

			ut.add(new Rectangle2D.Double(532 + 2 * 40, paddingTop + 61 + i * 85, 25, 25));
			ht.add(new Rectangle2D.Double(532 + 3 * 40, paddingTop + 61 + i * 85, 25, 25));
			ul.add(new Rectangle2D.Double(532 + 4 * 40, paddingTop + 61 + i * 85, 25, 25));
			hl.add(new Rectangle2D.Double(532 + 5 * 40, paddingTop + 61 + i * 85, 25, 25));
			at.add(new Rectangle2D.Double(532 + 6 * 40, paddingTop + 61 + i * 85, 25, 25));
			al.add(new Rectangle2D.Double(532 + 7 * 40, paddingTop + 61 + i * 85, 25, 25));
		}

		if (withFile) {
			while (file.hasNext()) {
				if (file.hasNextInt()) {
					int id;
					String action;
					// l - land / t - take off
					// hl - have already landed / ht - have already taken off
					// al - annuling landing / at - annuling taking off
					// s - sleep

					id = file.nextInt();
					action = file.next();
					// TODO ut, ul
					if (action.equals("t")) {
						Action.send(new SignalPilotAskTakingOffPermission(), _planes.get(id));
					} else if (action.equals("l")) {
						Action.send(new SignalPilotAskLandingPermission(), _planes.get(id));
					} else if (action.equals("ht")) {
						Action.send(new SignalPlaneAlreadyTakenOff(), _planes.get(id));
					} else if (action.equals("hl")) {
						Action.send(new SignalPlaneAlreadyLanded(), _planes.get(id));
					} else if (action.equals("at")) {
						Action.send(new SignalAnnulingTheTakingOff(), _planes.get(id));
					} else if (action.equals("al")) {
						Action.send(new SignalAnnulingTheLanding(), _planes.get(id));
					}
				} else if (file.hasNext()) { // Not number -> it will be sleep
												// or weather changes
					char action; // sleep or weather
					int sleepTime;

					action = file.next().charAt(0);

					if (action == 's') {
						sleepTime = file.nextInt();
						try {
							Thread.sleep(sleepTime);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						System.out.println("[Sleep] " + sleepTime);
					}

					if (action == 'w') {
						int t = file.nextInt();
						int r = file.nextInt();
						int w = file.nextInt();

						_weather.setWeather(t, w, r);

						System.out.println("[Weather] " + t + ", " + w + ", " + r);
					}
				} else { // Invalid symbol
					file.next();
				}
				// Thread.sleep(50);
			}
		}

		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent me) {
				super.mouseClicked(me);
				for (int i = 0; i < numOfRunways; i++) {
					if (runways.get(i).contains(me.getPoint())) {
						System.out.println("Runway (" + i + ")");
					}
				}

				for (int i = 0; i < numOfPlanes; i++) {
					if (planes.get(i).contains(me.getPoint())) {
						System.out.println("Plane (" + i + ")");
					}
				}

				// TODO ut, ul
				// Buttons
				for (int i = 0; i < numOfPlanes; i++) {
					if (t.get(i).contains(me.getPoint())) {
						Action.send(new SignalPilotAskTakingOffPermission(), _planes.get(i));
					} else if (l.get(i).contains(me.getPoint())) {
						Action.send(new SignalPilotAskLandingPermission(), _planes.get(i));
					} else if (ut.get(i).contains(me.getPoint())) {
						Action.send(new SignalPlaneIsUnderTakingOff(), _planes.get(i));
					} else if (ht.get(i).contains(me.getPoint())) {
						Action.send(new SignalPlaneAlreadyTakenOff(), _planes.get(i));
					} else if (ul.get(i).contains(me.getPoint())) {
						Action.send(new SignalPlaneIsUnderLanding(), _planes.get(i));
					} else if (hl.get(i).contains(me.getPoint())) {
						Action.send(new SignalPlaneAlreadyLanded(), _planes.get(i));
					} else if (at.get(i).contains(me.getPoint())) {
						Action.send(new SignalAnnulingTheTakingOff(), _planes.get(i));
					} else if (al.get(i).contains(me.getPoint())) {
						Action.send(new SignalAnnulingTheLanding(), _planes.get(i));
					}
				}

				// Minuses

				if (minuses.get(0).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature() - 1, _weather.getWind(), _weather.getRain());
				}

				if (minuses.get(1).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature(), _weather.getWind(), _weather.getRain() - 1);
				}

				if (minuses.get(2).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature(), _weather.getWind() - 1, _weather.getRain());
				}

				// Pluses
				if (pluses.get(0).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature() + 1, _weather.getWind(), _weather.getRain());
				}

				if (pluses.get(1).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature(), _weather.getWind(), _weather.getRain() + 1);
				}

				if (pluses.get(2).contains(me.getPoint())) {
					_weather.setWeather(_weather.getTemperature(), _weather.getWind() + 1, _weather.getRain());
				}

			}
		});
	}

	@Override
	protected void paintComponent(Graphics grphcs) {

		super.paintComponent(grphcs);
		Graphics2D g2d = (Graphics2D) grphcs;
		g2d.setPaint(Color.BLACK);

		Font f = new Font("Dialog", Font.BOLD, 20);

		for (int i = 0; i < numOfRunways; i++) {

			if (_runways.get(i).getIsFree()) {
				g2d.setPaint(Color.GREEN);

			} else {
				g2d.setPaint(Color.RED);
				g2d.setFont(f);
				g2d.drawString(Integer.toString(_runways.get(i).getPlaneId()), 25, paddingTop + 80 + i * 85);
			}

			g2d.draw(runways.get(i));
			g2d.fill(runways.get(i));
		}

		for (Shape s : planes) {
			g2d.setPaint(Color.GREEN); // Or red
			g2d.draw(s);
			g2d.fill(s);
		}
		// Buttons
		for (Shape s : t) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : l) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : ut) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : ht) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : ul) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : hl) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : at) {
			g2d.draw(s);
			g2d.fill(s);
		}
		for (Shape s : al) {
			g2d.draw(s);
			g2d.fill(s);
		}

		g2d.setFont(f);
		// Minus

		g2d.setPaint(Color.BLACK);
		g2d.draw(minuses.get(0));
		g2d.fill(minuses.get(0));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("-", 56, 56);

		// Plus
		g2d.setPaint(Color.BLACK);
		g2d.draw(pluses.get(0));
		g2d.fill(pluses.get(0));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("+", 204, 57);

		// Minus

		g2d.setPaint(Color.BLACK);
		g2d.draw(minuses.get(1));
		g2d.fill(minuses.get(1));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("-", 306, 56);

		// Plus
		g2d.setPaint(Color.BLACK);
		g2d.draw(pluses.get(1));
		g2d.fill(pluses.get(1));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("+", 454, 57);

		// Minus

		g2d.setPaint(Color.BLACK);
		g2d.draw(minuses.get(2));
		g2d.fill(minuses.get(2));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("-", 556, 56);

		// Plus
		g2d.setPaint(Color.BLACK);
		g2d.draw(pluses.get(2));
		g2d.fill(pluses.get(2));

		g2d.setPaint(Color.WHITE);
		g2d.drawString("+", 704, 57);

		g2d.setPaint(Color.BLACK);
		// Weather
		g2d.drawString("Temperature", 75, 25);
		g2d.drawString(Integer.toString(_weather.getTemperature()), 120, 56);
		g2d.drawString("Rain", 365, 25);
		g2d.drawString(Integer.toString(_weather.getRain()), 383, 56);
		g2d.drawString("Wind", 613, 25);
		g2d.drawString(Integer.toString(_weather.getWind()), 635, 56);
		g2d.drawString("t", 540, paddingTop + 45);
		g2d.drawString("l", 580, paddingTop + 45);
		g2d.drawString("ut", 614, paddingTop + 45);
		g2d.drawString("ht", 654, paddingTop + 45);
		g2d.drawString("ul", 694, paddingTop + 45);
		g2d.drawString("hl", 734, paddingTop + 45);
		g2d.drawString("at", 774, paddingTop + 45);
		g2d.drawString("al", 814, paddingTop + 45);

		g2d.setPaint(Color.BLACK);
		for (int i = 0; i < numOfPlanes; i++) {
			g2d.drawString(Integer.toString(i), 425, paddingTop + 83 + i * 85);
			if (_planes.get(i).getStatus() != null) {
				g2d.drawString(_planes.get(i).getStatus(), 462, paddingTop + 83 + i * 85);
			}
		}

		try {
			Thread.sleep(25);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		repaint();
	}

	@Override
	public Dimension getPreferredSize() {
		return dim;
	}
}