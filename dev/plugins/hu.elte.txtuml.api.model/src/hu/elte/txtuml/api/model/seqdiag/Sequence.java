package hu.elte.txtuml.api.model.seqdiag;

import hu.elte.txtuml.api.model.API;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.impl.InteractionRuntime;
import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@SequenceDiagramRelated
public abstract class Sequence {

	/**
	 * This class is not intended to be instantiated.
	 */
	protected Sequence() {
	}

	/**
	 * Asynchronously sends the given signal to the given target from the actor;
	 * also checks that the signal arrives and is processed properly.
	 * <p>
	 * Returns immediately after the model has received and processed the given
	 * signal. That is, the model is in the exact state in which it is left
	 * after processing the given signal.
	 * <p>
	 * As in case of any sequence actions (static methods of this class), it is
	 * ensured that the state of the model does not change until the next
	 * sequence action is reached.
	 * <p>
	 * Note that in case of {@link ExecMode#LENIENT LENIENT} execution mode and
	 * erroneous sequence diagram descriptions, this action may comprise the
	 * processing of multiple signals out of which the given is the last.
	 */
	public static void fromActor(Signal signal, ModelClass target) {
		InteractionRuntime.current().messageFromActor(signal, target);
	}

	/**
	 * Tells the sequence diagram executor that the given signal has to be sent
	 * to the given target from the given sender.
	 * <p>
	 * Returns immediately after the model has received and processed the given
	 * signal. That is, the model is in the exact state in which it is left
	 * after processing the given signal.
	 * <p>
	 * As in case of any sequence actions (static methods of this class), it is
	 * ensured that the state of the model does not change until the next
	 * sequence action is reached.
	 * <p>
	 * Note that in case of {@link ExecMode#LENIENT LENIENT} execution mode and
	 * erroneous sequence diagram descriptions, this action may comprise the
	 * processing of multiple signals out of which the given is the last.
	 */
	public static void send(ModelClass sender, Signal signal, ModelClass target) {
		InteractionRuntime.current().message(sender, signal, target);
	}

	/**
	 * The given operands are executed in an arbitrary order, their execution
	 * may even overlap. However, it is still insured that during the execution
	 * of the sequence diagram, only one of the operands is running at any given
	 * time. The executor may only switch to another operand when the currently
	 * executed has reached a sequence action (a static method of this class).
	 */
	public static void par(Interaction... operands) {
		InteractionRuntime.current().par(operands);
	}

	/**
	 * Verifies that the {@code ModelClass} instance is currently in the given
	 * state. If the assertion fails, an error will be added to the execution
	 * results.
	 * <p>
	 * Note that the state of the model <b>does not</b> change as a result of
	 * calling this method.
	 */
	public static void assertState(ModelClass instance, Class<?> state) {
		InteractionRuntime.current().assertState(instance, state);
	}

	/**
	 * Logs the given message.
	 */
	public static void log(String message) {
		API.log(message);
	}

}
