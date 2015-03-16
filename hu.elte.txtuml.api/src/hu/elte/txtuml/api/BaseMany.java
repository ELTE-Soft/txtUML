package hu.elte.txtuml.api;

import hu.elte.txtuml.api.backend.collections.JavaCollectionOfMany;
import hu.elte.txtuml.api.blocks.ParameterizedCondition;

import java.util.Iterator;

class BaseMany<T extends ModelClass> extends AssociationEnd<T> {

	private JavaCollectionOfMany<T> coll = JavaCollectionOfMany.create();

	public BaseMany() {
		isFinal = false;
	}

	@Override
	final AssociationEnd<T> init(Collection<T> other) {
		if (!isFinal && other != null && other instanceof BaseMany) {
			this.coll = ((BaseMany<T>) other).coll;
		}
		isFinal = true;
		return this;
	}

	BaseMany(T object1, T object2) {
		coll.add(object1);
		coll.add(object2);
	}

	BaseMany(CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();
	}

	BaseMany(Collection<T> collection) {
		for (T obj : collection) {
			coll.add(obj);
		}
	}

	BaseMany(Collection<T> collection, CollectionBuilder<T> builder) {
		this.coll = builder.getJavaCollection();

		for (T obj : collection) {
			coll.add(obj);
		}
	}

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
		} else {
			return this;
		}
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends Collection<T>> S typeKeepingAdd(T object) {
		return (S) add(object);
	}

	@Override
	@SuppressWarnings("unchecked")
	final <S extends Collection<T>> S typeKeepingRemove(T object) {
		return (S) remove(object);
	}

	int getSize() {
		return coll.size();
	}

	@Override
	public String toString() {
		return coll.toString();
	}

	@Override
	boolean checkUpperBound() {
		return true; // There is no upper bound of Many.
	}

	@Override
	boolean checkLowerBound() {
		return true; // There is no lower bound of Many.
	}
}
