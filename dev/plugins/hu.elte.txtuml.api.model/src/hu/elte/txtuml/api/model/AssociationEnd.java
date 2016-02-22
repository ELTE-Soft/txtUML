package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.backend.ManyCollection;
import hu.elte.txtuml.api.model.backend.MultiplicityException;
import hu.elte.txtuml.api.model.backend.SingleItemCollection;

/*
 * Multiple classes defined in this file.
 */

/**
 * Abstract base class for association ends in the model.
 * 
 * <p>
 * <b>Represents:</b> association end
 * <p>
 * <b>Usage:</b>
 * <p>
 * 
 * Association ends should be defined as inner classes of an association (a
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
 * using associations.
 * <p>
 * See the documentation of {@link Model} for an overview on modeling in JtxtUML.
 *
 * @see Association.Many
 * @see Association.One
 * @see Association.MaybeOne
 * @see Association.Some
 * @see Association.Multiple
 * @see Association.HiddenMany
 * @see Association.HiddenOne
 * @see Association.HiddenMaybeOne
 * @see Association.HiddenSome
 * @see Association.HiddenMultiple
 * 
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
public abstract class AssociationEnd<T extends ModelClass, C> implements
		ModelElement {

	C collection;

	/**
	 * Sole constructor of <code>AssociationEnd</code>.
	 */
	AssociationEnd() {
	}

	C getCollection() {
		return collection;
	}

	abstract boolean isEmpty();
	
	abstract boolean contains(ModelClass object);
	
	abstract void add(T object) throws MultiplicityException;

	abstract void remove(ModelClass object);

	/**
	 * Checks whether the lower bound of this association end's multiplicity is
	 * unoffended.
	 * 
	 * @return <code>true</code> if this association end has more or equal
	 *         elements than its lower bound, <code>false</code> otherwise
	 */
	abstract boolean checkLowerBound();

	@Override
	public String toString() {
		return collection.toString();
	}

}

/**
 * Base class for association ends having a multiplicity of 0..*.
 * <p>
 * Directly unusable by the user.
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class ManyBase<T extends ModelClass> extends AssociationEnd<T, Collection<T>> {

	{
		collection = new ManyCollection<>();
	}

	@Override
	boolean isEmpty() {
		return collection.isEmpty();
	}

	@Override
	boolean contains(ModelClass object) {
		return collection.contains(object);
	}

	@Override
	void add(T object) throws MultiplicityException {
		collection = collection.add(object);
	}

	@Override
	void remove(ModelClass object) {
		collection = collection.remove(object);
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of Many.
	}

}

/**
 * Base class for association ends having a multiplicity of 0..1.
 * <p>
 * Directly unusable by the user.
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class MaybeOneBase<T extends ModelClass> extends
		AssociationEnd<T, Collection<T>> {

	{
		collection = new SingleItemCollection<>();
	}

	@Override
	boolean isEmpty() {
		return collection.isEmpty();
	}
	
	@Override
	boolean contains(ModelClass object) {
		return collection.contains(object);
	}
	
	@Override
	void add(T object) throws MultiplicityException {
		if (!collection.isEmpty() && !collection.selectAny().equals(object)) {
			throw new MultiplicityException();
		}
		collection = collection.add(object);
	}

	@Override
	void remove(ModelClass object) {
		collection = collection.remove(object);
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of MaybeOne.
	}

}

/**
 * Base class for association ends having a user-defined multiplicity.
 * <p>
 * Inherits its implementation from <code>ManyBase</code>.
 * <p>
 * Directly unusable by the user.
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class MultipleBase<T extends ModelClass> extends ManyBase<T> {

	/**
	 * The actual lower bound of this association end.
	 */
	private final int min;
	
	/**
	 * The actual upper bound of this association end.
	 * <p>
	 * -1 means an infinite bound.
	 */
	private final int max;

	{
		Min min = getClass().getAnnotation(Min.class);
		this.min = min == null || min.value() < 0 ? 0 : min.value();

		Max max = getClass().getAnnotation(Max.class);
		this.max = max == null || max.value() < 0 ? -1 : max.value();
	}

	@Override
	boolean checkLowerBound() {
		return collection.count() >= min;
	}

	@Override
	void add(T object) throws MultiplicityException {
		if (max != -1 && collection.count() >= max) {
			throw new MultiplicityException();
		}
		super.add(object);
	}
}

/**
 * Base class for association ends having a multiplicity of 1.
 * <p>
 * Directly unusable by the user.
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class OneBase<T extends ModelClass> extends MaybeOneBase<T> {

	@Override
	boolean checkLowerBound() {
		return collection.count() > 0;
	}

}

/**
 * Base class for association ends having a multiplicity of 1..*.
 * <p>
 * Directly unusable by the user.
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class SomeBase<T extends ModelClass> extends ManyBase<T> {

	@Override
	boolean checkLowerBound() {
		return collection.count() > 0;
	}

}