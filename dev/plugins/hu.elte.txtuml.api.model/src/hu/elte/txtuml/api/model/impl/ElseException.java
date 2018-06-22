package hu.elte.txtuml.api.model.impl;

import hu.elte.txtuml.api.model.ImplRelated;

/**
 * Used by {@link hu.elte.txtuml.api.model.StateMachine.Transition#Else} to
 * represent the special boolean value of 'else'.
 * <p>
 * As a member of the {@linkplain hu.elte.txtuml.api.model.impl} package, this
 * type should <b>only be used to implement model executors</b>, not in the
 * model or in external libraries.
 */
@SuppressWarnings("serial")
public class ElseException extends RuntimeException implements ImplRelated {
}
