package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.PongClass;
import pingpong.j.model.TopClass;

public class TopClass_PongClass extends Composition {
	public class top extends HiddenContainer<TopClass> {}
	public class pong extends One<PongClass> {}
}
