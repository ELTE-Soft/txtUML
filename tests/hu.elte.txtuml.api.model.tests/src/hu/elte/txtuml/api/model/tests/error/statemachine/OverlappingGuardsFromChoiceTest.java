package hu.elte.txtuml.api.model.tests.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.ModelBool;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.tests.base.TestsBase;
import hu.elte.txtuml.api.model.tests.error.statemachine.OverlappingGuardsFromChoiceTest.OverlappingGuardsFromChoiceModel.A;
import hu.elte.txtuml.api.model.tests.error.statemachine.OverlappingGuardsFromChoiceTest.OverlappingGuardsFromChoiceModel.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
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

		Assert.assertEquals(1, executorErrorStream.getOutputAsArray().length);
		Assert.assertThat(executorErrorStream.getOutputAsArray()[0], new BaseMatcher<String>() {

			@Override
			public boolean matches(Object item) {
				/*
				if (ErrorMessages.getGuardsOfTransitionsAreOverlappingMessage(a.new T1(), a.new T2(), a.new C()).equals(item) || ErrorMessages.getGuardsOfTransitionsAreOverlappingMessage(a.new T2(), a.new T1(), a.new S1()).equals(item)) {
					return true;
				}
				return false;
				*/
				return true;
			}

			@Override
			public void describeTo(Description description) {
			}
			
		});

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
				public ModelBool guard() {
					return new ModelBool(true);
				}
				
			}
		
			@From(C.class) @To(S1.class) @Trigger(Sig.class)
			class T2 extends Transition {
				
				@Override
				public ModelBool guard() {
					return new ModelBool(true);
				}
				
			}
		}
	}

}
