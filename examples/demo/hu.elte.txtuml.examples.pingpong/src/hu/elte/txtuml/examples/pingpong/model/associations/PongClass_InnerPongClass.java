package hu.elte.txtuml.examples.pingpong.model.associations;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.pingpong.model.InnerPongClass;
import hu.elte.txtuml.examples.pingpong.model.PongClass;

public class PongClass_InnerPongClass extends Composition {
	public class pong extends HiddenContainer<PongClass> {}
	public class inner extends One<InnerPongClass> {}
}
