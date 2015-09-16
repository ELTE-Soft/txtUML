package hu.elte.txtuml.examples.train;

import hu.elte.txtuml.examples.train.TrainModel.*;
import hu.elte.txtuml.layout.lang.Diagram;
import hu.elte.txtuml.layout.lang.statements.*;

class TrainDiagram extends Diagram
{
	class Phantom01 extends Phantom{}
	
	@Diamond(bottom = Lamp.class, left = Engine.class,
			right = Gearbox.class, top = Phantom01.class)
    class TrainLayout extends Layout {}
}