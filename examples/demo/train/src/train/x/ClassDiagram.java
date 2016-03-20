package train.x;

import hu.elte.txtuml.api.layout.ClassDiagram;
import hu.elte.txtuml.api.layout.Diamond;
import train.x.model.Engine;
import train.x.model.Gearbox;
import train.x.model.Lamp;

<<<<<<< HEAD:examples/demo/hu.elte.txtuml.examples.train/src/hu/elte/txtuml/examples/train/XTrainDiagram.java
class XTrainDiagram extends ClassDiagram {
=======
public class ClassDiagram extends Diagram {
>>>>>>> master:examples/demo/train/src/train/x/ClassDiagram.java
	class TopPhantom extends Phantom {
	}

	@Diamond(bottom = Lamp.class, left = Engine.class, right = Gearbox.class, top = TopPhantom.class)
	class TrainLayout extends Layout {
	}
}
