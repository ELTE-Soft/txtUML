package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPart;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.Constraint;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.ExtensionEnd;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Reception;
import org.eclipse.uml2.uml.Signal;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 *
 * @author András Dobreff
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private PreferencesManager preferencesManager;
	
	private List<java.lang.Class<?>> elementsToBeAdded;
	private List<java.lang.Class<?>> connectorsToBeAdded;
	private List<java.lang.Class<?>> propertyFieldElementsToBeAdded;
	private List<java.lang.Class<?>> methodFieldElementsToBeAdded;
	
	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public ClassDiagramElementsManager(UMLModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		preferencesManager = new PreferencesManager();
		elementsToBeAdded = generateElementsToBeAdded();
		connectorsToBeAdded = generateConnectorsToBeAdded();
		propertyFieldElementsToBeAdded = generatePropertyFieldElementsToBeAdded();
		methodFieldElementsToBeAdded = generateMethodFieldElementsToBeAdded();
	}

	/**
	 * Returns the types of elements that are to be added
	 * @return Returns the types of elements that are to be added
	 */
	private List<java.lang.Class<?>> generateElementsToBeAdded() {
		List<java.lang.Class<?>> nodes = new LinkedList<java.lang.Class<?>>(
				Arrays.asList(
						Class.class,
						Component.class,
						DataType.class,
						Enumeration.class,
						InformationItem.class,
						InstanceSpecification.class,
						Interface.class,
						Model.class,
						Package.class,
						PrimitiveType.class
				));
		
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_CONSTRAINT_PREF))
			nodes.add(Constraint.class);
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_COMMENT_PREF))
			nodes.add(Comment.class);
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_SIGNAL_PREF))
			nodes.add(Signal.class);
		
		return nodes;
	}
	
	/**
	 * Returns the types of connectors that are to be added
	 * @return Returns the types of connectors that are to be added 
	 */
	private List<java.lang.Class<?>> generateConnectorsToBeAdded() {
		List<java.lang.Class<?>> connectors = Arrays.asList(
				Association.class,
				Generalization.class,
				InterfaceRealization.class,
				Realization.class
		);
		return connectors;
	}
	
	/**
	 * Returns the types of elements that are to be added to the 
	 * properties compartment of a class
	 * @return Returns the types of elements that are to be added 
	 * to the properties compartment of a class 
	 */
	private List<java.lang.Class<?>> generatePropertyFieldElementsToBeAdded() {
		List<java.lang.Class<?>> properties= Arrays.asList(Property.class, Port.class, ExtensionEnd.class);
		return properties;
	}
	
	/**
	 * Returns the types of elements that are to be added to the 
	 * methods compartment of a class
	 * @return Returns the types of elements that are to be added to the 
	 * methods compartment of a class 
	 */
	private List<java.lang.Class<?>> generateMethodFieldElementsToBeAdded() {
		List<java.lang.Class<?>> methods = Arrays.asList(Operation.class, Reception.class);
		return methods;
	}
	
	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements){
		List<java.lang.Class<?>> types = new LinkedList<java.lang.Class<?>>();
		
		types.addAll(elementsToBeAdded);
		types.addAll(connectorsToBeAdded);
		
		for(java.lang.Class<?> type : types){
			List<Element> listofTypes = modelManager.getElementsOfTypeFromList(elements, type);
			if(!listofTypes.isEmpty()){
				super.addElementsToEditpart(diagramEditPart, listofTypes);
			}
		}
		
		
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = diagramEditPart.getChildren();
		
		for(EditPart editPart : editParts){
			if(editPart instanceof ClassEditPart || editPart instanceof InterfaceEditPart){
				addSubElements(editPart);
			}
		}
	}
	
	/**
	 * Fills up the compartments of classes and interfaces
	 * @param ep - The EditPart of the class or interface
	 * @throws ServiceException
	 */
	private void addSubElements(EditPart ep){
		EObject parent = ((View) ep.getModel()).getElement();
		@SuppressWarnings("unchecked")
		List<EditPart> parentEditParts = ep.getChildren();
		List<Element> list = ((Element) parent).getOwnedElements();
		
		List<Element> properties = modelManager.getElementsOfTypesFromList(list, propertyFieldElementsToBeAdded);
		List<Element> methods = modelManager.getElementsOfTypesFromList(list, methodFieldElementsToBeAdded);
		
		removeAssociationProperties(properties);
		
		EditPart parametersEp = parentEditParts.get(1);
		addElementsToEditpart(parametersEp, properties);
	
		EditPart methodsEp = parentEditParts.get(2);
		addElementsToEditpart(methodsEp, methods);
	}
	
	/**
	 * Removes the {@link Property Properties} that have {@link Association Associations} from the given list
	 * @param properties - the list
	 */
	private void removeAssociationProperties(List<Element> properties){
		List<Element> propertiesToRemove = new LinkedList<Element>();
		for(Element property : properties){
			if(property instanceof Property){
				Property prop = (Property) property;
				if(prop.getAssociation() != null){
					propertiesToRemove.add(property);
				}
			}
		}
		properties.removeAll(propertiesToRemove);
	}
}


