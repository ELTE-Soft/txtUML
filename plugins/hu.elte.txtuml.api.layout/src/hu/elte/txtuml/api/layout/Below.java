package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.BelowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets that a node ({@link #val}) is directly
 * below another node ({@link #from}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(BelowContainer.class)
public @interface Below {
	Class<?> val();

	Class<?> from();

}
