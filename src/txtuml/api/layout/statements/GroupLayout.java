package txtuml.api.layout.statements;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import txtuml.api.layout.GroupLayoutType;
import txtuml.api.layout.containers.GroupLayoutContainer;
import txtuml.api.layout.elements.LayoutGroup;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(GroupLayoutContainer.class)
public @interface GroupLayout {
	Class<? extends LayoutGroup> value();

	GroupLayoutType layout();

}
