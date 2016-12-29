package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.ModelClass;

@Target(ElementType.TYPE)
public @interface FMU {
	Class<? extends ModelClass> fmuClass();
}
