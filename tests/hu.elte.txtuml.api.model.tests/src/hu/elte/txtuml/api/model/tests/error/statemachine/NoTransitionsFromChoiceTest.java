package hu.elte.txtuml.api.model.tests.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelBool;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelInt;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.tests.base.TestsBase;
import hu.elte.txtuml.api.model.tests.error.statemachine.NoTransitionsFromChoiceTest.NoTransitionsFromChoiceModel.A;
import hu.elte.txtuml.api.model.tests.error.statemachine.NoTransitionsFromChoiceTest.NoTransitionsFromChoiceModel.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class NoTransitionsFromChoiceTest extends TestsBase {

	@Test
	public void test() {
		
		A a = new A();
		Action.start(a);
		Action.send(a, new Sig(new ModelInt(2)));
		
		stopModelExecution();

		executionAsserter.assertErrors( x -> x.noTransitionFromChoice(a.new C()));
	}

	static class NoTransitionsFromChoiceModel extends Model {
		
		static class Sig extends Signal {
			ModelInt value;

			Sig() {
				this(new ModelInt(0));
			}

			Sig(ModelInt value) {
				this.value = value;
			}
		}
		
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
				public ModelBool guard() {
					Sig s = getSignal(Sig.class);
					return s.value.isEqual(new ModelInt(0));
				}
				
			}
		
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T2 extends Transition {
				
				@Override
				public ModelBool guard() {
					Sig s = getSignal(Sig.class);
					return s.value.isEqual(new ModelInt(1));
				}
				
			}
		}
	}
}
