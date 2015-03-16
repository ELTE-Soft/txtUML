package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;

public class Association implements ModelElement, LayoutLink {

	protected Association() {
	}

	public class Many<T extends ModelClass> extends BaseMany<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToUnlimited {
	}

	public class Some<T extends ModelClass> extends BaseSome<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.OneToUnlimited {
	}

	public class MaybeOne<T extends ModelClass> extends BaseMaybeOne<T>
			implements hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToOne {
	}

	public class One<T extends ModelClass> extends BaseOne<T> implements
			hu.elte.txtuml.api.semantics.Navigability.Navigable,
			hu.elte.txtuml.api.semantics.Multiplicity.One {
	}

	public class HiddenMany<T extends ModelClass> extends BaseMany<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToUnlimited {
	}

	public class HiddenSome<T extends ModelClass> extends BaseSome<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.OneToUnlimited {
	}

	public class HiddenMaybeOne<T extends ModelClass> extends BaseMaybeOne<T>
			implements hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.ZeroToOne {
	}

	public class HiddenOne<T extends ModelClass> extends BaseOne<T> implements
			hu.elte.txtuml.api.semantics.Navigability.NonNavigable,
			hu.elte.txtuml.api.semantics.Multiplicity.One {
	}

}
