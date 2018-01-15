package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * This is a container class for lifelines, messages and combined fragments.
 * With this structure one can describe what is the expected behavior of a model
 * in a given scenario and if the model diverges from the scenario the runtime
 * will return the point of divergence.
 * <p>
 * How to Use:
 * <ol>
 * <li>Create a new subclass of this.</li>
 * <li>Define lifelines as fields of the interaction. These need to be
 * subclasses of ModelClass.</li>
 * <li>Initialize these lifeline objects with the {@link #initialize()} method
 * (optional). It is important that these
 * <li>Define the method (optional) to initialize the lifelines and the
 * {@link #run()} method to define the behavior of the model.</li>
 * </ol>
 */
@SequenceDiagramRelated
public abstract class SequenceDiagram implements Interaction {

	/**
	 * The initialization of the lifelines and the model. Contains model code.
	 */
	public void initialize() {
	}

}
