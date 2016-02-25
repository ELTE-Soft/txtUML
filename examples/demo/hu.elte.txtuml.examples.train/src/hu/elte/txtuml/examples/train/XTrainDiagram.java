package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.examples.train.xmodel.Engine;
import hu.elte.txtuml.examples.train.xmodel.Gearbox;
import hu.elte.txtuml.examples.train.xmodel.Lamp;

class XTrainDiagram extends ClassDiagram {
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
