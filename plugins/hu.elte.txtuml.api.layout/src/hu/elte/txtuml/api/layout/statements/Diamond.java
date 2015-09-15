package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.statements.containers.DiamondContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DiamondContainer.class)
public @interface Diamond {
	Class<?> top();

	Class<?> bottom();

	Class<?> right();

	Class<?> left();

}
