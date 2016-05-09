package hu.elte.txtuml.export.papyrus.layout.txtuml;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.Relationship;

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
	private Map<String, Relationship> connectionMap = new HashMap<>();
	private Map<String, Element> elementMap = new HashMap<>();

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
			// Nodes
			for (RectangleObject node : report.getNodes()) {
				findElement(node.getName()).ifPresent((e) -> {
					this.elementMap.put(node.getName(), e);
				});
			}

			// Connections
			for (LineAssociation link : report.getLinks()) {
				Optional<? extends Relationship> elem = Optional.empty();
				if (link.getType() == AssociationType.generalization) {
					elem = findGeneralization(link);
				} else {
					elem = findAssociation(link);
				}
				elem.ifPresent((e) -> {
					this.connectionMap.put(link.getId(), e);
				});
			}
		}
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

	/**
	 * Returns the roots elements of all reports
	 * 
	 * @return
	 */
	public List<Pair<DiagramExportationReport, Element>> getDiagramRootsWithDiagramNames(TxtUMLLayoutDescriptor descriptor) {
		List<Pair<DiagramExportationReport, Element>> roots = new ArrayList<>();
		for (Pair<String, DiagramExportationReport> pair : descriptor.getReportsWithDiagramNames()) {
			DiagramExportationReport report = pair.getSecond();
			String name = report.getReferencedElementName();
			findElement(name).ifPresent(e -> roots.add(new Pair<>(report, e)));
		}
		return roots;
	}

	public Element findNode(String object) {
		return this.elementMap.get(object);
	}

	public Relationship findConnection(String object) {
		return this.connectionMap.get(object);
	}

	public Collection<Element> getNodes(DiagramExportationReport report) {
		return this.elementMap.values();
	}

	public Collection<Relationship> getConnections(DiagramExportationReport report) {
		return this.connectionMap.values();
	}
}
