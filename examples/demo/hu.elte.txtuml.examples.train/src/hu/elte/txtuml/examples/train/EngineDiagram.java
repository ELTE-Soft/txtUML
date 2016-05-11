package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.Left;
import hu.elte.txtuml.api.layout.North;
import hu.elte.txtuml.api.layout.StateMachineDiagram;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Engine.Init;
import hu.elte.txtuml.examples.train.model.Engine.Stopped;
import hu.elte.txtuml.examples.train.model.Engine.Working;

class EngineDiagram extends StateMachineDiagram<Engine> {
	
	@North(val = Init.class, from = Stopped.class)
	@Left(val = Stopped.class, from= Working.class)
	class EngineLayout extends Layout{}
}
