package hu.elte.txtuml.api.tests.error.statemachine;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.From;
import hu.elte.txtuml.api.Model;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.To;
import hu.elte.txtuml.api.Trigger;
import hu.elte.txtuml.api.backend.messages.ErrorMessages;
import hu.elte.txtuml.api.tests.base.TestsBase;
import hu.elte.txtuml.api.tests.error.statemachine.TwoOrMoreElseFromChoiceTest.TwoOrMoreElseFromChoiceModel.A;
import hu.elte.txtuml.api.tests.error.statemachine.TwoOrMoreElseFromChoiceTest.TwoOrMoreElseFromChoiceModel.Sig;
import hu.elte.txtuml.api.tests.util.SeparateClassloaderTestRunner;

import org.junit.Assert;
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

		Assert.assertArrayEquals(
				new String[] { ErrorMessages.getMoreThanOneElseTransitionsFromChoiceMessage(a.new C()) },
				executorErrorStream.getOutputAsArray());

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
				public ModelBool guard() {
					return new ModelBool.Else();
				}
				
			}
		
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T2 extends Transition {
				
				@Override
				public ModelBool guard() {
					return new ModelBool.Else();
				}
				
			}
		}
	}
}
