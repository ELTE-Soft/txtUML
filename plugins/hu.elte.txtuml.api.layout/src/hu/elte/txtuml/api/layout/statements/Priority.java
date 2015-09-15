package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.elements.LayoutAbstractLink;
import hu.elte.txtuml.api.layout.statements.containers.PriorityContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(PriorityContainer.class)
public @interface Priority {
	Class<? extends LayoutAbstractLink>[] val();

	int prior();

}
