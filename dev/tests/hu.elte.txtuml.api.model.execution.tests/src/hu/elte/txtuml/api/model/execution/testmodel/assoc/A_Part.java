package hu.elte.txtuml.api.model.execution.testmodel.assoc;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.execution.testmodel.A;
import hu.elte.txtuml.api.model.execution.testmodel.Part;

public class A_Part extends Composition {
	public class a extends ContainerEnd<A> {
	}

	public class p extends End<Any<Part>> {
	}
}
