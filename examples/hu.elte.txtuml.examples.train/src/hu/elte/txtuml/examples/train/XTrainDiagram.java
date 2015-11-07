package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.*;
import hu.elte.txtuml.examples.train.XTrain.*;

class XTrainDiagram extends Diagram
{
	class TopPhantom extends Phantom{}
	
	@Diamond(bottom = Lamp.class, left = Engine.class,
			right = Gearbox.class, top = TopPhantom.class)
    class TrainLayout extends Layout {}
}