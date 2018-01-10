package hu.elte.txtuml.api.model.seqdiag;

/**
 * This class represents an interaction UML element (equivalent to a full
 * sequence diagram). This is a container class for lifelines, messages and
 * combined fragments. With this structure one can describe what is the expected
 * behavior of a model in a given scenario and if the model diverges from the
 * scenario the runtime will return the point of divergence.
 * <p>
 * How to Use:
 * <ol>
 * <li>Create a new subclass of this or the {@link SequenceDiagram} class.</li>
 * <li>Define Lifelines as attributes. These need to subclasses of ModelClass.
 * </li>
 * <li>Define the {@link #initialize()} method (optional) to initialize the
 * lifelines and the {@link #run()} method to define the behavior of the
 * model.</li>
 * </ol>
 */
public abstract class Interaction implements Runnable {

	public void initialize() {
	}

	public abstract void run();

}
