package airport.gui;

import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import airport.xmodel.MakePlane;
import airport.xmodel.Plane;
import airport.xmodel.PlaneFactory;
import airport.xmodel.PlaneTowerUsage;
import airport.xmodel.Runway;
import airport.xmodel.Tower;
import airport.xmodel.TowerWeatherUsage;
import airport.xmodel.Weather;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;

@SuppressWarnings("serial")
public class Main extends JPanel {
	// Classes
	private static WindowWeather _weather;
	private static WindowPlanes _planes;
	private static WindowMap _map;

	private static Map<Integer, WindowOnePlane> _onePlane;

	// Others
	private static int fps = 24;
	private static int sps = Math.round(1000 / fps);
	// Windows
	private static JFrame mapWindow;
	private static JFrame weatherWindow;
	private static JFrame planesWindow;
	private static JPanel planesPanel;
	private static int windowPlanesWidth = 400;

	private static Map<Integer, JFrame> planeWindow;
	private static Map<Integer, JFrame> towerWindow;

	private static int currentPlaneId = 0;
	private static int currentTowerId = 0;

	// XModel
	private static ArrayList<Integer> towerIds;
	private static ArrayList<Integer> planeIds;

	private static Map<Integer, Plane> X_planes;
	private static Map<Integer, Runway> X_runways;
	private static Map<Integer, Tower> X_towers;
	private static Weather X_weather;
	private static PlaneFactory X_plane_factory;

	private static int numOfPlanes = 0;
	private static int numOfRunways = 0;
	private static int numOfTowers = 0;

	public static void main(String[] args) {
		ModelExecutor.create().run(() -> {
			
			// xmodel
			X_plane_factory = new PlaneFactory();
			Action.start(X_plane_factory);
			X_planes = new HashMap<Integer, Plane>();
			X_towers = new HashMap<Integer, Tower>();
			X_weather = Action.create(Weather.class, 10, 4, 4);
			Action.start(X_weather);

			planeIds = new ArrayList<Integer>();
			towerIds = new ArrayList<Integer>();

			// Towers
			// 0
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 160, 85, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));

			currentTowerId++;

			// 1
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 280, 240, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));
			currentTowerId++;

			// 2
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 315, 25, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));
			currentTowerId++;

			// 3
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 600, 70, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));
			currentTowerId++;

			// 4
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 460, 180, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));
			currentTowerId++;

			// 5
			towerIds.add(currentTowerId);
			X_towers.put(currentTowerId, Action.create(Tower.class, currentTowerId, 730, 250, 200));
			Action.link(TowerWeatherUsage.theTower.class, X_towers.get(currentTowerId),
					TowerWeatherUsage.theWeather.class, X_weather);
			Action.start(X_towers.get(currentTowerId));
			currentTowerId++;

			// Classes
			_map = new WindowMap();
			_planes = new WindowPlanes();
			_weather = new WindowWeather();
			_onePlane = new HashMap<Integer, WindowOnePlane>();

			// Windows
			// TODO: Them
			planeWindow = new HashMap<Integer, JFrame>();
			towerWindow = new HashMap<Integer, JFrame>();

			// ------------------------------------------ OK

			// Weather window
			weatherWindow = new JFrame();
			weatherWindow.setTitle("Weather");
			weatherWindow.setSize(380, 230);
			weatherWindow.setVisible(false);
			weatherWindow.setResizable(false);

			weatherWindow.getContentPane().add(_weather);

			// All planes window
			planesWindow = new JFrame();
			planesWindow.setTitle("Planes");
			planesWindow.setSize(600, 475);
			planesWindow.setVisible(false);
			planesWindow.setResizable(false);

			planesPanel = _planes;
			windowResize(planesPanel, 300);
			JScrollPane jScrollPane = new JScrollPane(planesPanel);

			jScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
			jScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			planesWindow.getContentPane().add(jScrollPane);

			// Main window
			mapWindow = new JFrame();
			mapWindow.setTitle("Map");
			mapWindow.setSize(855, 475);
			mapWindow.setResizable(false);
			mapWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			mapWindow.setVisible(true);
			mapWindow.getContentPane().add(_map);

			JMenuBar menuBar = new JMenuBar();

			JMenu file = new JMenu("File");
			JMenu view = new JMenu("View");

			JMenuItem exit = new JMenuItem("Exit");
			exit.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					System.exit(0);
				}
			});

			file.add(exit);

			// TODO: add action listener
			JMenuItem weather = new JMenuItem("Weather");
			weather.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					weatherWindow.setVisible(true);
					weatherWindow.setState(Frame.NORMAL);
				}
			});
			JMenuItem planes = new JMenuItem("Planes");
			planes.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					planesWindow.setVisible(true);
					planesWindow.setState(Frame.NORMAL);
				}
			});
			view.add(weather);
			view.add(planes);

			menuBar.add(file);
			menuBar.add(view);
			mapWindow.setJMenuBar(menuBar);

			// Refresh
			refresh(mapWindow);
		});
	}

	private static void refresh(JFrame frame) {
		frame.revalidate();
		frame.repaint();
	}

	private static void windowResize(JPanel panel, int height) {
		planesPanel.setPreferredSize(new Dimension(windowPlanesWidth, height));
	}

	public static void _planesRefresh() {
		_planes.refresh();
		_map.repaint();
	}

	public static void weatherRefresh() {
		_weather.refresh();
	}

	public static Weather getX_Weather() {
		return X_weather;
	}

	public static void addNewPlane(int newX, int newY, int angle) {

		Action.send(new MakePlane(currentPlaneId, newX, newY, angle), X_plane_factory);
		Plane thePlane = X_plane_factory.lastPlaneCreated();

		// wait for the creation of the plane
		while (thePlane == null || thePlane.getId() != currentPlaneId) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			thePlane = X_plane_factory.lastPlaneCreated();
		}
		
		planeIds.add(currentPlaneId);
		X_planes.put(currentPlaneId, thePlane);

		// Window
		System.out.println("1");
		WindowOnePlane onePlane = new WindowOnePlane(currentPlaneId);
		System.out.println("2");
		_onePlane.put(currentPlaneId, onePlane);

		planeWindow.put(currentPlaneId, new JFrame());

		planeWindow.get(currentPlaneId).setTitle("Plane - " + currentPlaneId);
		planeWindow.get(currentPlaneId).setSize(450, 230);
		planeWindow.get(currentPlaneId).setVisible(false);
		planeWindow.get(currentPlaneId).setResizable(false);
		planeWindow.get(currentPlaneId).getContentPane().add(onePlane);

		currentPlaneId++;

		windowResize(planesPanel, 232 + planeIds.size() * 30);
		_planesRefresh();

	}

	public static int getPlaneIdsSize() {
		return planeIds.size();
	}

	public static int getPlaneId(int id) {
		return planeIds.get(id);
	}

	public static Plane getPlane(int key) {
		Plane plane = X_planes.get(key);
		return plane;
	}

	public static int getTowerCount() {
		return towerIds.size();
	}

	public static int getTowerId(int id) {
		return towerIds.get(id);
	}

	public static Tower getTower(int key) {
		return X_towers.get(key);
	}

	public static void debug(int i) {

	}

	public static void openPlane(int planeId) {
		planeWindow.get(planeId).setVisible(true);

		planeWindow.get(planeId).setState(Frame.NORMAL);
	}

	public static void minus15(int planeId) {
		getPlane(planeId).plusAngle(-15);
	}

	public static void plus15(int planeId) {
		getPlane(planeId).plusAngle(15);
	}

	public static void go(int planeId) {
		getPlane(planeId).go(5);
	}

	public static int isInAroundTower(int id, Point point) {
		int towerId = _map.isInCircle(point);

		if (towerId == -1) {
			return -1;
		} else {
			return towerIds.get(towerId);
		}
	}

	public static void connectTowerAndPlane(int towerId, int planeId) {
		if (X_planes.get(planeId).getTowerId() != -1)
			Action.unlink(PlaneTowerUsage.thePlane.class, X_planes.get(planeId), PlaneTowerUsage.theTower.class,
					X_towers.get(X_planes.get(planeId).getTowerId()));

		if (towerId != -1)
			Action.link(PlaneTowerUsage.thePlane.class, X_planes.get(planeId), PlaneTowerUsage.theTower.class,
					X_towers.get(towerId));
	}

}
