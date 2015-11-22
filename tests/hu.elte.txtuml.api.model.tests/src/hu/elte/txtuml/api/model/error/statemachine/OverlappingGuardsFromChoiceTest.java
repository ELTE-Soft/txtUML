package hu.elte.txtuml.api.model.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.base.TestsBase;
import hu.elte.txtuml.api.model.error.statemachine.OverlappingGuardsFromChoiceTest.OverlappingGuardsFromChoiceModel.A;
import hu.elte.txtuml.api.model.error.statemachine.OverlappingGuardsFromChoiceTest.OverlappingGuardsFromChoiceModel.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class OverlappingGuardsFromChoiceTest extends TestsBase {

	@Test
	public void test() {
		
		A a = new A();
		Action.start(a);
		Action.send(a, new Sig());
		
		stopModelExecution();

		boolean msg1 = executionAsserter.checkErrors(x ->
				x.guardsOfTransitionsAreOverlapping(a.new T1(), a.new T2(),
						a.new C()));
		boolean msg2 = executionAsserter.checkErrors(x ->
				x.guardsOfTransitionsAreOverlapping(a.new T2(), a.new T1(),
						a.new C()));
	
		Assert.assertTrue(msg1 || msg2);
	}

	static class OverlappingGuardsFromChoiceModel {
		
		static class Sig extends Signal {}
		
		static class A extends ModelClass {
		
			class Init extends Initial {}
			class S1 extends State {}
			class C extends Choice {}
		
			@From(Init.class) @To(S1.class)
			class Initialize extends Transition {}
			@From(S1.class) @To(C.class) @Trigger(Sig.class)
			class S1_C extends Transition {}
			
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T1 extends Transition {
				
				@Override
				public boolean guard() {
					return true;
				}
				
			}
		
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T2 extends Transition {
				
				@Override
				public boolean guard() {
					return true;
				}
				
			}
		}
	}

}
