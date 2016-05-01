package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.gmf.runtime.diagram.ui.editparts.DiagramEditPart;
import org.eclipse.gmf.runtime.diagram.ui.editparts.GraphicalEditPart;
import org.eclipse.gmf.runtime.notation.View;
import org.eclipse.papyrus.uml.diagram.composite.custom.utils.CompositeEditPartUtil;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeCompartmentEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeEditPart;
import org.eclipse.papyrus.uml.diagram.composite.edit.parts.ClassCompositeNameEditPart;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Port;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.papyrus.api.CompositeDiagramElementsController;
import hu.elte.txtuml.export.papyrus.utils.ElementsManagerUtils;

public class CompositeDiagramElementsManager extends AbstractDiagramElementsManager {

	public CompositeDiagramElementsManager(DiagramEditPart diagramEditPart) {
		super(diagramEditPart);
	}

	@Override
	public void addElementsToDiagram(List<Element> elements) {
		//nondeterministic if the top element is generated or not. So drop it, if does not exist.
		if(this.diagramEditPart.getChildren().size() == 0){
			Element top = (Element) ((View)this.diagramEditPart.getModel()).getElement();
			ElementsManagerUtils.addElementToEditPart(this.diagramEditPart, top);
		}
		ClassCompositeEditPart composite = (ClassCompositeEditPart) this.diagramEditPart.getChildren().get(0);
		
		ClassCompositeCompartmentEditPart compartment = (ClassCompositeCompartmentEditPart) CompositeEditPartUtil.getCompositeCompartmentEditPart(composite);
		
		elements.forEach(e -> {
			if(e instanceof Property){
				CompositeDiagramElementsController.addPropertyToClassCompositeCompartementEditPart(compartment, (Property)e);
				
				@SuppressWarnings("unchecked")
				List<GraphicalEditPart> children = compartment.getChildren();
				
				for(GraphicalEditPart child : children){ 
					if(child instanceof ClassCompositeEditPart){
						addPortToComposite((ClassCompositeEditPart)child);
					}
				};
				
			}
		});
		
		addPortToComposite(composite);
	}
	
	private void addPortToComposite(ClassCompositeEditPart composite) {
		@SuppressWarnings("unchecked")
		List<GraphicalEditPart> children = composite.getChildren();
		@SuppressWarnings("unchecked")
		List<Port> ports = (List<Port>) (Object) children.stream().map(e-> ((View)children.get(0).getModel()).getElement()).filter(e -> e instanceof Port).collect(Collectors.toList());
		for(Port p : ports){
			CompositeDiagramElementsController.addPortToClassCompositeNameEditPart(getClassCompositeNameEditPart(composite), p);
		}
	}
	
	private ClassCompositeNameEditPart getClassCompositeNameEditPart(ClassCompositeEditPart composite){
		ClassCompositeNameEditPart result = null;
		for(Object child : composite.getChildren()){
			if(child instanceof ClassCompositeNameEditPart){
				result = (ClassCompositeNameEditPart) child;
			}
		};
		return result;
	}
}
