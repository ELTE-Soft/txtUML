package hu.elte.txtuml.export.uml2.tests.models.association;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.ModelClass;

class A extends ModelClass {
}

class B extends ModelClass {
}

class Maybe_One_A_Many_B extends Association {
	class AEnd1 extends MaybeOne<A> {
	}

	class BEnd1 extends Many<B> {
	}
}

class One_A_Some_B extends Association {
	class AEnd2 extends One<A> {
	}

	class BEnd2 extends Some<B> {
	}
}

class _3to4_A_0to100_B extends Association {
	@Min(3)
	@Max(4)
	class AEnd3 extends Multiple<A> {
	}

	@Min(0)
	@Max(100)
	class BEnd3 extends Multiple<B> {
	}
}

class Maybe_One_Hidden_A_Many_Hidden_B extends Association {
	class AEnd4 extends HiddenMaybeOne<A> {
	}

	class BEnd4 extends HiddenMany<B> {
	}
}

class One_Hidden_A_Some_Hidden_B extends Association {
	class AEnd5 extends HiddenOne<A> {
	}

	class BEnd5 extends HiddenSome<B> {
	}
}

class _3to4_Hidden_A_0to100_Hidden_B extends Association {
	@Min(3)
	@Max(4)
	class AEnd6 extends HiddenMultiple<A> {
	}

	@Min(0)
	@Max(100)
	class BEnd6 extends HiddenMultiple<B> {
	}
}

class One_Hidden_A_Many_B extends Association {
	class AEnd7 extends HiddenOne<A> {
	}

	class BEnd7 extends Many<B> {
	}
}

class Container_Many extends Composition {
	class Cont_End extends ContainerEnd<A> {
	}

	class Contained_End_1 extends Many<B> {
	}
}

class Hidden_Container_Many extends Composition {
	class Hidden_Cont_End extends HiddenContainer<A> {
	}

	class Contained_End_2 extends Many<B> {
	}
}




