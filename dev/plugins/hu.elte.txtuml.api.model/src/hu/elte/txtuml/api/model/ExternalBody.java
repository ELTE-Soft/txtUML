package hu.elte.txtuml.api.model;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marks that the body of the annotated method or constructor is not part of the
 * model and therefore it is not validated or exported. It may contain any valid
 * Java code, its correctness is the user's responsibility. However, in contrast
 * to {@link External}, the signature of the annotated method or constructor is
 * part of the model. It is validated, exported and can be called from other
 * (non-external) parts of the model.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 * 
 * @see External
 */
@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.METHOD, ElementType.CONSTRUCTOR })
@Documented
public @interface ExternalBody {

}
