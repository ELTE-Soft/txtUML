package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.ModelClass.Port;

/**
 * A base class for connectors in the model.
 * 
 * <p>
 * <b>Represents:</b> connector
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Define subclasses of either {@link Connector} or {@link Delegation}.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 */
public abstract class ConnectorBase {

	ConnectorBase() {
	}

	/**
	 * Abstract base class for connector ends in the model.
	 * 
	 * <p>
	 * <b>Represents:</b> connector end
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * Connector ends should be defined as inner classes of a connector (a
	 * subclass of {@link Connector} or {@link Delegation}).
	 * <p>
	 * The first of the two generic parameters of a connector end has to be a
	 * role of the class which this end belongs to. A role is represented by a
	 * non-container association end of a composite association (composition).
	 * The second parameter must be a port on the class to which the given
	 * association end (the first parameter) belongs to.
	 * <p>
	 * In case of a <b>delegation connector</b> (a subclass of
	 * {@link Delegation}), if this connector end belongs to the container class,
	 * the first parameter should be the container end of the same composite
	 * association (composition) which's non-container end is referenced in the
	 * other connector end.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> disallowed, inherit from its predefined
	 * subclasses instead (see the inner classes of {@link ConnectorBase})</li>
	 * </ul>
	 * 
	 * <p>
	 * See the documentation of {@link Model} for an overview on modeling in
	 * JtxtUML.
	 *
	 * @see ConnectorBase.One
	 */
	public abstract class ConnectorEnd<R extends AssociationEnd<?>, P extends Port<?, ?>> {
		ConnectorEnd() {
		}
	}

	/**
	 * Base class for connector ends with a multiplicity of 1.
	 * 
	 * <p>
	 * <b>Represents:</b> connector end with a multiplicity of 1
	 * <p>
	 * <b>Usage:</b>
	 * <p>
	 * 
	 * See the documentation of {@link ConnectorEnd}.
	 * 
	 * <p>
	 * <b>Java restrictions:</b>
	 * <ul>
	 * <li><i>Instantiate:</i> disallowed</li>
	 * <li><i>Define subtype:</i> allowed
	 * <p>
	 * <b>Subtype requirements:</b>
	 * <ul>
	 * <li>must be the inner class of a connector class (a subclass of
	 * {@link Connector})</li>
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
	 * <li><i>Nested classes:</i> disallowed</li>
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
	public abstract class One<R extends AssociationEnd<?>, P extends Port<?, ?>> extends ConnectorEnd<R, P> {
	}

}
