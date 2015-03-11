package hu.elte.txtuml.api;

import hu.elte.txtuml.api.Association.AssociationEnd;
import hu.elte.txtuml.api.backend.collections.AssociationsMap;
import hu.elte.txtuml.api.backend.collections.InitialStatesMap;
import hu.elte.txtuml.api.backend.collections.InnerClassInstancesMap;
import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
// import hu.elte.txtuml.importer.MethodImporter;
// import hu.elte.txtuml.importer.ModelImporter;
import hu.elte.txtuml.utils.InstanceCreator;

public class ModelClass extends ModelIdentifiedElementImpl implements
		ModelElement, ModelIdentifiedElement, LayoutNode {

	/*
	 * DESTROYED status is currently unreachable, FINALIZED is only by a class
	 * which has no state machine.
	 */
	private enum Status {
		READY, ACTIVE, FINALIZED, DESTROYED
	}

	private static InitialStatesMap initialStates = InitialStatesMap.create();

	private Status status;
	private State currentState;
	private final AssociationsMap associations = AssociationsMap.create();
	private final InnerClassInstancesMap innerClassInstances = InnerClassInstancesMap
			.create();

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

	protected ModelClass() {
		super();
		this.currentState = null;
		this.innerClassInstances.put(getClass(), this);

		// TODO: The following line creates an unwanted dependency
		// between the API and the Importer.
		// Can it be eliminated by AspectJ tricks?
		// if (!ModelImporter.isImporting()) {
		setCurrentStateToInitial();
		// }
	}

	private void setCurrentStateToInitial() {
		Class<? extends InitialState> initStateClass = getInitialState(getClass());
		if (initStateClass != null) {
			currentState = getInnerClassInstance(initStateClass);
			status = Status.READY;
		} else {
			status = Status.FINALIZED;
		}
	}

	public <T extends ModelClass, AE extends AssociationEnd<T>> AE assoc(
			Class<AE> otherEnd) {
		@SuppressWarnings("unchecked")
		AE ret = (AE) associations.get(otherEnd);
		if (ret == null) {
			ret = InstanceCreator.createInstance(otherEnd);
			associations.put(otherEnd, ret);
		}
		ret.setOwnerId(this.getIdentifier());
		return ret;
	}

	@SuppressWarnings("unchecked")
	<T extends ModelClass, AE extends AssociationEnd<T>> void addToAssoc(
			Class<AE> otherEnd, T object) {
		associations.put(
				otherEnd,
				(AE) InstanceCreator.createInstanceWithGivenParams(otherEnd,
						(Object) null).init(
						assoc(otherEnd).typeKeepingAdd(object)));
	}

	void start() {
		if (status != Status.READY) {
			return;
		}
		send(null); // to move from initial state
		status = Status.ACTIVE;
	}

	void send(Signal signal) {
		ModelExecutor.send(this, signal);
	}

	void processSignal(Signal signal) {
		if (currentState == null) { // no state machine
			return;
		}
		if (ModelExecutor.Settings.executorLog() && signal != null) {
			Action.executorFormattedLog("%10s %-15s    got signal: %-18s%n",
					getClass().getSimpleName(), getIdentifier(), signal
							.getClass().getSimpleName());
		}
		if (executeTransition(signal)) {
			callEntryAction();
		}
	}

	private boolean executeTransition(Signal signal) {
		Class<?> applicableTransition = null;
		for (Class<?> examinedStateClass = currentState.getClass(), parentClass = examinedStateClass
				.getEnclosingClass(); parentClass != null; examinedStateClass = parentClass, parentClass = examinedStateClass
				.getEnclosingClass()) {
			for (Class<?> c : parentClass.getDeclaredClasses()) {
				if (Transition.class.isAssignableFrom(c)) {
					Class<? extends State> from/* ,to */;
					try {
						from = c.getAnnotation(From.class).value(); // NullPointerException
																	// if no
																	// @From is
																	// set on
																	// the
																	// transition
						/* to = */c.getAnnotation(To.class).value(); // NullPointerException
																		// if no
																		// @To
																		// is
																		// set
																		// on
																		// the
																		// transition
					} catch (NullPointerException e) {
						// TODO show warning
						continue;
					}
					if (from != examinedStateClass
							|| notApplicableTrigger(c, signal)) {
						continue;
					}

					Transition transition = (Transition) getInnerClassInstance(c);
					transition.setSignal(signal);
					if (!transition.guard().getValue()) { // checking guard
						continue;
					}

					if (applicableTransition != null) {
						Action.executorErrorLog("Error: guards of transitions "
								+ applicableTransition.getName() + " and "
								+ c.getName() + " from class "
								+ currentState.getClass().getSimpleName()
								+ " are overlapping");
						continue;
					}
					applicableTransition = c;
				}
			}
		}

		if (applicableTransition == null) { // there was no transition which
											// could be used
			return false;
		}
		useTransition(applicableTransition);

		if (currentState instanceof Choice) {
			executeTransitionFromChoice(signal);
		}

		return true;
	}

	private void executeTransitionFromChoice(Signal signal) {
		Class<?> applicableTransition = null;
		Class<?> elseTransition = null;
		Class<?> examinedChoiceClass = currentState.getClass();
		Class<?> parentClass = examinedChoiceClass.getEnclosingClass();
		for (Class<?> c : parentClass.getDeclaredClasses()) {
			if (Transition.class.isAssignableFrom(c)) {
				Class<? extends State> from/* ,to */;
				try {
					from = c.getAnnotation(From.class).value(); // NullPointerException
																// if no @From
																// is set on the
																// transition
					/* to = */c.getAnnotation(To.class).value(); // NullPointerException
																	// if no @To
																	// is set on
																	// the
																	// transition
				} catch (NullPointerException e) {
					// TODO show warning
					continue;
				}
				if (from != examinedChoiceClass) { // actual transition is from
													// another state
					continue;
				}

				Transition transition = (Transition) getInnerClassInstance(c);
				transition.setSignal(signal);
				ModelBool resultOfGuard = transition.guard();
				if (!resultOfGuard.getValue()) { // check guard
					if (resultOfGuard instanceof ModelBool.Else) { // transition
																	// with else
																	// condition
						if (elseTransition != null) { // there was already a
														// transition with an
														// else condition
							Action.executorErrorLog("Error: there are more than one transitions from choice "
									+ examinedChoiceClass.getSimpleName()
									+ " with an Else condition");
							continue;
						}
						elseTransition = c;
					}
					continue;
				}
				if (applicableTransition != null) { // there was already an
													// applicable transition
					Action.executorErrorLog("Error: guards of transitions "
							+ applicableTransition.getName() + " and "
							+ c.getName() + " from class "
							+ examinedChoiceClass.getSimpleName()
							+ " are overlapping");
					continue;
				}
				applicableTransition = c;
			}
		}
		if (applicableTransition == null) { // there was no transition which
											// could be used
			if (elseTransition != null) { // but there was a transition with an
											// else condition
				useTransition(elseTransition);
			} else {
				Action.executorErrorLog("Error: there was no transition from choice class "
						+ examinedChoiceClass.getSimpleName()
						+ " which could be used");
			}
			return;
		}
		useTransition(applicableTransition);
		if (currentState instanceof Choice) {
			executeTransitionFromChoice(signal);
		}
	}

	private void useTransition(Class<?> transitionClass) {
		/*
		 * The signal has to be already set to the transition instance returned
		 * by the getInnerClassInstance(transitionClass) call.
		 */

		Transition transition = (Transition) getInnerClassInstance(transitionClass);
		Class<? extends State> from = transitionClass.getAnnotation(From.class)
				.value();
		Class<? extends State> to = transitionClass.getAnnotation(To.class)
				.value();
		if (ModelExecutor.Settings.executorLog()) {
			Action.executorFormattedLog(
					"%10s %-15s changes state: from: %-10s tran: %-18s to: %-10s%n",
					getClass().getSimpleName(), getIdentifier(),
					from.getSimpleName(), transitionClass.getSimpleName(),
					to.getSimpleName());
		}
		callExitAction(from);
		transition.effect();
		currentState = getInnerClassInstance(to);
	}

	private boolean notApplicableTrigger(Class<?> transitionClass, Signal signal) {
		Trigger trigger = transitionClass.getAnnotation(Trigger.class);
		if ((signal == null) == (trigger == null)
				&& ((signal == null) || (trigger.value()
						.isAssignableFrom(signal.getClass())))) {
			return false;
		}
		return true;
	}

	private void callExitAction(Class<? extends State> from) {
		while (currentState.getClass() != from) {
			if (ModelExecutor.Settings.executorLog()) {
				Action.executorFormattedLog(
						"%10s %-15s   exits state: %-18s%n", getClass()
								.getSimpleName(), getIdentifier(), currentState
								.getClass().getSimpleName());
			}
			currentState.exit();
			@SuppressWarnings("unchecked")
			Class<? extends State> currentParentState = (Class<? extends State>) currentState
					.getClass().getEnclosingClass();
			currentState = getInnerClassInstance(currentParentState);
		}
		currentState.exit();
	}

	private void callEntryAction() {
		currentState.entry();
		if (currentState instanceof CompositeState) {
			Class<? extends InitialState> initStateClass = getInitialState(currentState
					.getClass());
			if (initStateClass != null) {
				if (ModelExecutor.Settings.executorLog()) {
					Action.executorFormattedLog(
							"%10s %-15s  enters state: %-18s%n", getClass()
									.getSimpleName(), getIdentifier(),
							initStateClass.getSimpleName());
				}
				currentState = getInnerClassInstance(initStateClass);
				// no entry action needs to be called: initial states have none
				processSignal(null); // step forward from initial state
			}
		}
	}

	private <T> T getInnerClassInstance(Class<T> forWhat) {
		if (forWhat == null) {
			Action.executorErrorLog("Error: in class "
					+ getClass().getSimpleName()
					+ " a transition or state is used which is not an inner state of "
					+ getClass().getSimpleName());
			return null;
		}
		@SuppressWarnings("unchecked")
		T ret = (T) innerClassInstances.get(forWhat);
		if (ret == null) {
			ret = InstanceCreator.createInstanceWithGivenParams(forWhat,
					getInnerClassInstance(forWhat.getEnclosingClass()));
			innerClassInstances.put(forWhat, ret);
		}
		return ret;
	}

	private static Class<? extends InitialState> getInitialState(
			Class<?> forWhat) {
		synchronized (initialStates) {
			if (initialStates.containsKey(forWhat)) {
				return initialStates.get(forWhat);
			}
			for (Class<?> c : forWhat.getDeclaredClasses()) {
				if (InitialState.class.isAssignableFrom(c)) {
					@SuppressWarnings("unchecked")
					// it is checked
					Class<? extends InitialState> ret = (Class<? extends InitialState>) c;
					initialStates.put(forWhat, ret);
					return ret;
				}
			}
			initialStates.put(forWhat, null);
			return null;
		}
	}

	@Override
	public String toString() {
		return getClass().getSimpleName() + ":" + getIdentifier();
	}
}
