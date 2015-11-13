package hu.elte.txtuml.export.papyrus.api;

import java.util.Collection;
import java.util.List;

import org.eclipse.gef.EditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ModelEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExtensionEnd;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Reception;

import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

/**
 *
 * @author András Dobreff
 */
public class ClassDiagramElementsController {

	/**
	 * @param diagramEditPart
	 * @param element
	 */
	public static void addElementToClassDiagram(ModelEditPart diagramEditPart, Element element){
		ElementsManagerUtils.addElementToEditPart(diagramEditPart, element);
	}
	
	/**
	 * @param diagramEditPart
	 * @param elements
	 */
	public static void addElementsToClassDiagram(ModelEditPart diagramEditPart, Collection<? extends Element> elements){
		ElementsManagerUtils.addElementsToEditPart(diagramEditPart, elements);
	}

	//**PROPERTY**//
	
	/**
	 * @param classEditPart 
	 * @param property
	 */
	public static void addPropertyToClass(ClassEditPart classEditPart, Property property){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, property);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param properties 
	 */
	public static void addPropertiesToClass(ClassEditPart classEditPart,Collection<Property> properties){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, properties);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param property
	 */
	public static void addPropertyToInterface(InterfaceEditPart classEditPart, Property property){
		EditPart ep = getInterfaceAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, property);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param elements 
	 */
	public static void addPropertiesToInterface(InterfaceEditPart interfaceEditPart,Collection<Property> elements){
		EditPart ep = getInterfaceAttributeEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, elements);
		}
	}
	
	//**PORT**//
	
	/**
	 * @param classEditPart 
	 * @param port
	 */
	public static void addPortToClass(ClassEditPart classEditPart, Port port){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, port);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param ports 
	 */
	public static void addPortsToClass(ClassEditPart classEditPart,Collection<Port> ports){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, ports);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param port
	 */
	public static void addPortToInterface(InterfaceEditPart interfaceEditPart, Port port){
		EditPart ep = getInterfaceAttributeEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, port);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param ports 
	 */
	public static void addPortsToInterface(InterfaceEditPart interfaceEditPart,Collection<Port> ports){
		EditPart ep = getInterfaceAttributeEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, ports);
		}
	}
	
	//**EXTENSION END**//
	
	/**
	 * @param classEditPart 
	 * @param extensionEnd
	 */
	public static void addExtensionEndToClass(ClassEditPart classEditPart, ExtensionEnd extensionEnd){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, extensionEnd);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param extensionEnds 
	 */
	public static void addExtensionEndsToClass(ClassEditPart classEditPart,Collection<ExtensionEnd> extensionEnds){
		EditPart ep = getClassAttributeEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, extensionEnds);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param extensionEnd
	 */
	public static void addExtensionEndToInterface(InterfaceEditPart interfaceEditPart, ExtensionEnd extensionEnd){
		EditPart ep = getInterfaceAttributeEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, extensionEnd);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param extensionEnds 
	 */
	public static void addExtensionEndsToInterface(InterfaceEditPart interfaceEditPart,Collection<ExtensionEnd> extensionEnds){
		EditPart ep = getInterfaceAttributeEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, extensionEnds);
		}
	}
	
	
	//**OPERATION**//
	
	/**
	 * @param classEditPart
	 * @param operation
	 */
	public static void addOperationToClass(ClassEditPart classEditPart, Operation operation){
		EditPart ep = getClassOperationEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, operation);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param operations 
	 */
	public static void addOperationsToClass(ClassEditPart classEditPart, Collection<Operation> operations){
		EditPart ep = getClassOperationEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, operations);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param operation
	 */
	public static void addOperationToInterface(InterfaceEditPart interfaceEditPart, Operation operation){
		EditPart ep = getInterfaceOperationEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, operation);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param operations 
	 */
	public static void addOperationsToInterface(InterfaceEditPart interfaceEditPart, Collection<Operation> operations){
		EditPart ep = getInterfaceOperationEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, operations);
		}
	}
	
	//**RECEPTION**//
	
	/**
	 * @param classEditPart
	 * @param reception
	 */
	public static void addReceptionToClass(ClassEditPart classEditPart, Reception reception){
		EditPart ep = getClassOperationEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, reception);
		}
	}
	
	/**
	 * @param classEditPart
	 * @param receptions 
	 */
	public static void addReceptionsToClass(ClassEditPart classEditPart, Collection<Reception> receptions){
		EditPart ep = getClassOperationEditPart(classEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, receptions);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param reception
	 */
	public static void addReceptionToInterface(InterfaceEditPart interfaceEditPart, Reception reception){
		EditPart ep = getInterfaceOperationEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementToEditPart(ep, reception);
		}
	}
	
	/**
	 * @param interfaceEditPart
	 * @param receptions 
	 */
	public static void addReceptionsToInterface(InterfaceEditPart interfaceEditPart, Collection<Reception> receptions){
		EditPart ep = getInterfaceOperationEditPart(interfaceEditPart);
		if(ep != null) {
			ElementsManagerUtils.addElementsToEditPart(ep, receptions);
		}
	}
	
	private static EditPart getClassAttributeEditPart(ClassEditPart parent) {
		@SuppressWarnings("unchecked")
		List<EditPart> compartments = parent.getChildren();
		for(EditPart compartment : compartments) {
			if(compartment instanceof ClassAttributeCompartmentEditPart) {
				return compartment;
			}
		}
		return null;
	}

	private static EditPart getInterfaceAttributeEditPart(InterfaceEditPart parent) {
		@SuppressWarnings("unchecked")
		List<EditPart> compartments = parent.getChildren();
		for(EditPart compartment : compartments) {
			if(compartment instanceof InterfaceAttributeCompartmentEditPart) {
				return compartment;
			}
		}
		return null;
	}

	private static EditPart getClassOperationEditPart(ClassEditPart parent) {
		@SuppressWarnings("unchecked")
		List<EditPart> compartments = parent.getChildren();
		for(EditPart compartment : compartments) {
			if(compartment instanceof ClassOperationCompartmentEditPart) {
				return compartment;
			}
		}
		return null;
	}

	private static EditPart getInterfaceOperationEditPart(InterfaceEditPart parent) {
		@SuppressWarnings("unchecked")
		List<EditPart> compartments = parent.getChildren();
		for(EditPart compartment : compartments) {
			if(compartment instanceof ClassOperationCompartmentEditPart) {
				return compartment;
			}
		}
		return null;
	}
	
}
