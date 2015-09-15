package hu.elte.txtuml.api.layout.statements.containers;

import hu.elte.txtuml.api.layout.statements.Diamond;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The container for repeatable annotation {@link Diamond}.
 * 
 * @author Gabor Ferenc Kovacs
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface DiamondContainer {
	Diamond[] value() default {};
}
