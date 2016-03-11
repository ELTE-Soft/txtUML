package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.InnerPongClass;
import pingpong.j.model.PongClass;

public class PongClass_InnerPongClass extends Composition {
	public class pong extends HiddenContainer<PongClass> {}
	public class inner extends One<InnerPongClass> {}
}
