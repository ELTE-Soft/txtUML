package hu.elte.txtuml.api.deployment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>
 * Configures the properties of the model executor.
 * The {@link #value} defines the type of the executor. 
 * The default value is the Threaded runtime.
 *
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>@Runtime(RuntimeType.THREADED)</code>
 * </pre>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Runtime {
	RuntimeType value() default RuntimeType.THREADED;
}
