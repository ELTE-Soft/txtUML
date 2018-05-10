package hu.elte.txtuml.api.model.execution.diagnostics;

import java.util.HashSet;
import java.util.Set;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * Class for storing the state of an execution's diagnostics.
 * This state consists of active state-machine vertices assigned to instances of state-machine classes.
 * 
 * @author szokolai-mate
 */
public class DiagnosticsRegistry {
	
	private Set<DiagnosticsRegistryEntry> registry;

	public DiagnosticsRegistry() {
		this.registry = new HashSet<>();
	}
	
	public void addEntry(ModelClass c, Vertex v){
		this.registry.add(new DiagnosticsRegistryEntry(c, v));
	}
	
	public void removeEntry(ModelClass c, Vertex v){
		this.registry.remove(new DiagnosticsRegistryEntry(c, v));
	}

	public Iterable<DiagnosticsRegistryEntry> getRegistry() {
		return new HashSet<>(this.registry);
	}
	
	public void clear(){
		this.registry.clear();
	}
}
