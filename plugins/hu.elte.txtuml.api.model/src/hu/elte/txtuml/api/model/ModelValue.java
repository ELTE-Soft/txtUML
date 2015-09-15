package hu.elte.txtuml.api.model;

/**
 * Base interface for classes and interfaces that might be used in the model as
 * types of attributes or types of parameters and return values of constructors
 * and operations.
 * 
 * <p>
 * <b>Represents:</b> model objects or primitive values
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Should not be used directly.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 * 
 * @author Gabor Ferenc Kovacs
 * @see ModelClass
 * @see ModelType
 *
 */
public interface ModelValue extends ModelElement {
}
