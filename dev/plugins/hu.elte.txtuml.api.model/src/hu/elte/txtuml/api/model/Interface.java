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
 * {@code reception} (more precisely, its name must equal to the constant
 * {@link Interface#RECEPTION_NAME}). This reception will only accept signals
 * that are of a subtype of the given signal type (or all signals if the
 * parameters's type is {@code Signal}).
 * <p>
 * For specific use cases when an empty interface is needed, the
 * {@link Interface.Empty} type is present in the API.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
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
 * <li><i>Generic parameters:</i> disallowed</li>
 * <li><i>Constructors:</i> disallowed</li>
 * <li><i>Initialization blocks:</i> disallowed</li>
 * <li><i>Fields:</i> disallowed</li>
 * <li><i>Methods:</i> allowed, to represent receptions</li>
 * <li><i>Nested interfaces:</i> disallowed</li>
 * <li><i>Nested classes:</i> disallowed</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> allowed
 * </ul>
 * 
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model Model} for an
 * overview on modeling in JtxtUML.
 *
 * @see Interface.Empty
 */
public interface Interface {

	/**
	 * Mandatory name of every reception defined on an interface.
	 * <p>
	 * See the documentation of {@link Interface} for more information.
	 */
	@External
	public static final String RECEPTION_NAME = "reception";

	/**
	 * An empty model interface provided in the API for convenience.
	 * 
	 * <p>
	 * <b>Represents:</b> an empty interface
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 *
	 * Can be used whenever an interface is needed in the model, for example, as
	 * the provided or required interface of a port which means that no
	 * communication is possible on the specific port in the specific direction
	 * as this interface is empty, having no receptions.
	 * <p>
	 * Using {@code Empty} when an empty interface is needed is <i>not
	 * mandatory, but <b>recommended</b></i> to support code consistency and
	 * understandability, unless the interface which is to be used has a special
	 * meaning in the specific context.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Define subtype:</i> disallowed</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link hu.elte.txtuml.api.model.Model Model} for
	 * an overview on modeling in JtxtUML.
	 *
	 */
	public interface Empty extends Interface {
	}

}
