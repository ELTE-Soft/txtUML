package hu.elte.txtuml.api;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;

public abstract class StateMachine extends InnerClassInstancesHolder implements
		ModelElement, ModelIdentifiedElement {

	StateMachine() {
	}

	public class State implements ModelElement, LayoutNode {

		protected State() {
		}

		public void entry() {
		}

		public void exit() {
		}

		String stateIdentifier() {
			return getClass().getSimpleName();
		}

		@Override
		public String toString() {
			return "state:" + stateIdentifier();
		}

	}

	public class InitialState extends State {

		protected InitialState() {
		}

		public final void entry() {
		}

		public final void exit() {
		}

		@Override
		public String toString() {
			return "initial " + super.toString();
		}

	}

	public class CompositeState extends State {

		protected CompositeState() {
		}

		@Override
		public String toString() {
			return "composite " + super.toString();
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
			return "choice:" + stateIdentifier();
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
			// TODO: The following two lines create an unwanted dependency
			// between the API and the Importer.
			// Can it be eliminated by AspectJ tricks?
			// if (signal == null && MethodImporter.isImporting()) {
			// signal = MethodImporter.createSignal(getClass());
			// }
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
