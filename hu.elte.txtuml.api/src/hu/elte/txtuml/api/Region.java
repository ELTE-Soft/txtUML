package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.InitialsMap;
import hu.elte.txtuml.api.backend.logs.ErrorMessages;
import hu.elte.txtuml.api.backend.logs.LogMessages;

public abstract class Region extends StateMachine {

	private static InitialsMap initials = InitialsMap.create();

	private Vertex currentVertex;

	public abstract String getIdentifier();

	/*
	 * The sole constructor of <code>Region</code>. <p> Sets the
	 * <code>currentVertex</code> field to an instance of this region's initial
	 * pseudostate. The initial pseudostate is a nested class of the <i>actual
	 * region class</i> which is a subclass of {@link StateMachine.Initial}. <p>
	 * The <i>actual region class</i> refers to the class represented by the
	 * <code>java.lang.Class<?></code> object which is returned by the
	 * <code>getClass</code> method when this constructor is run. <p> If two or
	 * more initial pseudostates exist in this region (two or more nested
	 * classes which extend <code>StateMachine.Initial</code>), then an
	 * unspecified one will be used, without any runtime errors or warnings.
	 * However, it is indeed determined as an error in the model, so this case
	 * has to be completely avoided.
	 */
	Region() {
		super();

		Class<? extends Initial> initClass = getInitial(getClass());
		if (initClass != null) {
			currentVertex = getNestedClassInstance(initClass);
		} else {
			currentVertex = null;
		}
	}

	/**
	 * Returns the value of the <code>currentVertex</code> field, which is an
	 * object of the class which represents the currently active vertex of this
	 * region. If this method is called when no other methods of this class has
	 * been called or all of them has already returned, then this method returns
	 * either <code>null</code> (if this region is inactive) or an instance of a
	 * state class (an object, for which
	 * <code>instanceof StateMachine.State</code> returns true).
	 * 
	 * @return an object of the currently active vertex in this region
	 */
	final Vertex getCurrentVertex() {
		return currentVertex;
	}

	/**
	 * Inactivates this region by setting its <code>currentVertex</code> field
	 * to <code>null</code>.
	 */
	final void inactivate() {
		currentVertex = null;
	}

	/**
	 * Processes a signal and changes to another state if necessary (possibly
	 * after entering and exiting multiple vertices). Also executes every
	 * required exit and entry actions, transition effects. Stops after entering
	 * the first state which is not a composite state (and as a state, also not
	 * an initial or choice pseudostate).
	 * 
	 * @param signal
	 *            the signal object to be processed
	 * @throws NullPointerException
	 *             if <code>signal</code> is <code>null</code>
	 */
	void process(Signal signal) {
		if (currentVertex == null) { // no state machine
			return;
		}
		if (ModelExecutor.Settings.executorLog() && signal != null) {
			ModelExecutor.executorLog(LogMessages.getProcessingSignalMessage(
					this, signal));
		}
		if (findAndExecuteTransition(signal)) {
			callEntryAction();
		}
	}

	private boolean findAndExecuteTransition(Signal signal) {
		Transition applicableTransition = null;

		for (Class<?> examinedClass = currentVertex.getClass(), parentClass = examinedClass
				.getEnclosingClass(); parentClass != null; examinedClass = parentClass, parentClass = examinedClass
				.getEnclosingClass()) {

			for (Class<?> c : parentClass.getDeclaredClasses()) {
				if (Transition.class.isAssignableFrom(c)) {

					Transition transition = (Transition) getNestedClassInstance(c);

					if (!transition.isFromSource(examinedClass)
							|| notApplicableTrigger(c, signal)) {
						continue;
					}

					transition.setSignal(signal);

					if (!transition.guard().getValue()) { // checking guard
						continue;
					}

					if (applicableTransition != null) {
						ModelExecutor.executorErrorLog(ErrorMessages
								.getGuardsOfTransitionsAreOverlappingMessage(
										applicableTransition, transition,
										currentVertex));
						continue;
					}

					applicableTransition = transition;

					if (!ModelExecutor.Settings.dynamicChecks()) {
						break;
					}

				}
			}
		}

		if (applicableTransition == null) {
			// there was no transition which could be used
			return false;
		}

		executeTransition(applicableTransition);

		if (currentVertex instanceof Choice) {
			findAndExecuteTransitionFromChoice(signal);
		}

		return true;
	}

	private void findAndExecuteTransitionFromChoice(Signal signal) {
		Transition applicableTransition = null;
		Transition elseTransition = null;

		final Class<?> examinedChoiceClass = currentVertex.getClass();
		final Class<?> parentClass = examinedChoiceClass.getEnclosingClass();

		for (Class<?> c : parentClass.getDeclaredClasses()) {
			if (Transition.class.isAssignableFrom(c)) {

				Transition transition = (Transition) getNestedClassInstance(c);

				if (!transition.isFromSource(examinedChoiceClass)) {
					// actual transition is from another vertex
					continue;
				}

				transition.setSignal(signal);
				ModelBool resultOfGuard = transition.guard();

				if (!resultOfGuard.getValue()) { // check guard
					continue;
				}

				if (resultOfGuard instanceof ModelBool.Else) {
					// transition with else condition

					if (elseTransition != null) {
						// there was already a transition with an else condition

						ModelExecutor
								.executorErrorLog(ErrorMessages
										.getMoreThanOneTransitionsFromChoiceMessage(currentVertex));
						continue;
					}

					elseTransition = transition;

					continue;
				}

				if (applicableTransition != null) {
					// there was already an applicable transition

					ModelExecutor.executorErrorLog(ErrorMessages
							.getGuardsOfTransitionsAreOverlappingMessage(
									applicableTransition, transition,
									currentVertex));

					continue;
				}

				applicableTransition = transition;

				if (!ModelExecutor.Settings.dynamicChecks()) {
					break;
				}
			}
		}
		if (applicableTransition == null) {
			// there was no transition which could be used

			if (elseTransition != null) {
				// but there was a transition with an else condition

				executeTransition(elseTransition);
			} else {
				ModelExecutor
						.executorErrorLog(ErrorMessages.getNoTransitionFromChoiceMessage(currentVertex));
			}
			
			return;
		}

		executeTransition(applicableTransition);

		if (currentVertex instanceof Choice) {
			findAndExecuteTransitionFromChoice(signal);
		}
	}

	private void executeTransition(Transition transition) {
		if (ModelExecutor.Settings.executorLog()) {
			ModelExecutor.executorLog(LogMessages
					.getUsingTransitionMessage(Region.this, transition));
		}
		callExitAction(transition.getSource());
		transition.effect();
		currentVertex = transition.getTarget();
	}

	
	private boolean notApplicableTrigger(Class<?> transitionClass, Signal signal) {
		Trigger trigger = transitionClass.getAnnotation(Trigger.class);
		if ((signal == null) == (trigger == null)
				&& ((signal == null) || (trigger.value().isInstance(signal)))) {
			return false;
		}
		return true;
	}

	private void callExitAction(Vertex vertex) {
		while (currentVertex != vertex) {

			currentVertex.exit();

			Class<?> currentParentState = currentVertex.getClass()
					.getEnclosingClass();

			currentVertex = (Vertex) getNestedClassInstance(currentParentState);

			if (ModelExecutor.Settings.executorLog()) {
				ModelExecutor.executorLog(LogMessages.getLeavingVertexMessage(
						this, currentVertex));
			}
		}
		currentVertex.exit();
	}

	private void callEntryAction() {
		currentVertex.entry();
		if (currentVertex instanceof CompositeState) {
			Class<? extends Initial> initClass = getInitial(currentVertex
					.getClass());
			if (initClass != null) {
				if (ModelExecutor.Settings.executorLog()) {
					ModelExecutor.executorLog(LogMessages
							.getEnteringVertexMessage(this, currentVertex));
				}

				currentVertex = getNestedClassInstance(initClass);
				// no entry action needs to be called: initial pseudostates have
				// none

				process(null); // step forward from initial pseudostate
			}
		}
	}

	static Class<? extends Initial> getInitial(Class<?> forWhat) {
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
