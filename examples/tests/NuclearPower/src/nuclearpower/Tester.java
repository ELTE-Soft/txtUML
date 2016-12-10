package nuclearpower;

import java.io.IOException;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import nuclearpower.model.ButtonPressed;
import nuclearpower.model.ChangeToNotWarhousingPressed;
import nuclearpower.model.ChangeToWarhousingPressed;
import nuclearpower.model.Consumer;
import nuclearpower.model.NuclearPowerPlant;
import nuclearpower.model.SolarPanel;
import nuclearpower.model.WarhouseButtonPressed;
import nuclearpower.model.WatherChanged;
import nuclearpower.model.assocination.PlantConsumerAssociation;
import nuclearpower.model.assocination.PlantPanelAssociation;

public class Tester {	
	
	static NuclearPowerPlant nuclearPowerPlant;
	static SolarPanel solarPanel;
	static Consumer consumer;
	
	static void init () {
		nuclearPowerPlant = Action.create (NuclearPowerPlant.class);
		solarPanel = Action.create(SolarPanel.class);
		consumer = Action.create(Consumer.class);
		
		Action.link(PlantPanelAssociation.plant.class, nuclearPowerPlant, PlantPanelAssociation.panel.class, solarPanel);
		Action.link(PlantConsumerAssociation.plant.class, nuclearPowerPlant, PlantConsumerAssociation.consumer.class, consumer);
		
		Action.start(nuclearPowerPlant);
		Action.start(solarPanel);
		Action.start(consumer);
		

		
	}
	
	public static void main (String[] args) {
		ModelExecutor.create().setTraceLogging(true).launch(Tester::init);
		
		try {
			char c = (char) System.in.read();
			while (c != 'q') {
				if (c == 'c') {
					API.send(new ButtonPressed(), consumer);
				} else if (c == 'w') {
					API.send(new WatherChanged(), solarPanel);
				} else if (c == 'a') {
					API.send(new ChangeToWarhousingPressed(), solarPanel);
				}
				else if (c == 'b') {
					API.send(new ChangeToNotWarhousingPressed(), solarPanel);
				}
				c = (char) System.in.read();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}

