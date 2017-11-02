package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that the annotated element (type, field, method, constructor or super
 * type declaration) is not part of the model therefore it is not validated or
 * exported and is invisible from other (non-external) parts of the model. It
 * may contain any valid Java code, its correctness is the user's
 * responsibility.
 * <p>
 * In case of super types (super interfaces, in most cases), this annotation can
 * be used to suppress errors about external super types which would not be
 * allowed to be used in the model in any other way.
 * <p>
 * <b>Example:</b>
 * <p>
 * 
 * <pre>
 * <code>
 * class Glue extends ModelClass implements {@code @External} MyJavaInterface {
 * 	// ...  
 * }
 * </code>
 * </pre>
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @see ExternalBody
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.TYPE, ElementType.TYPE_USE, ElementType.FIELD, ElementType.METHOD, ElementType.CONSTRUCTOR })
@Documented
@Inherited
public @interface External {

}
