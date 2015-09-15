package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.statements.containers.RightContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(RightContainer.class)
public @interface Right {
	Class<?> val();

	Class<?> from();

}
