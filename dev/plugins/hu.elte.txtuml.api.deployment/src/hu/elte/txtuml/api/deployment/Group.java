package hu.elte.txtuml.api.deployment;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.deployment.GroupContainer;

/**
 * <p>
 * Configures which classes go to the same thread pool. It defines the number of
 * threads according to a linear function parameterized by the number of created
 * objects. {@link #max} defines how many threads could be at most.
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>@Group(contains = {A.class,B.class}, rate=0.5)</code>
 * </pre>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupContainer.class)
public @interface Group {
	Class<? extends ModelClass>[] contains();
	
	double rate() default 1.0;
	
}