package hu.elte.txtuml.examples.garage.interfaces;

public interface IControlled {
	public void stopDoor();
	public void startDoorDown();
	public void startDoorUp();
	public void startSiren();
	public void stopSiren();
	public void codeExpected();
	public void oldCodeExpected();
	public void newCodeExpected();
	public void progress(int percent);
	public void alarmOff();
	public void alarmOn();
}
