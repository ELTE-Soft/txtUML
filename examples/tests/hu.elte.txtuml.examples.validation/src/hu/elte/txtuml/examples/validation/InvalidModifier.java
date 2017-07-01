package hu.elte.txtuml.examples.validation;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;

public class InvalidModifier extends ModelClass {
	
	synchronized void f() {}
	
	final void g() {}
	
	static int i;
	
}