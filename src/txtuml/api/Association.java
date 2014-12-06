package txtuml.api;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Association implements ModelElement {
	protected Association() {}
	
	public abstract class AssociationEnd<T extends ModelClass> implements Collection<T> {
		AssociationEnd() {}
		
		@Override
		public final ModelBool isEmpty() {
			return new ModelBool(count().getValue() == 0);
		}
		
		abstract AssociationEnd<T> init(Collection<T> other);
		abstract <S extends Collection<T>> S typeKeepingAdd(T object);
		abstract <S extends Collection<T>> S typeKeepingRemove(T object);
		
		protected boolean isFinal = true;
	}
	
	public class Many<T extends ModelClass> extends AssociationEnd<T> {
		public Many() {
			isFinal = false;
		}

		@Override
		final synchronized AssociationEnd<T> init(Collection<T> other) {
			if (!isFinal && other != null && other instanceof Many) {
				this.set = ((Many<T>)other).set;
			}
			isFinal = true;
			return this;
		}
		
		Many(T object1, T object2) {
			set.add(object1);
			set.add(object2);
		}
		
		Many(CollectionBuilder<T> builder) {
			for (T obj : builder) {
				set.add(obj);
			}
		}

		Many(Collection<T> collection) {
			for (T obj : collection) {
				set.add(obj);
			}
		}
		
		Many(Collection<T> collection, CollectionBuilder<T> builder) {
			this(collection);
			for (T obj : builder) {
				set.add(obj);
			}
		}

		Many(Collection<T> collection, T object) {
			this(collection);
			set.add(object);
		}
		
		@Override
		public Iterator<T> iterator() {
			return set.iterator();
		}
		
		@Override
		public final ModelInt count() {
			return new ModelInt(set.size());
		}

		@Override
		public final ModelBool contains(ModelClass object) { 
			return new ModelBool(set.contains(object));
		}

		@Override
		public final T selectOne() { 
			return set.iterator().next();
		}
		
		@Override
		public final T selectOne(ParameterizedCondition<T> cond) { 
			for (T obj : set) {
				if (cond.check(obj).getValue()) {
					return obj;
				}
			}
			return null;
		}
		
		@Override
		public final Collection<T> selectAll(ParameterizedCondition<T> cond) { 
			CollectionBuilder<T> builder = new CollectionBuilder<>();
			for (T obj : set) {
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
			if (set.contains(object)) {
				CollectionBuilder<T> builder = new CollectionBuilder<>();
				for (T obj : set) {
					if (obj != object) {
						builder.append(obj);
					}
				}
				return new Many<T>(builder);
			} else {
				return this;
			}
		}
		
		@Override @SuppressWarnings("unchecked")
		final <S extends Collection<T>> S typeKeepingAdd(T object) {
			return (S)add(object);
		}
		
		@Override @SuppressWarnings("unchecked")
		final <S extends Collection<T>> S typeKeepingRemove(T object) {
			return (S)remove(object);
		}

		int getSize() {
			return set.size();
		}
		
		private Set<T> set = new HashSet<>();
	}
	
	public class Some<T extends ModelClass> extends Many<T> {
	}
	
	public class MaybeOne<T extends ModelClass> extends AssociationEnd<T> {
		public MaybeOne() {
			isFinal = false;
		}
		
		@Override
		final synchronized AssociationEnd<T> init(Collection<T> other) {
			if (!isFinal && other != null && other instanceof MaybeOne) {
				this.obj = ((MaybeOne<T>)other).obj;
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
				
				public void remove() {}
				
				private boolean hasNext = true;
			};
		}
		
		@Override
		public final ModelInt count() {
			return this.obj != null ? ModelInt.ONE : ModelInt.ZERO;
		}
		
		@Override
		public final ModelBool contains(ModelClass object) {
			return new ModelBool(this.obj == object); 
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
		public final Collection<T> add(T object){
			if (obj == null) {
				return new MaybeOne<T>(object);
			}			
			return new Many<T>(obj,object);
		}
		
		@Override
		public final Collection<T> addAll(Collection<T> objects) {
			return new Many<T>(objects, this.obj);
		}
		
		@Override
		public final Collection<T> remove(T object) {
			if (object == null || this.obj != object) {
				return this;
			}
			return new Empty<T>();
		}
						
		@Override @SuppressWarnings("unchecked")
		final <S extends Collection<T>> S typeKeepingAdd(T object) {
			if (object == null) {
				return (S)new MaybeOne<T>(object);
			}			
			return (S)new MaybeOne<T>(object);
		}
		
		@Override @SuppressWarnings("unchecked")
		final <S extends Collection<T>> S typeKeepingRemove(T object) {
			if (object == null || this.obj != object) {
				return (S)this;
			}
			return (S)new MaybeOne<T>();
		}
		
		private T obj = null;
	}
	
	public class One<T extends ModelClass> extends MaybeOne<T> {
	}
}