package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.elements.LayoutNonGroupElement;
import hu.elte.txtuml.api.layout.statements.containers.ShowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(ShowContainer.class)
public @interface Show {
	Class<? extends LayoutNonGroupElement>[] value();

}
