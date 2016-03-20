package train.j.model;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import train.j.model.signals.LightOff;
import train.j.model.signals.SwitchLight;

public class Lamp extends ModelClass {
	class Init extends Initial {
	}

	class Dark extends State {
	}

	class Light extends State {
	}

	@From(Init.class)
	@To(Dark.class)
	class Init_Dark extends Transition {
	}

	@From(Dark.class)
	@To(Light.class)
	@Trigger(SwitchLight.class)
	class Dark_Light extends Transition {
	}

	@From(Light.class)
	@To(Dark.class)
	@Trigger(SwitchLight.class)
	class Light_Dark extends Transition {
	}

	@From(Light.class)
	@To(Dark.class)
	@Trigger(LightOff.class)
	class Light_Dark2 extends Transition {
	}
}
