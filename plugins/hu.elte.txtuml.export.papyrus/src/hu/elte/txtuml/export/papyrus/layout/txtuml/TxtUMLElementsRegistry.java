package hu.elte.txtuml.export.papyrus.layout.txtuml;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.NamedElement;

/**
 * Finds the org.eclipse.uml2 element from a model according to the txtUML name 
 *
 * @author András Dobreff
 */
public class TxtUMLElementsRegistry {
	
	private UMLModelManager modelManager;
	private TxtUMLLayoutDescriptor descriptor;
	
	/**
	 * The Constructor
	 * @param modelManager - The model manager which serves the model elements
	 * @param descriptor - The {@Link TxtUMLLayoutDescriptor} which contains the txtUML Layout informations 
	 */
	public TxtUMLElementsRegistry(UMLModelManager modelManager, TxtUMLLayoutDescriptor descriptor) {
		this.modelManager = modelManager;
		this.descriptor = descriptor;
	}
	
	/**
	 * Gives the {@link TxtUMLLayoutDescriptor} that is used by the instance
	 * @return The layout descriptor
	 */
	public TxtUMLLayoutDescriptor getDescriptor(){
		return this.descriptor;
	}
	
	/**
	 * Finds the org.eclipse.uml2 element from a model according to the elements canonical name 
	 * @param nodeName - The model elements canonical name
	 * @return The appropriate model element or null if not found
	 */
	public Element findElement(String nodeName){
		if(!nodeName.startsWith(this.descriptor.modelName)) return null;
	
		String nodePath = nodeName.substring(this.descriptor.modelName.length()+1);
		String[] nodePathArray = nodePath.split("\\.");
		Element elem = this.modelManager.getRoot();
		
		for(String pathbit : nodePathArray){
			elem = getChildWithName(elem.getOwnedElements(), pathbit);
			if( elem == null ){
				return null;
			}
		}
		return elem;		
	}
	
	private Element getChildWithName(Collection<Element> elements, String name){
		for(Element elem : elements){
			if(elem instanceof NamedElement){
				if( ((NamedElement) elem).getName().equals(name) ){
					return elem;
				}
			}
		}
		return null;
	}
	
	/**
	 * Gives all model elements
	 * @return List of org.eclipse.uml2 model elements
	 */
	public List<Element> getNodes(){
		List<Element> elements = new LinkedList<Element>();
		Element elem;
		if(this.descriptor.report.isSuccessful()){
			for(RectangleObject rectangle : this.descriptor.report.getNodes()){
				elem = findElement(rectangle.getName());
				if(elem != null){
					elements.add(elem);
				}
			}
		}
		return elements;
		
	}
	
	/**
	 * Gives all connections
	 * @return List of org.eclipse.uml2 connections
	 */
	public List<Element> getConnections(){
		List<Element> elements = new LinkedList<Element>();
		Element elem;
		if(this.descriptor.report.isSuccessful()){
			for(LineAssociation association : this.descriptor.report.getLinks()){
				if(association.getType() == AssociationType.generalization){
					elem = findGeneralization(association.getId());
				}else{
					elem = findAssociation(association.getId());
				}
				
				if(elem != null){
					elements.add(elem);
				}
			}
		}
		return elements;
	}
	
	/**
	 * Finds an {@link Association} that matches the given ID
	 * @param associationId - the TxtUML ID of the association
	 * @return The Association model Element
	 */
	public Association findAssociation(String associationId){
		Element elem = findElement(associationId);
		if(elem  instanceof Association){
			return (Association) elem;
		}
		return null;
	}
	
	/**
	 * Finds an {@link Generalization} that matches the given ID
	 * @param generalizationId - the TxtUML ID of the generalization.
	 * 	This ID looks like: nameofsuperclass_nameofsubclass
	 * @return The Generalization model Element
	 */
	public Generalization findGeneralization(String generalizationId){
		String[] genid = generalizationId.split("_");
		Element superclass = findElement(genid[0]);
		Element subclass = findElement(genid[1]);
		
		if(superclass == null || subclass == null) return null;
		
		if(subclass instanceof Classifier){
			List<Generalization> gens = ((Classifier) subclass).getGeneralizations();
			for(Generalization gen : gens){
				Classifier classif =  gen.getGeneral();
				if(classif.equals(superclass)) return gen;
			}
			return null;
		}else{
			return null;
		}
		
	}
}
