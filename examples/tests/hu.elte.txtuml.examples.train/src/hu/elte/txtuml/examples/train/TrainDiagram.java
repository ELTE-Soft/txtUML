package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Gearbox;
import hu.elte.txtuml.examples.train.model.Lamp;

class TrainDiagram extends ClassDiagram {
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
