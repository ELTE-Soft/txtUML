package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.examples.validation.helpers.A;

class CompositionNoContainer extends Composition {

	public class a extends Container<A> {
	}
	
	public class b extends Container<A> {
	}
}

class CompositionBothContainer extends Composition {

	public class a extends Many<A> {
	}
	
	public class b extends Many<A> {
	}
	
}

class CompositionOK extends Composition {

	public class a extends Container<A> {
	}
	
	public class b extends Many<A> {
	}
	
}