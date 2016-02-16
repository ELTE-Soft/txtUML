package hu.elte.txtuml.api.layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.layout.containers.DiamondContainer;

/**
 * A diagram layout statement which sets that four nodes ({@link #top},
 * {@link #bottom}, {@link #right}, {@link #left}) are placed in a diamond
 * shape.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DiamondContainer.class)
public @interface Diamond {
	Class<?> top();

	Class<?> bottom();

	Class<?> right();

	Class<?> left();

}
