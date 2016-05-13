package hu.elte.txtuml.api.model.runtime;

import hu.elte.txtuml.api.model.ModelClass.Port;

/**
 * Wraps a port instance, providing additional runtime information and
 * management capabilities.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 */
public interface PortWrapper extends SignalTargetWrapper<Port<?, ?>>, RuntimeInfo {

}
