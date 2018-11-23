package train.x.diagrams;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Diamond;
import train.x.model.Engine;
import train.x.model.Gearbox;
import train.x.model.Lamp;

public class XTrainClassDiagram extends ClassDiagram {
	class TopPhantom extends Box {
		class TopPhantomLayout extends Layout{};
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
