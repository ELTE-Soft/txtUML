package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(InitialRealValues.class)
public @interface InitialRealValue {
	String variableName();
	double value();
}

@Target(ElementType.TYPE)
@interface InitialRealValues {
	InitialRealValue[] value();
}
