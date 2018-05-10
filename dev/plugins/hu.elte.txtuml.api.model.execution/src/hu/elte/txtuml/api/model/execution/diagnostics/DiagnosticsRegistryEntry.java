package hu.elte.txtuml.api.model.execution.diagnostics;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Vertex;
import hu.elte.txtuml.api.model.external.ModelClasses;

public class DiagnosticsRegistryEntry {
	
	private ModelClass c;
	private Vertex v;
	
	@Override
	public int hashCode() {
		return (c.getClass().getCanonicalName() + ModelClasses.getIdentifierOf(c) + v.getStringRepresentation()).hashCode();
	}
	
	@Override
	public boolean equals(Object obj) {
		return (this.hashCode() == obj.hashCode());
	}
	
	@Override
	public String toString(){
		return (c.getClass().getCanonicalName() + ModelClasses.getIdentifierOf(c) + v.getStringRepresentation());
	}
	
	public String toJSON(){
		return (
				"{\"class\":\"" + c.getClass().getCanonicalName() + "\"," +
				"\"id\":\"" + ModelClasses.getIdentifierOf(c) + "\"," +
				"\"element\":\"" + v.getClass().getCanonicalName() + "\"}"
				);
	}

	
	protected DiagnosticsRegistryEntry(ModelClass c, Vertex v){
		this.c = c;
		this.v = v;
	}	
};