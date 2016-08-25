package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;

import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.error.seqdiag.ValidationError;
import hu.elte.txtuml.api.model.execution.impl.DefaultModelExecutor;
import hu.elte.txtuml.api.model.seqdiag.BaseSequenceDiagramExecutor;
import hu.elte.txtuml.api.model.seqdiag.FragmentListener;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.InteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.RuntimeContext;
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
	protected FragmentCreationListener frCreator;

	private Boolean isLocked;
	private SequenceDiagramExecutorThread thread;
	private Interaction base;
	private ArrayList<ValidationError> errors;

	private ExecutorState state = ExecutorState.INITIALIZE;

	ArrayList<FragmentListener> frListeners;

	public SequenceDiagramExecutor() {
		isLocked = false;
		frListeners = new ArrayList<FragmentListener>();

		messageListener = new InvalidMessageSentListener(this);
		traceListener = new CommunicationListener(this);
		frCreator = new FragmentCreationListener(this);

		executor = new DefaultModelExecutor();
		executor.addWarningListener(messageListener);
		executor.addTraceListener(traceListener);

		this.addFragmentListener(frCreator);

		errors = new ArrayList<ValidationError>();

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

		InteractionWrapper interaction = ((RuntimeContext) Thread.currentThread()).getRuntime()
				.getInteractionWrapper(base);
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

		if (traceListener.suggestedMessagePattern.size() != 0) {
			this.errors
					.add(new InvalidMessageError(this.thread.getInteractionWrapper().getLifelines().get(0).getWrapped(),
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
		try {
			this.thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
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

	@Override
	public void addFragmentListener(FragmentListener listener) {
		this.frListeners.add(listener);
	}

	@Override
	public void removeFragmentListener(FragmentListener listener) {
		this.frListeners.remove(listener);

	}

	public enum ExecutorState {
		INITIALIZE, READY, ENDED
	}
}
