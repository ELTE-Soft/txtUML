package txtuml.importer;

import org.eclipse.uml2.uml.AggregationKind;

public class AssociationEnd
{
	private org.eclipse.uml2.uml.Type type;
	private String name;
	private boolean navigable;
	private AggregationKind aggregationKind;
	private int lowerBound;
	private int upperBound;
	
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name)
	{
		this(type, name, false);
	}
	
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name, boolean navigable)
	{
		this(type, name, navigable,AggregationKind.NONE_LITERAL);
	}
	
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name, boolean navigable,AggregationKind aggregationKind)
	{
		this(type, name, navigable,aggregationKind,1,1);
	}
	
	public AssociationEnd(org.eclipse.uml2.uml.Type type,String name,boolean navigable,AggregationKind aggregationKind,int lowerBound, int upperBound)
	{
		this.type=type;
		this.name=name;
		this.navigable=navigable;
		this.aggregationKind=aggregationKind;
		this.lowerBound=lowerBound;
		this.upperBound=upperBound;
	}
	
	public org.eclipse.uml2.uml.Type getType()
	{
		return type;
	}
	
	public String getName()
	{
		return name;
	}
	
	public boolean isNavigable()
	{
		return navigable;
	}
	
	public AggregationKind getAggregationKind()
	{
		return aggregationKind;
	}
	
	public int getLowerBound()
	{
		return lowerBound;
	}
	
	public int getUpperBound()
	{
		return upperBound;
	}
}
