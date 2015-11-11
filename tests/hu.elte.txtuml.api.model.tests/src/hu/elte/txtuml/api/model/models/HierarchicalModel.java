package hu.elte.txtuml.api.model.models;

import hu.elte.txtuml.api.model.Action;
import hu.elte.txtuml.api.model.From;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.To;
import hu.elte.txtuml.api.model.Trigger;

public class HierarchicalModel extends Model {

	public static class Sig0 extends Signal {}
	public static class Sig1 extends Signal {}
	public static class Sig2 extends Signal {}

	public static class A extends ModelClass {

		public class Init extends Initial {}
		public class S1 extends State {}
		public class CS1 extends CompositeState {
			
			@Override public void entry() {
				Action.log("CS1 entry");
			}

			@Override public void exit() {
				Action.log("CS1 exit");
			}

			public class Init extends Initial {}
			public class S2 extends State {}
			public class CS2 extends CompositeState {

				@Override public void entry() {
					Action.log("CS2 entry");
				}

				@Override public void exit() {
					Action.log("CS2 exit");
				}

				public class Init extends Initial {}
				public class S3 extends State {
					
					@Override public void entry() {
						Action.log("S3 entry");
					}

					@Override public void exit() {
						Action.log("S3 exit");
					}
					
				}

				@From(Init.class) @To(S3.class)
				public class Initialize extends Transition {}
				
			}

			@From(Init.class) @To(S2.class)
			public class Initialize extends Transition {}
			@From(S2.class) @To(CS2.class) @Trigger(Sig0.class)
			public class S2_CS2 extends Transition {}
			@From(CS2.class) @To(S2.class) @Trigger(Sig2.class)
			public class CS2_S2 extends Transition {}
			
		}

		@From(Init.class) @To(S1.class)
		public class Initialize extends Transition {}
		@From(S1.class) @To(CS1.class) @Trigger(Sig0.class)
		public class S1_CS1 extends Transition {}
		@From(CS1.class) @To(S1.class) @Trigger(Sig1.class)
		public class CS1_S1 extends Transition {}
	}

}
