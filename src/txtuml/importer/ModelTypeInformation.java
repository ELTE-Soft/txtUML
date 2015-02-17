package txtuml.importer;

class ModelTypeInformation {

	private final String expression;
	private final boolean literal;
	private final boolean calculated;
	private final Integer intVal;
	
	ModelTypeInformation(String expression, boolean literal, boolean calculated, Integer intVal)
	{
		this.expression=expression;
		this.literal=literal;
		this.calculated=calculated;
		this.intVal=intVal;
	}
	
	ModelTypeInformation(String expression, boolean literal, boolean calculated)
	{
		this(expression,literal,calculated,0);
	}
	
	String getExpression()
	{
		return expression;
	}
	boolean isLiteral()
	{
		return literal;
	}
	boolean isCalculated()
	{
		return calculated;
	}
	Integer getIntVal()
	{
		return intVal;
	}
	
}
