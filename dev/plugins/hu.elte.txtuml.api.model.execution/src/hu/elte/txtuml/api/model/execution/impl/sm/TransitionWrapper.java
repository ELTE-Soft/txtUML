package hu.elte.txtuml.api.model.execution.impl.sm;

import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.Trigger;
import hu.elte.txtuml.api.model.Trigger.AnyPort;
import hu.elte.txtuml.api.model.impl.ElseException;
import hu.elte.txtuml.api.model.impl.Wrapper;

public interface TransitionWrapper extends Wrapper<Transition> {

	VertexWrapper getTarget();

	/**
	 * Checks whether the given signal is triggering the wrapped transition.
	 * 
	 * @param signal
	 *            the signal to check
	 * @param port
	 *            the port through which the signal arrived (might be
	 *            {@code null} in case the signal did not arrive through a port)
	 * @return true if the signal does triggers the specified transition, false
	 *         otherwise
	 */
	boolean applicableTrigger(Signal signal, Port<?, ?> port);

	default boolean checkGuard() throws ElseException {
		return getWrapped().guard();
	}

	default void performEffect() {
		getWrapped().effect();
	}

	// create methods

	static TransitionWrapper create(Transition wrapped, VertexWrapper target) {
		Trigger trigger = wrapped.getClass().getAnnotation(Trigger.class);
		if (trigger == null) {
			return createUnlabeled(wrapped, target);
		} else {
			return createLabeled(wrapped, target, trigger.value(), trigger.port());
		}
	}

	static TransitionWrapper createUnlabeled(Transition wrapped, VertexWrapper target) {
		return new TransitionWrapper() {

			@Override
			public Transition getWrapped() {
				return wrapped;
			}

			@Override
			public VertexWrapper getTarget() {
				return target;
			}

			@Override
			public boolean applicableTrigger(Signal signal, Port<?, ?> port) {
				return signal == null;
			}

		};
	}

	static TransitionWrapper createLabeled(Transition wrapped, VertexWrapper target, Class<? extends Signal> trigger,
			Class<? extends Port<?, ?>> portOfTrigger) {
		return new TransitionWrapper() {

			@Override
			public Transition getWrapped() {
				return wrapped;
			}

			@Override
			public VertexWrapper getTarget() {
				return target;
			}

			@Override
			public boolean applicableTrigger(Signal signal, Port<?, ?> portInstance) {
				return trigger.isInstance(signal)
						&& (portOfTrigger == AnyPort.class || portOfTrigger.isInstance(portInstance));
			}

		};
	}

}
