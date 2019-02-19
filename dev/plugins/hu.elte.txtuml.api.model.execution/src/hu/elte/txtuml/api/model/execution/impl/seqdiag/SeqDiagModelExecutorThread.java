package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelClassRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.FIFOExecutorThread;
import hu.elte.txtuml.api.model.execution.impl.base.SignalWrapper;
import hu.elte.txtuml.api.model.execution.seqdiag.error.ErrorLevel;
import hu.elte.txtuml.api.model.execution.seqdiag.error.InvalidMessageError;
import hu.elte.txtuml.api.model.execution.seqdiag.error.ModelStateAssertError;
import hu.elte.txtuml.api.model.execution.seqdiag.error.SequenceDiagramProblem;
import hu.elte.txtuml.api.model.impl.ExecutorThread;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;
import hu.elte.txtuml.api.model.seqdiag.MessageParticipant;
import hu.elte.txtuml.utils.Logger;

/**
 * Class for the single model executor thread in a sequence diagram executor.
 * <p>
 * See the documentation of this package for an overview of the threads used in
 * the sequence diagram executor.
 */
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
	public DefaultSeqDiagRuntime getModelRuntime() {
		return (DefaultSeqDiagRuntime) super.getModelRuntime();
	}

	@Override
	public void doRun() {
		super.doRun();
		root.kill();
		// Ensures that the interaction threads are stopped with this.
	}

	@Override
	public void receiveLater(SignalWrapper signal, AbstractModelClassRuntime target) {
		Logger.sys.debug("sender: " + signal.getSenderOrNull() + " | target: " + target.getWrapped() + " | "
				+ signal.getWrapped());
		addEntry(() -> {
			target.receive(signal);
			compareToPattern(signal, MessageParticipant.create(target.getWrapped()), false);
		});
	}

	@Override
	public void receiveLaterViaAPI(SignalWrapper signal, AbstractModelClassRuntime target) {
		Logger.sys.debug("sender: API | target: " + target.getWrapped() + " | " + signal.getWrapped());
		addEntry(() -> {
			target.receive(signal);
			compareToPattern(signal, MessageParticipant.create(target.getWrapped()), true);
		});
	}

	/**
	 * Called <b>after</b> the given signal to the given target has arrived and
	 * it has been processed.
	 */
	private <T extends ModelClass> void compareToPattern(SignalWrapper wrapper, MessageParticipant<T> target,
			boolean viaAPI) {
		if (wrapper.isEmpty()) {
			return;
		}

		Signal signal = wrapper.getWrapped();

		Message<ModelClass, T> actual;
		if (viaAPI) {
			actual = Message.fromActor(signal, target);
		} else if (wrapper.isSenderKnown()) {
			actual = Message.fromObject(MessageParticipant.create(wrapper.getSenderOrNull()), signal, target);
		} else {
			actual = Message.fromUnknown(signal, target);
		}

		boolean result = root.testActual(actual);

		/*
		 * If the execution mode is LENIENT, any unmatched messages are
		 * automatically accepted. Otherwise, we show an error.
		 */
		if (!result && mode == ExecMode.STRICT) {
			root.getExecutor().addError(new InvalidMessageError(actual, ErrorLevel.ERROR));
		}
	}

	/**
	 * Verifies that the {@code ModelClass} instance is currently in the given
	 * state. If the assertion fails, a {@link ModelStateAssertError} will be
	 * added.
	 */
	public <T extends ModelClass> void assertState(Lifeline<T> instance, Class<?> state) {
		MessageParticipant<T> p = (MessageParticipant<T>) instance;
		if (!p.hasParticipant()) {
			root.getExecutor().addError(new SequenceDiagramProblem(
					"Cannot assert the state of an unbound proxy object.", ErrorLevel.ERROR));
			return;
		}

		ModelClass mc = p.getParticipant().get();
		Class<?> currentState = getModelRuntime().getRuntimeOf(mc).getCurrentState();
		if (!currentState.equals(state)) {
			root.getExecutor()
					.addError(new ModelStateAssertError(state.getCanonicalName(), currentState.getCanonicalName()));
		}
	}
}
