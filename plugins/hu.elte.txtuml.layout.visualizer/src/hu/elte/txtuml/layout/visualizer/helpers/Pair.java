package hu.elte.txtuml.layout.visualizer.helpers;

/**
 * Pair type. One type to store two values.
 * 
 * @author Balázs Gregorics
 *
 * @param <T1>
 *            First type of the pair.
 * @param <T2>
 *            Second type of the pair.
 */
public class Pair<T1, T2>
{
	
	// Variables
	
	/**
	 * First value.
	 */
	public T1 First;
	/**
	 * Second value.
	 */
	public T2 Second;
	
	// end Variables.
	
	// Ctors
	
	/**
	 * Create a default pair with null values.
	 */
	public Pair()
	{
		First = null;
		Second = null;
	}
	
	/**
	 * Create a pair.
	 * 
	 * @param f
	 *            First value of type T1.
	 * @param s
	 *            Second value of type T2.
	 */
	public Pair(T1 f, T2 s)
	{
		First = f;
		Second = s;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if (obj == this)
		{
			return true;
		}
		if (obj == null || obj.getClass() != this.getClass())
		{
			return false;
		}
		if (obj instanceof Pair<?, ?>)
		{
			Pair<T1, T2> p1 = this;
			Pair<?, ?> p2 = (Pair<?, ?>) obj;
			return p1.First.equals(p2.First) && p1.Second.equals(p2.Second);
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		final int prime = 37;
		int result = 1;
		result = prime * result + First.hashCode();
		result = prime * result + Second.hashCode();
		return result;
	}
	
	@Override
	public String toString()
	{
		return "<" + First + ", " + Second + ">";
	}
}
