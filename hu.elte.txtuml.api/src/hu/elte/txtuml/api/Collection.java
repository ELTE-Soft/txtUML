package hu.elte.txtuml.api;

import hu.elte.txtuml.api.blocks.ParameterizedCondition;

import java.util.Iterator;

public interface Collection<T extends ModelClass> extends
		Iterable<T> {

	/**
	 * Checks if this collection is empty.
	 * <p>
	 * This method is currently <b>only available in the API</b>, its use is <b>not
	 * exported to UML2</b>.
	 * 
	 * @return a <code>ModelBool</code> representing <code>true</code> if this
	 *         collection is empty, a <code>ModelBool</code> representing
	 *         <code>false</code> otherwise
	 */
	ModelBool isEmpty();

	/**
	 * Returns the number of elements in this collection.
	 * <p>
	 * This method is currently <b>only available in the API</b>, its use is <b>not
	 * exported to UML2</b>.
	 * 
	 * @return a <code>ModelInt</code> representing the size of this collection
	 */
	ModelInt count();

	/**
	 * Checks whether a certain model object is in this collection.
	 * <p>
	 * This method is currently <b>only available in the API</b>, its use is <b>not
	 * exported to UML2</b>.
	 * 
	 * @param object
	 *            the model object to check
	 * @return a <code>ModelBool</code> representing <code>true</code> if this
	 *         collection is contains the specified <code>object</code>, a
	 *         <code>ModelBool</code> representing <code>false</code> otherwise
	 */
	ModelBool contains(ModelClass object);

	/**
	 * Selects an element of this collection. Nor randomness, nor any iteration
	 * order is guaranteed, this method is allowed to return the same object
	 * each time it is called on the same collection.
	 * 
	 * @return an element of this collection, <code>null</code> if the
	 *         collection is empty
	 */
	T selectOne();

	Collection<T> selectAll(ParameterizedCondition<T> cond);

	Collection<T> add(T object);

	Collection<T> addAll(Collection<T> objects);

	Collection<T> remove(T object);

	/**
	 * Iterators over collections are purely for API implementation issues, they
	 * <b>must not</b> be used in the model.
	 */
	@Override
	public abstract Iterator<T> iterator();

	// Some of the methods above can be calculated with the use of the others.
	// The minimum needed:
	// - count()
	// - selectOne()
	// - selectAll(ParameterizedCondition<T>)
	// - add(T)

	public static final class Empty<T extends ModelClass> implements
			Collection<T> {
		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				@Override
				public boolean hasNext() {
					return false;
				}

				@Override
				public T next() {
					return null;
				}

				@Override
				public void remove() {
				}
			};
		}

		@Override
		public ModelBool isEmpty() {
			return ModelBool.FALSE;
		}

		@Override
		public ModelInt count() {
			return ModelInt.ZERO;
		}

		@Override
		public ModelBool contains(ModelClass object) {
			return ModelBool.FALSE;
		}

		@Override
		public T selectOne() {
			return null;
		}

		@Override
		public Collection<T> selectAll(ParameterizedCondition<T> cond) {
			return this;
		}

		@Override
		public Collection<T> add(T object) {
			return new BaseMaybeOne<T>(object);
		}

		@Override
		public Collection<T> addAll(Collection<T> objects) {
			return new BaseMany<T>(objects);
		}

		@Override
		public Collection<T> remove(ModelClass object) {
			return this;
		}
	}
}
