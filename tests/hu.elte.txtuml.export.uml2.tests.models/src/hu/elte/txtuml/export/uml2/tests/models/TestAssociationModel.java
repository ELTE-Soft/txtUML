package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.Max;
import hu.elte.txtuml.api.model.Min;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;

public class TestAssociationModel extends Model {
		class A extends ModelClass {}
		class B extends ModelClass {}
		class AB1 extends Association {
			class AEnd extends MaybeOne<A> {}
			class BEnd extends Many<B> {}
		}

		class AB2 extends Association {
			class AEnd extends One<A> {}
			class BEnd extends Some<B> {}
		}

		class AB3 extends Association {
			@Min(3) @Max(4)
			class AEnd extends Multiple<A> {}
			@Min(0) @Max(100)
			class BEnd extends Multiple<B> {}
		}

		class AB4 extends Association {
			class AEnd extends HiddenMaybeOne<A> {}
			class BEnd extends HiddenMany<B> {}
		}

		class AB5 extends Association {
			class AEnd extends HiddenOne<A> {}
			class BEnd extends HiddenSome<B> {}
		}

		class AB6 extends Association {
			@Min(3) @Max(4)
			class AEnd extends HiddenMultiple<A> {}
			@Min(0) @Max(100)
			class BEnd extends HiddenMultiple<B> {}
		}

}
