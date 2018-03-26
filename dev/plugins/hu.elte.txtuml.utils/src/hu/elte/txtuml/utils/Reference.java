package hu.elte.txtuml.utils;

import java.util.Objects;

/**
 * Mutable container of a single object. Useful in cases when an out or in/out
 * parameter is needed or when the value of a local variable should be changed
 * from a local class (eg. in lambda expressions).
 * <p>
 * <b>Note:</b> keeps strong reference on the contained object.
 * 
 * @param <E>
 *            the type of the referenced (contained) object
 */
public class Reference<E> {

	private E value;

	public static <E> Reference<E> empty() {
		return new Reference<>();
	}

	public static <E> Reference<E> of(E value) {
		return new Reference<>(value);
	}

	public Reference() {
		this.value = null;
	}

	public Reference(E value) {
		this.value = value;
	}

	public E get() {
		return value;
	}

	public void set(E newValue) {
		value = newValue;
	}

	public void clear() {
		value = null;
	}

	public boolean isEmpty() {
		return value == null;
	}

	@Override
	public String toString() {
		if (value == null) {
			return "empty_reference";
		}
		return "reference <" + value.toString() + ">";
	}

	@Override
	public int hashCode() {
		return Objects.hashCode(value);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || !(obj instanceof Reference)) {
			return false;
		}
		Reference<?> other = (Reference<?>) obj;
		if (!Objects.equals(value, other.value)) {
			return false;
		}
		return true;
	}

}
