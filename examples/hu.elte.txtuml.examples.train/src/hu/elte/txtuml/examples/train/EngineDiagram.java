package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Engine.*;

class EngineDiagram extends StateMachineDiagram<Engine> {
	
	@North(val = Init.class, from = Stopped.class)
	@Left(val = Stopped.class, from= Working.class)
	class EngineLayout extends Layout{}
}
