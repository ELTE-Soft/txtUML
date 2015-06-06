package hu.elte.txtuml.export.uml2.transform;


/**
 * Represents an importer. Every importer must extend this class or one of it's subclasses.
 * @author Adam Ancsin
 *
 */
abstract class AbstractImporter {
	
	/**
	 * The class of the txtUML model being imported.
	 */
	protected static Class<?> modelClass=null;
}
