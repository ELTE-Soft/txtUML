package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.elements.LayoutNode;
import hu.elte.txtuml.api.layout.statements.containers.AboveContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AboveContainer.class)
public @interface Above {
	Class<? extends LayoutNode> val();

	Class<? extends LayoutNode> from();

}
