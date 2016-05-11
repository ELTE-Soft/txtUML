package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.garage.control.model.Alarm;
import hu.elte.txtuml.examples.garage.control.model.Alarm.ExpectingCode;
import hu.elte.txtuml.examples.garage.control.model.Alarm.ExpectingNewCode;
import hu.elte.txtuml.examples.garage.control.model.Alarm.ExpectingOldCode;
import hu.elte.txtuml.examples.garage.control.model.Alarm.InAlarm;
import hu.elte.txtuml.examples.garage.control.model.Alarm.InitAlarm;
import hu.elte.txtuml.examples.garage.control.model.Alarm.Off;
import hu.elte.txtuml.examples.garage.control.model.Alarm.On;

public class AlarmSMDiagram extends StateMachineDiagram<Alarm> {
	@Column({InitAlarm.class, On.class, Off.class})
	@Row({InAlarm.class, On.class, ExpectingCode.class})
	@Column({ExpectingCode.class, ExpectingOldCode.class, ExpectingNewCode.class})
	class L extends Layout{}
}
