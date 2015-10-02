package hu.elte.txtuml.api.model;

import hu.elte.txtuml.api.model.backend.CollectionBuilder;
import hu.elte.txtuml.api.model.backend.MultiplicityException;
import hu.elte.txtuml.api.model.backend.collections.JavaCollectionOfMany;
import hu.elte.txtuml.api.model.blocks.ParameterizedCondition;

import java.util.Iterator;
import java.util.NoSuchElementException;

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
 * See the documentation of {@link Model} for an overview on modeling in txtUML.
 *
 * @author Gabor Ferenc Kovacs
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
public abstract class AssociationEnd<T extends ModelClass> implements
		Collection<T> {

	/**
	 * <code>AssociationEnd</code> is generally immutable but it has the ability
	 * to be created in an unfinalized state which means it might be changed
	 * once with the {@link AssociationEnd#init(Collection) init} method. This
	 * feature is added for the purposes of the API implementation.
	 */
	boolean isFinal = true;

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
	 * Checks if the <code>count</code> method returns 0.
	 */
	@Override
	public final boolean isEmpty() {
		return count() == 0;
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
	 * Checks whether the lower bound of this association end's multiplicity is
	 * unoffended.
	 * 
	 * @return <code>true</code> if this association end has more or equal
	 *         elements than its lower bound, <code>false</code> otherwise
	 */
	abstract boolean checkLowerBound();

	@Override
	public abstract String toString();

}

/**
 * Base class for association ends having a multiplicity of 0..*.
 * <p>
 * Directly unusable by the user.
 *
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class ManyBase<T extends ModelClass> extends AssociationEnd<T> {

	/**
	 * A java.lang.Collection to collect the values contained in this object.
	 */
	private JavaCollectionOfMany<T> coll = JavaCollectionOfMany.create();

	/**
	 * Creates an empty, unfinalized <code>ManyBase</code> instance which might
	 * be changed once by the {@link ManyBase#init(Collection) init} method.
	 */
	public ManyBase() {
		isFinal = false;
	}

	/**
	 * An initializer method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,</li>
	 * <li>the given collection is a subclass of <code>ManyBase.</code></li>
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
	@Override
	final AssociationEnd<T> init(Collection<T> other) {
		if (!isFinal && other != null && other instanceof ManyBase) {
			this.coll = ((ManyBase<T>) other).coll;
		}
		isFinal = true;
		return this;
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param object1
	 *            a model object this collection will contain
	 * @param object2
	 *            a model object this collection will contain
	 */
	ManyBase(T object1, T object2) {
		coll.add(object1);
		coll.add(object2);
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param builder
	 *            a mutable collection builder which's elements are to be
	 *            included in this collection (the builder will be used up as
	 *            this constructor calls
	 *            {@link CollectionBuilder#getJavaCollection()} )
	 */
	ManyBase(CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 */
	ManyBase(Collection<T> collection) {
		collection.forEach(coll::add);
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 * @param builder
	 *            a mutable collection builder which's elements are to be
	 *            included in this collection (the builder will be used up as
	 *            this constructor calls
	 *            {@link CollectionBuilder#getJavaCollection()})
	 */
	ManyBase(Collection<T> collection, CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();

		collection.forEach(coll::add);
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 * @param object
	 *            a model object to be included in this collection
	 */
	ManyBase(Collection<T> collection, T object) {
		this(collection);
		coll.add(object);
	}

	/**
	 * Creates a finalized <code>ManyBase</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param object
	 *            a model object to be included in this collection
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 */
	ManyBase(T object, Collection<T> collection) {
		coll.add(object);
		collection.forEach(coll::add);
	}

	@Override
	public Iterator<T> iterator() {
		return coll.iterator();
	}

	@Override
	public final int count() {
		return coll.size();
	}

	@Override
	public final boolean contains(ModelClass object) {
		return coll.contains(object);
	}

	@Override
	public final T selectAny() {
		Iterator<T> it = coll.iterator();
		if (it.hasNext()) {
			return it.next();
		} else {
			return null;
		}
	}

	@Override
	public final Collection<T> selectAll(ParameterizedCondition<T> cond) {
		CollectionBuilder<T> builder = new CollectionBuilder<>();
		coll.forEach(obj -> {
			if (cond.check(obj)) {
				builder.append(obj);
			}
		});
		return new ManyBase<T>(builder);
	}

	@Override
	public final Collection<T> add(T object) {
		return new ManyBase<T>(this, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new ManyBase<T>(this, new CollectionBuilder<T>().append(objects));
	}

	@Override
	public final Collection<T> remove(T object) {
		if (coll.contains(object)) {
			CollectionBuilder<T> builder = new CollectionBuilder<>();
			coll.forEach(obj -> {
				if (obj == null ? object == null : !obj.equals(object)) {
					builder.append(obj);
				}
			});
			return new ManyBase<T>(builder);
		}
		return this;
	}

	@Override
	AssociationEnd<T> typeKeepingAdd(T object) throws MultiplicityException {
		return (AssociationEnd<T>) add(object);
	}

	@Override
	final AssociationEnd<T> typeKeepingRemove(T object) {
		return (AssociationEnd<T>) remove(object);
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of Many.
	}

	@Override
	public String toString() {
		return coll.toString();
	}

}

/**
 * Base class for association ends having a multiplicity of 0..1.
 * <p>
 * Directly unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class MaybeOneBase<T extends ModelClass> extends AssociationEnd<T> {

	/**
	 * The model object contained in this collection. If <code>null</code>, this
	 * collection is empty.
	 */
	private T obj = null;

	/**
	 * Creates an empty, unfinalized <code>MaybeOneBase</code> instance which
	 * might be changed once by the {@link MaybeOneBase#init(Collection) init}
	 * method.
	 */
	public MaybeOneBase() {
		isFinal = false;
	}

	/**
	 * An initializer method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,</li>
	 * <li>the given collection is a subclass of <code>MaybeOneBase.</code></li>
	 * </ul>
	 * After this method returns (either way), this association end is surely
	 * finalized, so its <code>isFinal</code> field is set to be
	 * <code>true</code>.
	 * 
	 * @param other
	 *            the other collection to copy
	 * 
	 * @return this instance
	 * 
	 * @throws NullPointerException
	 *             if <code>other</code> is <code>null</code>
	 */
	@Override
	final AssociationEnd<T> init(Collection<T> other) {
		if (!isFinal && other != null && other instanceof MaybeOneBase) {
			this.obj = ((MaybeOneBase<T>) other).obj;
		}
		isFinal = true;
		return this;
	}

	/**
	 * Creates a finalized <code>MaybeOneBase</code> instance to contain the
	 * specified value.
	 * <p>
	 * Finalized means that this object will operate as it was immutable.
	 * 
	 * @param object
	 *            the model object to contain
	 */
	MaybeOneBase(T object) {
		this.obj = object;
	}

	@Override
	public final Iterator<T> iterator() {
		return new Iterator<T>() {

			private boolean hasNext = true;

			@Override
			public T next() {
				if (hasNext) {
					hasNext = false;
					return obj;
				}
				throw new NoSuchElementException();
			}

			@Override
			public boolean hasNext() {
				return hasNext;
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

		};
	}

	@Override
	public final int count() {
		return this.obj == null ? 0 : 1;
	}

	@Override
	public final boolean contains(ModelClass object) {
		return this.obj == null ? object == null : this.obj.equals(object);
	}

	@Override
	public final T selectAny() {
		return obj;
	}

	@Override
	public final Collection<T> selectAll(ParameterizedCondition<T> cond) {
		if (obj == null || cond.check(obj)) {
			return this;
		}
		return new Collection.Empty<T>();
	}

	@Override
	public final Collection<T> add(T object) {
		if (obj == null) {
			return new MaybeOneBase<T>(object);
		}
		return new ManyBase<T>(obj, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new ManyBase<T>(this.obj, objects);
	}

	@Override
	public final Collection<T> remove(T object) {
		if (object == null || object.equals(this.obj)) {
			return this;
		}
		return new Empty<T>();
	}

	@Override
	final AssociationEnd<T> typeKeepingAdd(T object)
			throws MultiplicityException {
		if (object == null) {
			return this;
		} else if (this.obj != null && !this.obj.equals(object)) {
			throw new MultiplicityException();
		}
		return new MaybeOneBase<T>(object);
	}

	@Override
	final AssociationEnd<T> typeKeepingRemove(T object) {
		if (object == null || !object.equals(this.obj)) {
			return this;
		}
		return new MaybeOneBase<T>();
	}

	@Override
	boolean checkLowerBound() {
		return true;
		// There is no lower bound of MaybeOne.
	}

	@Override
	public String toString() {
		return obj == null ? "null" : obj.toString();
	}

}

/**
 * Base class for association ends having a user-defined multiplicity.
 * <p>
 * Inherits its implementation from <code>ManyBase</code>.
 * <p>
 * Directly unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class MultipleBase<T extends ModelClass> extends ManyBase<T> {

	@Override
	boolean checkLowerBound() {
		Min min = getClass().getAnnotation(Min.class);
		return min == null || count() >= min.value();
	}

	@Override
	final AssociationEnd<T> typeKeepingAdd(T object)
			throws MultiplicityException {
		Max max = getClass().getAnnotation(Max.class);
		if (max != null && count() >= max.value()) {
			throw new MultiplicityException();
		}
		return super.typeKeepingAdd(object);
	}
}

/**
 * Base class for association ends having a multiplicity of 1.
 * <p>
 * Directly unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class OneBase<T extends ModelClass> extends MaybeOneBase<T> {

	@Override
	boolean checkLowerBound() {
		return count() > 0;
	}

}

/**
 * Base class for association ends having a multiplicity of 1..*.
 * <p>
 * Directly unusable by the user.
 * 
 * @author Gabor Ferenc Kovacs
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class SomeBase<T extends ModelClass> extends ManyBase<T> {

	@Override
	boolean checkLowerBound() {
		return count() > 0;
	}

}