package hu.elte.txtuml.examples.garage.interfaces;

public interface IControl {
	public void remoteControlButtonPressed();
	public void motionSensorActivated();
	public void alarmSensorActivated();
	public void doorReachedTop();
	public void doorReachedBottom();
	public void keyPress(int nr);
	public void starPressed();
	public void hashPressed();
}
