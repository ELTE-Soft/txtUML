package hu.elte.txtuml.utils;

/**
 * Quadruple type. One type to store four different types.
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
	private final T1 _first;
	private final T2 _second;
	private final T3 _third;
	private final T4 _fourth;

	public static <T1, T2, T3, T4> Quadruple<T1, T2, T3, T4> of(T1 first,
			T2 second, T3 third, T4 fourth) {
		return new Quadruple<T1, T2, T3, T4>(first, second, third, fourth);
	}

	/**
	 * Create Quadruple.
	 * 
	 * @param f
	 *            First value.
	 * @param s
	 *            Second value.
	 * @param t
	 *            Third value.
	 * @param fo
	 *            Fourth value.
	 */
	public Quadruple(T1 f, T2 s, T3 t, T4 fo) {
		_first = f;
		_second = s;
		_third = t;
		_fourth = fo;
	}

	public T1 getFirst() {
		return _first;
	}

	public T2 getSecond() {
		return _second;
	}

	public T3 getThird() {
		return _third;
	}

	public T4 getFourth() {
		return _fourth;
	}

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + ((_first == null) ? 0 : _first.hashCode());
		result = prime * result + ((_second == null) ? 0 : _second.hashCode());
		result = prime * result + ((_third == null) ? 0 : _third.hashCode());
		result = prime * result + ((_fourth == null) ? 0 : _fourth.hashCode());
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
		if (_first == null ? other._first != null : !_first
				.equals(other._first)) {
			return false;
		}
		if (_second == null ? other._second != null : !_second
				.equals(other._second)) {
			return false;
		}
		if (_third == null ? other._third != null : !_third
				.equals(other._third)) {
			return false;
		}
		if (_fourth == null ? other._fourth != null : !_fourth
				.equals(other._fourth)) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "<" + _first + ", " + _second + ", " + _third + ", " + _fourth
				+ ">";
	}
}
