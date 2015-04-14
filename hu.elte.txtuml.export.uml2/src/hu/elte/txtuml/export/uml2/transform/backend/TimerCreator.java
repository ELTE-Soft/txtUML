package hu.elte.txtuml.export.uml2.transform.backend;

import hu.elte.txtuml.export.uml2.utils.ElementTypeTeller;
import hu.elte.txtuml.stdlib.Timer;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

/**
 * This class is responsible for creating a txtUML timer in a model.
 * @author Ádám Ancsin
 *
 */
public class TimerCreator {

	/**
	 * Creates a txtUML timer class (with handler) in the specified UML2 model with the given profile.
	 * @param model The specified UML2 model.
	 * @param profile The given profile.
	 * @throws ImportException
	 *
	 * @author Ádám Ancsin
	 */
	public static void createTimer(Model model, Profile profile) throws ImportException
	{
		org.eclipse.uml2.uml.Class timerClass =
				model.createOwnedClass(ImporterConfiguration.timerName, false);
		try
		{
			timerClass.applyStereotype(profile.getOwnedStereotype(ImporterConfiguration.externalClassStereotypeName));
		}
		catch(Exception e)
		{
			throw new ImportException(
					"Cannot apply stereotype " + 
					ImporterConfiguration.externalClassStereotypeName +
					"to class: " + 
					timerClass.getName()
				);
		}
		
		for(Class<?> c : Timer.class.getDeclaredClasses())
		{
			if(ElementTypeTeller.isClass(c))
			{
				org.eclipse.uml2.uml.Class uml2Class =
						model.createOwnedClass(ImporterConfiguration.timerName+"_"+c.getSimpleName(),false);
				
				if(ElementTypeTeller.isExternalClass(c))
				{
					try
					{
						uml2Class.applyStereotype(profile.getOwnedStereotype(ImporterConfiguration.externalClassStereotypeName));
					}
					catch(Exception e)
					{
						throw new ImportException(
								"Cannot apply stereotype " + 
								ImporterConfiguration.externalClassStereotypeName +
								"to class: " + 
								uml2Class.getName()
							);
					}
				}
			}
		
		}
	}
}
