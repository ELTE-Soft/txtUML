package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.AssociationEnd.NonContainer;
import hu.elte.txtuml.api.model.AssociationEnd.NonNavigable;

// TODO document
public abstract class Association {

	// TODO document
	public abstract class End<C extends GeneralCollection<?>> extends AssociationEnd<C>
			implements NonContainer, Navigable {
	}

	// TODO document
	public abstract class HiddenEnd<C extends GeneralCollection<?>> extends AssociationEnd<C>
			implements NonContainer, NonNavigable {
	}

}
