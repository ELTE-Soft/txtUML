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
	//protected pointcut withinProject() : within(hu.elte.txtuml..*) && !within(hu.elte.txtuml.examples..*); // TODO only until the examples package exists
	protected pointcut withinExportUML2(): within(hu.elte.txtuml.export.uml2..*);
	protected pointcut withinModel() : within(ModelElement+) && !within(ExternalClass+) && !within(hu.elte.txtuml.api..*);
	protected pointcut importing() : if(ModelImporter.isImporting());
	protected pointcut isActive() : withinModel() && importing();
}
