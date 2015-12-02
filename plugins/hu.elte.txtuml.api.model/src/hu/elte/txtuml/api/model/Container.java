package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.Multiplicity.One;
import hu.elte.txtuml.api.model.assocends.Navigability.Navigable;

/**
 * Abstract base class for the container end of a composition association.
 * 
 * <p>
 * <b>Represents:</b> container end of a composition association
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * The container end should be defined as inner class of a composition (a
 * subclass of {@link Composition}).
 * <p>
 * When asked for the model objects at an association end (with the
 * {@link ModelClass#assoc(Class) assoc} method), an instance of the actual
 * representing class of that certain association end will be returned. As all
 * association ends are implementing the {@link Collection} interface, the
 * contained objects might be accessed through the collection methods.
 * <p>
 * The multiplicity of an association end might only be checked during model
 * execution. The upper bound is always checked, if it is ever offended, an
 * error message is shown in the model executor's error log (see the
 * documentation of the
 * {@link ModelExecutor.Settings#setExecutorErrorStream(java.io.PrintStream)
 * ModelExecutor.Settings.setExecutorErrorStream} method). However, this does
 * not cause the execution to fail. The lower bound might be offended
 * temporarily, but has to be restored before the current <i>execution step</i>
 * ends. It is checked at the beginning of the next <i>execution step</i> and an
 * error message is shown if it is still offended and the regarding model object
 * is not in {@link ModelClass.Status#DELETED DELETED} status. However, as this
 * check is relatively slow, it might be switched off along with other optional
 * checks with the {@link ModelExecutor.Settings#setDynamicChecks(boolean)
 * ModelExecutor.Settings.setDynamicChecks} method.
 * <p>
 * See the documentation of {@link Model} for information about execution steps.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, inherit from its predefined subclasses
 * instead (see the inner classes of {@link Association})</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Association} for details on defining and
 * using associations and {@link Composition} for associations structurally
 * containing the associated values.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
 *
 * @author Gabor Ferenc Kovacs
 * 
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
public abstract class Container<T extends ModelClass> extends OneBase<T>
		implements One, Navigable {
}
