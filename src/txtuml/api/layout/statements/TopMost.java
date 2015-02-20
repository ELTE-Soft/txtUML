package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.TopMostContainer;
import txtuml.api.layout.elements.LayoutAbstractNode;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(TopMostContainer.class)
public @interface TopMost {
	Class<? extends LayoutAbstractNode> value();

}
