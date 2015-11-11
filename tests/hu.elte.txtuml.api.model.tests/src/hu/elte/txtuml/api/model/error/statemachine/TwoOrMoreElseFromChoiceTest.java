package hu.elte.txtuml.api.model.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.base.TestsBase;
import hu.elte.txtuml.api.model.error.statemachine.TwoOrMoreElseFromChoiceTest.TwoOrMoreElseFromChoiceModel.A;
import hu.elte.txtuml.api.model.error.statemachine.TwoOrMoreElseFromChoiceTest.TwoOrMoreElseFromChoiceModel.Sig;
import hu.elte.txtuml.api.model.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class TwoOrMoreElseFromChoiceTest extends TestsBase {

	@Test
	public void test() {
		
		A a = new A();
		Action.start(a);
		Action.send(a, new Sig());
		
		stopModelExecution();

		executionAsserter.assertErrors( x -> x.moreThanOneElseTransitionsFromChoice(a.new C()));

	}

	static class TwoOrMoreElseFromChoiceModel extends Model {
		
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
					return Else();
				}
				
			}
		
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T2 extends Transition {
				
				@Override
				public boolean guard() {
					return Else();
				}
				
			}
		}
	}
}
