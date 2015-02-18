package txtuml.importer;

import txtuml.api.ModelElement;
import txtuml.api.ExternalClass;


abstract aspect AbstractImporterAspect
{
	protected pointcut withinProject() : within(txtuml..*) && !within(txtuml.examples..*); // TODO only until the examples package exists
	protected pointcut withinModel() : within(ModelElement+) && !within(ExternalClass+) && !within(txtuml.api..*);
	protected pointcut importing() : if(ModelImporter.isImporting());
	protected pointcut isActive() : withinModel() && importing();
}
