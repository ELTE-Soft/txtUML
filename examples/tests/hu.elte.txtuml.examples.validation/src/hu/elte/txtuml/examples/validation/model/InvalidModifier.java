package hu.elte.txtuml.examples.validation.model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class InvalidModifier extends ModelClass {
	
	synchronized void f() {}
	
	static final void g() {}
	
	static int i;
	
}