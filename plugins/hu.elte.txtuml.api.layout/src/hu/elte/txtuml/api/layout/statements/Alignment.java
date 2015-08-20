package hu.elte.txtuml.api.layout.statements;

import hu.elte.txtuml.api.layout.AlignmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alignment {
	AlignmentType value();

}
