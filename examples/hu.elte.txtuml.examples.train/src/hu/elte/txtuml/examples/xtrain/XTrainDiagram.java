package hu.elte.txtuml.examples.xtrain;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;

class XTrainDiagram extends Diagram {
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
