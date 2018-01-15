package hu.elte.txtuml.utils;

import java.util.Objects;

/**
 * Immutable triple type to store three objects of (possibly) different types.
 *
 * @param <T1>
 *            First type.
 * @param <T2>
 *            Second type.
 * @param <T3>
 *            Third type.
 */
public class Triple<T1, T2, T3> {
	private final T1 first;
	private final T2 second;
	private final T3 third;

	/**
	 * Create new Triple.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 * @param third
	 *            Third value.
	 */
	public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, T2 second, T3 third) {
		return new Triple<T1, T2, T3>(first, second, third);
	}

	/**
	 * Create new Triple.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 * @param third
	 *            Third value.
	 */
	public Triple(T1 first, T2 second, T3 third) {
		this.first = first;
		this.second = second;
		this.third = third;
	}

	public T1 getFirst() {
		return first;
	}

	public T2 getSecond() {
		return second;
	}

	public T3 getThird() {
		return third;
	}

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + Objects.hashCode(first);
		result = prime * result + Objects.hashCode(second);
		result = prime * result + Objects.hashCode(third);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Triple)) {
			return false;
		}
		Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second)
				&& Objects.equals(third, other.third);
	}

	@Override
	public String toString() {
		return "<" + first + ", " + second + ", " + third + ">";
	}
}
