package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.AssociationEnd.NonContainerEnd;
import hu.elte.txtuml.api.model.AssociationEnd.NonNavigable;

// TODO document
public abstract class Association {

	// TODO document
	public abstract class End<C extends GeneralCollection<?>> extends AssociationEnd<C>
			implements NonContainerEnd, Navigable {
	}

	// TODO document
	public abstract class HiddenEnd<C extends GeneralCollection<?>> extends AssociationEnd<C>
			implements NonContainerEnd, NonNavigable {
	}

}
