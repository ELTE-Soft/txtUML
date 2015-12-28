package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.LeftContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets that a node ({@link #val}) is directly
 * left to another node ({@link #from}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LeftContainer.class)
public @interface Left {
	Class<?> val();

	Class<?> from();

}
