package hu.elte.txtuml.examples.garage.interfaces;

public interface Controlled {

	void stopDoor();

	void startDoorDown();

	void startDoorUp();

	void startSiren();

	void stopSiren();

	void codeExpected();

	void oldCodeExpected();

	void newCodeExpected();

	void progress(int percent);

	void alarmOff();

	void alarmOn();

}
