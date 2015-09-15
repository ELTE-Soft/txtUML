package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.statements.containers.NorthContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(NorthContainer.class)
public @interface North {
	Class<?>[] val();

	Class<?>[] from();

	LinkEnd end() default LinkEnd.Default;

}
