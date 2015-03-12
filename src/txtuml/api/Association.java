package txtuml.api;

import java.util.Iterator;

import txtuml.api.backend.collections.JavaCollectionOfMany;
import txtuml.api.blocks.ParameterizedCondition;

public class Association implements ModelElement {

	protected Association() {
	}

	public abstract class AssociationEnd<T extends ModelClass> extends
			ModelIdentifiedElementImpl implements Collection<T> {

		protected boolean isFinal = true;
		private String ownerId;

		AssociationEnd() {
		}

		void setOwnerId(String newId) {
			ownerId = newId;
		}

		String getOwnerId() {
			return ownerId;
		}

		@Override
		public final ModelBool isEmpty() {
			return new ModelBool(count().getValue() == 0);
		}

		abstract AssociationEnd<T> init(Collection<T> other);

		abstract <S extends Collection<T>> S typeKeepingAdd(T object);

		abstract <S extends Collection<T>> S typeKeepingRemove(T object);

		@Override
		public abstract String toString();

	}

	public class Many<T extends ModelClass> extends AssociationEnd<T> {

		private JavaCollectionOfMany<T> coll = JavaCollectionOfMany.create();

		public Many() {
			isFinal = false;
		}

		@Override
		final AssociationEnd<T> init(Collection<T> other) {
			if (!isFinal && other != null && other instanceof Many) {
				this.coll = ((Many<T>) other).coll;
			}
			isFinal = true;
			return this;
		}

		Many(T object1, T object2) {
			coll.add(object1);
			coll.add(object2);
		}

		Many(CollectionBuilder<T> builder) {
			this.coll = builder.getJavaCollection();
		}

		Many(Collection<T> collection) {
			for (T obj : collection) {
				coll.add(obj);
			}
		}

		Many(Collection<T> collection, CollectionBuilder<T> builder) {
			this.coll = builder.getJavaCollection();

			for (T obj : collection) {
				coll.add(obj);
			}
		}

		Many(Collection<T> collection, T object) {
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
			return new Many<T>(builder);
		}

		@Override
		public final Collection<T> add(T object) {
			return new Many<T>(this, object);
		}

		@Override
		public final Collection<T> addAll(Collection<T> objects) {
			return new Many<T>(this, new CollectionBuilder<T>().append(objects));
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
				return new Many<T>(builder);
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

	}

	public class Some<T extends ModelClass> extends Many<T> {
	}

	public class MaybeOne<T extends ModelClass> extends AssociationEnd<T> {

		private T obj = null;

		public MaybeOne() {
			isFinal = false;
		}

		@Override
		final AssociationEnd<T> init(Collection<T> other) {
			if (!isFinal && other != null && other instanceof MaybeOne) {
				this.obj = ((MaybeOne<T>) other).obj;
			}
			isFinal = true;
			return this;
		}

		MaybeOne(T object) {
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
			return new MaybeOne<T>(selectOne(cond));
		}

		@Override
		public final Collection<T> add(T object) {
			if (obj == null) {
				return new MaybeOne<T>(object);
			}
			return new Many<T>(obj, object);
		}

		@Override
		public final Collection<T> addAll(Collection<T> objects) {
			return new Many<T>(objects, this.obj);
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
		final <S extends Collection<T>> S typeKeepingAdd(T object) {
			if (object == null) {
				return (S) this;
			}
			return (S) new MaybeOne<T>(object);
		}

		@Override
		@SuppressWarnings("unchecked")
		final <S extends Collection<T>> S typeKeepingRemove(T object) {
			if (object == null || !object.equals(this.obj)) {
				return (S) this;
			}
			return (S) new MaybeOne<T>();
		}

		@Override
		public String toString() {
			return obj == null ? "null" : obj.toString();
		}

	}

	public class One<T extends ModelClass> extends MaybeOne<T> {
	}
}
