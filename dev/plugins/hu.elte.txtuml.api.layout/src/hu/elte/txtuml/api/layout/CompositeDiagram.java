package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.model.ModelClass;

/**
 * Classes defining the layout of composite structure diagrams have to inherit from
 * this base class.
 * 
 * @param <T>
 *            The model class that the diagrams shows the internal structure of.
 */
public abstract class CompositeDiagram<T extends ModelClass> extends Diagram {

}
