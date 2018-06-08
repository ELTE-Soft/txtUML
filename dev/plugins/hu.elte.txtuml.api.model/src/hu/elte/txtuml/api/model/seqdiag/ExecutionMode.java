package hu.elte.txtuml.api.model.seqdiag;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import hu.elte.txtuml.api.model.impl.SequenceDiagramRelated;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
@SequenceDiagramRelated
public @interface ExecutionMode {
	ExecMode value();
	
	ExecMode DEFAULT = ExecMode.STRICT;
}
