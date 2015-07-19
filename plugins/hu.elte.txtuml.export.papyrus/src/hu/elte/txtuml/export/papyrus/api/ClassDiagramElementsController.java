package hu.elte.txtuml.export.papyrus.api;

import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

import java.util.Collection;
import java.util.List;

import org.eclipse.core.runtime.Assert;
import org.eclipse.gef.EditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ClassOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceAttributeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.InterfaceOperationCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.clazz.edit.parts.ModelEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.ExtensionEnd;
import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Reception;

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
		ElementsManagerUtils.addElementsToEditpart(diagramEditPart, elements);
	}

	//**PROPERTY**//
	
	/**
	 * @param classEditPart 
	 * @param property
	 */
	public static void addPropertyToClass(ClassEditPart classEditPart, Property property){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, property);
	}
	
	/**
	 * @param classEditPart
	 * @param properties 
	 */
	public static void addPropertiesToClass(ClassEditPart classEditPart,Collection<Property> properties){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, properties);
	}
	
	/**
	 * @param classEditPart
	 * @param property
	 */
	public static void addPropertyToInterface(InterfaceEditPart classEditPart, Property property){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, property);
	}
	
	/**
	 * @param classEditPart
	 * @param elements 
	 */
	public static void addPropertiesToInterface(InterfaceEditPart classEditPart,Collection<Property> elements){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, elements);
	}
	
	//**PORT**//
	
	/**
	 * @param classEditPart 
	 * @param port
	 */
	public static void addPortToClass(ClassEditPart classEditPart, Port port){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, port);
	}
	
	/**
	 * @param classEditPart
	 * @param ports 
	 */
	public static void addPortsToClass(ClassEditPart classEditPart,Collection<Port> ports){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, ports);
	}
	
	/**
	 * @param classEditPart
	 * @param port
	 */
	public static void addPortToInterface(InterfaceEditPart classEditPart, Port port){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, port);
	}
	
	/**
	 * @param classEditPart
	 * @param ports 
	 */
	public static void addPortsToInterface(InterfaceEditPart classEditPart,Collection<Port> ports){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, ports);
	}
	
	//**EXTENSION END**//
	
	/**
	 * @param classEditPart 
	 * @param extensionEnd
	 */
	public static void addExtensionEndToClass(ClassEditPart classEditPart, ExtensionEnd extensionEnd){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, extensionEnd);
	}
	
	/**
	 * @param classEditPart
	 * @param extensionEnds 
	 */
	public static void addExtensionEndsToClass(ClassEditPart classEditPart,Collection<ExtensionEnd> extensionEnds){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof ClassAttributeCompartmentEditPart);
		ClassAttributeCompartmentEditPart attrCompEP = (ClassAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, extensionEnds);
	}
	
	/**
	 * @param classEditPart
	 * @param extensionEnd
	 */
	public static void addExtensionEndToInterface(InterfaceEditPart classEditPart, ExtensionEnd extensionEnd){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementToEditPart(attrCompEP, extensionEnd);
	}
	
	/**
	 * @param classEditPart
	 * @param extensionEnds 
	 */
	public static void addExtensionEndsToInterface(InterfaceEditPart classEditPart,Collection<ExtensionEnd> extensionEnds){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(1) instanceof InterfaceAttributeCompartmentEditPart);
		InterfaceAttributeCompartmentEditPart attrCompEP = (InterfaceAttributeCompartmentEditPart) compartements.get(1);
		ElementsManagerUtils.addElementsToEditpart(attrCompEP, extensionEnds);
	}
	
	
	//**OPERATION**//
	
	/**
	 * @param classEditPart
	 * @param operation
	 */
	public static void addOperationToClass(ClassEditPart classEditPart, Operation operation){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof ClassOperationCompartmentEditPart);
		ClassOperationCompartmentEditPart operCompEP = (ClassOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementToEditPart(operCompEP, operation);
	}
	
	/**
	 * @param classEditPart
	 * @param operations 
	 */
	public static void addOperationsToClass(ClassEditPart classEditPart, Collection<Operation> operations){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof ClassOperationCompartmentEditPart);
		ClassOperationCompartmentEditPart operCompEP = (ClassOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementsToEditpart(operCompEP, operations);
	}
	
	/**
	 * @param interfaceEditPart
	 * @param operation
	 */
	public static void addOperationToInterface(InterfaceEditPart interfaceEditPart, Operation operation){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = interfaceEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof InterfaceOperationCompartmentEditPart);
		InterfaceOperationCompartmentEditPart operCompEP = (InterfaceOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementToEditPart(operCompEP, operation);
	}
	
	/**
	 * @param interfaceEditPart
	 * @param operations 
	 */
	public static void addOperationsToInterface(InterfaceEditPart interfaceEditPart, Collection<Operation> operations){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = interfaceEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof InterfaceOperationCompartmentEditPart);
		InterfaceOperationCompartmentEditPart operCompEP = (InterfaceOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementsToEditpart(operCompEP, operations);
	}
	
	//**RECEPTION**//
	
	/**
	 * @param classEditPart
	 * @param reception
	 */
	public static void addReceptionToClass(ClassEditPart classEditPart, Reception reception){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof ClassOperationCompartmentEditPart);
		ClassOperationCompartmentEditPart operCompEP = (ClassOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementToEditPart(operCompEP, reception);
	}
	
	/**
	 * @param classEditPart
	 * @param receptions 
	 */
	public static void addReceptionsToClass(ClassEditPart classEditPart, Collection<Reception> receptions){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = classEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof ClassOperationCompartmentEditPart);
		ClassOperationCompartmentEditPart operCompEP = (ClassOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementsToEditpart(operCompEP, receptions);
	}
	
	/**
	 * @param interfaceEditPart
	 * @param reception
	 */
	public static void addReceptionToInterface(InterfaceEditPart interfaceEditPart, Reception reception){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = interfaceEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof InterfaceOperationCompartmentEditPart);
		InterfaceOperationCompartmentEditPart operCompEP = (InterfaceOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementToEditPart(operCompEP, reception);
	}
	
	/**
	 * @param interfaceEditPart
	 * @param receptions 
	 */
	public static void addReceptionsToInterface(InterfaceEditPart interfaceEditPart, Collection<Reception> receptions){
		@SuppressWarnings("unchecked")
		List<EditPart> compartements = interfaceEditPart.getChildren();
		Assert.isTrue(compartements.size() == 4);
		Assert.isTrue(compartements.get(2) instanceof InterfaceOperationCompartmentEditPart);
		InterfaceOperationCompartmentEditPart operCompEP = (InterfaceOperationCompartmentEditPart) compartements.get(2);
		ElementsManagerUtils.addElementsToEditpart(operCompEP, receptions);
	}
	
}
