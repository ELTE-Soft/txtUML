package hu.elte.txtuml.api.model.seqdiag;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

/**
 * Currently only used by plantUML exporter.
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@SequenceDiagramRelated
public @interface FragmentType {
	public CombinedFragmentType value();
}
