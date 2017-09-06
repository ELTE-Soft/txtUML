package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(InitialBooleanValues.class)
public @interface InitialBooleanValue {
	String variableName();
	boolean value();
}

@Target(ElementType.TYPE)
@interface InitialBooleanValues {
	InitialBooleanValue[] value();
}
