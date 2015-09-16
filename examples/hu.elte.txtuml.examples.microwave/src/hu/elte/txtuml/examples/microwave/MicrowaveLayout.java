package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.examples.microwave.MicrowaveModel.Human;

class MicrowaveDiagram extends Diagram
{
	@Left(val = MicrowaveModel.Microwave.class, 
			from = Human.class)
    class MicrowaveLayout extends Layout {}
}