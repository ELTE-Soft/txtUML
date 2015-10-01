package hu.elte.txtuml.export.uml2.transform.importers;

import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Type;

/**
 * Instances of this class are responsible for matching txtUML types to UML2
 * types.
 * 
 * @author Adam Ancsin
 *
 */
public class TypeImporter {

	private ModelImporter modelImporter;

	public TypeImporter(ModelImporter modelImporter) {
		this.modelImporter = modelImporter;
	}

	/**
	 * Imports the specified source txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The imported UML2 type.
	 *
	 * @author Adam Ancsin
	 */
	public org.eclipse.uml2.uml.Type importType(
			org.eclipse.jdt.core.dom.Type sourceType) {
		if (sourceType == null) {
			return null;
		} else if (isPrimitiveType(sourceType)) {
			return importPrimitiveType(sourceType);
		} else {
			return importNonPrimitiveType(sourceType);
		}
	}

	/**
	 * Imports the specified source non-primitive txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The imported UML2 type.
	 *
	 * @author Adam Ancsin
	 */
	private Type importNonPrimitiveType(org.eclipse.jdt.core.dom.Type sourceType) {
		Model importedModel = this.modelImporter.getImportedModel();
		if (sourceType != null && importedModel != null) {
			String typeName = sourceType.toString();
			return importedModel.getOwnedType(typeName);
		} else {
			return null;
		}
	}

	/**
	 * Imports the specified source primitive txtUML type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return The imported UML2 type.
	 *
	 * @author Adam Ancsin
	 */
	private Type importPrimitiveType(org.eclipse.jdt.core.dom.Type sourceType) {
		String typeName = sourceType.toString();
		if (typeName.equals("int")) {
			return this.modelImporter.getPrimitiveTypesHolder().getInteger();
		} else if (typeName.equals("boolean")) {
			return this.modelImporter.getPrimitiveTypesHolder().getBoolean();
		} else if (typeName.equals("String")) {
			return this.modelImporter.getPrimitiveTypesHolder().getString();
		} else {
			return null;
		}
	}

	/**
	 * Returns whether the specified source txtUML type is a primitive type.
	 * 
	 * @param sourceType
	 *            The specified source type.
	 * @return <code>true</code>, if the specified source type is a primitive
	 *         type, <code>false</code> otherwise
	 *
	 * @author Adam Ancsin
	 */
	private boolean isPrimitiveType(org.eclipse.jdt.core.dom.Type sourceType) {
		if (sourceType == null) {
			return false;
		} else if (sourceType.isPrimitiveType()) {
			return true;
		} else if (sourceType.toString().equals("String")) {
			return true;
		} else {
			return false;
		}
	}
}
