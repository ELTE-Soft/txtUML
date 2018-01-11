package hu.elte.txtuml.api.model.execution;

import java.util.List;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.SeqDiagExecutorImpl;
import hu.elte.txtuml.api.model.execution.seqdiag.error.SeqDiagValidationError;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

/**
 * Class responsible for executing the SequenceDiagram code.
 * <p>
 * This class executes the model, and watches for the described behavior to
 * happen. If it does happen, then the SequenceDiagram returns with no errors,
 * if it does not, it marks the errors by showing where did the model execution
 * diverge from the expected execution path.
 * <p>
 * On how to write Sequence Diagrams see the documentation of the
 * {@link SequenceDiagram} or {@link Interaction} classes.
 * <p>
 * <h2>How to Use:</h2>
 * <ol>
 * <li>Create new instance with the {@link #create} method.
 * <li>Set the Interaction to run by using the
 * {@link #setInteraction(Interaction) } method. Its Single parameter is an
 * interaction subclass instance.</li>
 * <li>Run the Executor using the {@link #start()} or {@link #run()} methods
 * more on these below.</li>
 * </ol>
 * Calling {@link #run()} translates to calling {@linkplain #start()},
 * {@linkplain #shutdown()} and {@linkplain #awaitShutdown()} in this order. The
 * {@link #start} method is for simply starting the executor and not waiting for
 * it to finish running.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@SequenceDiagramRelated
public interface SequenceDiagramExecutor extends CastedModelExecutor<SequenceDiagramExecutor> {

	static SequenceDiagramExecutor create() {
		return new SeqDiagExecutorImpl();
	}

	SequenceDiagramExecutor setInteraction(Interaction interaction) throws LockedSeqDiagExecutorException;

	List<SeqDiagValidationError> getErrors();

	/**
	 * Not supported; use the {@link #setInteraction} method instead.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	default SequenceDiagramExecutor setInitialization(Runnable initialization) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}
}
