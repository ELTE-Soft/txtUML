package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;

/**
 * A base class for assembly connectors in the model.
 * 
 * <p>
 * <b>Represents:</b> assembly connector
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * An assembly connector in the model is a subclass of <code>Connector</code>,
 * having two inner classes which both extend {@link ConnectorEnd}. These two
 * inner classes will represent the two ends of this connector.
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
 * <li>must have two inner classes which are subclasses of
 * {@link ConnectorEnd}</li>
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
 * <li><i>Nested classes:</i> allowed at most two, both of which are non-static
 * and are subclasses of {@link ConnectorEnd}</li>
 * <li><i>Nested enums:</i> disallowed</li>
 * </ul>
 * </li>
 * <li><i>Inherit from the defined subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class Connector extends ConnectorBase {

}
