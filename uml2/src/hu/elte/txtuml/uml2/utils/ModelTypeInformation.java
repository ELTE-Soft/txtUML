package hu.elte.txtuml.uml2.utils;

public class ModelTypeInformation {

	private final String expression;
	private final boolean literal;
	private final boolean calculated;
	private final Integer intVal;
	
	public ModelTypeInformation(String expression, boolean literal, boolean calculated, Integer intVal)
	{
		this.expression=expression;
		this.literal=literal;
		this.calculated=calculated;
		this.intVal=intVal;
	}
	
	public ModelTypeInformation(String expression, boolean literal, boolean calculated)
	{
		this(expression,literal,calculated,0);
	}
	
	public String getExpression()
	{
		return expression;
	}
	public boolean isLiteral()
	{
		return literal;
	}
	public boolean isCalculated()
	{
		return calculated;
	}
	public Integer getIntVal()
	{
		return intVal;
	}
	
}
