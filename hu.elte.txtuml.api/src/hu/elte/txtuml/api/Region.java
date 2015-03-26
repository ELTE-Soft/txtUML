package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.InitialsMap;

public abstract class Region extends StateMachine {

	private static InitialsMap initials = InitialsMap.create();

	private State currentState;

	public abstract String getIdentifier(); 
	
	Region() {
		super();

		Class<? extends Initial> initStateClass = getInitialState(getClass());
		if (initStateClass != null) {
			currentState = getInnerClassInstance(initStateClass);
		} else {
			currentState = null;
		}
	}

	final State getCurrentState() {
		return currentState;
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

	private class TransitionExecutor {

		private Transition transition;
		private Class<?> transitionClass;
		private Class<? extends State> from;
		private Class<? extends State> to;

		TransitionExecutor() {
			this.transition = null;
			this.transitionClass = null;
			this.from = null;
			this.to = null;
		}

		boolean isEmpty() {
			return transition == null;
		}

		void set(Transition transition, Class<?> transitionClass,
				Class<? extends State> from, Class<? extends State> to) {
			this.transition = transition;
			this.transitionClass = transitionClass;
			this.from = from;
			this.to = to;
		}

		Class<?> getTransitionClass() {
			return transitionClass;
		}

		/**
		 * The signal has to be already set on the transition contained in this
		 * executor before calling this method.
		 */
		void execute() {
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

	}

	private boolean executeTransition(Signal signal) {
		final TransitionExecutor applicableTransitionExecutor = new TransitionExecutor();

		for (Class<?> examinedStateClass = currentState.getClass(), parentClass = examinedStateClass
				.getEnclosingClass(); parentClass != null; examinedStateClass = parentClass, parentClass = examinedStateClass
				.getEnclosingClass()) {

			for (Class<?> c : parentClass.getDeclaredClasses()) {
				if (Transition.class.isAssignableFrom(c)) {
					Class<? extends State> from, to;
					try {
						from = c.getAnnotation(From.class).value();
						to = c.getAnnotation(To.class).value();
					} catch (NullPointerException e) {
						// if no @From or @To annotation is present on the
						// transition, a NullPointerException is thrown

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

					if (!applicableTransitionExecutor.isEmpty()) {
						Action.executorErrorLog("Error: guards of transitions "
								+ applicableTransitionExecutor
										.getTransitionClass().getName()
								+ " and " + c.getName() + " from class "
								+ currentState.getClass().getSimpleName()
								+ " are overlapping");
						continue;
					}

					applicableTransitionExecutor.set(transition, c, from, to);
					;

				}
			}
		}

		if (applicableTransitionExecutor.isEmpty()) { // there was no transition
														// which
														// could be used
			return false;
		}
		applicableTransitionExecutor.execute();

		if (currentState instanceof Choice) {
			executeTransitionFromChoice(signal);
		}

		return true;
	}

	private void executeTransitionFromChoice(Signal signal) {
		final TransitionExecutor applicableTransitionExecutor = new TransitionExecutor();
		final TransitionExecutor elseTransitionExecutor = new TransitionExecutor();

		final Class<?> examinedChoiceClass = currentState.getClass();
		final Class<?> parentClass = examinedChoiceClass.getEnclosingClass();

		for (Class<?> c : parentClass.getDeclaredClasses()) {
			if (Transition.class.isAssignableFrom(c)) {
				Class<? extends State> from, to;
				try {
					from = c.getAnnotation(From.class).value();
					to = c.getAnnotation(To.class).value();
				} catch (NullPointerException e) {
					// if no @From or @To annotation is present on the
					// transition, a NullPointerException is thrown

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
						if (!elseTransitionExecutor.isEmpty()) { // there was
																	// already a
							// transition with an
							// else condition
							Action.executorErrorLog("Error: there are more than one transitions from choice "
									+ examinedChoiceClass.getSimpleName()
									+ " with an Else condition");
							continue;
						}
						elseTransitionExecutor.set(transition, c, from, to);
						;
					}
					continue;
				}
				if (!applicableTransitionExecutor.isEmpty()) { // there was
																// already an
																// applicable
																// transition
					Action.executorErrorLog("Error: guards of transitions "
							+ applicableTransitionExecutor.getTransitionClass()
									.getName() + " and " + c.getName()
							+ " from class "
							+ examinedChoiceClass.getSimpleName()
							+ " are overlapping");
					continue;
				}
				applicableTransitionExecutor.set(transition, c, from, to);
				;
			}
		}
		if (applicableTransitionExecutor.isEmpty()) { // there was no transition
														// which
														// could be used
			if (!elseTransitionExecutor.isEmpty()) { // but there was a
														// transition with an
														// else condition
				elseTransitionExecutor.execute();
			} else {
				Action.executorErrorLog("Error: there was no transition from choice class "
						+ examinedChoiceClass.getSimpleName()
						+ " which could be used");
			}
			return;
		}

		applicableTransitionExecutor.execute();

		if (currentState instanceof Choice) {
			executeTransitionFromChoice(signal);
		}
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
			Class<? extends Initial> initStateClass = getInitialState(currentState
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

	static Class<? extends Initial> getInitialState(Class<?> forWhat) {
		synchronized (initials) {
			if (initials.containsKey(forWhat)) {
				return initials.get(forWhat);
			}
			for (Class<?> c : forWhat.getDeclaredClasses()) {
				if (Initial.class.isAssignableFrom(c)) {
					@SuppressWarnings("unchecked")
					// it is checked
					Class<? extends Initial> ret = (Class<? extends Initial>) c;
					initials.put(forWhat, ret);
					return ret;
				}
			}
			initials.put(forWhat, null);
			return null;
		}
	}

}
