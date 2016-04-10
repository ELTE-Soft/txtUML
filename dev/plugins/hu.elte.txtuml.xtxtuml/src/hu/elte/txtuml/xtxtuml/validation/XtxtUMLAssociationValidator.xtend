package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import org.eclipse.xtext.validation.Check

import static hu.elte.txtuml.xtxtuml.validation.XtxtUMLIssueCodes.*
import static hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage.Literals.*

class XtxtUMLAssociationValidator extends XtxtUMLClassValidator {

	@Check
	def checkAssociationHasExactlyTwoEnds(TUAssociation association) {
		if (association.ends.length != 2) {
			error("Association " + association.name + " must have exactly two ends", association,
				TU_MODEL_ELEMENT__NAME, ASSOCIATION_END_COUNT_MISMATCH);
		}
	}

	@Check
	def checkContainerEndIsAllowedAndNeededOnlyInComposition(TUAssociation association) {
		val containerEnds = association.ends.filter[isContainer];
		if (association instanceof TUComposition) {
			if (containerEnds.length != 1) {
				error("Composition " + association.name + " must have exactly one container end", association,
					TU_MODEL_ELEMENT__NAME, CONTAINER_END_COUNT_MISMATCH);
			}
		} else {
			containerEnds.forEach [
				error("Container end " + name + " of association " + association.name +
					" must not be present in an association", it, TU_ASSOCIATION_END__CONTAINER,
					CONTAINER_END_IN_ASSOCIATION);
			]
		}
	}

}
