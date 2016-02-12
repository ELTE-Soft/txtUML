package hu.elte.txtuml.api.model;

/**
 * Base class for data types in the model.
 * 
 * <p>
 * <b>Represents:</b> a data type
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Inherit from this class to define data types of the model. Fields of the
 * subclass will represent attributes of the data type, while inheritance
 * between subclasses of <code>DataType</code> will represent extending data
 * types in the model. That means, due to the restrictions of Java, each data
 * type can extend at most one data type.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a top level class (not a nested or local class)</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> allowed, only with parameters of types which are
 * subclasses of <code>DataType</code> or primitives (including {@code String} )
 * </li>
 * <li><i>Initialization blocks:</i> allowed, containing only simple assignments
 * to set the default values of its fields</li>
 * <li><i>Fields:</i> allowed, must be final, only of types which are subclasses
 * of <code>DataType</code> or primitives (including {@code String}); they
 * represent attributes of the data type</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed, to represent extending
 * data types</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * class Employee extends DataType {
 * 
 *  public Employee(String name, int id) {
 *    this.name = name;
 *    this.id = id;
 *  }
 * 
 * 	public final String name;
 * 
 * 	public final int id;
 *  
 * 	{@literal //...}
 *  
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class DataType {

}
