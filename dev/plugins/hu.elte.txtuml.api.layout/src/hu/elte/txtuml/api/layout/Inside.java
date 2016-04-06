package hu.elte.txtuml.api.layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.layout.Diagram.Box;

/**
 * A diagram layout statement which sets that the annotated {@link Box}
 * layout class is the diagram inside {@link #value} .
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Inside {
	Class<?> value();

}