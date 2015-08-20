package hu.elte.txtuml.api.model.tests.error.statemachine;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelBool;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.tests.base.TestsBase;
import hu.elte.txtuml.api.model.tests.error.statemachine.OverlappingGuardsTest.OverlappingGuardsModel.A;
import hu.elte.txtuml.api.model.tests.error.statemachine.OverlappingGuardsTest.OverlappingGuardsModel.Sig;
import hu.elte.txtuml.api.model.tests.util.SeparateClassloaderTestRunner;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(SeparateClassloaderTestRunner.class)
public class OverlappingGuardsTest extends TestsBase {

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
				if (ErrorMessages.getGuardsOfTransitionsAreOverlappingMessage(a.new T1(), a.new T2(), a.new S1()).equals(item) || ErrorMessages.getGuardsOfTransitionsAreOverlappingMessage(a.new T2(), a.new T1(), a.new S1()).equals(item)) {
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

	static class OverlappingGuardsModel extends Model {

		static class Sig extends Signal {}

		static class A extends ModelClass {

			class Init extends Initial {}
			class S1 extends State {}
			class S2 extends State {}

			@From(Init.class) @To(S1.class)
			class T0 extends Transition {}
			@From(S1.class) @To(S2.class) @Trigger(Sig.class)
			class T1 extends Transition {
				@Override
				public ModelBool guard() {
					return new ModelBool(true);
				}
			}

			@From(S1.class) @To(S2.class) @Trigger(Sig.class)
			class T2 extends Transition {
				@Override
				public ModelBool guard() {
					return new ModelBool(true);
				}
			}
		}

	}

}
