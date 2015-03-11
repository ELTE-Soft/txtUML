package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.problems.MultiplicityException;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;
import hu.elte.txtuml.api.primitives.ModelBool;
import hu.elte.txtuml.api.primitives.ModelInt;

import java.util.Iterator;

class DefaultMaybeOne<T extends ModelClass> extends AssociationEnd<T> {
	
	private T obj = null;

	public DefaultMaybeOne() {
		isFinal = false;
	}

	@Override
	final AssociationEnd<T> init(Collection<T> other) {
		if (!isFinal && other != null && other instanceof DefaultMaybeOne) {
			this.obj = ((DefaultMaybeOne<T>) other).obj;
		}
		isFinal = true;
		return this;
	}

	DefaultMaybeOne(T object) {
		this.obj = object;
	}

	@Override
	public final Iterator<T> iterator() {
		return new Iterator<T>() {
			public T next() {
				hasNext = false;
				return obj;
			}

			public boolean hasNext() {
				return hasNext;
			}

			public void remove() {
			}

			private boolean hasNext = true;
		};
	}

	@Override
	public final ModelInt count() {
		return this.obj != null ? ModelInt.ONE : ModelInt.ZERO;
	}

	@Override
	public final ModelBool contains(ModelClass object) {
		return new ModelBool(this.obj == null ? object == null
				: this.obj.equals(object));
	}

	@Override
	public final T selectOne() {
		return obj;
	}

	@Override
	public final T selectOne(ParameterizedCondition<T> cond) {
		if (obj == null || cond.check(obj).getValue()) {
			return obj;
		} else {
			return null;
		}
	}

	@Override
	public final Collection<T> selectAll(ParameterizedCondition<T> cond) {
		return new DefaultMaybeOne<T>(selectOne(cond));
	}

	@Override
	public final Collection<T> add(T object) {
		if (obj == null) {
			return new DefaultMaybeOne<T>(object);
		}
		return new DefaultMany<T>(obj, object);
	}

	@Override
	public final Collection<T> addAll(Collection<T> objects) {
		return new DefaultMany<T>(objects, this.obj);
	}

	@Override
	public final Collection<T> remove(T object) {
		if (object == null || object.equals(this.obj)) {
			return this;
		}
		return new Empty<T>();
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends Collection<T>> S typeKeepingAdd(T object) throws MultiplicityException {
		if (object == null) {
			return (S) this;
		} else if (this.obj != null && !this.obj.equals(object)) {
			throw new MultiplicityException(); // TODO set message
		}
		return (S) new DefaultMaybeOne<T>(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends Collection<T>> S typeKeepingRemove(T object) {
		if (object == null || !object.equals(this.obj)) {
			return (S) this;
		}
		return (S) new DefaultMaybeOne<T>();
	}

	@Override
	public String toString() {
		return obj == null ? "null" : obj.toString();
	}

	@Override
	boolean checkUpperBound() {
		return false;
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of MaybeOne.
	}
}
