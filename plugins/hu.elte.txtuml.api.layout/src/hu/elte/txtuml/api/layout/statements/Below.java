package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.statements.containers.BelowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(BelowContainer.class)
public @interface Below {
	Class<?> val();

	Class<?> from();

}
