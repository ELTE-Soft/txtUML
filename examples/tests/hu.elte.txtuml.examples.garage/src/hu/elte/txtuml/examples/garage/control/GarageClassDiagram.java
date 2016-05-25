package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Door;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;
import hu.elte.txtuml.examples.garage.control.model.Motor;

public class GarageClassDiagram extends ClassDiagram {

	/*@Contains({RemoteControlButtonPressed.class, MotionSensorActivated.class,
		AlarmSensorActivated.class, DoorReachedTop.class, DoorReachedBottom.class,
		KeyPress.class, StarPressed.class, HashPressed.class})
	class ExternalSignalGroup extends NodeGroup{}
	
	@Contains({DoorTimerExpired.class, ReenableMotor.class, ChangeMotorMode.class,
		KeyboardTimerExpired.class, WaitForCode.class, KeyboardTimeout.class})
	class InternalSignalGroup extends NodeGroup{}*/
	
	@Diamond(top = Door.class, left = Alarm.class, 
			bottom= Keyboard.class, right = Motor.class)
	//@LeftMost(ExternalSignalGroup.class)
	//@TopMost(InternalSignalGroup.class)
    class GarageLayout extends Layout {}
}
