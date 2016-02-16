package hu.elte.txtuml.api.model.backend;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.blocks.ParameterizedCondition;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class SingleItemCollection<T> implements Collection<T> {

	/**
	 * The model object contained in this collection. If <code>null</code>, this
	 * collection is empty.
	 */
	private T obj = null;

	/**
	 * Creates an empty <code>SingleItemCollection</code> instance.
	 */
	public SingleItemCollection() {
	}

	/**
	 * Creates an immutable <code>SingleItemCollection</code> instance to contain the
	 * specified value.
	 * 
	 * @param object
	 *            the model object to contain
	 */
	public SingleItemCollection(T object) {
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
	public final boolean contains(Object object) {
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
			return new SingleItemCollection<T>(object);
		}
		return new ManyCollection<T>(obj, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new ManyCollection<T>(this.obj, objects);
	}

	@Override
	public final Collection<T> remove(Object object) {
		if (object == null || !object.equals(this.obj)) {
			return this;
		}
		return new Empty<T>();
	}

	@Override
	public String toString() {
		return obj == null ? "null" : obj.toString();
	}

}
