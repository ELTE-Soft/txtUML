package hu.elte.txtuml.xtxtuml.validation

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*

class XtxtUMLAssociationValidator extends XtxtUMLClassValidator {

	@Check
	def checkAssociationsHaveExactlyTwoEnds(TUAssociation association) {
		val numberOfEnds = association.ends.length
		if (2 != numberOfEnds) {
			error("Association " + association.name + " must have exactly two ends", association,
				XtxtUMLPackage.eINSTANCE.TUModelElement_Name, ASSOCIATION_END_COUNT_MISMATCH, numberOfEnds.toString)
		}
	}

	@Check
	def checkContainerEndIsAllowedAndNeededOnlyInComposition(TUAssociation association) {
		val containerEnds = association.ends.filter[isContainer]
		val numberOfContainerEnds = containerEnds.length
		if (association instanceof TUComposition) {
			if (1 != numberOfContainerEnds) {
				error("Composition " + association.name + " must have exactly one container end", association,
					XtxtUMLPackage.eINSTANCE.TUModelElement_Name, CONTAINER_END_COUNT_MISMATCH, association.name)
			}
		} else {
			containerEnds.forEach [
				error("Container end " + it.name + " must not be present in an association", it,
					XtxtUMLPackage.eINSTANCE.TUAssociationEnd_Container, ASSOCIATION_CONTAINS_CONTAINER_END, it.name)
			]
		}
	}

}
