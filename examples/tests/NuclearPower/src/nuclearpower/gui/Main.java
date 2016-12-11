package nuclearpower.gui;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.execution.ModelExecutor;
import nuclearpower.model.Battery;
import nuclearpower.model.Consumer;
import nuclearpower.model.NuclearPowerPlant;
import nuclearpower.model.SolarPanel;
import nuclearpower.model.UI;
import nuclearpower.model.assocination.BatteryUIAssociation;
import nuclearpower.model.assocination.ConsumerUIAssociation;
import nuclearpower.model.assocination.PanelBatteryComposition;
import nuclearpower.model.assocination.PanelUIAssociation;
import nuclearpower.model.assocination.PlantConsumerAssociation;
import nuclearpower.model.assocination.PlantPanelAssociation;
import nuclearpower.model.assocination.PlantUIAssociation;

public class Main {
	
	static NuclearPowerPlant nuclearPowerPlant;
	static SolarPanel solarPanel;
	static Consumer consumer;
	static Battery battery;
	static UI ui;
	static GUI gui;
	public static void main(String[] args) {
		gui = new GUI();
		ModelExecutor.create().setTraceLogging(true).launch(Main::init);
		gui.setVisible(true);
		
	}
	
	private static void init(){
		setupModel();
		launchModel();
	}

	private static void launchModel() {
		Action.start(nuclearPowerPlant);
		Action.start(solarPanel);
		Action.start(battery);
		Action.start(consumer);
	}

	private static void setupModel() {
		nuclearPowerPlant = Action.create (NuclearPowerPlant.class);
		solarPanel = Action.create(SolarPanel.class);
		consumer = Action.create(Consumer.class);
		battery = Action.create(Battery.class, 100);
		Action.link (PanelBatteryComposition.panel.class, solarPanel, PanelBatteryComposition.battery.class, battery);
		
		ui = Action.create(UI.class);
		ui.gui = gui;
		Action.link(PlantPanelAssociation.plant.class, nuclearPowerPlant, PlantPanelAssociation.panel.class, solarPanel);
		Action.link(PlantConsumerAssociation.plant.class, nuclearPowerPlant, PlantConsumerAssociation.consumer.class, consumer);
		
		Action.link(PlantUIAssociation.plant.class, nuclearPowerPlant, PlantUIAssociation.ui.class, ui);
		Action.link(PanelUIAssociation.panel.class, solarPanel, PanelUIAssociation.ui.class, ui);
		Action.link(ConsumerUIAssociation.consumer.class, consumer, ConsumerUIAssociation.ui.class, ui);
		Action.link (BatteryUIAssociation.battery.class, battery, BatteryUIAssociation.ui.class, ui);
		
		gui.setSolarPanel(solarPanel);
		gui.setConsumer(consumer);
	}

}
