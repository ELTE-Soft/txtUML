package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.PriorityContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which sets the priority ({@link #prior}) of a link
 * ({@link #val}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(PriorityContainer.class)
public @interface Priority {
	Class<?>[] val();

	int prior();

}
