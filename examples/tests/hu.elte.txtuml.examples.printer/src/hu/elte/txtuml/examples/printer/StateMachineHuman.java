package hu.elte.txtuml.examples.printer;

import hu.elte.txtuml.api.layout.Column;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.printer.model.Human;
import hu.elte.txtuml.examples.printer.model.Human.DoPrint;
import hu.elte.txtuml.examples.printer.model.Human.Init;

class HumanSM extends StateMachineDiagram<Human> {
	@Column({Init.class, DoPrint.class})
	class L extends Layout{}
}
