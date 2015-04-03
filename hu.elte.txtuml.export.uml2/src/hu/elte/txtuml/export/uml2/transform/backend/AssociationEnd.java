package hu.elte.txtuml.export.uml2.transform.backend;

import org.eclipse.uml2.uml.AggregationKind;

/**
 * Instances of this class represent an association end.
 * @author Ádám Ancsin
 *
 */
public class AssociationEnd
{
	/**
	 * The UML2 data type of the association end.
	 */
	private org.eclipse.uml2.uml.Type type;
	
	/**
	 * The name of the association end.
	 */
	private String name;

	/**
	 * The navigability of the association end. True, if navigable, false, if not.
	 */
	private boolean navigable;
	
	/**
	 * The aggregation kind of the association end. Can be shared, composite, none.
	 */
	private AggregationKind aggregationKind;
	
	/**
	 * The lower bound of the end's multiplicity.
	 */
	private int lowerBound;
	
	/**
	 * The upper bound of the end's multiplicity.
	 */
	private int upperBound;
	
	/**
	 * Creates a non-navigable, non-aggregate association end instance with 1..1 multiplicity.
	 * @param type The type of the end.
	 * @param name The name of the end.
	 */
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name)
	{
		this(type, name,1,1);
	}
	
	/**
	 * Creates a non-navigable, non-aggregate association end instance.
	 * @param type The type of the end.
	 * @param name The name of the end.
	 * @param lowerBound The lower bound of the end's multiplicity.
	 * @param upperBound The upper bound of the end's multiplicity.
	 */
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name, int lowerBound, int upperBound)
	{
		this(type, name,lowerBound,upperBound,AggregationKind.NONE_LITERAL);
	}
	
	/**
	 * Creates a non-navigable association end instance.
	 * @param type The type of the end.
	 * @param name The name of the end.
	 * @param lowerBound The lower bound of the end's multiplicity.
	 * @param upperBound The upper bound of the end's multiplicity.
	 * @param aggregationKind The aggregation kind of the end. Can be shared, composite, none.
	 */
	public AssociationEnd(org.eclipse.uml2.uml.Type type, String name,int lowerBound, int upperBound,AggregationKind aggregationKind)
	{
		this(type, name, lowerBound, upperBound, aggregationKind, false);
	}
	
	/**
	 * Creates an association end instance.
	 * @param type The type of the end.
	 * @param name The name of the end.
	 * @param lowerBound The lower bound of the end's multiplicity.
	 * @param upperBound The upper bound of the end's multiplicity.
	 * @param aggregationKind The aggregation kind of the end. Can be shared, composite, none.
	 * @param navigable The navigability of the association end. True, if navigable, false, if not.
	 */
	public AssociationEnd(
			org.eclipse.uml2.uml.Type type,
			String name,
			int lowerBound,
			int upperBound,
			AggregationKind aggregationKind,
			boolean navigable
		)
	{
		this.type=type;
		this.name=name;
		this.navigable=navigable;
		this.aggregationKind=aggregationKind;
		this.lowerBound=lowerBound;
		this.upperBound=upperBound;
	}
	
	/**
	 * Gets the UML2 type of the association end.
	 * @return The UML2 type of the association end.
	 *
	 * @author Ádám Ancsin
	 */
	public org.eclipse.uml2.uml.Type getType()
	{
		return type;
	}
	
	/**
	 * Gets the name of the association end.
	 * @return The name of the association end.
	 *
	 * @author Ádám Ancsin
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Decides if the association end is navigable or not.
	 * @return The decision.
	 *
	 * @author Ádám Ancsin
	 */
	public boolean isNavigable()
	{
		return navigable;
	}
	
	/**
	 * Gets the aggregation kind of the association end.
	 * @return The aggregation kind of the association end.
	 *
	 * @author Ádám Ancsin
	 */
	public AggregationKind getAggregationKind()
	{
		return aggregationKind;
	}
	
	/**
	 * Gets the lower bound of the end's multiplicity.
	 * @return The lower bound of the end's multiplicity.
	 *
	 * @author Ádám Ancsin
	 */
	public int getLowerBound()
	{
		return lowerBound;
	}
	
	/**
	 * Gets the upper bound of the end's multiplicity.
	 * @return The upper bound of the end's multiplicity.
	 *
	 * @author Ádám Ancsin
	 */
	public int getUpperBound()
	{
		return upperBound;
	}
}
