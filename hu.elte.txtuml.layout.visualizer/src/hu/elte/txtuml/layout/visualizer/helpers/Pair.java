package hu.elte.txtuml.layout.visualizer.helpers;

public class Pair<T1, T2>
{
	public T1 First;
	public T2 Second;
	
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
		if (this instanceof Pair<?, ?> && obj instanceof Pair<?, ?>)
		{
			Pair<T1, T2> p1 = (Pair<T1, T2>) this;
			@SuppressWarnings({ "unchecked" })
			Pair<T1, T2> p2 = (Pair<T1, T2>) obj; // Checked
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
