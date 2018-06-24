package hu.elte.txtuml.api.model.execution;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.DefaultSeqDiagExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.error.SequenceDiagramProblem;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

/**
 * Class responsible for executing the SequenceDiagram code.
 * <p>
 * This class executes the model, and watches for the described behavior to
 * happen. If it does happen, then the SequenceDiagram returns with no errors,
 * if it does not, it marks the errors by showing where did the model execution
 * diverge from the expected execution path.
 * <p>
 * On how to write Sequence Diagrams, see the documentation of the
 * {@link SequenceDiagram} class.
 * <p>
 * <h2>How to Use</h2>
 * <ol>
 * <li>Create new instance with the {@link #create} method.</li>
 * <li>Set the Interaction to run by using the {@link #setDiagram} method. Its
 * single parameter is a sequence diagram subclass instance.</li>
 * <li>Run the executor using the {@link #start()} or {@link #run()} methods.
 * Calling {@link #run()} translates to calling {@linkplain #start()},
 * {@linkplain #shutdown()} and {@linkplain #awaitShutdown()} in this order. The
 * {@link #start} method is for simply starting the executor and not waiting for
 * it to finish running.</li>
 * </ol>
 * The executor uses multiple threads inside itself, some for sequence diagram
 * execution and one for the model execution. However, it is ensured that at any
 * given time, only one of these may execute code written by the user. Therefore
 * no synchronization is required between the sequence diagram description and
 * the model itself.
 * <p>
 * Note that without calling {@link #shutdown()}, a sequence diagram execution
 * will never stop.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
@SequenceDiagramRelated
public interface SequenceDiagramExecutor extends CastedModelExecutor<SequenceDiagramExecutor> {

	static SequenceDiagramExecutor create() {
		return new DefaultSeqDiagExecutor();
	}

	SequenceDiagramExecutor setDiagram(SequenceDiagram diagram) throws LockedSeqDiagExecutorException;

	ImmutableList<SequenceDiagramProblem> getErrors();

	/**
	 * Not supported; use the {@link #setDiagram} method instead.
	 * 
	 * @throws UnsupportedOperationException
	 *             always
	 */
	@Override
	SequenceDiagramExecutor setInitialization(Runnable initialization) throws UnsupportedOperationException;
}
