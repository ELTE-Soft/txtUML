package hu.elte.txtuml.export.papyrus.elementsmanagers;

import java.util.Arrays;
import java.util.List;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.gmf.runtime.notation.Diagram;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Comment;
import org.eclipse.uml2.uml.Component;
import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Element;
import org.eclipse.uml2.uml.Enumeration;
import org.eclipse.uml2.uml.Generalization;
import org.eclipse.uml2.uml.InformationItem;
import org.eclipse.uml2.uml.InstanceSpecification;
import org.eclipse.uml2.uml.Interface;
import org.eclipse.uml2.uml.InterfaceRealization;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Package;
import org.eclipse.uml2.uml.PrimitiveType;
import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.Realization;
import org.eclipse.uml2.uml.Signal;
import org.eclipse.uml2.uml.Type;

import hu.elte.txtuml.export.papyrus.UMLModelManager;
import hu.elte.txtuml.export.papyrus.api.ClassDiagramElementCreator;

/**
 * An abstract class for adding/removing elements to ClassDiagrams.
 */
public class ClassDiagramElementsManager extends AbstractDiagramElementsManager {

	protected ClassDiagramElementCreator elementCreator;

	private static final List<java.lang.Class<? extends Element>> elementsToBeAdded = Arrays.asList(Class.class,
			Component.class, DataType.class, Enumeration.class, InformationItem.class, InstanceSpecification.class,
			Interface.class, Model.class, Package.class, PrimitiveType.class, Comment.class, Signal.class);

	private static final List<java.lang.Class<? extends Element>> connectorsToBeAdded = Arrays.asList(Association.class,
			Generalization.class, InterfaceRealization.class, Realization.class);
	
	/**
	 * The Constructor
	 * 
	 * @param diagram - The diagram which should be populated
	 * @param domain - The TransactionalEditingDomain
	 * @param monitor - A ProgressMonitor which can be informed about the state of diagram generation 
	 */
	public ClassDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain, IProgressMonitor monitor) {
		super(diagram);
		this.elementCreator = new ClassDiagramElementCreator(domain);
		this.monitor = monitor;
	}
	
	/**
	 * The Constructor
	 * 
	 * @param diagram - The diagram which should be populated
	 * @param domain - The TransactionalEditingDomain 
	 */
	public ClassDiagramElementsManager(Diagram diagram, TransactionalEditingDomain domain) {
		this(diagram, domain, new NullProgressMonitor());
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see hu.elte.txtuml.export.papyrus.elementsmanagers.
	 * AbstractDiagramElementsManager#addElementsToDiagram(java.util.List)
	 */
	@Override
	public void addElementsToDiagram(List<Element> elements) {
		List<Element> diagramelements = UMLModelManager.getElementsOfTypesFromList(elements, elementsToBeAdded);
		List<Element> diagramconnections = UMLModelManager.getElementsOfTypesFromList(elements, connectorsToBeAdded);

		for (Element e : diagramelements) {
			if (e instanceof Class)
				this.elementCreator.createClassForDiagram(this.diagram, (Class) e, null, this.monitor);
			if (e instanceof Signal) {
				this.elementCreator.createSignalForDiagram(this.diagram, (Signal) e, null, this.monitor);
			}
		}

		for (Element e : diagramconnections) {
			if (e instanceof Association) {
				Association assoc = (Association) e;
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
			}

			if (e instanceof Generalization) {
				this.elementCreator.createGeneralizationForNodes((Generalization) e, null, this.diagram, this.monitor);
			}
		}
	}
}
