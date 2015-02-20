package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.WestContainer;
import txtuml.api.layout.elements.LayoutElement;
import txtuml.api.layout.elements.LayoutNode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(WestContainer.class)
public @interface West {
	Class<? extends LayoutElement> value();

	Class<? extends LayoutNode> from();

}
