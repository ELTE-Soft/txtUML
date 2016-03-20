package train.j;

<<<<<<< HEAD:examples/demo/hu.elte.txtuml.examples.train/src/hu/elte/txtuml/examples/train/TrainDiagram.java
import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.train.model.Engine;
import hu.elte.txtuml.examples.train.model.Gearbox;
import hu.elte.txtuml.examples.train.model.Lamp;

class TrainDiagram extends ClassDiagram {
=======
import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;
import train.j.model.Engine;
import train.j.model.Gearbox;
import train.j.model.Lamp;

public class ClassDiagram extends Diagram {
>>>>>>> master:examples/demo/train/src/train/j/ClassDiagram.java
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
