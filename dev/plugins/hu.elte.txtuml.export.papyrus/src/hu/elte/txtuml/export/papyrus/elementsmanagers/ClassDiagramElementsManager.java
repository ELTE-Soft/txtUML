package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Type;

import hu.elte.txtuml.export.papyrus.api.elementcreators.ClassDiagramNotationManager;
import hu.elte.txtuml.export.papyrus.elementproviders.ClassDiagramElementsProvider;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ArrangeException;
import hu.elte.txtuml.export.papyrus.elementsarrangers.ClassDiagramElementsArranger;
import hu.elte.txtuml.utils.Logger;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager {

	protected ClassDiagramNotationManager notationManager;
	protected ClassDiagramElementsProvider elementsProvider;
	protected ClassDiagramElementsArranger arranger;

	public ClassDiagramElementsManager(Diagram diagram, ClassDiagramElementsProvider provider,
			TransactionalEditingDomain domain, ClassDiagramElementsArranger arranger, IProgressMonitor monitor) {
		super(diagram);
		this.notationManager = new ClassDiagramNotationManager(diagram, domain); // TODO:
																		// Consider
																		// DI
		this.arranger = arranger;
		try {
			this.arranger.arrange(monitor);
		} catch (ArrangeException e) {
			Logger.user.error(e.getMessage());
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.elementsProvider = provider;
		this.monitor = monitor;
	}

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
		this.elementsProvider.getClasses().forEach((clazz) -> this.notationManager.createClassForDiagram(clazz,
				this.arranger.getBoundsForElement(clazz), this.monitor));
		this.elementsProvider.getSignals().forEach((signal) -> this.notationManager.createSignalForDiagram(
				signal, this.arranger.getBoundsForElement(signal), this.monitor));

		this.elementsProvider.getAssociations().forEach((assoc) -> {
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
			this.notationManager.createAssociationForNodes((Classifier) memberT1, (Classifier) memberT2, assoc, route,
					sourceAnchor, targetAnchor, this.monitor);
		});

		this.elementsProvider.getGeneralizations()
				.forEach((generalization) -> this.notationManager.createGeneralizationForNodes(generalization,
						this.arranger.getRouteForConnection(generalization),
						this.arranger.getSourceAnchorForConnection(generalization),
						this.arranger.getTargetAnchorForConnection(generalization),this.monitor));
	}
}
