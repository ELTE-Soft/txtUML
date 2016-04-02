package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.gmf.runtime.notation.View;
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

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.api.ClassDiagramElementCreator;
import hu.elte.txtuml.export.papyrus.api.ClassDiagramElementsController;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager{

	protected ClassDiagramElementCreator elementCreator;
	
	private List<java.lang.Class<? extends Element>> elementsToBeAdded;
	private List<java.lang.Class<? extends Element>> connectorsToBeAdded;
	
	/**
	 * The Constructor
	 * @param modelManager - The ModelManager which serves the model elements
	 * @param diagramEditPart - The DiagramEditPart of the diagram which is to be handled
	 */
	public ClassDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain) {
		super(diagram);
		elementsToBeAdded = generateElementsToBeAdded();
		connectorsToBeAdded = generateConnectorsToBeAdded();
		this.elementCreator = new ClassDiagramElementCreator(domain);
	}
	
	public ClassDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain, IProgressMonitor monitor) {
		this(diagram, domain);
		this.monitor = monitor;
	}
	
	

	/**
	 * Returns the types of elements that are to be added
	 * @return Returns the types of elements that are to be added
	 */
	private List<java.lang.Class<? extends Element>> generateElementsToBeAdded() {
		List<java.lang.Class<? extends Element>> nodes = new LinkedList<>(Arrays.asList(
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
		
		if(PreferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_CONSTRAINT_PREF))
			nodes.add(Constraint.class);
		if(PreferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_COMMENT_PREF))
			nodes.add(Comment.class);
		if(PreferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_SIGNAL_PREF))
			nodes.add(Signal.class);
		
		return nodes;
	}
	
	/**
	 * Returns the types of connectors that are to be added
	 * @return Returns the types of connectors that are to be added 
	 */
	private List<java.lang.Class<? extends Element>> generateConnectorsToBeAdded() {
		List<java.lang.Class<? extends Element>> connectors = new LinkedList<>(Arrays.asList(
				Association.class,
				Generalization.class,
				InterfaceRealization.class,
				Realization.class
		));
		
		return connectors;
	}

	/*
	 * (non-Javadoc)
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements){
		List<Element> diagramelements = UMLModelManager.getElementsOfTypesFromList(elements, elementsToBeAdded);
		List<Element> diagramconnections = UMLModelManager.getElementsOfTypesFromList(elements, connectorsToBeAdded);

		for(Element e : diagramelements){
			if(e instanceof Class)
				this.elementCreator.createClassForDiagram(this.diagram, (Class) e, this.monitor);
			if(e instanceof Signal){
				this.elementCreator.createSignalForDiagram(this.diagram, (Signal) e, this.monitor);
			}
		}
//TODO: Replace		
//		ClassDiagramElementsController.addElementsToClassDiagram((ModelEditPart) diagramEditPart, diagramelements);
//		ClassDiagramElementsController.addElementsToClassDiagram((ModelEditPart) diagramEditPart, diagramconnections);
/*		
		@SuppressWarnings("unchecked")
		List<EditPart> editParts = diagramEditPart.getChildren();
		
		for(EditPart editPart : editParts){
			if(editPart instanceof ClassEditPart || editPart instanceof InterfaceEditPart){
				addSubElements(editPart);
			}
		}
*/
	}
	
	/**
	 * Fills up the compartments of classes and interfaces
	 * @param ep - The EditPart of the class or interface
	 * @throws ServiceException
	 */
	private void addSubElements(EditPart ep){
		EObject parent = ((View) ep.getModel()).getElement();
		List<Element> list = ((Element) parent).getOwnedElements();
		
		List<Property> properties = UMLModelManager.getElementsOfTypeFromList(list, Property.class);
		List<Port> ports = UMLModelManager.getElementsOfTypeFromList(list, Port.class);
		List<ExtensionEnd> extensionEnds = UMLModelManager.getElementsOfTypeFromList(list, ExtensionEnd.class);
		
		List<Operation> operations = UMLModelManager.getElementsOfTypeFromList(list, Operation.class);
		List<Reception> receptions = UMLModelManager.getElementsOfTypeFromList(list, Reception.class);
		
		removeAssociationProperties(properties);
		
		if(ep instanceof ClassEditPart){
			ClassDiagramElementsController.addPropertiesToClass((ClassEditPart) ep, properties);
			ClassDiagramElementsController.addPortsToClass((ClassEditPart) ep, ports);
			ClassDiagramElementsController.addExtensionEndsToClass((ClassEditPart) ep, extensionEnds);
			ClassDiagramElementsController.addOperationsToClass((ClassEditPart) ep, operations);
			ClassDiagramElementsController.addReceptionsToClass((ClassEditPart) ep, receptions);
		}else if( ep instanceof InterfaceEditPart){
			ClassDiagramElementsController.addPropertiesToInterface((InterfaceEditPart) ep, properties);
			ClassDiagramElementsController.addPortsToInterface((InterfaceEditPart) ep, ports);
			ClassDiagramElementsController.addExtensionEndsToInterface((InterfaceEditPart) ep, extensionEnds);
			ClassDiagramElementsController.addOperationsToInterface((InterfaceEditPart) ep, operations);
			ClassDiagramElementsController.addReceptionsToInterface((InterfaceEditPart) ep, receptions);
		}
	}
	
	/**
	 * Removes the {@link Property Properties} that have {@link Association Associations} from the given list
	 * @param properties - the list
	 */
	private void removeAssociationProperties(List<Property> properties){
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


