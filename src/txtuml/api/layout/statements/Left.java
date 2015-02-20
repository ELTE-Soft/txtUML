package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.LeftContainer;
import txtuml.api.layout.elements.LayoutAbstractNode;
import txtuml.api.layout.elements.LayoutNode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LeftContainer.class)
public @interface Left {
	Class<? extends LayoutAbstractNode> value();

	Class<? extends LayoutNode> from();

}
