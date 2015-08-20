package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.LinkEnd;
import hu.elte.txtuml.api.layout.elements.LayoutElement;
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
	Class<? extends LayoutElement>[] val();

	Class<? extends LayoutElement>[] from();

	LinkEnd end() default LinkEnd.Default;

}
