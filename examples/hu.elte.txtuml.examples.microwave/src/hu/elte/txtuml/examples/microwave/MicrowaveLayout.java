package hu.elte.txtuml.examples.microwave;

import hu.elte.txtuml.examples.microwave.MicrowaveModel.*;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.statements.*;

class MicrowaveDiagram extends Diagram
{
	@Left(val = MicrowaveModel.Microwave.class, 
			from = Human.class)
    class MicrowaveLayout extends Layout {}
}