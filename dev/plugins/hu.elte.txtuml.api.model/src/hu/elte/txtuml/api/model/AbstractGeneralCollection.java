package hu.elte.txtuml.api.model;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.StringJoiner;
import java.util.function.Consumer;

import hu.elte.txtuml.api.model.error.CollectionCreationError;
import hu.elte.txtuml.api.model.error.EmptyCollectionError;
import hu.elte.txtuml.api.model.error.LowerBoundError;
import hu.elte.txtuml.api.model.error.MultiplicityError;
import hu.elte.txtuml.api.model.error.UninitializedCollectionError;
import hu.elte.txtuml.api.model.error.UpperBoundError;
import hu.elte.txtuml.api.model.utils.Collections;
import hu.elte.txtuml.utils.InstanceCreator;
import hu.elte.txtuml.utils.RuntimeInvocationTargetException;

//TODO document and review
abstract class AbstractGeneralCollection<E, B extends java.util.Collection<E>, C extends AbstractGeneralCollection<E, B, C>>
		extends GeneralCollection<E> implements @External Cloneable {

	/**
	 * The Java collection that contains the elements of this collection. Should
	 * only be accessed through {@link #getBackend()} because that method checks
	 * whether this backend is {@code null}.
	 */
	private B backend = null;

	AbstractGeneralCollection() {
	}

	/**
	 * Must be used with extreme care as this constructor sets the backend of
	 * this collection without any multiplicity checks.
	 */
	AbstractGeneralCollection(B backend) {
		this.backend = backend;
	}

	// ACCESSIBLE METHODS

	@ExternalBody
	@Override
	public final boolean isEmpty() {
		return getBackend().isEmpty();
	}

	@ExternalBody
	@Override
	public final int size() {
		return getBackend().size();
	}

	@ExternalBody
	@Override
	public final E one() {
		try {
			return getBackend().iterator().next();
		} catch (NoSuchElementException e) {
			throw new EmptyCollectionError();
		}
	}

	@ExternalBody
	@Override
	public final boolean contains(Object element) {
		return getBackend().contains(element);
	}

	@ExternalBody
	@Override
	public final C add(E element) {
		return createSameTyped(builder -> {
			builder.addAll(getBackend());
			builder.add(element);
		});
	}

	@ExternalBody
	@Override
	public final C remove(Object element) {
		return createSameTyped(builder -> {
			Iterator<E> it = getBackend().iterator();
			while (it.hasNext()) {
				E e = it.next();
				if (e.equals(element)) {
					builder.addAll(it);
					break;
				} else {
					builder.add(e);
				}
			}
		});
	}

	@ExternalBody
	@Override
	public final int getLowerBound() {
		return getLowerBoundPackagePrivate();
	}

	@ExternalBody
	@Override
	public final int getUpperBound() {
		return getUpperBoundPackagePrivate();
	}

	@ExternalBody
	@Override
	public final Iterator<E> iterator() {
		return getBackend().iterator();
	}

	@ExternalBody
	@Override
	@SuppressWarnings("unchecked")
	protected final C clone() {
		try {
			return (C) super.clone();
		} catch (CloneNotSupportedException e) {
			// Cannot happen.
			e.printStackTrace();
			return null;
		}
	}

	@External
	@Override
	public final int hashCode() {
		return getBackend().hashCode();
	}

	@ExternalBody
	@Override
	public final boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}
		AbstractGeneralCollection<?, ?, ?> other = (AbstractGeneralCollection<?, ?, ?>) obj;
		return this.getBackend().equals(other.getBackend());
	}

	@ExternalBody
	@Override
	public String toString() {
		StringJoiner joiner = new StringJoiner(", ");
		getBackend().forEach(e -> joiner.add(e.toString()));
		return joiner.toString();
	}

	// PACKAGE PRIVATE HELPER METHODS

	static <C extends GeneralCollection<?>, E> C create(Class<C> collectionType, Consumer<Builder<E>> backendBuilder)
			throws CollectionCreationError, MultiplicityError {
		return fillUninitialized(createUninitialized(collectionType), backendBuilder);
	}

	static <E> Any<E> createAnyOf(Consumer<Builder<E>> backendBuilder) {
		return fillUninitialized(new Any<>(), backendBuilder);
	}

	/**
	 * Same as the {@link #as} method, but this does not check whether this
	 * collection can be safely converted to the given type.
	 */
	@SuppressWarnings("unchecked")
	final <C2 extends GeneralCollection<?>> C2 asUnsafe(Class<C2> collectionType)
			throws CollectionCreationError, MultiplicityError {
		C2 result = createUninitialized(collectionType);
		((AbstractGeneralCollection<E, ?, ?>) result).createAndSetBackend(builder -> builder.addAll(this));
		return result;
	}

	/**
	 * Creates a new collection with the same dynamic type as this collection
	 * but with the specified elements.
	 */
	final C createSameTyped(Consumer<Builder<E>> backendBuilder) throws MultiplicityError {
		C other = clone();
		((AbstractGeneralCollection<E, ?, C>) other).createAndSetBackend(backendBuilder);
		return other;
	}

	final B getBackend() {
		if (backend == null) {
			throw new UninitializedCollectionError();
		}
		return backend;
	}

	/**
	 * A non-final version of {@link #getLowerBound()} that enables optimization
	 * in the default collections defined in this package.
	 */
	int getLowerBoundPackagePrivate() {
		return Collections.getLowerBound(getClass());
	}

	/**
	 * A non-final version of {@link #getUpperBound()} that enables optimization
	 * in the default collections defined in this package.
	 */
	int getUpperBoundPackagePrivate() {
		return Collections.getUpperBound(getClass());
	}

	abstract B createBackend(Consumer<Builder<E>> backendBuilder);

	// PRIVATE HELPER METHODS

	/**
	 * Creates an uninitialized instance of the given collection type.
	 */
	private static <C extends GeneralCollection<?>> C createUninitialized(Class<C> collectionType)
			throws CollectionCreationError {
		try {
			return InstanceCreator.create(collectionType);
		} catch (IllegalArgumentException | RuntimeInvocationTargetException e) {
			throw new CollectionCreationError(e);
		}
	}

	/**
	 * Fills an uninitialized collection instance with the specified elements
	 * after checking its lower and upper bound.
	 */
	private static <C extends GeneralCollection<?>, E> C fillUninitialized(C collection,
			Consumer<Builder<E>> backendBuilder) throws MultiplicityError {
		try {
			@SuppressWarnings("unchecked")
			AbstractGeneralCollection<E, ?, ?> casted = ((AbstractGeneralCollection<E, ?, ?>) collection);
			casted.createAndSetBackend(backendBuilder);
		} catch (ClassCastException e) {
			throw new CollectionCreationError(e);
		}

		return collection;
	}

	/**
	 * Sets the backend of this collection after checking its lower and upper
	 * bound.
	 */
	private void setBackend(B backend) throws MultiplicityError {
		int size = backend.size();
		if (size < getLowerBoundPackagePrivate()) {
			throw new LowerBoundError(this);
		}

		int upperBound = getUpperBoundPackagePrivate();
		if (size > upperBound && upperBound >= 0) {
			throw new UpperBoundError(this);
		}

		this.backend = backend;
	}

	/**
	 * Sets the backend of this collection after checking its lower and upper
	 * bound.
	 */
	private void createAndSetBackend(Consumer<Builder<E>> backendBuilder) throws MultiplicityError {
		B backend = createBackend(backendBuilder);
		setBackend(backend);
	}

	// BUILDER

	/**
	 * General builder interface for mutable collections (backends).
	 */
	@FunctionalInterface
	interface Builder<E> {

		@SafeVarargs
		static <E> Consumer<Builder<E>> createConsumerFor(E... elements) {
			return builder -> builder.add(elements);
		}

		/**
		 * The method to override in implementors.
		 */
		void addNoReturn(E element);

		default Builder<E> add(E element) {
			addNoReturn(element);
			return this;
		}

		default Builder<E> add(E[] elements) {
			for (E e : elements) {
				this.addNoReturn(e);
			}
			return this;
		}

		default Builder<E> addAll(Iterable<? extends E> elements) {
			elements.forEach(this::addNoReturn);
			return this;
		}

		default Builder<E> addAll(Iterator<? extends E> elements) {
			elements.forEachRemaining(this::addNoReturn);
			return this;
		}

	}

}
