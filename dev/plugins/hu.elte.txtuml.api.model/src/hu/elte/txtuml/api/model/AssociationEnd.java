package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.assocends.Bounds;
import hu.elte.txtuml.api.model.runtime.collections.Maybe;
import hu.elte.txtuml.api.model.runtime.collections.Sequence;

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
 * error message is shown in the model executor's error log. However, this does
 * not cause the execution to fail. The lower bound might be offended
 * temporarily, but has to be restored before the current <i>execution step</i>
 * ends. It is checked at the beginning of the next <i>execution step</i> and an
 * error message is shown if it is still offended and the regarding model object
 * is not in {@link ModelClass.Status#DELETED DELETED} status. However, as this
 * check is relatively slow, it might be switched off along with other optional
 * checks.
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
 * See the documentation of {@link Model} for an overview on modeling in
 * JtxtUML.
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
public abstract class AssociationEnd<T extends ModelClass, C extends Collection<T>> implements Bounds {

	AssociationEnd() {
	}

	public abstract C createEmptyCollection();

}

abstract class MaybeEnd<T extends ModelClass> extends AssociationEnd<T, Maybe<T>> {

	MaybeEnd() {
	}

	@Override
	public Maybe<T> createEmptyCollection() {
		return Maybe.empty();
	}

}

abstract class ManyEnd<T extends ModelClass> extends AssociationEnd<T, Sequence<T>> {

	ManyEnd() {
	}

	@Override
	public Sequence<T> createEmptyCollection() {
		return Sequence.empty();
	}

}

abstract class MultipleEnd<T extends ModelClass> extends ManyEnd<T> {

	private final int lowerBound = lowerBound();
	private final int upperBound = upperBound();

	@Override
	public boolean checkLowerBound(int actualSize) {
		return actualSize >= lowerBound;
	}

	@Override
	public boolean checkUpperBound(int actualSize) {
		return upperBound == -1 || actualSize <= upperBound;
	}

}
