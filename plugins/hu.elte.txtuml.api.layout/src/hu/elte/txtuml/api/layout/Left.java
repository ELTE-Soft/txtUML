package hu.elte.txtuml.api.layout;

import hu.elte.txtuml.api.layout.containers.LeftContainer;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Repeatable(LeftContainer.class)
public @interface Left {
	Class<?> val();

	Class<?> from();

}
