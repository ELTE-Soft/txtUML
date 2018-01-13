package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.FIFOExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.base.SignalWrapper;
import hu.elte.txtuml.api.model.execution.seqdiag.error.InvalidMessageError;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;

@SequenceDiagramRelated
class SeqDiagModelExecutorThread extends FIFOExecutorThread implements ExecutorThread {

	private final InteractionThread root;
	private final ExecMode mode;

	public SeqDiagModelExecutorThread(DefaultSeqDiagRuntime defaultSeqDiagRuntime, AbstractModelExecutor<?> executor,
			InteractionThread root, ExecMode mode, Runnable initialization) {
		super(executor, defaultSeqDiagRuntime, initialization);
		this.root = root;
		this.mode = mode;
	}

	@Override
	public void doRun() {
		super.doRun();
		root.kill();
	}
	
	@Override
	public void receiveLater(SignalWrapper signal, AbstractModelClassRuntime target) {
		addEntry(() -> {
			target.receive(signal);
			compareToPattern(signal, target.getWrapped(), false);
		});
	}

	@Override
	public void receiveLaterViaAPI(SignalWrapper signal, AbstractModelClassRuntime target) {
		addEntry(() -> {
			target.receive(signal);
			compareToPattern(signal, target.getWrapped(), true);
		});
	}

	private void compareToPattern(SignalWrapper wrapper, ModelClass target, boolean viaAPI) {
		if (wrapper.isEmpty()) {
			return;
		}

		Signal signal = wrapper.getWrapped();

		Message actual;
		if (viaAPI) {
			actual = Message.fromActor(signal, target);
		} else if (wrapper.isSenderKnown()) {
			actual = Message.fromObject(wrapper.getSenderOrNull(), signal, target);
		} else {
			actual = Message.fromUnknown(signal, target);
		}

		boolean result = root.testActual(actual);
		if (!result && mode == ExecMode.STRICT) {
			root.getExecutor().addError(new InvalidMessageError(actual));
		}
	}
}