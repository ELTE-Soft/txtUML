package hu.elte.txtuml.export.uml2.transform.aspects;

import hu.elte.txtuml.api.ExternalClass;
import hu.elte.txtuml.api.ModelElement;
import hu.elte.txtuml.export.uml2.transform.*;

/**
 * Represents an importer aspect. Every importer aspect should extend this aspect, or one of it's sub-aspects.
 * It contains pointcuts used by other, specific importer aspects.
 * @author Ádám Ancsin
 *
 */
abstract aspect AbstractImporterAspect
{
	/**
	 * This pointcut indicates that execution is within the export.uml2 package
	 * 
	 * @author Ádám Ancsin
	 */
	protected pointcut withinExportUML2(): within(hu.elte.txtuml.export.uml2..*);
	
	/**
	 * This pointcut indicates that execution is within a txtUML model
	 * 
	 * @author Ádám Ancsin
	 */
	protected pointcut withinModel() : within(ModelElement+) && !within(ExternalClass+) && !within(hu.elte.txtuml.api..*);
	
	/**
	 * This pointcut indicates that model import is in progress.
	 * 
	 * @author Ádám Ancsin
	 */
	protected pointcut importing() : if(ModelImporter.isImporting());
	
	/**
	 * This pointcut indicates that model import is in progress and execution is within a txtUML model.
	 * 
	 * @author Ádám Ancsin
	 */
	protected pointcut isActive() : withinModel() && importing();
}
