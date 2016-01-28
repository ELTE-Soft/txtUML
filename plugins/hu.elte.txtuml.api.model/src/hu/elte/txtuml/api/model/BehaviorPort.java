package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * A runtime annotation to mark behavior ports.
 * 
 * <p>
 * <b>Represents:</b> that the annotated port is a behavior port
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Annotate a port (a subclass of {@link Port}) with this annotation to show
 * that the specific port is behavior port which means that it is connected to
 * the state machine of its containing model object on one side.
 * 
 * <p>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
public @interface BehaviorPort {
}
