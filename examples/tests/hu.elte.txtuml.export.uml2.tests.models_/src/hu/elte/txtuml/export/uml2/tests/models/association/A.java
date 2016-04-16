package hu.elte.txtuml.export.uml2.tests.models.association;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.ModelClass;

class A extends ModelClass {
}

class B extends ModelClass {
}

class Maybe_One_A_Many_B extends Association {
	class AEnd extends MaybeOne<A> {
	}

	class BEnd extends Many<B> {
	}
}

class One_A_Some_B extends Association {
	class AEnd extends One<A> {
	}

	class BEnd extends Some<B> {
	}
}

class _3to4_A_0to100_B extends Association {
	@Min(3)
	@Max(4)
	class AEnd extends Multiple<A> {
	}

	@Min(0)
	@Max(100)
	class BEnd extends Multiple<B> {
	}
}

class Maybe_One_Hidden_A_Many_Hidden_B extends Association {
	class AEnd extends HiddenMaybeOne<A> {
	}

	class BEnd extends HiddenMany<B> {
	}
}

class One_Hidden_A_Some_Hidden_B extends Association {
	class AEnd extends HiddenOne<A> {
	}

	class BEnd extends HiddenSome<B> {
	}
}

class _3to4_Hidden_A_0to100_Hidden_B extends Association {
	@Min(3)
	@Max(4)
	class AEnd extends HiddenMultiple<A> {
	}

	@Min(0)
	@Max(100)
	class BEnd extends HiddenMultiple<B> {
	}
}

class One_Hidden_A_Many_B extends Association {
	class AEnd extends HiddenOne<A> {
	}

	class BEnd extends Many<B> {
	}
}




