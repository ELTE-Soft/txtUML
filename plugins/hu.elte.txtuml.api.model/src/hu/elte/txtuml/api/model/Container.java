package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.Multiplicity.One;
import hu.elte.txtuml.api.model.assocends.Navigability.Navigable;

public class Container<T extends ModelClass> extends OneBase<T> implements One,
		Navigable {
}
