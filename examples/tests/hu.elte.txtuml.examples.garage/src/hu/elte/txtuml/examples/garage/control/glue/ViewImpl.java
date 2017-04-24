package hu.elte.txtuml.examples.garage.control.glue;

import hu.elte.txtuml.examples.garage.interfaces.Controlled;

public class ViewImpl implements View {

	private volatile Controlled controlled;

	private ViewImpl() {
	}

	private static class SingletonHelper {
		private static final ViewImpl INSTANCE = new ViewImpl();
	}

	public synchronized static ViewImpl getInstance() {
		return SingletonHelper.INSTANCE;
	}

	public void setControlled(Controlled controlled) {
		this.controlled = controlled;
	}

	@Override
	public void stopDoor() {
		controlled.stopDoor();
	}

	@Override
	public void startDoorDown() {
		controlled.startDoorDown();
	}

	@Override
	public void startDoorUp() {
		controlled.startDoorUp();
	}

	@Override
	public void startSiren() {
		controlled.startSiren();
	}

	@Override
	public void stopSiren() {
		controlled.stopSiren();
	}

	@Override
	public void codeExpected() {
		controlled.codeExpected();
	}

	@Override
	public void oldCodeExpected() {
		controlled.oldCodeExpected();
	}

	@Override
	public void newCodeExpected() {
		controlled.newCodeExpected();
	}

	@Override
	public void progress(int percent) {
		controlled.progress(percent);
	}

	@Override
	public void alarmOff() {
		controlled.alarmOff();
	}

	@Override
	public void alarmOn() {
		controlled.alarmOn();
	}

}
