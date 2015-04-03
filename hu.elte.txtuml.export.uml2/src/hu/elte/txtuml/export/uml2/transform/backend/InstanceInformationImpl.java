package hu.elte.txtuml.export.uml2.transform.backend;

/**
 * An implementation of the InstanceInformation interface.
 * Instances of this class store and provide information of dummy instances
 * needed by the importer.
 * @author Ádám Ancsin
 *
 */
class InstanceInformationImpl implements InstanceInformation {

	/**
	 * The expression of the dummy instance.
	 */
	private final String expression;
	
	/**
	 * Indicates that the dummy instance is a literal or not.
	 */
	private final boolean literal;
	
	/**
	 * Indicates that the dummy instance is a calculated instance or not.
	 */
	private final boolean calculated;
	
	/**
	 * Creates an InstanceInformationImpl instance.
	 * @param expression The expression of the dummy instance.
	 * @param literal Indicates that the dummy instance is a literal or not.
	 * @param calculated Indicates that the dummy instance is a calculated instance or not.
	 */ 
	InstanceInformationImpl(String expression, boolean literal, boolean calculated)
	{
		this.expression=expression;
		this.literal=literal;
		this.calculated=calculated;
		
	}
	
	/**
	 * Creates an InstanceInformationImpl instance for a dummy instance that is not
	 * a literal nor a calculated instance.
	 * @param expression The expression of the dummy instance.
	 */
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
