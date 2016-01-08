package hu.elte.txtuml.examples.validation;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.validation.helpers.A;

class AssociationWithoutEnds extends Association {

}

class AssociationWithOneEnd extends Association {

	public class a extends Many<A> {
	}

}

class AssociationWithThreeEnds extends Association {

	public class a extends Many<A> {
	}
	
	public class b extends Many<A> {
	}
	
	public class c extends Many<A> {
	}

}