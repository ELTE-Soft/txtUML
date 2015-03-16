package hu.elte.txtuml.layout.lang.statements;

import hu.elte.txtuml.layout.lang.AlignmentType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

//TODO doc
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Alignment {
	AlignmentType value();

}
