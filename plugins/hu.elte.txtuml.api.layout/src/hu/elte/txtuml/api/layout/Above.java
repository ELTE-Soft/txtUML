package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.AboveContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(AboveContainer.class)
public @interface Above {
	Class<?> val();

	Class<?> from();

}
