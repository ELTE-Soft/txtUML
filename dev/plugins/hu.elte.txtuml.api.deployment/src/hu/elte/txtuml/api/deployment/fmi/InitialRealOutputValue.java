package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
public @interface InitialRealOutputValue {
	String variableName();
	double value();
}
