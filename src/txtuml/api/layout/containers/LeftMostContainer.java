package txtuml.api.layout.containers;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.statements.LeftMost;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface LeftMostContainer {
	LeftMost[] value() default {};
}