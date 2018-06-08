package hu.elte.txtuml.export.uml2.tests.models.association;

import hu.elte.txtuml.api.model.Any;
import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.One;
import hu.elte.txtuml.api.model.OneToAny;
import hu.elte.txtuml.api.model.ZeroToOne;

class A extends ModelClass {
}

class B extends ModelClass {
}

class Maybe_One_A_Many_B extends Association {
	class AEnd1 extends End<ZeroToOne<A>> {
	}

	class BEnd1 extends End<Any<B>> {
	}
}

class One_A_Some_B extends Association {
	class AEnd2 extends End<One<A>> {
	}

	class BEnd2 extends End<OneToAny<B>> {
	}
}

class _3to4_A_0to100_B extends Association {
	class AEnd3 extends End<_3to4<A>> {
	}

	class BEnd3 extends End<_0to100<B>> {
	}
}

class Maybe_One_Hidden_A_Many_Hidden_B extends Association {
	class AEnd4 extends HiddenEnd<ZeroToOne<A>> {
	}

	class BEnd4 extends HiddenEnd<Any<B>> {
	}
}

class One_Hidden_A_Some_Hidden_B extends Association {
	class AEnd5 extends HiddenEnd<One<A>> {
	}

	class BEnd5 extends HiddenEnd<OneToAny<B>> {
	}
}

class _3to4_Hidden_A_0to100_Hidden_B extends Association {
	class AEnd6 extends HiddenEnd<_3to4<A>> {
	}

	class BEnd6 extends HiddenEnd<_0to100<B>> {
	}
}

class One_Hidden_A_Many_B extends Association {
	class AEnd7 extends HiddenEnd<One<A>> {
	}

	class BEnd7 extends End<Any<B>> {
	}
}

class Container_Many extends Composition {
	class Cont_End extends ContainerEnd<A> {
	}

	class Contained_End_1 extends End<Any<B>> {
	}
}

class Hidden_Container_Many extends Composition {
	class Hidden_Cont_End extends HiddenContainerEnd<A> {
	}

	class Contained_End_2 extends End<Any<B>> {
	}
}

@Min(3)
@Max(4)
class _3to4<T> extends Collection<T, _3to4<T>> {}

@Min(0)
@Max(100)
class _0to100<T> extends Collection<T, _3to4<T>> {}


