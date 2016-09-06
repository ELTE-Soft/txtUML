package hu.elte.txtuml.api.model.seqdiag;

import java.lang.reflect.Field;

import hu.elte.txtuml.api.model.ModelClass;

public abstract class Runtime {

	public Runtime() {
	}

	public abstract BaseInteractionWrapper createInteractionWrapper(Interaction interaction);

	public abstract <T extends ModelClass> BaseLifelineWrapper<T> createLifelineWrapper(Field lifeline);

	public abstract BaseCombinedFragmentWrapper createCombinedFragmentWrapper(CombinedFragmentType type, String name);

	public abstract void setCurrentInteraction(BaseInteractionWrapper interaction);

	public abstract BaseInteractionWrapper getCurrentInteraction();

	public abstract void setExecutionMode(CombinedFragmentType type);

	public abstract void executionModeEnded();

	public abstract CombinedFragmentType getExecutionMode();

	public abstract BaseSequenceDiagramExecutor getExecutor();
}
