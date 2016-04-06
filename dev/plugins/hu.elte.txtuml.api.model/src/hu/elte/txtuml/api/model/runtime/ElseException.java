package hu.elte.txtuml.api.model.runtime;

/**
 * Used by {@link hu.elte.txtuml.api.model.StateMachine.Transition#Else} to
 * represent the special boolean value of 'else'.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
@SuppressWarnings("serial")
public class ElseException extends RuntimeException {
}