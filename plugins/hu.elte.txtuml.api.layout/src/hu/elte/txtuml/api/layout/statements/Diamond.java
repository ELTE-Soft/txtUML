package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.elements.LayoutNode;
import hu.elte.txtuml.api.layout.statements.containers.DiamondContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DiamondContainer.class)
public @interface Diamond {
	Class<? extends LayoutNode> top();

	Class<? extends LayoutNode> bottom();

	Class<? extends LayoutNode> right();

	Class<? extends LayoutNode> left();

}
