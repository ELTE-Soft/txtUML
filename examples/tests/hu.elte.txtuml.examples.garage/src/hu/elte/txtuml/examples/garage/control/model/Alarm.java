package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.examples.garage.control.glue.Glue;
import hu.elte.txtuml.examples.garage.control.model.associations.KeyboardProvidesCode;
import hu.elte.txtuml.examples.garage.control.model.signals.external.AlarmSensorActivated;
import hu.elte.txtuml.examples.garage.control.model.signals.external.HashPressed;
import hu.elte.txtuml.examples.garage.control.model.signals.external.KeyPress;
import hu.elte.txtuml.examples.garage.control.model.signals.external.StarPressed;
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
			Glue.getInstance().stopSiren();
			Glue.getInstance().alarmOff();
		}
	}

	public class On extends State {
		@Override
		public void entry() {
			Glue.getInstance().alarmOn();
		}
	}

	public class ExpectingCode extends State {
		@Override
		public void entry() {
			Glue.getInstance().codeExpected();
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
			Action.send(new WaitForCode(), kb);
		}
	}

	public class InAlarm extends State {
		@Override
		public void entry() {
			Glue.getInstance().startSiren();
		}
	}

	public class ExpectingOldCode extends State {
		@Override
		public void entry() {
			Glue.getInstance().oldCodeExpected();
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
			Action.send(new WaitForCode(), kb);
		}
	}

	public class ExpectingNewCode extends State {
		@Override
		public void entry() {
			Glue.getInstance().newCodeExpected();
			Keyboard kb = Alarm.this.assoc(KeyboardProvidesCode.Provider.class).selectAny();
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