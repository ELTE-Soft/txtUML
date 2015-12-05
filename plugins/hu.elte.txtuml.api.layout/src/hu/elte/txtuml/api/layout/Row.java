package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.RowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets that some nodes or node groups (
 * {@link #value}) are placed in a row.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RowContainer.class)
public @interface Row {
	Class<?>[] value();

}
