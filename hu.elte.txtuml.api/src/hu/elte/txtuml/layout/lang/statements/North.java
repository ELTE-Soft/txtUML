package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.LinkEnd;
import hu.elte.txtuml.layout.lang.elements.LayoutElement;
import hu.elte.txtuml.layout.lang.statements.containers.NorthContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO doc
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(NorthContainer.class)
public @interface North {
	Class<? extends LayoutElement>[] val();

	Class<? extends LayoutElement>[] from();

	LinkEnd end() default LinkEnd.Default;

}
