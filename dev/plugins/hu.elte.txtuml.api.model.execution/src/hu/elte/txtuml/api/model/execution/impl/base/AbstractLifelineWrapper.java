package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Runtime;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.runtime.LifelineWrapper;
import hu.elte.txtuml.api.model.runtime.ModelClassWrapper;
import hu.elte.txtuml.api.model.seqdiag.Lifeline;

public class AbstractLifelineWrapper<T extends ModelClass> extends BaseWrapper<Lifeline<?>> implements LifelineWrapper<T> {

	private ModelClassWrapper instance;
	
	public AbstractLifelineWrapper(Lifeline wrapped,ModelClassWrapper instance) {
		super(wrapped);
		
		this.instance = instance;
	}

	@Override
	public String getStringRepresentation() {
		return "Lifeline:";
	}

	@Override
	public Runtime getRuntime() {
		return Runtime.currentRuntime();
	}

	@Override
	public void send(Signal signal) {
		instance.send(signal);
		
	}

	@Override
	public ModelClassWrapper getInstance() {
		return this.instance;
	}

}
