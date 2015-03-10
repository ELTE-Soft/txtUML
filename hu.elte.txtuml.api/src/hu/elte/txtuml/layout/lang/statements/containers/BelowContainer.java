package hu.elte.txtuml.layout.lang.statements.containers;

import hu.elte.txtuml.layout.lang.statements.Below;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface BelowContainer {
	Below[] value() default {};
}