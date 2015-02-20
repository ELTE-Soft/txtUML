package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.containers.InGroupContainer;
import txtuml.api.layout.elements.LayoutElement;
import txtuml.api.layout.elements.LayoutGroup;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(InGroupContainer.class)
public @interface InGroup {
	Class<? extends LayoutElement> value();

	Class<? extends LayoutGroup> group();

}
