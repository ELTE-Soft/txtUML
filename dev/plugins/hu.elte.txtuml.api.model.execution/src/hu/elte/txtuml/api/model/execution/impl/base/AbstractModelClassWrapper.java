package hu.elte.txtuml.api.model.execution.impl.base;

import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Navigable;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelClass.Status;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.execution.impl.assoc.AssociationEndWrapper;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultipleContainerException;
import hu.elte.txtuml.api.model.execution.impl.assoc.MultiplicityException;
import hu.elte.txtuml.api.model.execution.impl.sm.StateMachineParser;
import hu.elte.txtuml.api.model.execution.impl.sm.TransitionWrapper;
import hu.elte.txtuml.api.model.execution.impl.sm.VertexWrapper;
import hu.elte.txtuml.api.model.runtime.ElseException;
import hu.elte.txtuml.api.model.runtime.ModelClassWrapper;

/**
 * Abstract base class for {@link ModelClassWrapper} implementations.
 */
public abstract class AbstractModelClassWrapper extends AbstractSignalTargetWrapper<ModelClass>
		implements ModelClassWrapper {

	private final ModelExecutorThread thread;
	private final String identifier;

	private volatile String name;

	/**
	 * May only be accessed from the owner thread.
	 */
	private Status status;

	/**
	 * May only be accessed from the owner thread.
	 */
	private VertexWrapper currentVertex;

	public AbstractModelClassWrapper(ModelClass wrapped, ModelExecutorThread thread, String identifier) {
		super(wrapped);
		this.thread = thread;
		this.identifier = identifier;
		this.name = getTypeOfWrapped().getSimpleName() + "@" + identifier;
		this.currentVertex = StateMachineParser.parse(wrapped);

		if (currentVertex == null) {
			this.status = Status.FINALIZED;
		} else {
			this.status = Status.READY;
		}
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Status getStatus() {
		return status;
	}

	public VertexWrapper getCurrentVertexWrapper() {
		return currentVertex;
	}

	protected void setStatus(Status status) {
		this.status = status;
	}

	@Override
	public ModelExecutorThread getThread() {
		return thread;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}

	/**
	 * Returns a short string representation of the described object.
	 * <p>
	 * The default implementation returns the name of the described object.
	 */
	@Override
	public String getStringRepresentation() {
		return name;
	}

	@Override
	@SuppressWarnings("unchecked")
	public <S extends Signal> S getCurrentTrigger() {
		return (S) getThread().getCurrentTriggeringSignal();
	}

	@Override
	public <T extends ModelClass, C extends GeneralCollection<T>, AE extends AssociationEnd<C> & Navigable> C navigateThroughAssociation(
			Class<AE> otherEnd) {
		return getAssoc(otherEnd).getCollection();
	}

	@Override
	public void send(Signal signal) {
		getThread().send(signal, this);
	}
	
	@Override
	public void sent(Signal signal)
	{
		getThread().sent(signal,this);
	}
	
	public void traceSender(Signal signal, AbstractModelClassWrapper sender)
	{
		getRuntime().trace(x -> x.sendingSignal(sender.getWrapped(), signal));
	}

	@Override
	public void send(Signal signal, AbstractPortWrapper sender) {
		getThread().send(signal, sender, this);
	}

	@Override
	public void receive(Signal signal) {
		process(signal, null);
	}

	/**
	 * This implementation is <b>not</b> thread-safe. Should only be called from
	 * the owner thread.
	 */
	@Override
	public void receive(Signal signal, AbstractPortWrapper sender) {
		process(signal, sender.getWrapped());
	}

	/**
	 * Processes a signal and changes to another state if necessary (possibly
	 * after entering and exiting multiple vertices). Also executes every
	 * required exit and entry actions, transition effects. Stops after entering
	 * the first state which is not a composite state (and as a state, also not
	 * an initial or choice pseudostate).
	 * <p>
	 * Is <b>not</b> thread-safe. Should only be called from the owner thread.
	 * 
	 * @param signal
	 *            the signal object to be processed
	 * @param port
	 *            the port through which the signal arrived (might be
	 *            {@code null} in case the signal did not arrive through a port)
	 * @throws NullPointerException
	 *             if {@code signal} is {@code null}
	 */
	protected void process(Signal signal, Port<?, ?> port) {
		getThread().setCurrentTriggeringSignal(signal);

		Status currentStatus = getStatus();
		if (currentStatus != Status.ACTIVE) {
			if (currentStatus == Status.DELETED) {
				getRuntime().warning(x -> x.signalArrivedToDeletedObject(getWrapped(), signal));
				return;
			}
		} else {
			if (signal != null) {
				getRuntime().trace(x -> x.processingSignal(getWrapped(), signal));
			}

			if (findAndExecuteTransition(signal, port)) {
				callEntryAction();
				return;
			}
		}

		if (signal != null) {
			getRuntime().warning(x -> x.lostSignalAtObject(signal, getWrapped()));
		} else {
			getRuntime().error(x -> x.missingInitialTransition(currentVertex.getWrapped()));
		}

	}

	/**
	 * Finds and executes a transition from the current vertex if that is
	 * <b>not</b> a choice pseudostate. Does not check if the value of
	 * {@code currentVertex} is truly <i>not</i> a choice pseudostate but should
	 * only be called in this case.
	 * 
	 * @param signal
	 *            the triggering signal instance
	 * @param port
	 *            the port through which the signal arrived (might be
	 *            {@code null} in case the signal did not arrive through a port)
	 * @return true if an applicable transition was found and executed, false
	 *         otherwise
	 */
	private boolean findAndExecuteTransition(Signal signal, Port<?, ?> port) {
		VertexWrapper fromState = null;
		TransitionWrapper applicableTransition = null;

		for (VertexWrapper examined = currentVertex; examined != null; examined = examined.getContainer()) {

			for (TransitionWrapper transition : examined.getOutgoings()) {

				if (transition.notApplicableTrigger(signal, port)) {
					continue;
				}

				try {
					if (!transition.checkGuard()) {
						continue;
					}
				} catch (ElseException e) {
					getRuntime().error(x -> x.elseGuardFromNonChoiceVertex(transition.getWrapped()));
					continue;
				}

				if (applicableTransition != null) {
					// there was already an applicable transition

					final Transition tmp = applicableTransition.getWrapped();
					getRuntime().error(x -> x.guardsOfTransitionsAreOverlapping(tmp, transition.getWrapped(),
							currentVertex.getWrapped()));
					continue;
				}

				fromState = examined;
				applicableTransition = transition;

				if (!getRuntime().dynamicChecks()) {
					break;
				}

			}
		}

		if (applicableTransition == null) {
			// no applicable transition found
			return false;
		}

		executeTransition(fromState, applicableTransition);

		if (currentVertex.isChoice()) {
			getRuntime().trace(x -> x.enteringVertex(getWrapped(), currentVertex.getWrapped()));
			findAndExecuteTransitionFromChoice();
		}

		return true;
	}

	/**
	 * Finds and executes a transition from the current vertex if that is a
	 * choice pseudostate. Does not check if the value of {@code currentVertex}
	 * is truly a choice pseudostate but should only be called in this case.
	 */
	private void findAndExecuteTransitionFromChoice() {
		TransitionWrapper applicableTransition = null;
		TransitionWrapper elseTransition = null;

		for (TransitionWrapper transition : currentVertex.getOutgoings()) {
			try {
				if (!transition.checkGuard()) { // check guard
					continue;
				}
			} catch (ElseException e) {
				// transition with else condition

				if (elseTransition != null) {
					// there was already a transition with an else condition

					getRuntime().error(x -> x.moreThanOneElseTransitionsFromChoice(currentVertex.getWrapped()));
					continue;
				}

				elseTransition = transition;

				continue;
			}

			if (applicableTransition != null) {
				// there was already an applicable transition

				final Transition tmp = applicableTransition.getWrapped();
				getRuntime().error(x -> x.guardsOfTransitionsAreOverlapping(tmp, transition.getWrapped(),
						currentVertex.getWrapped()));

				continue;
			}

			applicableTransition = transition;

			if (!getRuntime().dynamicChecks()) {
				break;
			}
		}
		if (applicableTransition != null) {
			// we found an applicable transition

			executeTransition(currentVertex, applicableTransition);
		} else {
			// no applicable transition

			if (elseTransition != null) {
				// but there is a transition with an else condition

				executeTransition(currentVertex, elseTransition);
			} else {
				// no way to move from choice

				getRuntime().error(x -> x.noTransitionFromChoice(currentVertex.getWrapped()));
				return;
			}
		}

		if (currentVertex.isChoice()) {
			findAndExecuteTransitionFromChoice();
		}
	}

	/**
	 * Executes the specified transition.
	 * 
	 * @param transition
	 *            the transition to be executed
	 */
	private void executeTransition(VertexWrapper fromVertex, TransitionWrapper transition) {
		callExitAction(fromVertex);
		getRuntime().trace(x -> x.usingTransition(getWrapped(), transition.getWrapped()));
		transition.performEffect();
		currentVertex = transition.getTarget();
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
	private void callExitAction(VertexWrapper vertex) {
		while (true) {
			currentVertex.performExit();
			getRuntime().trace(x -> x.leavingVertex(getWrapped(), currentVertex.getWrapped()));

			if (currentVertex == vertex) {
				break;
			}

			currentVertex = currentVertex.getContainer();
		}
	}

	/**
	 * Calls the entry action of the <code>currentVertex</code>. If it is a
	 * <code>CompositeState</code>, this method finds its initial pseudostate,
	 * enters it, and calls the {@link #process(Signal) process} method with a
	 * <code>null</code> parameter to step forward from the initial pseudostate.
	 * This will result in a recursion which ends when it reaches a vertex that
	 * is neither a pseudostate, nor a composite state.
	 */
	private void callEntryAction() {
		getRuntime().trace(x -> x.enteringVertex(getWrapped(), currentVertex.getWrapped()));
		currentVertex.performEntry();
		if (currentVertex.isComposite()) {
			currentVertex = currentVertex.getInitialOfSubSM();
			getRuntime().trace(x -> x.enteringVertex(getWrapped(), currentVertex.getWrapped()));
			// no entry action needs to be called: initial pseudostates have
			// none

			process(null, null); // step forward from initial pseudostate
		}
	}

	/**
	 * Checks whether this model object is in {@link Status#DELETED}.
	 */
	public abstract boolean isDeleted();

	/**
	 * Returns the wrapper of the requested association end which has the
	 * collection containing the objects in association with the wrapped
	 * ModelClass instance and being on the specified opposite association end.
	 */
	public abstract <T extends ModelClass, C extends GeneralCollection<T>> AssociationEndWrapper<T, C> getAssoc(
			Class<? extends AssociationEnd<C>> otherEnd);

	/**
	 * Checks if the specified object is an element of the collection containing
	 * the objects in association with the wrapped ModelClass instance and being
	 * on the specified opposite association end.
	 */
	public abstract <T extends ModelClass, C extends GeneralCollection<T>> boolean hasAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object);

	/**
	 * Adds the specified object to the collection containing the objects in
	 * association with the wrapped ModelClass instance and being on the
	 * specified opposite association end.
	 */
	public abstract <T extends ModelClass, C extends GeneralCollection<T>> void addToAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object)
			throws MultiplicityException, MultipleContainerException;

	/**
	 * Removes the specified object from the collection containing the objects
	 * in association with the wrapped ModelClass instance and being on the
	 * specified opposite association end.
	 */
	public abstract <T extends ModelClass, C extends GeneralCollection<T>> void removeFromAssoc(
			Class<? extends AssociationEnd<C>> otherEnd, T object);

	@Override
	public abstract void delete();

	@Override
	public abstract <P extends Port<?, ?>> P getPortInstance(Class<P> portType);

}
