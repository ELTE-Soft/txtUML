package hu.elte.txtuml.api.deployment.fmi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.AssociationEnd;

@Target(ElementType.TYPE)
public @interface FMUAssociationEnd {
	Class<? extends AssociationEnd<?>> fmuAssociationEnd();
}
