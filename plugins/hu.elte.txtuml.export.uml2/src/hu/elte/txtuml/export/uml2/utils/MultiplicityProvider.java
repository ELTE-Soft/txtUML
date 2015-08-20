package hu.elte.txtuml.export.uml2.utils;

import hu.elte.txtuml.api.assocends.Multiplicity;

public class MultiplicityProvider {

	/**
	 * Decides if the txtUML element represented by the specified class has 1..1 multiplicity.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isOne(Class<?> specifiedClass)
	{
		return Multiplicity.One.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the txtUML element represented by the specified class has 0..1 multiplicity.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isZeroToOne(Class<?> specifiedClass)
	{
		return Multiplicity.ZeroToOne.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the txtUML element represented by the specified class has 0..* multiplicity.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isZeroToUnlimited(Class<?> specifiedClass)
	{
		return Multiplicity.ZeroToUnlimited.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the txtUML element represented by the specified class has 1..* multiplicity.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean isOneToUnlimited(Class<?> specifiedClass)
	{
		return Multiplicity.OneToUnlimited.class.isAssignableFrom(specifiedClass);
	}
	
	/**
	 * Decides if the txtUML element represented by the specified class has invalid multiplicity.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The decision.
	 *
	 * @author Adam Ancsin
	 */
	public static boolean hasInvalidMultiplicity(Class<?> specifiedClass)
	{
		return  !isZeroToOne(specifiedClass) &&
				!isZeroToUnlimited(specifiedClass) &&
				!isOne(specifiedClass) &&
				!isOneToUnlimited(specifiedClass);
	}
	
	/**
	 * Gets the lower bound of the multiplicity of the txtUML element represented by the specified class.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The lower bound of the specified element's multiplicity.
	 *
	 * @author Adam Ancsin
	 */
	public static int getLowerBound(Class<?> specifiedClass)
	{
		if(isZeroToOne(specifiedClass) || isZeroToUnlimited(specifiedClass))
			return 0;
		else
			return 1;
	}
	
	/**
	 * Gets the upper bound of the multiplicity of the txtUML element represented by the specified class.
	 * @param specifiedClass The specified class representing a txtUML element.
	 * @return The upper bound of the specified element's multiplicity.
	 *
	 * @author Adam Ancsin
	 */
	public static int getUpperBound(Class<?> specifiedClass)
	{
		if(isOneToUnlimited(specifiedClass) || isZeroToUnlimited(specifiedClass))
			return org.eclipse.uml2.uml.LiteralUnlimitedNatural.UNLIMITED;
		else
			return 1;
	}
}
