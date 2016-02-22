package hu.elte.txtuml.api.model.external;

/**
 * Base type for external classes in the model. External classes are those
 * which's implementation is not part of the model, and from the scope of the
 * model they function as black boxes. They might be used to bring external
 * features into the model or to communicate with components of the program that
 * are not part of the model.
 * 
 * <p>
 * <b>Represents:</b> external class
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * From outside the model, use external classes according to the rules and
 * conventions of the Java language. The only restriction is that an external
 * class may not be abstract because that could cause problems during model
 * execution or exportation. From inside the model, use them as any passive
 * model class (that is, one that may not react to any asynchronous events).
 * Call its methods as operations or use its fields as attributes. All its
 * fields used from inside the model must be of a type extending
 * {@link hu.elte.txtuml.api.model.ModelClass ModelClass} or primitives
 * (including <code>String</code>). Also all parameter and return types of
 * methods called from inside the model must extend <code>ModelClass</code> or
 * be primitives (including <code>String</code>).
 * <p>
 * As the txtUML API uses its own thread for model execution, external classes
 * probably need synchronization.
 * <p>
 * When a method of an external class is called from the model (on the model's
 * executor thread), it can call back to methods of model classes, get or set
 * fields before it returns. However, if it is called from a different thread,
 * it may only communicate with the model through signals or by creating new
 * model objects, as it might be done from anywhere outside the model. For
 * details about managing the model from outside, see the documentation of
 * {@link hu.elte.txtuml.api.model.Model Model}.
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
 * <li><i>Generic parameters:</i> allowed</li>
 * <li><i>Constructors:</i> allowed</li>
 * <li><i>Initialization blocks:</i> allowed</li>
 * <li><i>Fields:</i> allowed</li>
 * <li><i>Methods:</i> allowed</li>
 * <li><i>Nested interfaces:</i> allowed</li>
 * <li><i>Nested classes:</i> allowed</li>
 * <li><i>Nested enums:</i> allowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed
 * </ul>
 * 
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model Model} for an
 * overview on modeling in JtxtUML.
 */
public interface ExternalClass extends ExternalType {

}
