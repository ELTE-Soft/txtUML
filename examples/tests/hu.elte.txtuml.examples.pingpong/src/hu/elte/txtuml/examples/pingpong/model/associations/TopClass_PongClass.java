package hu.elte.txtuml.examples.pingpong.model.associations;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.pingpong.model.PongClass;
import hu.elte.txtuml.examples.pingpong.model.TopClass;

public class TopClass_PongClass extends Composition {
	public class top extends HiddenContainer<TopClass> {}
	public class pong extends One<PongClass> {}
}
