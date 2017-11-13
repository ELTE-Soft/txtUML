package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.error.seqdiag.InvalidMessageError;
import hu.elte.txtuml.api.model.error.seqdiag.ModelRuntimeException;
import hu.elte.txtuml.api.model.error.seqdiag.PatternMismatchError;
import hu.elte.txtuml.api.model.execution.TraceListener;
import hu.elte.txtuml.api.model.seqdiag.BaseCombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseMessageWrapper;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;

public class CommunicationListener extends AbstractSequenceDiagramModelListener implements TraceListener {

	protected BaseFragmentWrapper suggestedMessagePattern;

	private ModelClass currentSender;
	private Signal sentSignal;

	public CommunicationListener(SequenceDiagramExecutor executor) {
		super(executor);
		currentSender = null;
		sentSignal = null;
	}

	@Override
	public void executionStarted() {

	}

	@Override
	public void sendingSignal(ModelClass sender, Signal signal) {
		currentSender = sender;
		sentSignal = signal;
	}

	@Override
	public void processingSignal(ModelClass object, Signal signal, Boolean isAPI) {

		if (suggestedMessagePattern == null) {
			suggestedMessagePattern = this.executor.getThread().getRuntime().getCurrentInteraction().getFragments();
		}

		BaseMessageWrapper sentWrapper = null;

		if (sentSignal != null && currentSender != null && signal.equals(sentSignal)) {
			sentWrapper = new MessageWrapper(currentSender, sentSignal, object);
		} else {
			sentWrapper = new MessageWrapper(null, signal, object, isAPI);
		}

		if (suggestedMessagePattern.size() > 0) {
			if (suggestedMessagePattern instanceof BaseCombinedFragmentWrapper) {
				try {
					((BaseCombinedFragmentWrapper) suggestedMessagePattern).checkMessageSendToPattern(sentWrapper);
				} catch (PatternMismatchError ex) {
					executor.addError(ex);
				}
			} else {
				throw new ModelRuntimeException(
						"Something went bad while parsing the model - invalid model was parsed");
			}
		} else {
			if (this.executor.getThread().getRuntime().getExecutionMode() != ExecMode.LENIENT
					&& suggestedMessagePattern instanceof BaseCombinedFragmentWrapper
					&& ((BaseCombinedFragmentWrapper) suggestedMessagePattern).hasOverlapWarning()) {
				executor.addError(
						new InvalidMessageError(object, "The model sent more signals than the pattern overlapped"));
			}
		}

		currentSender = null;
		sentSignal = null;
	}

	@Override
	public void usingTransition(ModelClass object, Transition transition) {
	}

	@Override
	public void enteringVertex(ModelClass object, Vertex vertex) {
	}

	@Override
	public void leavingVertex(ModelClass object, Vertex vertex) {
	}

	@Override
	public void executionTerminated() {
	}

}
