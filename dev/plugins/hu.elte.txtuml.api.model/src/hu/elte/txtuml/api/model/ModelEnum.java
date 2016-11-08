package hu.elte.txtuml.api.model;

/**
 * Marker interface for enums in the model.
 * 
 * <p>
 * <b>Represents:</b> model enum
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Inherit an enum from this interface to implement enums of the model. Model
 * enums may only have constants, no methods, fields or constructors might be
 * defined.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level enum (not a nested or local enum)</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * enum MyEnum extends ModelEnum {
 *   A, B, C
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public interface ModelEnum {

}
