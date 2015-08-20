package hu.elte.txtuml.api.tests.models;

import hu.elte.txtuml.api.Association;
import hu.elte.txtuml.api.From;
import hu.elte.txtuml.api.Model;
import hu.elte.txtuml.api.ModelClass;
import hu.elte.txtuml.api.Signal;
import hu.elte.txtuml.api.To;
import hu.elte.txtuml.api.Trigger;

public class SimpleModel extends Model {

	public static class Sig extends Signal {}

	public static class A extends ModelClass {

		public class Init extends Initial {}
		public class S1 extends State {}
		public class S2 extends State {}

		@From(Init.class) @To(S1.class)
		public class Initialize extends Transition {}
		@From(S1.class) @To(S2.class) @Trigger(Sig.class)
		public class S1_S2 extends Transition {}
		@From(S2.class) @To(S1.class) @Trigger(Sig.class)
		public class S2_S1 extends Transition {}
	}

	public static class B extends ModelClass {}

	public class A_B extends Association {
		public class a extends MaybeOne<A> {}
		public class b extends One<B> {}
	}

}
