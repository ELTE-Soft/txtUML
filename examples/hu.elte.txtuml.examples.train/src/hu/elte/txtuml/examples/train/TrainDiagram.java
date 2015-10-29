package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.api.layout.Diagram;
import hu.elte.txtuml.api.layout.Diamond;
import hu.elte.txtuml.examples.train.TrainModel.Engine;
import hu.elte.txtuml.examples.train.TrainModel.Gearbox;
import hu.elte.txtuml.examples.train.TrainModel.Lamp;

class TrainDiagram extends Diagram
{
	class TopPhantom extends Phantom{}
	
	@Diamond(bottom = Lamp.class, left = Engine.class,
			right = Gearbox.class, top = TopPhantom.class)
    class TrainLayout extends Layout {}
}