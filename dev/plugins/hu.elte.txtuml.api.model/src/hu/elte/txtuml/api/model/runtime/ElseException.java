package hu.elte.txtuml.api.model.runtime;

/**
 * Used by {@link hu.elte.txtuml.api.model.StateMachine.Transition#Else} to
 * represent the special boolean value of 'else'.
 */
@SuppressWarnings("serial")
public class ElseException extends RuntimeException {
}