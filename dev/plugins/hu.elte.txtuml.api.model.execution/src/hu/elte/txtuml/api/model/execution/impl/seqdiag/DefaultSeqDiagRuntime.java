package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Method;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.singlethread.SingleThreadPortRuntime;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

@SequenceDiagramRelated
public class DefaultSeqDiagRuntime
		extends SingleThreadModelRuntime<SingleThreadModelClassRuntime, SingleThreadPortRuntime> {

	private final SeqDiagModelExecutorThread modelThread;
	private final InteractionThread rootInteraction;

	protected DefaultSeqDiagRuntime(DefaultSeqDiagExecutor executor, SequenceDiagram diagram, Runnable initialization) {
		super(executor);

		ExecMode mode;
		try {
			Method m = diagram.getClass().getMethod("run");
			mode = getExecutionMode(m);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

		rootInteraction = new InteractionThread(executor, diagram);
		modelThread = new SeqDiagModelExecutorThread(this, executor, rootInteraction, mode, () -> {
			diagram.initialize();
			initialization.run();
			rootInteraction.start();
		});
	}

	private ExecMode getExecutionMode(Method runMethod) {
		ExecutionMode annot = runMethod.getAnnotation(ExecutionMode.class);
		return annot == null ? ExecutionMode.DEFAULT : annot.value();
	}

	@Override
	public DefaultSeqDiagExecutor getExecutor() {
		return (DefaultSeqDiagExecutor) super.getExecutor();
	}

	@Override
	public SingleThreadModelClassRuntime createModelClassRuntime(ModelClass object) {
		return new SingleThreadModelClassRuntime(object, modelThread);
	}

	@Override
	public SingleThreadPortRuntime createPortRuntime(Port<?, ?> portInstance, ModelClass owner) {
		return new SingleThreadPortRuntime(portInstance, getRuntimeOf(owner));
	}

	@Override
	public void start() {
		modelThread.start();
	}

}
