package nuclearpower;

import java.io.IOException;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import nuclearpower.model.Battery;
import nuclearpower.model.ChargePanelBattery;
import nuclearpower.model.Consumer;
import nuclearpower.model.ConsumerButtonPressed;
import nuclearpower.model.NuclearPowerPlant;
import nuclearpower.model.ProduceEnergyWithPanel;
import nuclearpower.model.SolarPanel;
import nuclearpower.model.StopUsingPanel;
import nuclearpower.model.UI;
import nuclearpower.model.UsePanelBattery;
import nuclearpower.model.Weather;
import nuclearpower.model.WeatherButtonPressed;
import nuclearpower.model.assocination.BatteryUIAssociation;
import nuclearpower.model.assocination.ConsumerUIAssociation;
import nuclearpower.model.assocination.PanelBatteryComposition;
import nuclearpower.model.assocination.PanelUIAssociation;
import nuclearpower.model.assocination.PanelWeatherAssociation;
import nuclearpower.model.assocination.PlantConsumerAssociation;
import nuclearpower.model.assocination.PlantPanelAssociation;
import nuclearpower.model.assocination.PlantUIAssociation;
import nuclearpower.model.assocination.WeatherUIAssociation;

public class Tester {

	static NuclearPowerPlant nuclearPowerPlant;
	static SolarPanel solarPanel;
	static Consumer consumer;
	static Weather weather;
	static UI ui;

	static void init() {
		nuclearPowerPlant = Action.create(NuclearPowerPlant.class);
		solarPanel = Action.create(SolarPanel.class);
		consumer = Action.create(Consumer.class);
		weather = Action.create(Weather.class);

		Battery battery = new Battery(100);
		Action.link(PanelBatteryComposition.panel.class, solarPanel, PanelBatteryComposition.battery.class, battery);

		ui = Action.create(UI.class);
		ui.gui = new GUIInterface() {

			@Override
			public void batteryStateChanged(String newState) {
				System.out.println("Battery state changed to " + newState);
			}

			@Override
			public void batteryCapacityChanged(int capacity) {
				System.out.println("Battery capacity: " + capacity);
			}

			@Override
			public void consumerStateChanged(String newState) {
				System.out.println("Consumer: " + newState);
			}

			@Override
			public void panelStateChanged(String newState) {
				System.out.println("Solar panel: " + newState);
			}

			@Override
			public void powerStationStateChanged(String newState) {
				System.out.println("Power Station: " + newState);
			}

			@Override
			public void setSolarPanel(ModelClass modelClass) {
			}

			@Override
			public void setConsumer(ModelClass modelClass) {
			}

			@Override
			public void weatherChanged(String newWeather) {
				System.out.println("Weather is " + newWeather);
			}

			@Override
			public void setWeather(Weather modelClass) {
			}
		};

		Action.link(PlantPanelAssociation.plant.class, nuclearPowerPlant, PlantPanelAssociation.panel.class,
				solarPanel);
		Action.link(PlantConsumerAssociation.plant.class, nuclearPowerPlant, PlantConsumerAssociation.consumer.class,
				consumer);
		Action.link(PanelBatteryComposition.battery.class, battery, PanelBatteryComposition.panel.class, solarPanel);
		Action.link(PanelWeatherAssociation.weather.class, weather, PanelWeatherAssociation.panel.class, solarPanel);
		
		Action.link(PlantUIAssociation.plant.class, nuclearPowerPlant, PlantUIAssociation.ui.class, ui);
		Action.link(PanelUIAssociation.panel.class, solarPanel, PanelUIAssociation.ui.class, ui);
		Action.link(ConsumerUIAssociation.consumer.class, consumer, ConsumerUIAssociation.ui.class, ui);
		Action.link(BatteryUIAssociation.battery.class, battery, BatteryUIAssociation.ui.class, ui);
		Action.link(WeatherUIAssociation.weather.class, weather, WeatherUIAssociation.ui.class, ui);
		Action.start(nuclearPowerPlant);
		Action.start(solarPanel);
		Action.start(battery);
		Action.start(consumer);
		Action.start(weather);
	}

	public static void main(String[] args) {
		ModelExecutor.create().setTraceLogging(true).launch(Tester::init);

		try {
			char c = (char) System.in.read();
			while (c != 'q') {
				if (c == 'c') {
					API.send(new ConsumerButtonPressed(), consumer);
				} else if (c == 'w') {
					API.send(new WeatherButtonPressed(), weather);
				} else if (c == 'e') {
					API.send(new ProduceEnergyWithPanel(), solarPanel);
				} else if (c == 'b') {
					API.send(new ChargePanelBattery(), solarPanel);
				} else if (c == 'u') {
					API.send(new UsePanelBattery(), solarPanel);
				} else if (c == 's') {
					API.send(new StopUsingPanel(), solarPanel);
				}
				c = (char) System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	void setWeather(Weather weather) {
	}
}
