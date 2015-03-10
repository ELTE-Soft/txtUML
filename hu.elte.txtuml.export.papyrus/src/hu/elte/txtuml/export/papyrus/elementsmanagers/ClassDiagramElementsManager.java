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
import org.eclipse.uml2.uml.Element;

public class ClassDiagramElementsManager extends AbstractDiagramElementsManager{
	
	private PreferencesManager preferencesManager;
	
	private List<String> elementsToBeAdded;
	private List<String> connectorsToBeAdded;
	private List<String> propertyFieldElementsToBeAdded;
	private List<String> methodFieldElementsToBeAdded;
	
	public ClassDiagramElementsManager(ModelManager modelManager,DiagramEditPart diagramEditPart) {
		super(modelManager, diagramEditPart);
		preferencesManager = new PreferencesManager();
		elementsToBeAdded = generateElementsToBeAdded();
		connectorsToBeAdded = generateConnectorsToBeAdded();
		propertyFieldElementsToBeAdded = generatePropertyFieldElementsToBeAdded();
		methodFieldElementsToBeAdded = generateMethodFieldElementsToBeAdded();
	}

	private List<String> generateElementsToBeAdded() {
		List<String> nodes = new LinkedList<String>(Arrays.asList("Class", "Interface", "Package", "Component"));
		
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_CONSTRAINT_PREF))
			nodes.add("Constraint");
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_COMMENT_PREF))
			nodes.add("Comment");
		if(preferencesManager.getBoolean(PreferencesManager.CLASS_DIAGRAM_SIGNAL_PREF))
			nodes.add("Signal");
		
		return nodes;
	}
	
	private List<String> generateConnectorsToBeAdded() {
		List<String> connectors = Arrays.asList("Association", "Generalization", "InterfaceRealization");
		return connectors;
	}
	
	private List<String> generatePropertyFieldElementsToBeAdded() {
		List<String> properties= Arrays.asList("Property","Port");
		return properties;
	}
	
	private List<String> generateMethodFieldElementsToBeAdded() {
		List<String> methods = Arrays.asList("Operation","Reception");
		return methods;
	}
	

	public void addElementsToDiagram(List<Element> elements) throws ServiceException {
		List<String> types = new LinkedList<String>();
		types.addAll(elementsToBeAdded);
		types.addAll(connectorsToBeAdded);
		
		for(String type : types){
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
