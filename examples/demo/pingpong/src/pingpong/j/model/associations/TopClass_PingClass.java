package pingpong.j.model.associations;

import hu.elte.txtuml.api.model.Composition;
import pingpong.j.model.PingClass;
import pingpong.j.model.TopClass;

public class TopClass_PingClass extends Composition {
	public class top extends HiddenContainer<TopClass> {}
	public class ping extends One<PingClass> {}
}
