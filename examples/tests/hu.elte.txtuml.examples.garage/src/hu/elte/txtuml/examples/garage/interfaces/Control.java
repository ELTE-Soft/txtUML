package hu.elte.txtuml.examples.garage.interfaces;

public interface Control {

	void remoteControlButtonPressed();

	void motionSensorActivated();

	void alarmSensorActivated();

	void doorReachedTop();

	void doorReachedBottom();

	void keyPress(int nr);

	void starPressed();

	void hashPressed();

}
