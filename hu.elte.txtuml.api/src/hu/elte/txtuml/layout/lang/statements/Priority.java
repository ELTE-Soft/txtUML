package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.elements.LayoutLink;
import hu.elte.txtuml.layout.lang.statements.containers.PriorityContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(PriorityContainer.class)
public @interface Priority {
	Class<? extends LayoutLink> val();

	int prior();

}
