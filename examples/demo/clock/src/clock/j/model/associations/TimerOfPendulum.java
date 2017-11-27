package clock.j.model.associations;

import clock.j.model.classes.Pendulum;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.stdlib.timers.Timer;

public class TimerOfPendulum extends Association {
	public class timer extends One<Timer> {}
	public class pendulum extends HiddenMaybeOne<Pendulum> {}
}
