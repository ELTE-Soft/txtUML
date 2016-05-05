package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.garage.control.model.Motor;
import hu.elte.txtuml.examples.garage.control.model.Motor.HeadingDown;
import hu.elte.txtuml.examples.garage.control.model.Motor.HeadingUp;
import hu.elte.txtuml.examples.garage.control.model.Motor.InitMotor;
import hu.elte.txtuml.examples.garage.control.model.Motor.MovingDown;
import hu.elte.txtuml.examples.garage.control.model.Motor.MovingUp;

class MotorSM extends StateMachineDiagram<Motor> {
	@Row({InitMotor.class, HeadingUp.class, MovingUp.class})
	@Row({MovingDown.class, HeadingDown.class})
	@Column({HeadingUp.class, MovingDown.class})
	@Column({MovingUp.class, HeadingDown.class})
	class L extends Layout{}
}
