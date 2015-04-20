package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.MultiplicityException;

/**
 * Abstract base class for association ends in the model.
 * 
 * <p>
 * <b>Represents:</b> association end
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Association ends should be defined as nested classes of an association (a
 * subclass of {@link Association}).
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
 * ends (see below). It is checked at the beginning of the next <i>execution
 * step</i> and an error message is shown if is still offended and the regarding
 * model object is not in {@link ModelClass.Status#DELETED DELETED} status.
 * However, as this check is relatively slow, it might be switched off with
 * other optional checks with the
 * {@link ModelExecutor.Settings#setDynamicChecks(boolean)
 * ModelExecutor.Settings.setDynamicChecks} method.
 * <p>
 * An <b>execution step</b> starts when an asynchronous event (like a signal
 * event) is chosen by the executor to be processed and ends when that event and
 * all the synchronous events caused by it (like a state machine changing state,
 * entry and exit actions, transition effects, operation calls, etc.), have been
 * processed.
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed</li>
 * <li><i>Define subtype:</i> disallowed, inherit from its predefined subclasses
 * instead (see the nested classes of {@link Association})</li>
 * </ul>
 * 
 * <p>
 * See the documentation of {@link Association} for details on defining and
 * using associations.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
 * @see Association.Many
 * @see Association.One
 * @see Association.MaybeOne
 * @see Association.Some
 * @see Association.HiddenMany
 * @see Association.HiddenOne
 * @see Association.HiddenMaybeOne
 * @see Association.HiddenSome
 * 
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
public abstract class AssociationEnd<T extends ModelClass> implements
		Collection<T> {

	/**
	 * <code>AssociationEnd</code> is generally immutable but it has the ability
	 * to be created in an unfinalized state which means it might be changed
	 * once with the {@link AssociationEnd#init(Collection) init} method. This
	 * feature is added for the purposes of the API implementation.
	 */
	protected boolean isFinal = true;

	/**
	 * The owner model object of this association end.
	 */
	private ModelClass owner;

	/**
	 * Sole constructor of <code>AssociationEnd</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is neither instantiated, nor
	 * directly inherited by the user.
	 */
	AssociationEnd() {
	}

	/**
	 * An initializer method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,</li>
	 * <li>the given collection is of a certain type, see the concrete
	 * definitions of this method for details.</li>
	 * </ul>
	 * After this method returns (either way), this association end is surely
	 * finalized, so its <code>isFinal</code> field is set to be
	 * <code>true</code>.
	 * 
	 * @param other
	 *            the other collection to copy
	 * @return this instance
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	abstract AssociationEnd<T> init(Collection<T> other);

	/**
	 * Sets the owner of this association end.
	 * 
	 * @param newOwner
	 *            the new owner of this association end
	 */
	void setOwner(ModelClass newOwner) {
		owner = newOwner;
	}

	/**
	 * @return the owner of this association end
	 */
	ModelClass getOwner() {
		return owner;
	}

	/**
	 * Checks if the <code>count</code> method returns a <code>ModelInt</code>
	 * representing zero.
	 */
	@Override
	public final ModelBool isEmpty() {
		return new ModelBool(count().getValue() == 0);
	}

	/**
	 * Creates a new association end having the elements of this end and also
	 * the specified <code>object</code> parameter. This is a <i>type
	 * keeping</i> add operation, which means that the method prefers keeping
	 * the type of the result than executing the add operation. So if the latter
	 * makes impossible the former, the latter is not performed.
	 * <p>
	 * To be more specific, if this method returns <i>r</i> and <i>u</i> is an
	 * unfinalized instance of this object's dynamic type then calling the
	 * <code>init</code> method of <i>u</i> with <i>r</i> as its parameter,
	 * copying <i>r</i> to <i>u</i> must be performed successfully.
	 * <p>
	 * Despite causing no direct errors or exception throws, this method should
	 * not be called with a <code>null</code> parameter as association ends are
	 * intended to contain only non-null values.
	 * 
	 * @param object
	 *            the model object to be added to this association end, should
	 *            not be <code>null</code>
	 * @return the result of the add operation
	 * @throws MultiplicityException
	 *             if the upper bound of this association end's multiplicity has
	 *             been offended
	 */
	abstract AssociationEnd<T> typeKeepingAdd(T object)
			throws MultiplicityException;

	/**
	 * Creates a new association end having the elements of this end without the
	 * specified <code>object</code> parameter. This is a <i>type keeping</i>
	 * remove operation, which means that the method prefers keeping the type of
	 * the result than executing the remove operation. So if the latter makes
	 * impossible the former, the latter is not performed.
	 * <p>
	 * To be more specific, if this method returns <i>r</i> and <i>u</i> is an
	 * unfinalized instance of this object's dynamic type then calling the
	 * <code>init</code> method of <i>u</i> with <i>r</i> as its parameter,
	 * copying <i>r</i> to <i>u</i> must be performed successfully.
	 * 
	 * @param object
	 *            the model object to be removed from this association end
	 * @return the result of the operation
	 */
	abstract AssociationEnd<T> typeKeepingRemove(T object);

	/**
	 * Checks whether the upper bound of this association end's multiplicity is
	 * unoffended.
	 * 
	 * @return <code>true</code> if this association end has less or equal
	 *         elements than its upper bound, <code>false</code> otherwise
	 */
	abstract boolean checkUpperBound();

	/**
	 * Checks whether the lower bound of this association end's multiplicity is
	 * unoffended.
	 * 
	 * @return <code>true</code> if this association end has more or equal
	 *         elements than its lower bound, <code>false</code> otherwise
	 */
	abstract boolean checkLowerBound();

	/**
	 * Returns the size if this collection. Differs from <code>count</code> in
	 * that this method returns an integer instead of <code>ModelInt</code>.
	 * This is used only in the API to optimize this query.
	 * 
	 * @return the size of this collection
	 */
	abstract int getSize();

	@Override
	public abstract String toString();

}
