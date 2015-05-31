package hu.elte.txtuml.layout.visualizer.helpers;

/**
 * Triple type. One type to store three different types.
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
public class Quadraple<T1, T2, T3, T4>
{
	/**
	 * First value.
	 */
	public T1 First;
	/**
	 * Second value.
	 */
	public T2 Second;
	/**
	 * Third value.
	 */
	public T3 Third;
	/**
	 * Fourth value.
	 */
	public T4 Fourth;
	
	/**
	 * Create Quadraple.
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
	public Quadraple(T1 f, T2 s, T3 t, T4 fo)
	{
		First = f;
		Second = s;
		Third = t;
		Fourth = fo;
	}
	
	/**
	 * Get First and Second value as a Pair.
	 * 
	 * @return Pair of First and Second.
	 */
	public Pair<T1, T2> ToFirstPair()
	{
		return new Pair<T1, T2>(First, Second);
	}
	
	/**
	 * Get Second and Third value as a Pair.
	 * 
	 * @return Pair of Second and Third.
	 */
	public Pair<T2, T3> ToSecondPair()
	{
		return new Pair<T2, T3>(Second, Third);
	}
	
	/**
	 * Get First and Third value as a Pair.
	 * 
	 * @return Pair of First and Third.
	 */
	public Pair<T1, T3> ToThirdPair()
	{
		return new Pair<T1, T3>(First, Third);
	}
	
	@Override
	public String toString()
	{
		return "<" + First + ", " + Second + ", " + Third + ", " + Fourth + ">";
	}
}
