package hu.elte.txtuml.api.model.backend;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.backend.collections.JavaCollectionOfMany;
import hu.elte.txtuml.api.model.blocks.ParameterizedCondition;

import java.util.Iterator;

public class ManyCollection<T> implements Collection<T> {

	/**
	 * A java.lang.Collection to collect the values contained in this object.
	 */
	private JavaCollectionOfMany<T> coll = JavaCollectionOfMany.create();

	/**
	 * Creates an empty <code>ManyCollection</code> instance.
	 */
	public ManyCollection() {
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
	 * 
	 * @param element1
	 *            an object this collection will contain
	 * @param element2
	 *            an object this collection will contain
	 */
	public ManyCollection(T element1, T element2) {
		coll.add(element1);
		coll.add(element2);
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
	 * 
	 * @param builder
	 *            a mutable collection builder which's elements are to be
	 *            included in this collection (the builder will be used up as
	 *            this constructor calls
	 *            {@link CollectionBuilder#getJavaCollection()} )
	 */
	public ManyCollection(CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
	 * 
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 */
	public ManyCollection(Collection<T> collection) {
		collection.forEach(coll::add);
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
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
	public ManyCollection(Collection<T> collection, CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();

		collection.forEach(coll::add);
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
	 * 
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 * @param element
	 *            an object to be included in this collection
	 */
	public ManyCollection(Collection<T> collection, T element) {
		this(collection);
		coll.add(element);
	}

	/**
	 * Creates an immutable <code>ManyCollection</code> instance to contain
	 * the specified values.
	 * 
	 * @param element
	 *            an object to be included in this collection
	 * @param collection
	 *            a collection which's elements are to be included in this
	 *            collection
	 */
	public ManyCollection(T element, Collection<T> collection) {
		coll.add(element);
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
	public final boolean contains(Object object) {
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
		return new ManyCollection<T>(builder);
	}

	@Override
	public final Collection<T> add(T object) {
		return new ManyCollection<T>(this, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new ManyCollection<T>(this,
				new CollectionBuilder<T>().append(objects));
	}

	@Override
	public final Collection<T> remove(Object object) {
		if (coll.contains(object)) {
			CollectionBuilder<T> builder = new CollectionBuilder<>();
			coll.forEach(obj -> {
				if (obj == null ? object == null : !obj.equals(object)) {
					builder.append(obj);
				}
			});
			return new ManyCollection<T>(builder);
		}
		return this;
	}

	@Override
	public String toString() {
		return coll.toString();
	}

}
