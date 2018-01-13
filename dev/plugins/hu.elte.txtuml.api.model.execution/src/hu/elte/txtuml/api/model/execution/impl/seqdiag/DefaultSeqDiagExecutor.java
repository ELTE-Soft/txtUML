package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.ImmutableList;

import hu.elte.txtuml.api.model.execution.LockedSeqDiagExecutorException;
import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelExecutor;
import hu.elte.txtuml.api.model.execution.impl.base.AbstractModelRuntime;
import hu.elte.txtuml.api.model.execution.impl.base.SwitchOnLogging;
import hu.elte.txtuml.api.model.execution.seqdiag.error.MessageError;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

@SequenceDiagramRelated
public class DefaultSeqDiagExecutor extends AbstractModelExecutor<SequenceDiagramExecutor>
		implements SequenceDiagramExecutor {

	/**
	 * May only be accessed when holding the monitor of this list.
	 */
	private final List<MessageError> errors = new ArrayList<>();

	private SequenceDiagram diagram;

	public DefaultSeqDiagExecutor() {
		this("");
	}

	public DefaultSeqDiagExecutor(String name) {
		super(name, SwitchOnLogging.DEFAULT_LOGGING_AND_DIAGNOSTICS_SERVICE);
		addWarningListener(new InvalidMessageSentListener(this));
	}

	@Override
	protected AbstractModelRuntime<?, ?> createRuntime(Runnable initialization) {
		return new DefaultSeqDiagRuntime(this, diagram, initialization);
	}

	@Override
	public SequenceDiagramExecutor setDiagram(SequenceDiagram diagram) throws LockedSeqDiagExecutorException {
		checkIfLocked();
		this.diagram = diagram;
		return self();
	}

	public ImmutableList<MessageError> getErrors() {
		synchronized (this.errors) {
			return ImmutableList.copyOf(this.errors);
		}
	}

	/**
	 * Thread-safe.
	 */
	protected void addError(MessageError error) {
		synchronized (errors) {
			this.errors.add(error);
		}
	}

	@Override
	public SequenceDiagramExecutor setInitialization(Runnable initialization) throws UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public DefaultSeqDiagExecutor self() {
		return this;
	}

	@Override
	protected void checkIfLocked() throws LockedSeqDiagExecutorException {
		if (getStatus() != Status.CREATED) {
			throw new LockedSeqDiagExecutorException();
		}
	}
}
