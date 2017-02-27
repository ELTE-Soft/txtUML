package hu.elte.txtuml.export.javascript.json.model.cd;

import org.eclipse.persistence.oxm.annotations.XmlAccessMethods;
import org.eclipse.uml2.uml.Association;
import org.eclipse.uml2.uml.Class;
import org.eclipse.uml2.uml.Classifier;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.layout.visualizer.model.LineAssociation;

/**
 * 
 * Holds information about an association which has end names, multiplicities,
 * visibilities, navigabilities (and can be a composition)
 * 
 */
public class ClassAttributeLink extends ClassLink {

	@XmlAccessMethods(getMethodName = "getFrom")
	private AssociationEnd from;
	@XmlAccessMethods(getMethodName = "getTo")
	private AssociationEnd to;
	@XmlAccessMethods(getMethodName = "getName")
	protected String name;

	/**
	 * No-arg constructor required for serialization
	 */
	protected ClassAttributeLink() {
	}

	/**
	 * Creates a ClassAttributeLink based on the EMF-UML model-elements and
	 * layout information provided
	 * 
	 * @param layout
	 *            the layout informations of this link
	 * @param assoc
	 *            the EMF-UML model-element which holds informations of this
	 *            diagram element
	 * @param fromClass
	 *            the EMF-UML model Class belonging to the from end of this
	 *            association
	 * @param toClass
	 *            the EMF-UML model Class belonging to the to end of this
	 *            association
	 * @throws UnexpectedEndException
	 *             Exception is thrown if an association's end could not be
	 *             linked to the EMF-UML model
	 */
	public ClassAttributeLink(LineAssociation layout, Association assoc, Classifier fromClass, Classifier toClass)
			throws UnexpectedEndException {
		super(layout);
		name = assoc.getLabel();
		from = null;
		to = null;
		for (Property end : assoc.getMemberEnds()) {
			Class endClass = (Class) end.getType();
			// when handling reflexive links then first add the to end's, then
			// the from end's model information (to match the Papyrus exporter
			// behavior)
			if (endClass == toClass && to == null) {
				to = new AssociationEnd(end);
			} else if (endClass == fromClass && from == null) {
				from = new AssociationEnd(end);
			} else {
				throw new UnexpectedEndException(end.getName());
			}
		}
	}

	/**
	 * 
	 * @return the AssocitonEnd representing the from end
	 */
	public AssociationEnd getFrom() {
		return from;
	}

	/**
	 * 
	 * @return the AssocitonEnd representing the to end
	 */
	public AssociationEnd getTo() {
		return to;
	}

	/**
	 * 
	 * @return the name of the association
	 */
	public String getName() {
		return name;
	}

}
