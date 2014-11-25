package txtuml.api;

import java.util.Iterator;

public interface Collection<T extends ModelClass> extends ModelElement, Iterable<T> {
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
	//  - count()
	//  - selectOne()
	//  - selectAll(ParameterizedCondition<T>)
	//  - add(T)
}

class EmptyCollection<T extends ModelClass> implements Collection<T> {
	@Override
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			public boolean hasNext() {
				return false;
			}
			public T next() {
				return null;
			}
			public void remove() {}
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
		Association a = null;
		return 	a.new MaybeOne<T>(object);
	}

	@Override
	public Collection<T> addAll(Collection<T> objects) {
		Association a = null;
		return 	a.new Many<T>(objects);
	}

	@Override
	public Collection<T> remove(ModelClass object) {
		return this;
	}
}
