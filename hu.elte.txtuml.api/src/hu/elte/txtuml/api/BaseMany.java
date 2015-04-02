package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.JavaCollectionOfMany;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;

import java.util.Iterator;

/**
 * Base class of association ends having a 0..* multiplicity.
 * <p>
 * See the documentation of the {@link hu.elte.txtuml.api} package to get an
 * overview on modeling in txtUML.
 *
 * @author Gábor Ferenc Kovács
 *
 * @param <T>
 *            the type of model objects to be contained in this collection
 */
class BaseMany<T extends ModelClass> extends AssociationEnd<T> {

	/**
	 * A java.lang.Collection to keep the values contained in this object.
	 */
	private JavaCollectionOfMany<T> coll = JavaCollectionOfMany.create();

	/**
	 * Creates an empty, unfinalized <code>BaseMany</code> instance which might
	 * be changed once using the <code>init</code> method.
	 */
	public BaseMany() {
		isFinal = false;
	}

	/**
	 * An initializer method which changes this instance to be a copy of the
	 * <code>other</code> collection, if certain conditions are met:
	 * <ul>
	 * <li>this instance is unfinalized, so the value of its
	 * <code>isFinal</code> field is <code>false</code>,
	 * <li>the given collection is a subclass of BaseMany</li>
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
		if (!isFinal && other != null && other instanceof BaseMany) {
			this.coll = ((BaseMany<T>) other).coll;
		}
		isFinal = true;
		return this;
	}

	/**
	 * Creates a finalized <code>BaseMany</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param object1
	 *            a model object this collection will contain
	 * @param object2
	 *            a model object this collection will contain
	 */
	BaseMany(T object1, T object2) {
		coll.add(object1);
		coll.add(object2);
	}

	/**
	 * Creates a finalized <code>BaseMany</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param builder
	 *            a mutable builder used to gather the elements of this
	 *            collection
	 */
	BaseMany(CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();
	}

	/**
	 * Creates a finalized <code>BaseMany</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param collection
	 *            a collection the elements of which will all be in this
	 *            collection as well
	 */
	BaseMany(Collection<T> collection) {
		for (T obj : collection) {
			coll.add(obj);
		}
	}

	/**
	 * Creates a finalized <code>BaseMany</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param collection
	 *            a collection the elements of which will all be in this
	 *            collection as well
	 * @param builder
	 *            a mutable collection builder the elements of which will all be
	 *            in this collection as well
	 */
	BaseMany(Collection<T> collection, CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();

		for (T obj : collection) {
			coll.add(obj);
		}
	}

	/**
	 * Creates a finalized <code>BaseMany</code> instance to contain the
	 * specified values.
	 * <p>
	 * Finalized means that this object will operate as its class was immutable.
	 * 
	 * @param collection
	 *            a collection the elements of which will all be in this
	 *            collection as well
	 * @param object
	 *            a model object to be included in this collection
	 */
	BaseMany(Collection<T> collection, T object) {
		this(collection);
		coll.add(object);
	}

	@Override
	public Iterator<T> iterator() {
		return coll.iterator();
	}

	@Override
	public final ModelInt count() {
		return new ModelInt(coll.size());
	}

	@Override
	public final ModelBool contains(ModelClass object) {
		return new ModelBool(coll.contains(object));
	}

	@Override
	public final T selectOne() {
		return coll.iterator().next();
	}

	@Override
	public final T selectOne(ParameterizedCondition<T> cond) {
		for (T obj : coll) {
			if (cond.check(obj).getValue()) {
				return obj;
			}
		}
		return null;
	}

	@Override
	public final Collection<T> selectAll(ParameterizedCondition<T> cond) {
		CollectionBuilder<T> builder = new CollectionBuilder<>();
		for (T obj : coll) {
			if (cond.check(obj).getValue()) {
				builder.append(obj);
			}
		}
		return new BaseMany<T>(builder);
	}

	@Override
	public final Collection<T> add(T object) {
		return new BaseMany<T>(this, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new BaseMany<T>(this, new CollectionBuilder<T>().append(objects));
	}

	@Override
	public final Collection<T> remove(T object) {
		if (coll.contains(object)) {
			CollectionBuilder<T> builder = new CollectionBuilder<>();
			for (T obj : coll) {
				if (obj == null ? object == null : !obj.equals(object)) {
					builder.append(obj);
				}
			}
			return new BaseMany<T>(builder);
		}
		return this;
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends AssociationEnd<T>> S typeKeepingAdd(T object) {
		return (S) add(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends AssociationEnd<T>> S typeKeepingRemove(T object) {
		return (S) remove(object);
	}

	@Override
	boolean checkUpperBound() {
		return true; // There is no upper bound of Many.
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of Many.
	}

	@Override
	int getSize() {
		return coll.size();
	}

	@Override
	public String toString() {
		return coll.toString();
	}

}
