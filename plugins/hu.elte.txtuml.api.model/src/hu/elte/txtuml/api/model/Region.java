package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ModelExecutor.Report;
import hu.elte.txtuml.api.model.backend.collections.InitialsMap;

/**
 * Base class for regions in the model.
 * 
 * <p>
 * <b>Represents:</b> region
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * By the current implementation, <code>Region</code> might not be used directly
 * as all model classes may have at most one region and that is represented by
 * the {@link ModelClass} itself extending <code>Region</code>.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, use its subclasses, like
 * {@link ModelClass}</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public abstract class Region extends StateMachine {

	/**
	 * A static map to cache the initial pseudostates of regions and composite
	 * states.
	 */
	private static InitialsMap initials = InitialsMap.create();

	/**
	 * The current vertex of this region.
	 */
	private Vertex currentVertex;

	/**
	 * @return a unique identifier of this object
	 */
	abstract String getIdentifier();

	/**
	 * The sole constructor of <code>Region</code>.
	 * <p>
	 * Sets the <code>currentVertex</code> field to an instance of this region's
	 * initial pseudostate. The initial pseudostate is a nested class of the
	 * <i>actual region class</i> which is a subclass of
	 * {@link StateMachine.Initial}. The <i>actual region class</i> refers to
	 * the class represented by the {@code java.lang.Class<?>} object which is
	 * returned by the <code>getClass</code> method when this constructor is
	 * run.
	 * <p>
	 * If two or more initial pseudostates exist in this region (two or more
	 * nested classes which extend <code>StateMachine.Initial</code>), then an
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
	 * state class (an object, for which <code>instanceof
	 * StateMachine.State</code> returns true).
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
		if (signal != null) {
			Report.event.forEach(x -> x.processingSignal(this, signal));
		}
		if (findAndExecuteTransition(signal)) {
			callEntryAction();
		}
	}

	/**
	 * Finds and executes a transition from the current vertex if that is
	 * <b>not</b> a choice pseudostate. Does not check if the value of
	 * <code>currentVertex</code> is truly <i>not</i> a choice psuedostate but
	 * should only be called in this case.
	 * 
	 * @param signal
	 *            the received signal
	 * @return <code>true</code> if an applicable transition was found and
	 *         executed, <code>false</code> otherwise
	 * @see Region#findAndExecuteTransitionFromChoice(Signal)
	 */
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
						final Transition tmp = applicableTransition;
						Report.error.forEach(x -> x
								.guardsOfTransitionsAreOverlapping(tmp,
										transition, currentVertex));
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
			// no applicable transition found
			return false;
		}

		executeTransition(applicableTransition);

		if (currentVertex instanceof Choice) {
			findAndExecuteTransitionFromChoice(signal);
		}

		return true;
	}

	/**
	 * Finds and executes a transition from the current vertex if that is a
	 * choice pseudostate. Does not check if the value of
	 * <code>currentVertex</code> is truly a choice psuedostate but should only
	 * be called in this case.
	 * 
	 * @param signal
	 *            the signal which triggered the original transition (the one
	 *            which led to the current choice pseudostate)
	 * @see Region#findAndExecuteTransition(Signal)
	 */
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

						Report.error
								.forEach(x -> x
										.moreThanOneElseTransitionsFromChoice(currentVertex));
						continue;
					}

					elseTransition = transition;

					continue;
				}

				if (applicableTransition != null) {
					// there was already an applicable transition

					final Transition tmp = applicableTransition;
					Report.error.forEach(x -> x
							.guardsOfTransitionsAreOverlapping(tmp, transition,
									currentVertex));

					continue;
				}

				applicableTransition = transition;

				if (!ModelExecutor.Settings.dynamicChecks()) {
					break;
				}
			}
		}
		if (applicableTransition != null) {
			// we found an applicable transition

			executeTransition(applicableTransition);
		} else {
			// no applicable transition

			if (elseTransition != null) {
				// but there is a transition with an else condition

				executeTransition(elseTransition);
			} else {
				// no way to move from choice

				Report.error.forEach(x -> x
						.noTransitionFromChoice(currentVertex));
				return;
			}

		}

		if (currentVertex instanceof Choice) {
			findAndExecuteTransitionFromChoice(signal);
		}
	}

	/**
	 * Execute the specified transition.
	 * 
	 * @param transition
	 *            the transition to be executed
	 */
	private void executeTransition(Transition transition) {
		callExitAction(transition.getSource());
		Report.event.forEach(x -> x.usingTransition(this, transition));
		transition.effect();
		currentVertex = transition.getTarget();
	}

	/**
	 * Checks whether the given signal is <b>not</b> triggering the specified
	 * transition.
	 * 
	 * @param transitionClass
	 *            the class representing the transition to check
	 * @param signal
	 *            the signal to check
	 * @return <code>true</code> if the signal does <b>not</b> trigger the
	 *         specified transition, <code>false</code> otherwise
	 */
	private boolean notApplicableTrigger(Class<?> transitionClass, Signal signal) {
		Trigger trigger = transitionClass.getAnnotation(Trigger.class);
		if ((signal == null) == (trigger == null)
				&& ((signal == null) || (trigger.value().isInstance(signal)))) {
			return false;
		}
		return true;
	}

	/**
	 * Calls the exit action of the current vertex and of all enclosing
	 * composite states one by one going higher in the state hierarchy until it
	 * reaches the specified <code>vertex</code> parameter. It means that the
	 * given parameter must be either the <code>currentVertex</code> or a
	 * composite state which contains (directly or non-directly) the current
	 * vertex. If this condition is not met, calling this method will result in
	 * an unspecified behavior.
	 * 
	 * @param vertex
	 *            the top vertex in the state hierarchy to exit
	 */
	private void callExitAction(Vertex vertex) {
		while (true) {
			currentVertex.exit();
			Report.event.forEach(x -> x.leavingVertex(this, currentVertex));

			if (currentVertex == vertex) {
				break;
			}
			
			Class<?> currentParentState = currentVertex.getClass()
					.getEnclosingClass();

			currentVertex = (Vertex) getNestedClassInstance(currentParentState);
		}
	}

	/**
	 * Calls the entry action of the <code>currentVertex</code>. If it is a
	 * <code>CompositeState</code>, this method finds its initial pseudostate,
	 * enters it, and calls the {@link Region#process(Signal) process} method
	 * with a <code>null</code> parameter to step forward from the initial
	 * pseudostate. This will result in a recursion which ends when it reaches a
	 * vertex that is neither a pseudostate, nor a compositie state.
	 */
	private void callEntryAction() {
		Report.event.forEach(x -> x.enteringVertex(this, currentVertex));
		currentVertex.entry();
		if (currentVertex instanceof CompositeState) {
			Class<? extends Initial> initClass = getInitial(currentVertex
					.getClass());
			if (initClass != null) {

				currentVertex = getNestedClassInstance(initClass);
				Report.event.forEach(x -> x.enteringVertex(this, currentVertex));
				// no entry action needs to be called: initial pseudostates have
				// none

				process(null); // step forward from initial pseudostate
			}
		}
	}

	/**
	 * Returns the initial pseudostate of the region or composite state
	 * represented by <code>forWhat</code>. It uses the {@link Region#initials
	 * initials} static field's value to cache the results.
	 * <p>
	 * If <code>forWhat</code> has no nested class which is a subclass of
	 * {@link StateMachine.Initial Initial}, this method returns
	 * <code>null</code>.
	 * 
	 * 
	 * @param forWhat
	 *            the class representing the region or composite state which's
	 *            initial pseudostate is to be found
	 * @return the class representing the initial pseudostate of
	 *         <code>forWhat</code>, or <code>null</code> if no such pseudostate
	 *         exists
	 * @throws NullPointerException
	 *             if <code>forWhat</code> is <code>null</code>
	 */
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
