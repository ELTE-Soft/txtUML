package hu.elte.txtuml.api;

/**
 * Base class of txtUML models.
 * 
 * <p>
 * <b>Represents:</b> model
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Every element of the model must be nested classes of the same subclass of
 * <code>Model</code>.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>none</li>
 * </ul>
 * <p>
 * <b>Subtype restrictions:</b>
 * <ul>
 * <li><i>Be abstract:</i> disallowed</li>
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> disallowed</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> allowed, both static and non-static, to represent
 * elements of the model; all nested classes must be subclasses of an API class
 * representing a certain model element</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * <b>Example:</b>
 * 
 * <pre>
 * <code>
 * public class ExampleModel extends Model {
 * 	class ExampleClass1 extends ModelClass {
 *  		//...
 * 	}
 * 
 * 	class ExampleClass2 extends ModelClass {
 *  		//...
 * 	}
 *  
 * 	public static class ExampleSignal extends Signal {}
 *  
 * 	//...
 * 
 * }
 * </code>
 * </pre>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see ModelClass
 * @see Association
 * @see Signal
 */
public class Model extends Action {

	/**
	 * Sole constructor of <code>Model</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Protected because this class is intended to be inherited from but not
	 * instantiated. However, <code>Model</code> has to be a non-abstract class
	 * to make sure that it is instantiatable when that is needed for the API or
	 * the model exportation.
	 */
	protected Model() {
	}

}