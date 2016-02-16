package hu.elte.txtuml.export.papyrus.layout.txtuml;

import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.export.uml2.mapping.ModelMapUtils;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.export.DiagramType;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.utils.Triple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Behavior;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Transition;

/**
 * Finds the org.eclipse.uml2 element from a model according to the txtUML name 
 */
public class TxtUMLElementsRegistry {
	
	private TxtUMLLayoutDescriptor descriptor;
	private Map<String, ModelMapProvider> modelMapProviders;
	
	/**
	 * The Constructor
	 * @param resource  - The resource where the UML2 element are to be find
	 * @param descriptor - The {@Link TxtUMLLayoutDescriptor} which contains the txtUML Layout informations 
	 * @throws ModelMapException 
	 */
	public TxtUMLElementsRegistry(Resource resource, TxtUMLLayoutDescriptor descriptor) {
		this.descriptor = descriptor;
		try {
			this.modelMapProviders = ModelMapUtils.collectModelMapProviders(descriptor.projectName,
																		descriptor.mappingFolder, resource);
		} catch (ModelMapException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gives the {@link TxtUMLLayoutDescriptor} that is used by the instance
	 * @return The layout descriptor
	 */
	public TxtUMLLayoutDescriptor getDescriptor(){
		return this.descriptor;
	}
	
	/**
	 * Returns the roots elements of all reports 
	 * @return
	 */
	public List<Triple<DiagramType, String, Element>> getDiagramRootsWithDiagramNames(){
		List<Triple<DiagramType, String, Element>> roots = new ArrayList<>();
		for(Pair<String, DiagramExportationReport> pair : this.descriptor.getReportsWithDiagramNames()){
			DiagramExportationReport report = pair.getSecond();
			String name = report.getReferencedElementName();
			DiagramType type = report.getType();
			if(type.equals(DiagramType.Class)){
				findElement(report.getModelName()).ifPresent(
						e -> roots.add(new Triple<>(type, pair.getFirst(), e))
					);
			}else if(type.equals(DiagramType.StateMachine)){
				Optional<Element> classOfStatemachine =  findElement(name);
				if(classOfStatemachine.isPresent()){
					Behavior behavior =  ((org.eclipse.uml2.uml.BehavioredClassifier) classOfStatemachine.get()).getClassifierBehavior();
					roots.add(new Triple<>(type, pair.getFirst(), behavior));
				}
			}
		}
		return roots;
	}
	
	/**
	 * Finds the org.eclipse.uml2 element from a model according to the elements canonical name 
	 * @param nodeName - The model elements canonical name
	 * @return The appropriate model element or null if not found
	 */
	public Optional<Element> findElement(String nodeName){
		for(ModelMapProvider modelMapProvider:  modelMapProviders.values()){
			Element elem = (Element) modelMapProvider.getByName(nodeName);
			if(elem != null){
				return Optional.of(elem);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * Returns all elements that are should be on the diagram according to the report 
	 * @param diagramName - The name of the diagram which's Elements are required
	 * @return List of org.eclipse.uml2 model elements
	 */
	public List<Element> getNodes(String diagramName){
		List<Element> elements = new LinkedList<Element>();
		DiagramExportationReport report = this.descriptor.getReport(diagramName);
		if(report != null && report.isSuccessful()){
			for(RectangleObject rectangle : report.getNodes()){
				findElement(rectangle.getName()).ifPresent(elements::add);
			}
		}
		return elements;
	}
	
	/**
	 * Returns all connections that are should be on the diagram according to the report 
	 * @param diagramName - The name of the diagram which's {@link org.eclipse.uml2.uml.Connection Connections} are required
	 * @return List of org.eclipse.uml2 model connections
	 */
	public List<Element> getConnections(String diagramName){
		List<Element> elements = new LinkedList<Element>();
		Optional<? extends Element> elem;
		DiagramExportationReport report = this.descriptor.getReport(diagramName);
		
		if(report != null && report.isSuccessful()){
			for(LineAssociation association : report.getLinks()){
				if(association.getType() == AssociationType.generalization){
					elem = findGeneralization(association.getFrom(), association.getTo());
				}else{
					elem = findAssociation(association.getId());
				}
				
				if(elem.isPresent()){
					elements.add(elem.get());
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
	public Optional<Association> findAssociation(String associationId){
		Optional<Element> elem = findElement(associationId);
		if(elem.isPresent() && elem.get() instanceof Association){
			return Optional.of((Association) elem.get());
		}
		return Optional.empty();
	}
	
	/**
	 * Finds an {@link Generalization} that matches the given ID
	 * @param from - Id of the starting object 
	 * @param to - Id of the ending object
	 * @return The Generalization model Element
	 */
	public Optional<Generalization> findGeneralization(String from, String to){
		Optional<Element> superclass = findElement(from);
		Optional<Element> subclass = findElement(to);
		
		if(superclass.isPresent() && subclass.isPresent() && subclass.get() instanceof Classifier){
			List<Generalization> gens = ((Classifier) subclass.get()).getGeneralizations();
			for(Generalization gen : gens){
				Classifier classif =  gen.getGeneral();
				if(classif.equals(superclass.get())) return Optional.of(gen);
			}
		}
		return Optional.empty();
	}

	/**
	 * Finds an {@link Transition} that matches the given ID
	 * @param id - the TxtUML ID of the transition
	 * @return The Transition model Element
	 */
	public Optional<Transition> findTransition(String id) {
		Optional<Element> elem = findElement(id);
		if(elem.isPresent() && elem.get() instanceof Transition){
			return Optional.of((Transition) elem.get());
		}
		return Optional.empty();
	}
}
