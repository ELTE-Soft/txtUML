package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;

public abstract class StateMachine extends InnerClassInstancesHolder implements
		ModelElement, ModelIdentifiedElement {

	StateMachine() {
	}

	public class Vertex implements ModelElement, LayoutNode {
		
		Vertex() {
		}
		
		String vertexIdentifier() {
			return getClass().getSimpleName();
		}

		@Override
		public String toString() {
			return "vertex: " + vertexIdentifier();
		}

	}
	
	public class Pseudostate extends Vertex {
		
		Pseudostate() {
		}

		@Override
		public String toString() {
			return "pseudostate: " + vertexIdentifier();
		}
		
	}
	
	public class State extends Vertex {

		protected State() {
		}

		public void entry() {
		}

		public void exit() {
		}

		@Override
		public String toString() {
			return "state: " + vertexIdentifier();
		}

	}

	public class Initial extends State {

		protected Initial() {
		}

		public final void entry() {
		}

		public final void exit() {
		}

		@Override
		public String toString() {
			return "initial: " + super.toString();
		}

	}

	public class CompositeState extends State {

		protected CompositeState() {
		}

		@Override
		public String toString() {
			return "composite state: " + super.toString();
		}

	}

	public class Choice extends State {

		protected Choice() {
		}

		public final void entry() {
		}

		public final void exit() {
		}

		@Override
		public String toString() {
			return "choice:" + vertexIdentifier();
		}

	}

	public class Transition implements ModelElement, LayoutLink {

		protected Transition() {
		}

		private Signal signal;

		public void effect() {
		}

		public ModelBool guard() {
			return new ModelBool(true);
		}

		@SuppressWarnings("unchecked")
		protected final <T extends Signal> T getSignal() {
			return (T) signal;
		}

		final void setSignal(Signal s) {
			signal = s;
		}

		@Override
		public String toString() {
			Class<? extends Transition> cls = getClass();
			From from = cls.getAnnotation(From.class);
			String fromAsString = from == null ? "???" : from.value()
					.toString();
			To to = cls.getAnnotation(To.class);
			String toAsString = to == null ? "???" : to.value().toString();
			return "transition:" + getClass().getSimpleName() + " ("
					+ fromAsString + "->" + toAsString + ")";
		}

	}

}
