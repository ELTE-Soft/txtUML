package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.examples.microwave.model.Human;
import hu.elte.txtuml.examples.microwave.model.Microwave;

public class MicrowaveClassDiagram extends ClassDiagram {
	@Left(val = Microwave.class, from = Human.class)
	class MicrowaveLayout extends Layout {
	}
}