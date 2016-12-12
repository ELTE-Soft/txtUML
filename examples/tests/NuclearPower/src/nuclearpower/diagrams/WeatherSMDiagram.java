package nuclearpower.diagrams;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import nuclearpower.model.Weather;
import nuclearpower.model.Weather.Rainy;
import nuclearpower.model.Weather.Sunny;
import nuclearpower.model.Weather.init;

public class WeatherSMDiagram extends StateMachineDiagram<Weather>{

	@Row({init.class, Rainy.class, Sunny.class})
	class L extends Layout{}
}
