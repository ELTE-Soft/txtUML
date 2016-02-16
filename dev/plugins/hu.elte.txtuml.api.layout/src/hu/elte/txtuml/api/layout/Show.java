package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.ShowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets that some nodes and/or links (
 * {@link #value}) should appear on the diagram.
 * <p>
 * <b>Note:</b>
 * <p>
 * Using this statement is only necessary in case of nodes and/or links which
 * are not referenced in any other statement.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ShowContainer.class)
public @interface Show {
	Class<?>[] value();

}
