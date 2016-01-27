package hu.elte.txtuml.api.model;

/**
 * Base type for interfaces in the model.
 * 
 * <p>
 * <b>Represents:</b> interface
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Create a Java interface that extends {@code Interface}.
 * 
 * <p>
 * <i>Receptions:</i> To add a reception to this interface, define an abstract
 * (non-default) Java method on it with {@code void} return type and exactly one
 * parameter the type of which must be a subtype of {@link Signal} (or
 * {@code Signal} itself). This reception method must also have the name
 * {@code reception} (more precisely, must equal to the constant
 * {@link Interface#RECEPTION_NAME}). This reception will only accept signals
 * that are of a subtype of the given signal type (or all signals if
 * the parameters's type is {@code Signal}).
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> allowed
 * <p>
 * <b>Subtype requirements:</b>
 * <ul>
 * <li>must be a Java interface</li>
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
 *
 * @author Gabor Ferenc Kovacs
 *
 */
public interface Interface extends ModelElement {

	/**
	 * Mandatory name of every reception defined on an interface.
	 * <p>
	 * See the documentation of {@link Interface} for more information.
	 */
	public static final String RECEPTION_NAME = "reception";

}
