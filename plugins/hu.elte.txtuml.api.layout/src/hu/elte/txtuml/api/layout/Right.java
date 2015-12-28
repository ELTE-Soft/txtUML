package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.RightContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets that a node ({@link #val}) is directly
 * right to another node ({@link #from}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RightContainer.class)
public @interface Right {
	Class<?> val();

	Class<?> from();

}
