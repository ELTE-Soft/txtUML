package nuclearpower.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Image;

import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import nuclearpower.GUIInterface;
import nuclearpower.model.ChangeToNotWarhousingPressed;
import nuclearpower.model.ChangeToWarhousingPressed;
import nuclearpower.model.ConsumerButtonPressed;
import nuclearpower.model.Weather;
import nuclearpower.model.WeatherButtonPressed;

public class GUI extends JFrame implements GUIInterface{
	private static final long serialVersionUID = 1L;

	private static final String ASSETS_FOLDER = "/nuclearpower/gui/assets/";
	
	private JLabel powerPlantLabel = new JLabel();
	private JLabel solarPanelLabel = new JLabel();
	private JLabel weatherLabel = new JLabel();
	private JLabel batteryStateLabel = new JLabel();
	private JLabel batteryCapacityLabel = new JLabel();
	private JLabel houseLabel = new JLabel();
	
	private JLabel powerPlantImagePanel = new JLabel();
	private JLabel solarPanelImagePanel = new JLabel();
	private JLabel houseImagePanel = new JLabel();
	private JLabel batteryImagePanel = new JLabel();
	
	private JButton weatherButton = new JButton("Change weather");
	private JButton houseButton = new JButton("Switch");
	private JButton warehousingButton = new JButton("Warehousing");
	private JButton notwarehousingButton = new JButton("Not warehousing");
	
	private ModelClass solarPanel;
	private ModelClass consumer;
	private ModelClass weather; 
	
	private static ImageIcon POWERPLANT_OFF;
	private static ImageIcon POWERPLANT_NORMAL;
	private static ImageIcon POWERPLANT_FULL;
	private static ImageIcon WEATHER_SUNNY;
	private static ImageIcon WEATHER_RAINY;
	private static ImageIcon BATTERY_EMPTY;
	private static ImageIcon BATTERY_1;
	private static ImageIcon BATTERY_2;
	private static ImageIcon BATTERY_3;
	private static ImageIcon BATTERY_4;
	private static ImageIcon BATTERY_FULL;
	private static ImageIcon CONSUMER_OFF;
	private static ImageIcon CONSUMER_ON;
	
	public GUI(){
		super("Nuclear Power Station model");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	    // set the jframe size and location
	    setPreferredSize(new Dimension(1200, 900));
	    setUpUIElementActions();
	    createImages();
	    createLayout();
	    
	    pack();
	    setLocationRelativeTo(null);
	}

	private void setUpUIElementActions() {
		weatherButton.addActionListener((action) -> {
			Action.send(new WeatherButtonPressed(), weather);
		});
		
		warehousingButton.addActionListener((action) -> {
			Action.send(new ChangeToWarhousingPressed(), solarPanel);
		});
		
		notwarehousingButton.addActionListener((action) -> {
			Action.send(new ChangeToNotWarhousingPressed(), solarPanel);
		});
		
		houseButton.addActionListener((action) -> {
			Action.send(new ConsumerButtonPressed(), consumer);
		});
	}

	private void createImages() {
		ImageIcon powerPlantOffImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "powerstation_off.png"));
		POWERPLANT_OFF = new ImageIcon(powerPlantOffImage.getImage().getScaledInstance(300, 275, Image.SCALE_SMOOTH));
		ImageIcon powerPlantNormalImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "powerstation_normal.png"));
		POWERPLANT_NORMAL = new ImageIcon(powerPlantNormalImage.getImage().getScaledInstance(300, 275, Image.SCALE_SMOOTH));
		ImageIcon powerPlantFullImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "powerstation_full.png"));
		POWERPLANT_FULL = new ImageIcon(powerPlantFullImage.getImage().getScaledInstance(300, 275, Image.SCALE_SMOOTH));
		
		ImageIcon weatherSunnyImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "solarpanel_sunny.png"));
		WEATHER_SUNNY = new ImageIcon(weatherSunnyImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
		ImageIcon weatherRainyImage = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "solarpanel_rainy.png"));
		WEATHER_RAINY = new ImageIcon(weatherRainyImage.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
		
		ImageIcon battery0Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_empty.png"));
		BATTERY_EMPTY = new ImageIcon(battery0Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		ImageIcon battery1Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_1.png"));
		BATTERY_1 = new ImageIcon(battery1Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		ImageIcon battery2Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_2.png"));
		BATTERY_2 = new ImageIcon(battery2Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		ImageIcon battery3Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_3.png"));
		BATTERY_3 = new ImageIcon(battery3Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		ImageIcon battery4Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_4.png"));
		BATTERY_4 = new ImageIcon(battery4Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		ImageIcon battery5Image = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "accu_5.png"));
		BATTERY_FULL = new ImageIcon(battery5Image.getImage().getScaledInstance(150, 300, Image.SCALE_SMOOTH));
		
		ImageIcon house_on = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "house_on.png"));
		CONSUMER_ON = new ImageIcon(house_on.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
		ImageIcon house_off = new ImageIcon(this.getClass().getResource(ASSETS_FOLDER + "house_off.png"));
		CONSUMER_OFF = new ImageIcon(house_off.getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH));
	}

	private void createLayout() {

		Font font = new Font("Serif", Font.BOLD, 24);
		houseLabel.setFont(font);
		batteryStateLabel.setFont(font);
		solarPanelLabel.setFont(font);
		powerPlantLabel.setFont(font);
		weatherLabel.setFont(font);
		
		GroupLayout layout = new GroupLayout(this.getContentPane());
		this.getContentPane().setLayout(layout);
		//layout.setAutoCreateGaps(true);
		layout.setAutoCreateContainerGaps(true);
		
		layout.setHorizontalGroup(
				   layout.createSequentialGroup()
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(houseLabel)
				    		  .addComponent(houseImagePanel)
				    		  .addComponent(houseButton))
				      .addGap(100)
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(weatherLabel)
				    		  .addComponent(weatherButton)
				    		  .addComponent(solarPanelLabel)
				    		  .addGroup(layout.createSequentialGroup()
				    		  			.addComponent(warehousingButton)
				    		  			.addComponent(notwarehousingButton))
				    		  .addComponent(solarPanelImagePanel)
				    		  .addComponent(batteryStateLabel)
				    		  .addComponent(batteryCapacityLabel)
				    		  .addComponent(batteryImagePanel)
				    		  
				    		  	)
				      .addGap(100)
				      .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)
				    		  .addComponent(powerPlantLabel)
				    		  .addComponent(powerPlantImagePanel))
				);
		layout.setVerticalGroup(
		   layout.createParallelGroup()
		   		.addGroup(layout.createSequentialGroup()
	   				.addComponent(houseLabel)
			        .addComponent(houseImagePanel)
			        .addComponent(houseButton))
		   		.addGap(100)
		      .addGroup(layout.createSequentialGroup()
		    		.addComponent(weatherLabel)
	    		  	.addComponent(weatherButton)
		    		.addComponent(solarPanelLabel)
	    		  	.addGroup(layout.createParallelGroup()
	    		  			.addComponent(warehousingButton)
	    		  			.addComponent(notwarehousingButton))
	    		  	.addComponent(solarPanelImagePanel)
	    		  	.addComponent(batteryStateLabel)
	    		  	.addComponent(batteryCapacityLabel)
	    		  	.addComponent(batteryImagePanel)
	    		  	)
		      .addGap(100)
		      .addGroup(layout.createSequentialGroup()  
		    		  .addComponent(powerPlantLabel)
		    		  .addComponent(powerPlantImagePanel))
		);
	}

	@Override
	public void consumerStateChanged(String newState) {
		this.houseLabel.setText("Consumer: "+newState);
		if("On".equals(newState)){
			this.houseImagePanel.setIcon(CONSUMER_ON);
		}else{
			this.houseImagePanel.setIcon(CONSUMER_OFF);
		}
	}

	@Override
	public void panelStateChanged(String newState) {
		this.solarPanelLabel.setText("Solar Panel: "+newState);
	}

	@Override
	public void powerStationStateChanged(String newState) {
		this.powerPlantLabel.setText("Power Station: "+newState);
		if("Normal power".equals(newState)){
			this.powerPlantImagePanel.setIcon(POWERPLANT_NORMAL);
		}else if("Full power".equals(newState)){
			this.powerPlantImagePanel.setIcon(POWERPLANT_FULL);
		}else{
			this.powerPlantImagePanel.setIcon(POWERPLANT_OFF);
		}
	}

	@Override
	public void batteryStateChanged(String newState) {
		this.batteryStateLabel.setText("Battery: "+newState);
	}

	@Override
	public void batteryCapacityChanged(int capacity) {
		this.batteryCapacityLabel.setText("Battery capacity: "+capacity);
		if(capacity < 20){
			this.batteryImagePanel.setIcon(BATTERY_EMPTY);
		}else if(capacity < 40){
			this.batteryImagePanel.setIcon(BATTERY_1);
		}else if(capacity < 60){
			this.batteryImagePanel.setIcon(BATTERY_2);
		}else if(capacity < 80){
			this.batteryImagePanel.setIcon(BATTERY_3);
		}else if(capacity < 100){
			this.batteryImagePanel.setIcon(BATTERY_4);
		}else if(capacity == 100){
			this.batteryImagePanel.setIcon(BATTERY_FULL);
		}
	}

	@Override
	public void setSolarPanel(ModelClass modelClass) {
		this.solarPanel = modelClass;
	}

	@Override
	public void setConsumer(ModelClass modelClass) {
		this.consumer = modelClass;
	}

	@Override
	public void setWeather(Weather modelClass) {
		this.weather = modelClass;
	}
	
	@Override
	public void weatherChanged(String newWeather) {
		this.weatherLabel.setText("Weather: "+newWeather);
		if("Sunny".equals(newWeather)){
			this.solarPanelImagePanel.setIcon(WEATHER_SUNNY);
		}else{
			this.solarPanelImagePanel.setIcon(WEATHER_RAINY);
		}
	}
}
