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
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ClassDiagramElementsArranger;
import hu.elte.txtuml.export.papyrus.elementsarrangers.IDiagramElementsArranger;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager {

	protected ClassDiagramElementCreator elementCreator;
	protected ClassDiagramElementsProvider elementsProvider;
	protected ClassDiagramElementsArranger arranger;

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
			TransactionalEditingDomain domain, ClassDiagramElementsArranger arranger, IProgressMonitor monitor) {
		super(diagram);
		this.elementCreator = new ClassDiagramElementCreator(domain); // TODO:
																		// Consider
																		// DI
		this.arranger = arranger;
		try {
			this.arranger.arrange(monitor);
		} catch (ArrangeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
			TransactionalEditingDomain domain, ClassDiagramElementsArranger arranger) {
		this(diagram, provider, domain, arranger, new NullProgressMonitor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.
	 * AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram() {
		elementsProvider.getClasses().forEach((clazz) -> this.elementCreator.createClassForDiagram(this.diagram, clazz,
				this.arranger.getBoundsForElement(clazz), this.monitor));
		elementsProvider.getSignals().forEach((signal) -> this.elementCreator.createSignalForDiagram(this.diagram,
				signal, this.arranger.getBoundsForElement(signal), this.monitor));

		elementsProvider.getAssociations().forEach((assoc) -> {
			// A txtUML scpecific implementation. Assoiciations are exported
			// in a way that they are the owner of both ends
			// TODO: This is NOT sure (Must ask nboldi), and logic has to be
			// moved to the creator function.
			Property member1 = assoc.getMemberEnds().get(0);
			Property member2 = assoc.getMemberEnds().get(1);
			Type memberT1 = member1.getType();
			Type memberT2 = member2.getType();

			List<Point> route = this.arranger.getRouteForConnection(assoc);
			String sourceAnchor = this.arranger.getSourceAnchorForConnection(assoc);
			String targetAnchor = this.arranger.getTargetAnchorForConnection(assoc);
			this.elementCreator.createAssociationForNodes((Classifier) memberT1, (Classifier) memberT2, assoc,
					this.diagram, route, sourceAnchor, targetAnchor, this.monitor);
		});

		elementsProvider.getGeneralizations()
				.forEach((generalization) -> this.elementCreator.createGeneralizationForNodes(generalization,
						this.arranger.getRouteForConnection(generalization), this.diagram, this.monitor));
	}
}
