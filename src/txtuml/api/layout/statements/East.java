package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.EastContainer;
import txtuml.api.layout.elements.LayoutElement;
import txtuml.api.layout.elements.LayoutNode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(EastContainer.class)
public @interface East {
	Class<? extends LayoutElement> value();

	Class<? extends LayoutNode> from();

}
