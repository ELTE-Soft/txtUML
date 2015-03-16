package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.statements.containers.DiamondContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO doc
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(DiamondContainer.class)
public @interface Diamond {
	Class<? extends LayoutNode> top();

	Class<? extends LayoutNode> bottom();

	Class<? extends LayoutNode> right();

	Class<? extends LayoutNode> left();

}
