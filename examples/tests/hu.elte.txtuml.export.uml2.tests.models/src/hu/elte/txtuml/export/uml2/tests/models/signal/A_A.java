package hu.elte.txtuml.export.uml2.tests.models.signal;

import hu.elte.txtuml.api.model.Association;

public class A_A extends Association {

	public class thisEnd extends One<A> {
	}

	public class thatEnd extends One<A> {
	}

}