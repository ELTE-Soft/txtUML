package hu.elte.txtuml.api;

/**
 * Base class for events in the model.
 * 
 * <p>
 * <b>Represents:</b> event
 * <p>
 * <b>Usage:</b>
 * <p>
 * As event is just a base class for events, it is not used directly to define
 * certain kinds of events.
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed
 * <li><i>Define subtype:</i> disallowed, inherit from its predefined
 * subclasses, like {@link Signal}
 * </ul>
 * 
 * <p>
 * <b>Note:</b> the only event type currently implemented in txtUML is a signal
 * event, which is represented by referencing its signal directly.
 * <p>
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 * @see Signal
 * 
 */
public class Event implements ModelElement, ModelIdentifiedElement {

	/**
	 * Sole constructor of <code>Event</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is only used by the user
	 * through its subclasses.
	 */
	Event() {
	}

}
