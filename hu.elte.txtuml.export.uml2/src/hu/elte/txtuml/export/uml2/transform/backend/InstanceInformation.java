package hu.elte.txtuml.export.uml2.transform.backend;

public interface InstanceInformation {

	static InstanceInformation createLiteral(String expression)
	{
		return new InstanceInformationImpl (expression,true,false);
	}
	
	static InstanceInformation createCalculated(String expression)
	{
		return new InstanceInformationImpl (expression,false,true);
	}
	
	static InstanceInformation create(String expression)
	{
		return new InstanceInformationImpl (expression);
	}

	String getExpression();

	boolean isLiteral();

	boolean isCalculated();
	
	

}
