package hu.elte.txtuml.api;

import hu.elte.txtuml.api.blocks.ParameterizedCondition;
import hu.elte.txtuml.api.primitives.ModelBool;
import hu.elte.txtuml.api.primitives.ModelInt;

import java.util.Iterator;

public interface Collection<T extends ModelClass> extends
		ModelIdentifiedElement, Iterable<T> {

	ModelBool isEmpty();

	ModelInt count();

	ModelBool contains(ModelClass object);

	T selectOne();

	T selectOne(ParameterizedCondition<T> cond);

	Collection<T> selectAll(ParameterizedCondition<T> cond);

	Collection<T> add(T object);

	Collection<T> addAll(Collection<T> objects);

	Collection<T> remove(T object);

	// Some of the methods above can be calculated with the use of the others.
	// The minimum needed:
	// - count()
	// - selectOne()
	// - selectAll(ParameterizedCondition<T>)
	// - add(T)

	public static class Empty<T extends ModelClass> extends
			ModelIdentifiedElementImpl implements Collection<T> {
		@Override
		public Iterator<T> iterator() {
			return new Iterator<T>() {
				public boolean hasNext() {
					return false;
				}

				public T next() {
					return null;
				}

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
		public T selectOne(ParameterizedCondition<T> cond) {
			return null;
		}

		@Override
		public Collection<T> selectAll(ParameterizedCondition<T> cond) {
			return this;
		}

		@Override
		public Collection<T> add(T object) {
			return new Association().new MaybeOne<T>(object);
		}

		@Override
		public Collection<T> addAll(Collection<T> objects) {
			return new Association().new Many<T>(objects);
		}

		@Override
		public Collection<T> remove(ModelClass object) {
			return this;
		}
	}
}
