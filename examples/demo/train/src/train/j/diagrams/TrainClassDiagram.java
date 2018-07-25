package train.j.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Diamond;
import train.j.model.Engine;
import train.j.model.Gearbox;
import train.j.model.Lamp;

public class TrainClassDiagram extends ClassDiagram {
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
