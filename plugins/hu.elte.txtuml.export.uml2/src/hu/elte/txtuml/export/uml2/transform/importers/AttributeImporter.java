package hu.elte.txtuml.export.uml2.transform.importers;

import hu.elte.txtuml.export.uml2.utils.ElementModifiersAssigner;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Signal;

public class AttributeImporter {
	private TypeImporter typeImporter;
	private Classifier owner;
	
	/**
	 * Creates an <code>AttributeImporter</code> instance.
	 * 
	 * @param typeImporter
	 *            The type importer used.
	 * @param owner
	 *            The owning classifier.
	 */
	public AttributeImporter(TypeImporter typeImporter, Classifier owner) {
		this.typeImporter = typeImporter;
		this.owner = owner;
	}
	
	/**
	 * Imports classifier attributes from the specified field declaration. Note:
	 * a field declaration can declare more than one fields.
	 * 
	 * @param fieldDeclaration
	 *            The specified field declaration.
	 * @return A collection of the imported attributes.
	 *
	 * @author Ádám Ancsin
	 */
	public Collection<Property> importClassifierAttributesFromFieldDeclaration(FieldDeclaration fieldDeclaration) {
		LinkedList<Property> importedAttributes = new LinkedList<>();
		Map<String, org.eclipse.uml2.uml.Type> attributeNamesAndTypes =
				obtainAttributeNamesAndTypesFromFieldDeclaration(fieldDeclaration);
		
		attributeNamesAndTypes.forEach( (name, type) -> {
			Property property = createClassifierAttribute(name, type);
			ElementModifiersAssigner.assignModifiersForElementBasedOnDeclaration(property, fieldDeclaration);
			importedAttributes.add(property);
		});
		
		return importedAttributes;
	}	
	
	/**
	 * Creates a classifier attribute in the owner classifier with the given
	 * name and type.
	 * 
	 * @param attributeName
	 *            The given attribute name.
	 * @param attributeType
	 *            The given attribute type.
	 * @return
	 *
	 * @author Ádám Ancsin
	 */
	private Property createClassifierAttribute(String attributeName, org.eclipse.uml2.uml.Type attributeType) {
		
		if (this.owner instanceof Signal) {
			return ((Signal) this.owner)
					.createOwnedAttribute(attributeName, attributeType);
		} else if (this.owner instanceof org.eclipse.uml2.uml.Class) {
			return ((org.eclipse.uml2.uml.Class) this.owner)
					.createOwnedAttribute(attributeName, attributeType);
		}
		
		return null;
	}
	
	/**
	 * Obtains the names and types of the declared attributes of the specified
	 * field declaration.
	 * 
	 * @param fieldDeclaration
	 *            The specified field declaration.
	 * @return A map of the obtained attribute names and types.
	 *
	 * @author Ádám Ancsin
	 */
	private Map<String, org.eclipse.uml2.uml.Type> obtainAttributeNamesAndTypesFromFieldDeclaration(
			FieldDeclaration fieldDeclaration) {
	
		org.eclipse.jdt.core.dom.Type type = fieldDeclaration.getType();
		org.eclipse.uml2.uml.Type importedType = this.typeImporter.importType(type);
		
		HashMap<String, org.eclipse.uml2.uml.Type> attributes = new HashMap<>();
		for(Object fragment: fieldDeclaration.fragments()) {
			if (fragment instanceof VariableDeclarationFragment) {
				VariableDeclarationFragment frg = (VariableDeclarationFragment) fragment;
				String fieldName = frg.getName().toString();
				attributes.put(fieldName, importedType);
			}
		}
		
		return attributes;
	}
}
