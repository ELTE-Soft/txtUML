package nuclearpower;

import hu.elte.txtuml.api.layout.Above;
import hu.elte.txtuml.api.layout.Below;
import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Row;
import nuclearpower.model.SolarPanel;
import nuclearpower.model.Weather;
import nuclearpower.model.Battery;
import nuclearpower.model.Consumer;
import nuclearpower.model.NuclearPowerPlant;

public class NuclearPowerClassDiagram extends ClassDiagram{
	@Below(from=SolarPanel.class, val=Battery.class)
	@Above(from=SolarPanel.class, val=Weather.class)
	@Row({Consumer.class, SolarPanel.class, NuclearPowerPlant.class})
	class L extends Layout{}
}
