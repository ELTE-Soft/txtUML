package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.validation.helpers.A;

class CompositionNoContainer extends Composition {

	public class a extends End<Any<A>> {
	}
	
	public class b extends End<Any<A>> {
	}
}

class CompositionBothContainer extends Composition {

	public class a extends ContainerEnd<A> {
	}
	
	public class b extends ContainerEnd<A> {
	}
	
}

class CompositionOK extends Composition {

	public class a extends ContainerEnd<A> {
	}
	
	public class b extends End<Any<A>> {
	}
	
}