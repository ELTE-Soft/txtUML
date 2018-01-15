package clock.j.model.associations;

import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.api.model.ZeroToOne;
import hu.elte.txtuml.api.stdlib.timers.Timer;

public class TimerOfPendulum extends Association {
	public class timer extends End<One<Timer>> {}
	public class pendulum extends HiddenEnd<ZeroToOne<Pendulum>> {}
}
