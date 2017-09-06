package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Repeatable(InitialIntegerValues.class)
public @interface InitialIntegerValue {
	String variableName();
	int value();
}

@Target(ElementType.TYPE)
@interface InitialIntegerValues {
	InitialIntegerValue[] value();
}
