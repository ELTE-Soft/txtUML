package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.PriorityContainer;
import txtuml.api.layout.elements.LayoutLink;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(PriorityContainer.class)
public @interface Priority {
	Class<? extends LayoutLink> value();

	int prior();

}
