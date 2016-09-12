package hu.elte.txtuml.api.model.execution.impl.seqdiag;

import java.lang.reflect.Field;
import java.util.LinkedList;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.execution.impl.seqdiag.fragments.CombinedFragmentWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseInteractionWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseLifelineWrapper;
import hu.elte.txtuml.api.model.seqdiag.BaseSequenceDiagramExecutor;
import hu.elte.txtuml.api.model.seqdiag.CombinedFragmentType;
import hu.elte.txtuml.api.model.seqdiag.ExecutionMode;
import hu.elte.txtuml.api.model.seqdiag.FragmentType;
import hu.elte.txtuml.api.model.seqdiag.Interaction;
import hu.elte.txtuml.api.model.seqdiag.Position;

public class DefaultRuntime extends hu.elte.txtuml.api.model.seqdiag.Runtime {

	private InteractionWrapper currentInteraction;
	private CombinedFragmentType executionMode = CombinedFragmentType.STRICT;
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
				if (baseMode != null && baseType == null) {
					switch (baseMode.value()) {
					case Lenient:
						/*this.executionMode = CombinedFragmentType.PAR;
						break;*/
					case Sequential:
						this.executionMode = CombinedFragmentType.SEQ;
						break;
					case Strict:
						this.executionMode = CombinedFragmentType.STRICT;
						break;
					}
				} else if (baseMode == null && baseType != null) {
					this.executionMode = baseType.value();
				} else {
					this.executionMode = CombinedFragmentType.STRICT;
				}
			} catch (Exception e) {

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
			e.printStackTrace();
		}

		return new LifelineWrapper<T>(currentInteraction, data, lifeline.getDeclaredAnnotation(Position.class).value(),
				lifeline.getName());
	}

	@Override
	public CombinedFragmentWrapper createCombinedFragmentWrapper(CombinedFragmentType type, String name) {
		return CombinedFragmentWrapper.createWrapper(null,currentInteraction, type, name);
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
	public void setExecutionMode(CombinedFragmentType mode) {
		this.lastTypes.push(this.executionMode);
		this.executionMode = mode;
	}

	@Override
	public void executionModeEnded() {
		this.executionMode = this.lastTypes.pop();
	}

	@Override
	public CombinedFragmentType getExecutionMode() {
		return this.executionMode;
	}

	@Override
	public BaseSequenceDiagramExecutor getExecutor() {
		return executor;
	}
}
