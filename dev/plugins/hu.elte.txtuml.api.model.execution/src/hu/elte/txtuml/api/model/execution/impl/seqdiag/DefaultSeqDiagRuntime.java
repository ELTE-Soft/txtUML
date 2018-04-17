package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Method;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadPortRuntime;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

/**
 * The model runtime which helps the execution of the sequence diagram. Starts
 * the single model executor thread which then will start the root interaction
 * thread.
 */
@SequenceDiagramRelated
public class DefaultSeqDiagRuntime extends SingleThreadModelRuntime<SeqDiagModelClassRuntime, SingleThreadPortRuntime> {

	private final SeqDiagModelExecutorThread modelThread;

	protected DefaultSeqDiagRuntime(DefaultSeqDiagExecutor executor, SequenceDiagram diagram, Runnable initialization) {
		super(executor);

		ExecMode mode;
		try {
			Method m = diagram.getClass().getMethod("run");
			mode = getExecutionMode(m);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		/*
		 * The root interaction thread that will execute the diagram itself as
		 * an interaction.
		 */
		InteractionThread rootInteraction = new InteractionThread(this, diagram);

		modelThread = new SeqDiagModelExecutorThread(this, executor, rootInteraction, mode, () -> {
			// The initialization of the model executor thread.

			diagram.initialize();
			initialization.run();
			/*
			 * The initialization received from the model executor must be
			 * executed to signal those who have called awaitInitialization on
			 * the model executor.
			 */
			rootInteraction.start();
		});
	}

	private ExecMode getExecutionMode(Method runMethod) {
		ExecutionMode annot = runMethod.getAnnotation(ExecutionMode.class);
		return annot == null ? ExecutionMode.DEFAULT : annot.value();
	}

	// The required methods of an implementer of the SinglethreadModelRuntime

	@Override
	public DefaultSeqDiagExecutor getExecutor() {
		return (DefaultSeqDiagExecutor) super.getExecutor();
	}

	@Override
	public SeqDiagModelClassRuntime createModelClassRuntime(ModelClass object) {
		return new SeqDiagModelClassRuntime(object, modelThread);
	}

	@Override
	public SingleThreadPortRuntime createPortRuntime(Port<?, ?> portInstance, ModelClass owner) {
		return new SingleThreadPortRuntime(portInstance, getRuntimeOf(owner));
	}

	@Override
	public void start() {
		modelThread.start();
	}

	SeqDiagModelExecutorThread getModelThread() {
		return modelThread;
	}
}
