package hu.elte.txtuml.api.model.seqdiag;

/**
 * This class represents an interaction UML element ( equivalent to a full
 * sequence diagram ). This is a container class for lifelines,messages and
 * combined fragments. With this structure one can describe what is the expected
 * behavior of a model in a given scenario and if the model diverges from the
 * scenario the runtime will return the point of divergence.
 * 
 * @author Turi Zoltan(G4R8AJ)
 *
 */
public abstract class Interaction implements Runnable {

	public Interaction() {
	}

	public void initialize() {
	};

	public abstract void run();
}
