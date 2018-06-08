package hu.elte.txtuml.utils;

import java.util.Objects;

/**
 * Immutable pair type to store two objects of (possibly) different types.
 *
 * @param <T1>
 *            First type.
 * @param <T2>
 *            Second type.
 */
public class Pair<T1, T2> {

	private final T1 first;
	private final T2 second;

	/**
	 * Create new Pair.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 */
	public static <T1, T2> Pair<T1, T2> of(T1 first, T2 second) {
		return new Pair<T1, T2>(first, second);
	}

	/**
	 * Create new Pair.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 */
	public Pair(T1 first, T2 second) {
		this.first = first;
		this.second = second;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + Objects.hashCode(first);
		result = prime * result + Objects.hashCode(second);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Pair)) {
			return false;
		}
		Pair<?, ?> other = (Pair<?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second);
	}

	@Override
	public String toString() {
		return "<" + first + ", " + second + ">";
	}

}
