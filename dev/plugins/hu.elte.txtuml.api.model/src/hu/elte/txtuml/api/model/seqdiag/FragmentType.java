package hu.elte.txtuml.api.model.seqdiag;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
public @interface FragmentType {
	public CombinedFragmentType value();
}
