package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.elements.LayoutAbstractNode;
import hu.elte.txtuml.layout.lang.elements.LayoutNode;
import hu.elte.txtuml.layout.lang.statements.containers.BelowContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(BelowContainer.class)
public @interface Below {
	Class<? extends LayoutAbstractNode> val();

	Class<? extends LayoutNode> from();

}
