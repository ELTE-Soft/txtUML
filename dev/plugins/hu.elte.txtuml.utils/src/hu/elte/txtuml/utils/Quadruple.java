package hu.elte.txtuml.utils;

import java.util.Objects;

/**
 * Immutable quadruple type to store four objects of (possibly) different types.
 *
 * @param <T1>
 *            First type.
 * @param <T2>
 *            Second type.
 * @param <T3>
 *            Third type.
 * @param <T4>
 *            Fourth type.
 */
public class Quadruple<T1, T2, T3, T4> {
	private final T1 first;
	private final T2 second;
	private final T3 third;
	private final T4 fourth;

	/**
	 * Create new Quadruple.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 * @param tthird
	 *            Third value.
	 * @param fo
	 *            Fourth value.
	 */
	public static <T1, T2, T3, T4> Quadruple<T1, T2, T3, T4> of(T1 first, T2 second, T3 third, T4 fourth) {
		return new Quadruple<T1, T2, T3, T4>(first, second, third, fourth);
	}

	/**
	 * Create new Quadruple.
	 * 
	 * @param first
	 *            First value.
	 * @param second
	 *            Second value.
	 * @param tthird
	 *            Third value.
	 * @param fo
	 *            Fourth value.
	 */
	public Quadruple(T1 first, T2 second, T3 third, T4 fourth) {
		this.first = first;
		this.second = second;
		this.third = third;
		this.fourth = fourth;
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

	public T4 getFourth() {
		return fourth;
	}

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + Objects.hashCode(first);
		result = prime * result + Objects.hashCode(second);
		result = prime * result + Objects.hashCode(third);
		result = prime * result + Objects.hashCode(fourth);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (!(obj instanceof Quadruple)) {
			return false;
		}
		Quadruple<?, ?, ?, ?> other = (Quadruple<?, ?, ?, ?>) obj;
		return Objects.equals(first, other.first) && Objects.equals(second, other.second)
				&& Objects.equals(third, other.third) && Objects.equals(fourth, other.fourth);
	}

	@Override
	public String toString() {
		return "<" + first + ", " + second + ", " + third + ", " + fourth + ">";
	}
}
