package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.seqdiag.BaseInteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseSequenceDiagramExecutor;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

/**
 * 
 * 
 *
 * Class responsible for executing the SequenceDiagram code.
 * <p>
 * This class executes the model, and watches for the described behavior to
 * happen. If it does happen then the SequenceDiagram returns with no errors, if
 * it does not it marks the errors by showing where did the model-execution
 * diverge from the expected execution path
 * </p>
 * On how to write Sequence Diagrams see {@link SequenceDiagram} or
 * {@link Interaction} classes documentation
 * <p>
 * How to Use:
 * <ol>
 * <li>Set the Interaction to run by using the {@code setInteraction() } method.
 * Its Single parameter is an interaction</li>
 * <li>Run the Executor using the {@code start()} or {@code run()} methods more
 * on these below</li>
 * </ol>
 * The execution can be started using the following two methods: The first
 * method is {@code run()}, which translates to calling start,
 * awaitInititialization and awaitShutdown after another in this order The
 * second( {@code start}) is for simply starting the executor and not waiting
 * for it to finish running.
 * </p>
 * 
 * @author Turi Zoltan(G4R8AJ)
 */
public class SequenceDiagramExecutor implements Runnable, BaseSequenceDiagramExecutor {

	protected InvalidMessageSentListener messageListener;
	protected CommunicationListener traceListener;
	protected DefaultModelExecutor executor;

	private Boolean isLocked;
	private SequenceDiagramExecutorThread thread;
	private Interaction base;
	private ArrayList<ValidationError> errors;

	private ExecutorState state = ExecutorState.INITIALIZE;

	public SequenceDiagramExecutor() {
		isLocked = false;

		messageListener = new InvalidMessageSentListener(this);
		traceListener = new CommunicationListener(this);

		errors = new ArrayList<ValidationError>();

		prepareExecutor();
	}

	private void prepareExecutor() {
		executor = new DefaultModelExecutor();
		executor.addWarningListener(messageListener);
		executor.addTraceListener(traceListener);
		errors.clear();
	}

	public void reInitialize() {
		prepareExecutor();
		state = ExecutorState.INITIALIZE;
	}

	public void setInteraction(Interaction interaction) throws Exception {
		if (isLocked)
			throw new Exception("Invalid method call! Executor is currently executing an interaction");
		this.base = interaction;
	}

	public SequenceDiagramExecutor start() {
		thread = new SequenceDiagramExecutorThread(this);
		thread.start();
		return this;
	}

	public void run() {
		this.start().shutdown().awaitTermination();
	}

	public void execute() {
		isLocked = true;

		BaseInteractionWrapper interaction = this.thread.getRuntime().createInteractionWrapper(base);
		this.thread.getRuntime().setCurrentInteraction(interaction);

		executor.setInitialization(new Runnable() {
			public void run() {
				interaction.getWrapped().initialize();
				interaction.prepare();
				state = ExecutorState.READY;
				interaction.getWrapped().run();
			}
		});

		executor.launch();
		executor.shutdown();
		executor.awaitTermination();
		state = ExecutorState.ENDED;

		if (traceListener.suggestedMessagePattern != null && traceListener.suggestedMessagePattern.size() != 0) {
			this.errors.add(new InvalidMessageError(
					this.thread.getRuntime().getCurrentInteraction().getLifelines().get(0).getWrapped(),
					"The pattern given is bigger than the model"));
		}

	}

	public SequenceDiagramExecutor shutdown() {

		if (state.equals(ExecutorState.ENDED)) {
			this.executor = null;
		}
		isLocked = false;
		return this;
	}

	public void awaitTermination() {
		while (true) {
			try {
				this.thread.join();
				return;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public ArrayList<ValidationError> getErrors() {
		return this.errors;
	}

	public void addError(ValidationError error) {
		this.errors.add(error);
	}

	public SequenceDiagramExecutor self() {
		return this;
	}

	public SequenceDiagramExecutorThread getThread() {
		return this.thread;
	}

	public void awaitInitialization() {
		executor.awaitInitialization();
	}

	public enum ExecutorState {
		INITIALIZE, READY, ENDED
	}

	@Override
	public boolean checkThread(Thread thread) {
		if (this.thread.equals(thread)) {
			return true;
		}
		return false;
	}
}
