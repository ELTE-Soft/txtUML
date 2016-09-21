package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Transition;

import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;
import hu.elte.txtuml.export.uml2.mapping.ModelMapUtils;
import hu.elte.txtuml.layout.export.DiagramExportationReport;
import hu.elte.txtuml.layout.visualizer.model.AssociationType;
import hu.elte.txtuml.layout.visualizer.model.LineAssociation;
import hu.elte.txtuml.layout.visualizer.model.RectangleObject;
import hu.elte.txtuml.utils.Pair;

/**
 * Finds the org.eclipse.uml2 element from a model according to the txtUML name
 */
public class TxtUMLElementsMapper {

	private Collection<ModelMapProvider> modelMapProviders;
	private Map<DiagramExportationReport, Map<String, Element>> connectionMaps = new HashMap<>();
	private Map<DiagramExportationReport, Map<String, Element>> elementMaps = new HashMap<>();

	/**
	 * The Constructor
	 * 
	 * @param resource
	 *            - The resource where the UML2 element are to be find
	 * @param descriptor
	 *            - The {@Link TxtUMLLayoutDescriptor} which contains the txtUML
	 *            Layout informations
	 * @throws ModelMapException
	 */
	public TxtUMLElementsMapper(Resource resource, TxtUMLLayoutDescriptor descriptor) {
		try {
			this.modelMapProviders = ModelMapUtils
					.collectModelMapProviders(descriptor.projectName, descriptor.mappingFolder, resource).values();

			init(modelMapProviders, descriptor);
		} catch (ModelMapException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Maps every txtUML element found in the description to UML2 model
	 * elements. Saves the mapping to the appropriate properties
	 * 
	 * @param modelMapProviders
	 * @param descriptor
	 */
	private void init(Collection<ModelMapProvider> modelMapProviders, TxtUMLLayoutDescriptor descriptor) {
		for (DiagramExportationReport report : descriptor.getReports()) {
			this.elementMaps.put(report, new HashMap<String, Element>());
			this.connectionMaps.put(report, new HashMap<String, Element>());

			// Nodes
			addNodesRecursively(report, report.getNodes());

			// Connections
			addConnections(report, report.getLinks(), report.getNodes());

			String refElementName = report.getReferencedElementName();
			findElement(refElementName).ifPresent(n -> this.elementMaps.get(report).put(refElementName, n));
		}
	}
	
	private void addNodesRecursively(DiagramExportationReport report, Set<RectangleObject> nodes) {
		for (RectangleObject node : nodes) {
			if(node.hasInner()){
				addNodesRecursively(report, node.getInner().Objects);
			}
			findElement(node.getName()).ifPresent((e) -> {
				this.elementMaps.get(report).put(node.getName(), e);
			});
		}
	}
	
	private void addConnections(DiagramExportationReport report, Set<LineAssociation> links, Set<RectangleObject> objects) {
		for (LineAssociation link : links) {
			addConnection(report, link);
		}
		
		addConnectionsRecursivelyFromObjects(report, objects);
	}

	
	
	private void addConnectionsRecursivelyFromObjects(DiagramExportationReport report, Set<RectangleObject> objects){
		objects.forEach(object -> {
			if(object.hasInner()){
				object.getInner().Assocs.forEach(assoc -> addConnection(report, assoc));
				addConnectionsRecursivelyFromObjects(report, object.getInner().Objects);
			}
		});
	}

	private void addConnection(DiagramExportationReport report,LineAssociation link) {
		Optional<? extends Element> elem = Optional.empty();
		if (link.getType() == AssociationType.generalization) {
			elem = findGeneralization(link);
		} else {
			elem = findAssociation(link);
			if (!elem.isPresent()) {
				elem = findTransition(link);
			}
		}
		elem.ifPresent((e) -> {
			this.connectionMaps.get(report).put(link.getId(), e);
		});
	}

	/**
	 * Finds the org.eclipse.uml2 element from a model according to the elements
	 * canonical name
	 * 
	 * @param nodeName
	 *            - The model elements canonical name
	 * @return The appropriate model element or null if not found
	 */
	private Optional<Element> findElement(String nodeName) {
		for (ModelMapProvider modelMapProvider : this.modelMapProviders) {
			Element elem = (Element) modelMapProvider.getByName(nodeName);
			if (elem != null) {
				return Optional.of(elem);
			}
		}
		return Optional.empty();
	}

	private Optional<Association> findAssociation(LineAssociation assoc) {
		Optional<Element> elem = findElement(assoc.getId());
		if (elem.isPresent() && elem.get() instanceof Association) {
			return Optional.of((Association) elem.get());
		}
		return Optional.empty();
	}

	private Optional<Generalization> findGeneralization(LineAssociation generalization) {
		Optional<Element> superclass = findElement(generalization.getFrom());
		Optional<Element> subclass = findElement(generalization.getTo());

		if (superclass.isPresent() && subclass.isPresent() && subclass.get() instanceof Classifier) {
			List<Generalization> gens = ((Classifier) subclass.get()).getGeneralizations();
			for (Generalization gen : gens) {
				Classifier classif = gen.getGeneral();
				if (classif.equals(superclass.get())) {
					return Optional.of(gen);
				}
			}
		}
		return Optional.empty();
	}

	private Optional<Transition> findTransition(LineAssociation trans) {
		Optional<Element> elem = findElement(trans.getId());
		if (elem.isPresent() && elem.get() instanceof Transition) {
			return Optional.of((Transition) elem.get());
		}
		return Optional.empty();
	}

	/**
	 * Returns the roots elements of all reports
	 * 
	 * @return
	 */
	public List<Pair<DiagramExportationReport, Element>> getDiagramRootsWithDiagramNames(
			TxtUMLLayoutDescriptor descriptor) {
		List<Pair<DiagramExportationReport, Element>> roots = new ArrayList<>();
		for (Pair<String, DiagramExportationReport> pair : descriptor.getReportsWithDiagramNames()) {
			DiagramExportationReport report = pair.getSecond();
			String name = report.getReferencedElementName();
			findElement(name).ifPresent(e -> roots.add(new Pair<>(report, e)));
		}
		return roots;
	}

	public IDiagramElementsMapper getMapperForReport(DiagramExportationReport report) {
		switch (report.getType()) {
		case Class:
			return new ClassDiagramElementsMapper(this.elementMaps.get(report), this.connectionMaps.get(report));
		case StateMachine:
			return new StateMachineDiagramElementsMapper(this.elementMaps.get(report), this.connectionMaps.get(report));
		default:
			return null;
		}
	}
}
