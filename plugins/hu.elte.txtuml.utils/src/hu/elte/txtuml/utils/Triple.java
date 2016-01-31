package hu.elte.txtuml.utils;

/**
 * Triple type. One type to store three different types.
 *
 * @param <T1>
 *            First type.
 * @param <T2>
 *            Second type.
 * @param <T3>
 *            Third type.
 */
public class Triple<T1, T2, T3> {
	private final T1 _first;
	private final T2 _second;
	private final T3 _third;

	public static <T1, T2, T3> Triple<T1, T2, T3> of(T1 first, T2 second,
			T3 third) {
		return new Triple<T1, T2, T3>(first, second, third);
	}

	/**
	 * Create Triple.
	 * 
	 * @param f
	 *            First value.
	 * @param s
	 *            Second value.
	 * @param t
	 *            Third value.
	 */
	public Triple(T1 f, T2 s, T3 t) {
		_first = f;
		_second = s;
		_third = t;
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

	@Override
	public int hashCode() {
		final int prime = 10007;
		int result = prime + ((_first == null) ? 0 : _first.hashCode());
		result = prime * result + ((_second == null) ? 0 : _second.hashCode());
		result = prime * result + ((_third == null) ? 0 : _third.hashCode());
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
		return true;
	}

	@Override
	public String toString() {
		return "<" + _first + ", " + _second + ", " + _third + ">";
	}
}
