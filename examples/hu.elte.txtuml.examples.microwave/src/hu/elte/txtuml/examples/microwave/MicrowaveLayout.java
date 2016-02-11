package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Microwave;

class MicrowaveDiagram extends Diagram {
	@Left(val = Microwave.class, from = Human.class)
	class MicrowaveLayout extends Layout {
	}
}