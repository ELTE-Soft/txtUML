package hu.elte.txtuml.api.tests.models;

import hu.elte.txtuml.api.Action;
import hu.elte.txtuml.api.From;
import hu.elte.txtuml.api.Model;
import hu.elte.txtuml.api.ModelBool;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.To;
import hu.elte.txtuml.api.Trigger;

public class TransitionsModel extends Model {

	public static class Sig1 extends Signal {}
	public static class Sig2 extends Signal {}
	public static class Sig3 extends Signal {}

	public static class A extends ModelClass {

		public ModelBool value = new ModelBool(true);
		
		public class Init extends Initial {}
		public class S extends State {
			@Override public void entry() {
				Action.log("entry");
			}
			
			@Override public void exit() {
				Action.log("exit");
			}			
		}
		
		@From(Init.class) @To(S.class)
		public class Initialize extends Transition {}
		
		@From(S.class) @To(S.class) @Trigger(Sig1.class)
		public class T1 extends Transition {
			@Override public void effect() {
				Action.log("T1");
			}
		}
		
		@From(S.class) @To(S.class) @Trigger(Sig2.class)
		public class T2 extends Transition {
			@Override public void effect() {
				Action.log("T2");
			}
		}

		@From(S.class) @To(S.class) @Trigger(Sig3.class)
		public class T3 extends Transition {
			@Override public ModelBool guard() {
				return value;
			}
			
			@Override public void effect() {
				Action.log("T3");
				value = new ModelBool(false);
			}
		}

		@From(S.class) @To(S.class) @Trigger(Sig3.class)
		public class T4 extends Transition {
			@Override public ModelBool guard() {
				return value.not();
			}
			
			@Override public void effect() {
				Action.log("T4");
				value = new ModelBool(true);
			}
		}

	}
}
