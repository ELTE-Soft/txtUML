package hu.elte.txtuml.examples.garage.interfaces;

import hu.elte.txtuml.api.stdlib.world.SwingWorldObjectListener;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.AlarmOff;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.AlarmOn;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.CodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.NewCodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.OldCodeExpected;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.Progress;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StartDoorDown;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StartDoorUp;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StartSiren;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StopDoor;
import hu.elte.txtuml.examples.garage.control.model.signals.external.out.StopSiren;

public class Glue extends SwingWorldObjectListener {

	private final Controlled controlled;

	public Glue(Controlled controlled) {
		this.controlled = controlled;
	}

	@SignalHandler
	public void accept(AlarmOff s) {
		controlled.alarmOff();
	}

	@SignalHandler
	public void accept(AlarmOn s) {
		controlled.alarmOn();
	}

	@SignalHandler
	public void accept(CodeExpected s) {
		controlled.codeExpected();
	}

	@SignalHandler
	public void accept(NewCodeExpected s) {
		controlled.newCodeExpected();
	}

	@SignalHandler
	public void accept(OldCodeExpected s) {
		controlled.oldCodeExpected();
	}

	@SignalHandler
	public void accept(Progress s) {
		controlled.progress(s.percent);
	}

	@SignalHandler
	public void accept(StartDoorDown s) {
		controlled.startDoorDown();
	}

	@SignalHandler
	public void accept(StartDoorUp s) {
		controlled.startDoorUp();
	}

	@SignalHandler
	public void accept(StartSiren s) {
		controlled.startSiren();
	}

	@SignalHandler
	public void accept(StopDoor s) {
		controlled.stopDoor();
	}

	@SignalHandler
	public void accept(StopSiren s) {
		controlled.stopSiren();
	}

}
