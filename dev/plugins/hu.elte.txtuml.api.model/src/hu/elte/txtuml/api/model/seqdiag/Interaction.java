package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

/**
 * This class represents an interaction UML element ( equivalent to a full
 * sequence diagram ). This is a container class for lifelines,messages and
 * combined fragments. With this structure one can describe what is the expected
 * behavior of a model in a given scenario and if the model diverges from the
 * scenario the runtime will return the point of divergence.
 * <p>
 * How to Use: <br/>
 * <ol>
 * <li>Create a new subclass of this or the {@link SequenceDiagram} class</li>
 * <li>Define Lifelines as attributes. These need to be a subclass of ModelClass
 * </li>
 * <li>Define the initialize(optional) to initialize the lifelines and the run
 * to define the behaviour of the model</li>
 * </ol>
 * </p>
 *
 */
public abstract class Interaction implements Runnable {

	public Interaction() {
	}

	public void initialize() {
	};

	public abstract void run();

	private BaseSequenceDiagramExecutor getExecutor() {
		return RuntimeContext.getCurrentExecutorThread().getRuntime().getExecutor();
	}

	public Signal lastSignal() {
		return getExecutor().lastSignal();
	}

	public ModelClass lastSender() {
		return getExecutor().lastSender();
	}

	public ModelClass lastReceiver() {
		return getExecutor().lastReceiver();
	}
}
