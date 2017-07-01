package hu.elte.txtuml.examples.garage.control.model;

import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.examples.garage.interfaces.Controlled;

public class View extends ModelClass implements @External Controlled {

	@External
	private volatile Controlled controlled;

	private View() {
	}

	@External
	private static class SingletonHelper {
		private static final View INSTANCE = new View();
	}

	@ExternalBody
	public static View getInstance() {
		return SingletonHelper.INSTANCE;
	}
	
	@External
	public void setControlled(Controlled controlled) {
		this.controlled = controlled;
	}

	@ExternalBody
	@Override
	public void stopDoor() {
		controlled.stopDoor();
	}

	@ExternalBody
	@Override
	public void startDoorDown() {
		controlled.startDoorDown();
	}

	@ExternalBody
	@Override
	public void startDoorUp() {
		controlled.startDoorUp();
	}

	@ExternalBody
	@Override
	public void startSiren() {
		controlled.startSiren();
	}

	@ExternalBody
	@Override
	public void stopSiren() {
		controlled.stopSiren();
	}

	@ExternalBody
	@Override
	public void codeExpected() {
		controlled.codeExpected();
	}

	@ExternalBody
	@Override
	public void oldCodeExpected() {
		controlled.oldCodeExpected();
	}

	@ExternalBody
	@Override
	public void newCodeExpected() {
		controlled.newCodeExpected();
	}

	@ExternalBody
	@Override
	public void progress(int percent) {
		controlled.progress(percent);
	}

	@ExternalBody
	@Override
	public void alarmOff() {
		controlled.alarmOff();
	}

	@ExternalBody
	@Override
	public void alarmOn() {
		controlled.alarmOn();
	}

}
