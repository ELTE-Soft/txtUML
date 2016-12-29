package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.Signal;

@Target(ElementType.TYPE)
public @interface FMUOutput {
	Class<? extends Signal> outputSignal();
}
