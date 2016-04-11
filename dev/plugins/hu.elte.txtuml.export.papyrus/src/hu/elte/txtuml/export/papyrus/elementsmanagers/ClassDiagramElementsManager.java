package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import hu.elte.txtuml.export.papyrus.api.ClassDiagramElementCreator;
import hu.elte.txtuml.export.papyrus.elementproviders.ClassDiagramElementsProvider;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager {

	protected ClassDiagramElementCreator elementCreator;
	protected ClassDiagramElementsProvider elementsProvider;

	/**
	 * The Constructor
	 * 
	 * @param diagram
	 *            - The diagram which should be populated
	 * @param domain
	 *            - The TransactionalEditingDomain
	 * @param monitor
	 *            - A ProgressMonitor which can be informed about the state of
	 *            diagram generation
	 */
	public ClassDiagramElementsManager(Diagram diagram, ClassDiagramElementsProvider provider,
			TransactionalEditingDomain domain, IProgressMonitor monitor) {
		super(diagram);
		this.elementCreator = new ClassDiagramElementCreator(domain); // TODO:
																		// Consider
																		// DI
		this.elementsProvider = provider;
		this.monitor = monitor;
	}

	/**
	 * The Constructor
	 * 
	 * @param diagram
	 *            - The diagram which should be populated
	 * @param domain
	 *            - The TransactionalEditingDomain
	 */
	public ClassDiagramElementsManager(Diagram diagram, ClassDiagramElementsProvider provider,
			TransactionalEditingDomain domain) {
		this(diagram, provider, domain, new NullProgressMonitor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.
	 * AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements) {
		elementsProvider.getClasses()
				.forEach((clazz) -> this.elementCreator.createClassForDiagram(this.diagram, clazz, null, this.monitor));
		elementsProvider.getSignals().forEach(
				(signal) -> this.elementCreator.createSignalForDiagram(this.diagram, signal, null, this.monitor));

		elementsProvider.getAssociations().forEach((assoc) -> {
			// A txtUML scpecific implementation. Assoiciations are exported
			// in a way that they are the owner of both ends
			// TODO: This is NOT sure (Must ask nboldi), and logic has to be
			// moved to the creator function.
			Property member1 = assoc.getMemberEnds().get(0);
			Property member2 = assoc.getMemberEnds().get(1);
			Type memberT1 = member1.getType();
			Type memberT2 = member2.getType();

			List<Point> route = Arrays.asList(new Point(0, 50), new Point(50, 250), new Point(250, 250),
					new Point(250, 50), new Point(50, 0));
			this.elementCreator.createAssociationForNodes((Classifier) memberT1, (Classifier) memberT2, assoc,
					this.diagram, route, this.monitor);
		});

		elementsProvider.getGeneralizations().forEach((generalization) -> this.elementCreator
				.createGeneralizationForNodes(generalization, null, this.diagram, this.monitor));
	}
}
