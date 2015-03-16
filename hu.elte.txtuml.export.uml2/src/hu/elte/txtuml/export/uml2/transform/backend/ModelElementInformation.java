package hu.elte.txtuml.export.uml2.transform.backend;

public class ModelElementInformation {

	private final String expression;
	private final boolean literal;
	private final boolean calculated;
	private final Integer intVal;
	
	public ModelElementInformation(String expression, boolean literal, boolean calculated, Integer intVal)
	{
		this.expression=expression;
		this.literal=literal;
		this.calculated=calculated;
		this.intVal=intVal;
	}
	
	public ModelElementInformation(String expression, boolean literal, boolean calculated)
	{
		this(expression,literal,calculated,0);
	}
	
	public ModelElementInformation(String expression)
	{
		this(expression, false, false);
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
