package hu.elte.txtuml.utils;

import java.lang.reflect.InvocationTargetException;


/**
 * Unchecked version of {@link java.lang.reflect.InvocationTargetException}.
 * <p>
 * Used by {@link InstanceCreator}.
 */
@SuppressWarnings("serial")
public class RuntimeInvocationTargetException extends
		RuntimeException {

	public RuntimeInvocationTargetException(String message, InvocationTargetException ex) {
		super(message, ex.getCause());
	}

}

