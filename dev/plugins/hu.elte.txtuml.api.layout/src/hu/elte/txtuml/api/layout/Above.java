package hu.elte.txtuml.api.layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.layout.containers.AboveContainer;

/**
 * A diagram layout statement which sets that a node ({@link #val}) is directly
 * above another node ({@link #from}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AboveContainer.class)
public @interface Above {
	Class<?> val();

	Class<?> from();

}
