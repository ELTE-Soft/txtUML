package hu.elte.txtuml.api;

import java.util.Iterator;

import hu.elte.txtuml.api.backend.problems.MultiplicityException;

/*
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
 * <code>assoc</code> method of {@link ModelClass}), an instance of the actual
 * representing class of that certain association end will be returned. As all
 * association ends are implementing the {@link Collection} interface, the
 * contained objects might be accessed through the collection methods.
 * <p>
 * 
 * TODO checking multiplicity when???
 * 
 * <p>
 * <b>Java restrictions:</b>
 * <ul>
 * <li><i>Instantiate:</i> disallowed
 * <li><i>Define subtype:</i> disallowed, inherit from its predefined subclasses
 * instead (see the nested classes of {@link Association})</li>
 * </ul>
 *
 * See the documentation of {@link Association} for details on defining and
 * using associations.
 * 
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 * @see Association.Many
 * @see Association.One
 * @see Association.MaybeOne 
 * @see Association.Some
 * @see Association.HiddenMany
 * @see Association.HiddenOne
 * @see Association.HiddenMaybeOne
 * @see Association.Some
 * 
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
public abstract class AssociationEnd<T extends ModelClass> implements
		Collection<T> {

	/**
	 * <code>AssociationEnd</code> is generally immutable but it has the ability
	 * to be created in an unfinalized state which means it might be changed
	 * once with the <code>init</code> method. This feature is added for the
	 * purposes of the API implementation.
	 */
	protected boolean isFinal = true;

	/**
	 * The owner model element of this association end.
	 */
	private ModelIdentifiedElement owner;

	/**
	 * Sole constructor of <code>AssociationEnd</code>.
	 * <p>
	 * <b>Implementation note:</b>
	 * <p>
	 * Package private to make sure that this class is only used by the user
	 * through its subclasses.
	 */
	AssociationEnd() {
	}

	/**
	 * An initilazing method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,
	 * <li>the given collection is of a certain type, see the concrete
	 * definitions for details</li>
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
	 * Sets the owner model element of this association end.
	 * 
	 * @param newOwner
	 *            the new owner of this association end
	 */
	void setOwner(ModelIdentifiedElement newOwner) {
		owner = newOwner;
	}

	/**
	 * @return the owner of this association end
	 */
	ModelIdentifiedElement getOwner() {
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
	 * Creates a new association end having the elements of this end and also
	 * the specified <code>object</code> parameter. This is a <i>type
	 * keeping</i> add operation, which means that the method prefers keeping
	 * the type of the result than executing the add operation. So if the latter
	 * makes impossible the former, the latter is not performed.
	 * <p>
	 * To be more specific, if this method returns <i>r</i> and a new
	 * unfinalized instance <i>u</i> is created of this object's dynamic type
	 * then calling the <code>init</code> method of <i>u</i> with <i>r</i> as
	 * its parameter, copying <i>r</i> to <i>u</i> must be performed
	 * successfully.
	 * 
	 * @param object
	 *            the model object to be added to this association end
	 * @return the result of the operation
	 * @throws MultiplicityException
	 *             if he upper bound of this association end's multiplicity has
	 *             been offended
	 */
	abstract <S extends AssociationEnd<T>> S typeKeepingAdd(T object)
			throws MultiplicityException;

	/**
	 * Creates a new association end having the elements of this end without the
	 * specified <code>object</code> parameter. This is a <i>type keeping</i>
	 * remove operation, which means that the method prefers keeping the type of
	 * the result than executing the remove operation. So if the latter makes
	 * impossible the former, the latter is not performed.
	 * <p>
	 * To be more specific, if this method returns <i>r</i> and a new
	 * unfinalized instance <i>u</i> is created of this object's dynamic type
	 * then calling the <code>init</code> method of <i>u</i> with <i>r</i> as
	 * its parameter, copying <i>r</i> to <i>u</i> must be performed
	 * successfully.
	 * 
	 * @param object
	 *            the model object to be added to this association end
	 * @return the result of the operation
	 */
	abstract <S extends AssociationEnd<T>> S typeKeepingRemove(T object);

	@Override
	public abstract String toString();

}
