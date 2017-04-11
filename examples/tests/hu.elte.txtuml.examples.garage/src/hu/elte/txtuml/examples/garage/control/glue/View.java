package hu.elte.txtuml.examples.garage.control.glue;

import hu.elte.txtuml.examples.garage.interfaces.Controlled;

public interface View extends Controlled {

	public static View getInstance() {
		return ViewImpl.getInstance();
	}

	@Override
	void stopDoor();

	@Override
	void startDoorDown();

	@Override
	void startDoorUp();

	@Override
	void startSiren();

	@Override
	void stopSiren();

	@Override
	void codeExpected();

	@Override
	void oldCodeExpected();

	@Override
	void newCodeExpected();

	@Override
	void progress(int percent);

	@Override
	void alarmOff();

	@Override
	void alarmOn();

}
