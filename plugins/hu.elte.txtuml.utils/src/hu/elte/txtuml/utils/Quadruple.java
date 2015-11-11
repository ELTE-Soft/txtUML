package hu.elte.txtuml.utils;


/**
 * Quadruple type. One type to store four different types.
 * 
 * @author Balázs Gregorics
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
public class Quadruple<T1, T2, T3, T4>
{
	private final T1 _first;
	private final T2 _second;
	private final T3 _third;
	private final T4 _fourth;
	
	public static <T1, T2, T3, T4> Quadruple<T1, T2, T3, T4> of(T1 first, T2 second, T3 third, T4 fourth) {
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
	public Quadruple(T1 f, T2 s, T3 t, T4 fo)
	{
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
	
	/**
	 * Get First and Second value as a Pair.
	 * 
	 * @return Pair of First and Second.
	 */
	public Pair<T1, T2> ToFirstPair()
	{
		return new Pair<T1, T2>(_first, _second);
	}
	
	/**
	 * Get Second and Third value as a Pair.
	 * 
	 * @return Pair of Second and Third.
	 */
	public Pair<T2, T3> ToSecondPair()
	{
		return new Pair<T2, T3>(_second, _third);
	}
	
	/**
	 * Get First and Third value as a Pair.
	 * 
	 * @return Pair of First and Third.
	 */
	public Pair<T1, T3> ToThirdPair()
	{
		return new Pair<T1, T3>(_first, _third);
	}
	
	@Override
	public String toString()
	{
		return "<" + _first + ", " + _second + ", " + _third + ", " + _fourth + ">";
	}
}
