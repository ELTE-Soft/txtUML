package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.elements.LayoutNonGroupElement;
import hu.elte.txtuml.layout.lang.statements.containers.ShowContainer;

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
