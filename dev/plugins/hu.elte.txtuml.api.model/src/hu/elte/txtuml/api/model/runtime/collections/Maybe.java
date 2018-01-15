package hu.elte.txtuml.api.model.runtime.collections;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Predicate;

import hu.elte.txtuml.api.model.Collection;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;

/**
 * An immutable collection which may contain at most one element but is also
 * allowed to be empty.
 * <p>
 * See the documentation of {@link hu.elte.txtuml.api.model.Model} for an
 * overview on modeling in JtxtUML.
 * 
 * @param <T>
 *            the type of the contained element
 */
public class Maybe<T> implements Collection<T> {

	/**
	 * The element contained in this collection or {@code null} if this
	 * collection is empty.
	 */
	@External
	private final T obj;

	/**
	 * Creates an empty {@code Maybe}.
	 */
	@ExternalBody
	public static <T> Maybe<T> empty() {
		return new Maybe<T>(null);
	}

	/**
	 * Creates a {@code Maybe} that contains the specified element.
	 * 
	 * @param element
	 *            the element this collection will contain
	 */
	@ExternalBody
	public static <T> Maybe<T> of(T element) {
		return new Maybe<T>(element);
	}

	@ExternalBody
	private Maybe(T element) {
		this.obj = element;
	}

	@Override
	@ExternalBody
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
	@ExternalBody
	public final int count() {
		return this.obj == null ? 0 : 1;
	}

	@Override
	@ExternalBody
	public final boolean contains(Object element) {
		return this.obj == null ? element == null : this.obj.equals(element);
	}

	@Override
	@ExternalBody
	public final T selectAny() {
		return obj;
	}

	@Override
	@ExternalBody
	public final Collection<T> selectAll(Predicate<T> cond) {
		if (obj == null || cond.test(obj)) {
			return this;
		}
		return Maybe.empty();
	}

	@Override
	@ExternalBody
	public final Collection<T> add(T element) {
		if (obj == null) {
			return Maybe.of(element);
		}
		return Sequence.<T> builder().add(obj).add(element).build();
	}

	@Override
	@ExternalBody
	public final Collection<T> addAll(Collection<T> elements) {
		return Sequence.<T> builder().add(obj).addAll(elements).build();
	}

	@Override
	@ExternalBody
	public final Collection<T> remove(Object element) {
		if (element == null || !element.equals(this.obj)) {
			return this;
		}
		return Maybe.empty();
	}

	@Override
	@ExternalBody
	public String toString() {
		return obj == null ? "null" : obj.toString();
	}

}
