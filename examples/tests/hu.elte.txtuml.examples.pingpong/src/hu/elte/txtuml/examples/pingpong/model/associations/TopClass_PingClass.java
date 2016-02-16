package hu.elte.txtuml.examples.pingpong.model.associations;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.pingpong.model.PingClass;
import hu.elte.txtuml.examples.pingpong.model.TopClass;

public class TopClass_PingClass extends Composition {
	public class top extends HiddenContainer<TopClass> {}
	public class ping extends One<PingClass> {}
}
