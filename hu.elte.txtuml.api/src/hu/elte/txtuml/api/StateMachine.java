package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;

public abstract class StateMachine extends InnerClassInstancesHolder implements
		ModelElement {

	StateMachine() {
	}

	public class Vertex implements ModelElement, LayoutNode {

		Vertex() {
		}

		public void entry() {
		}

		public void exit() {
		}

		String vertexIdentifier() {
			return getClass().getSimpleName();
		}

		@Override
		public String toString() {
			return "vertex:" + vertexIdentifier();
		}

	}

	public class Pseudostate extends Vertex {

		Pseudostate() {
		}

		@Override
		public final void entry() {
		}

		@Override
		public final void exit() {
		}

		@Override
		public String toString() {
			return "pseudostate:" + vertexIdentifier();
		}

	}

	public class Initial extends Pseudostate {

		protected Initial() {
		}

		@Override
		public String toString() {
			return "initial_" + super.toString();
		}

	}

	public class Choice extends Pseudostate {

		protected Choice() {
		}

		@Override
		public String toString() {
			return "choice_" + super.toString();
		}

	}

	public class State extends Vertex {

		protected State() {
		}

		@Override
		public String toString() {
			return "state:" + vertexIdentifier();
		}

	}

	public class CompositeState extends State {

		protected CompositeState() {
		}

		@Override
		public String toString() {
			return "composite_" + super.toString();
		}

	}

	public class Transition implements ModelElement, LayoutLink {

		private final Vertex source;
		private final Vertex target;
		private Signal signal;

		protected Transition() {
			Class<? extends Transition> cls = getClass();

			From from = cls.getAnnotation(From.class);
			To to = cls.getAnnotation(To.class);

			if (from == null || to == null) {
				this.source = null;
				this.target = null;
				// TODO show error
			} else {
				this.source = getInnerClassInstance(from.value());
				this.target = getInnerClassInstance(to.value());
			}
		}

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

		Vertex getSource() {
			return source;
		}

		Vertex getTarget() {
			return target;
		}

		boolean isFromSource(Class<?> c) {
			return source == null ? false : c == source.getClass();
		}

		@Override
		public String toString() {
			return "transition:" + getClass().getSimpleName() + " ("
					+ source.toString() + " --> " + target.toString() + ")";
		}

	}

}
