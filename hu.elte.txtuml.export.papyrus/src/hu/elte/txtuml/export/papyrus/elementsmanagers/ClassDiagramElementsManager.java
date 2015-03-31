package hu.elte.txtuml.export.papyrus.elementsmanagers;

import hu.elte.txtuml.export.papyrus.ModelManager;
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
import org.eclipse.uml2.uml.*;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Package;


public class ClassDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private PreferencesManager preferencesManager;
	
	private List<java.lang.Class<?>> elementsToBeAdded;
	private List<java.lang.Class<?>> connectorsToBeAdded;
	private List<java.lang.Class<?>> propertyFieldElementsToBeAdded;
	private List<java.lang.Class<?>> methodFieldElementsToBeAdded;
	
	public ClassDiagramElementsManager(ModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		preferencesManager = new PreferencesManager();
		elementsToBeAdded = generateElementsToBeAdded();
		connectorsToBeAdded = generateConnectorsToBeAdded();
		propertyFieldElementsToBeAdded = generatePropertyFieldElementsToBeAdded();
		methodFieldElementsToBeAdded = generateMethodFieldElementsToBeAdded();
	}

	private List<java.lang.Class<?>> generateElementsToBeAdded() {
		List<java.lang.Class<?>> nodes = new LinkedList<java.lang.Class<?>>(Arrays.asList(Class.class, Interface.class, Package.class, Component.class));
		
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_CONSTRAINT_PREF))
			nodes.add(Constraint.class);
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_COMMENT_PREF))
			nodes.add(Comment.class);
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_SIGNAL_PREF))
			nodes.add(Signal.class);
		
		return nodes;
	}
	
	private List<java.lang.Class<?>> generateConnectorsToBeAdded() {
		List<java.lang.Class<?>> connectors = Arrays.asList(Association.class, Generalization.class, InterfaceRealization.class);
		return connectors;
	}
	
	private List<java.lang.Class<?>> generatePropertyFieldElementsToBeAdded() {
		List<java.lang.Class<?>> properties= Arrays.asList(Property.class, Port.class);
		return properties;
	}
	
	private List<java.lang.Class<?>> generateMethodFieldElementsToBeAdded() {
		List<java.lang.Class<?>> methods = Arrays.asList(Operation.class, Reception.class);
		return methods;
	}
	

	public void addElementsToDiagram(List<Element> elements) throws ServiceException {
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
	
	private void addSubElements(EditPart ep) throws ServiceException{
		EObject parent = ((View) ep.getModel()).getElement();
		@SuppressWarnings("unchecked")
		List<EditPart> parentEditParts = ep.getChildren();
		List<Element> list = ((Element) parent).getOwnedElements();
		
		List<Element> properties = modelManager.getElementsOfTypesFromList(list, propertyFieldElementsToBeAdded);
		List<Element> methods = modelManager.getElementsOfTypesFromList(list, methodFieldElementsToBeAdded);
	
		EditPart parametersEp = parentEditParts.get(1);
		addElementsToEditpart(parametersEp, properties);
	
		EditPart methodsEp = parentEditParts.get(2);
		addElementsToEditpart(methodsEp, methods);
	}
}
