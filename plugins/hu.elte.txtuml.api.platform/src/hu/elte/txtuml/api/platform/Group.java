package hu.elte.txtuml.api.platform;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.platform.GroupContainer;

/**
 * <p>
 * Configures which classes go to the same thread pool. It defines the number of
 * threads according to a linear function parameterized by the number of created
 * objects. <b>max</b> defines how many threads could be at most.
 * <p>
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>@Group(contains = {A.class,B.class}, max = 10, constant = 5, gradient = 0.1)</code>
 * </pre>
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupContainer.class)
public @interface Group {
	Class<? extends ModelClass>[] contains();

	double gradient() default 0;

	int constant() default 1;

	int max() default 1;
}