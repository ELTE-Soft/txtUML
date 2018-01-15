package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Field;
import java.util.LinkedList;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.error.seqdiag.ModelRuntimeException;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.CombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseInteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseLifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseSequenceDiagramExecutor;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.ExecMode;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.FragmentType;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Position;

public class DefaultRuntime extends hu.elte.txtuml.api.model.seqdiag.Runtime {

	private InteractionWrapper currentInteraction;
	private ExecMode executionMode = ExecMode.STRICT;
	private CombinedFragmentType fragmentMode = CombinedFragmentType.STRICT;
	private LinkedList<CombinedFragmentType> lastTypes;
	private SequenceDiagramExecutor executor;
	private boolean runMethodChecked = false;

	public DefaultRuntime(SequenceDiagramExecutor executor) {
		lastTypes = new LinkedList<CombinedFragmentType>();
		this.executor = executor;
	}

	@Override
	public BaseInteractionWrapper createInteractionWrapper(Interaction interaction) {

		if (!runMethodChecked) {
			runMethodChecked = true;
			try {
				ExecutionMode baseMode = interaction.getClass().getMethod("run").getAnnotation(ExecutionMode.class);
				FragmentType baseType = interaction.getClass().getMethod("run").getAnnotation(FragmentType.class);
				
				if (baseMode != null) {
					this.executionMode = baseMode.value();
				}

				if (baseType != null) {
					this.fragmentMode = baseType.value();
				}

			} catch (Exception e) {
				throw new ModelRuntimeException(e.getMessage());
			}
		}

		return new InteractionWrapper(interaction);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends ModelClass> BaseLifelineWrapper<T> createLifelineWrapper(Field lifeline) {
		T data = null;
		try {
			data = (T) lifeline.get(currentInteraction.getWrapped());
		} catch (Exception e) {
			throw new ModelRuntimeException("All lifeline fields need to be accessible in the interaction:"
					+ currentInteraction.getWrapped().toString());
		}

		return new LifelineWrapper<T>(currentInteraction, data, lifeline.getDeclaredAnnotation(Position.class).value(),
				lifeline.getName());
	}

	@Override
	public CombinedFragmentWrapper createCombinedFragmentWrapper(CombinedFragmentType type, String name) {
		return CombinedFragmentWrapper.createWrapper(null, currentInteraction, type, name);
	}

	@Override
	public InteractionWrapper getCurrentInteraction() {
		return this.currentInteraction;
	}

	@Override
	public void setCurrentInteraction(BaseInteractionWrapper interaction) {
		this.currentInteraction = (InteractionWrapper) interaction;
	}

	@Override
	public void setFragmentMode(CombinedFragmentType mode) {
		this.lastTypes.push(this.fragmentMode);
		this.fragmentMode = mode;
	}

	@Override
	public void fragmentModeEnded() {
		this.fragmentMode = this.lastTypes.pop();
	}

	CombinedFragmentType getFragmentMode() {
		return this.fragmentMode;
	}

	@Override
	public ExecMode getExecutionMode() {
		return this.executionMode;
	}

	@Override
	public BaseSequenceDiagramExecutor getExecutor() {
		return executor;
	}
}
