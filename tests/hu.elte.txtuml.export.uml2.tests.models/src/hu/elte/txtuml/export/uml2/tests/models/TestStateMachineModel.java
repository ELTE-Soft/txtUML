package hu.elte.txtuml.export.uml2.tests.models;

import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class TestStateMachineModel extends Model {
	class TestSignal extends Signal {}
	
	class TestClass extends ModelClass {
		class A extends Initial {}
		
		class B extends State {}
		
		@From(A.class) @To(B.class)
		class AB extends Transition {}
		
		@From(B.class) @To(B.class) @Trigger(TestSignal.class)
		class BB extends Transition {}		
	}
}
