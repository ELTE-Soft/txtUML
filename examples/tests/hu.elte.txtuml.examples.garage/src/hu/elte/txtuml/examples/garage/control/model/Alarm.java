package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.stdlib.world.World;
import hu.elte.txtuml.api.stdlib.world.WorldObject;
import hu.elte.txtuml.examples.garage.control.model.associations.KeyboardProvidesCode;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.AlarmSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.HashPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.KeyPress;
import hu.elte.txtuml.examples.garage.control.model.signals.external.in.StarPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.AlarmOff;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.AlarmOn;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.CodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.NewCodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.OldCodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StartSiren;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StopSiren;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.KeyboardTimeout;
import hu.elte.txtuml.examples.garage.control.model.signals.internal.WaitForCode;

public class Alarm extends ModelClass {
	int code = 8;

	public class InitAlarm extends Initial {
	}

	@From(InitAlarm.class)
	@To(On.class)
	public class TInitAlarm extends Transition {
	}

	public class Off extends State {
		@Override
		public void entry() {
			WorldObject view = World.get(View.id());

			Action.send(new StopSiren(), view);
			Action.send(new AlarmOff(), view);
		}
	}

	public class On extends State {
		@Override
		public void entry() {
			Action.send(new AlarmOn(), World.get(View.id()));
		}
	}

	public class ExpectingCode extends State {
		@Override
		public void entry() {
			Action.send(new CodeExpected(), World.get(View.id()));
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).one();
			Action.send(new WaitForCode(), kb);
		}
	}

	public class InAlarm extends State {
		@Override
		public void entry() {
			Action.send(new StartSiren(), World.get(View.id()));
		}
	}

	public class ExpectingOldCode extends State {
		@Override
		public void entry() {
			Action.send(new OldCodeExpected(), World.get(View.id()));
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).one();
			Action.send(new WaitForCode(), kb);
		}
	}

	public class ExpectingNewCode extends State {
		@Override
		public void entry() {
			Action.send(new NewCodeExpected(), World.get(View.id()));
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).one();
			Action.send(new WaitForCode(), kb);
		}
	}

	@From(On.class)
	@To(ExpectingCode.class)
	@Trigger(AlarmSensorActivated.class)
	public class TActivate extends Transition {
	}

	@From(On.class)
	@To(InAlarm.class)
	@Trigger(KeyPress.class)
	public class TWrongKey1 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key != code;
		}
	}

	@From(On.class)
	@To(Off.class)
	@Trigger(KeyPress.class)
	public class TCorrectKey1 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key == code;
		}
	}

	@From(ExpectingCode.class)
	@To(InAlarm.class)
	@Trigger(KeyPress.class)
	public class TWrongKey2 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key != code;
		}
	}

	@From(ExpectingCode.class)
	@To(Off.class)
	@Trigger(KeyPress.class)
	public class TCorrectKey2 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key == code;
		}
	}

	@From(InAlarm.class)
	@To(Off.class)
	@Trigger(KeyPress.class)
	public class TCorrectKey3 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key == code;
		}
	}

	@From(ExpectingOldCode.class)
	@To(ExpectingNewCode.class)
	@Trigger(KeyPress.class)
	public class TCorrectKey4 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key == code;
		}
	}

	@From(ExpectingOldCode.class)
	@To(Off.class)
	@Trigger(KeyPress.class)
	public class TWrongKey4 extends Transition {
		@Override
		public boolean guard() {
			return getTrigger(KeyPress.class).key != code;
		}
	}

	@From(ExpectingNewCode.class)
	@To(Off.class)
	@Trigger(KeyPress.class)
	public class TNewKey extends Transition {
		@Override
		public void effect() {
			code = getTrigger(KeyPress.class).key;
		}
	}

	@From(Off.class)
	@To(On.class)
	@Trigger(StarPressed.class)
	public class TSwitchOn extends Transition {
	}

	@From(Off.class)
	@To(ExpectingOldCode.class)
	@Trigger(HashPressed.class)
	public class TChangeCode extends Transition {
	}

	@From(ExpectingCode.class)
	@To(InAlarm.class)
	@Trigger(KeyboardTimeout.class)
	public class TNoCodeGiven extends Transition {
	}

	@From(ExpectingOldCode.class)
	@To(Off.class)
	@Trigger(KeyboardTimeout.class)
	public class TNoOldCodeGiven extends Transition {
	}

	@From(ExpectingNewCode.class)
	@To(Off.class)
	@Trigger(KeyboardTimeout.class)
	public class TNoNewCodeGiven extends Transition {
	}
}