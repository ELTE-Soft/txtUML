package hu.elte.txtuml.layout.visualizer.helpers;

public class Triple<T1, T2, T3>
{
	public T1 First;
	public T2 Second;
	public T3 Third;
	
	public Triple(T1 f, T2 s, T3 t)
	{
		First = f;
		Second = s;
		Third = t;
	}
	
	public Pair<T1, T2> ToFirstPair()
	{
		return new Pair<T1, T2>(First, Second);
	}
	
	public Pair<T2, T3> ToSecondPair()
	{
		return new Pair<T2, T3>(Second, Third);
	}
	
	public Pair<T1, T3> ToThirdPair()
	{
		return new Pair<T1, T3>(First, Third);
	}
	
	@Override
	public String toString()
	{
		return "<" + First + ", " + Second + ", " + Third + ">";
	}
}
