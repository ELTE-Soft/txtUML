package hu.elte.txtuml.examples.validation.model;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.examples.validation.model.helpers.A;

class AssociationWithoutEnds extends Association {

}

class AssociationWithOneEnd extends Association {

	public class a extends End<Any<A>> {
	}

}

class AssociationWithThreeEnds extends Association {

	public class a extends End<Any<A>> {
	}
	
	public class b extends End<Any<A>> {
	}
	
	public class c extends End<Any<A>> {
	}

}