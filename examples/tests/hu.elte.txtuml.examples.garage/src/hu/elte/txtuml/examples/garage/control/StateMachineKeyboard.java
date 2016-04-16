package hu.elte.txtuml.examples.garage.control;

import hu.elte.txtuml.api.layout.Row;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.garage.control.model.Keyboard;
import hu.elte.txtuml.examples.garage.control.model.Keyboard.Idle;
import hu.elte.txtuml.examples.garage.control.model.Keyboard.InitKeyboard;
import hu.elte.txtuml.examples.garage.control.model.Keyboard.Waiting;

class KeyboardSM extends StateMachineDiagram<Keyboard> {
	@Row({InitKeyboard.class, Idle.class, Waiting.class})
	class L extends Layout{}
}
