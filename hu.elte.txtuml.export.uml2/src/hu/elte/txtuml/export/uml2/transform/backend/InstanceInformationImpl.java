package hu.elte.txtuml.export.uml2.transform.backend;

class InstanceInformationImpl implements InstanceInformation {

	private final String expression;
	private final boolean literal;
	private final boolean calculated;
	
	InstanceInformationImpl(String expression, boolean literal, boolean calculated)
	{
		this.expression=expression;
		this.literal=literal;
		this.calculated=calculated;
		
	}
	
	InstanceInformationImpl(String expression)
	{
		this(expression, false, false);
	}
	
	@Override
	public String getExpression()
	{
		return expression;
	}
	
	@Override
	public boolean isLiteral()
	{
		return literal;
	}
	
	@Override
	public boolean isCalculated()
	{
		return calculated;
	}

}
