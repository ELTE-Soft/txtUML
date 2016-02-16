package hu.elte.txtuml.api.layout;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A diagram layout statement which can be applied to a node group and sets the
 * alignment of its elements ({@link #value}).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alignment {
	AlignmentType value();

}
