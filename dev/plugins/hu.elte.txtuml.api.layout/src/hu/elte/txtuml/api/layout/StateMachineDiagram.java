package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.model.ModelClass;

/**
 * Classes defining the layout of state machine diagrams have to inherit from
 * this base class.
 * 
 * @param <T>
 *            The model class owning the state machine.
 */
public abstract class StateMachineDiagram<T extends ModelClass> extends Diagram {

}
