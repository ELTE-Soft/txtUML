package hu.elte.txtuml.xtxtuml.validation;

import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociationEnd
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUMultiplicity
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

	@Check
	def checkContainerEndMultiplicity(TUAssociationEnd assocEnd) {
		val multipl = assocEnd.multiplicity;
		if (assocEnd.container && multipl != null) { // container end with explicit multiplicity
			warning("The multiplicity of container end " + assocEnd.name +
				" is implicitly 0..1 – the specified multiplicity will be ignored", assocEnd,
				TU_ASSOCIATION_END__MULTIPLICITY, WRONG_ASSOCIATION_END_MULTIPLICITY);
		}
	}

	@Check
	def checkMultiplicityIsNotCustom(TUAssociationEnd assocEnd) { // TODO support custom multiplicities
		val it = assocEnd.multiplicity;
		if (
			!( it == null // omitted
			|| any // *
			|| lower == 1 && !upperSet // 1
			|| (lower == 0 || lower == 1) && (upperInf || upper == 1) // 0|1..1|*
		)) {
			error("The multiplicity of association end " + assocEnd.name + " is invalid – "
				+ "custom multiplicities are not supported in XtxtUML yet, please use one of the following: 1, *, 0..1, 1..1, 0..*, 1..*",
				assocEnd, TU_ASSOCIATION_END__MULTIPLICITY, UNSUPPORTED_MULTIPLICITY);
		}
	}
	
	@Check
	def checkEmptyMultiplicity(TUMultiplicity multipl) {
		if (multipl != null && !multipl.any) { // explicit, not *
			val exactlyZero = multipl.lower == 0 && (!multipl.upperSet || !multipl.isUpperInf && multipl.upper == 0);
			val lowerIsGreaterThanUpper = multipl.upperSet && !multipl.upperInf && multipl.lower > multipl.upper;
			if (exactlyZero || lowerIsGreaterThanUpper) {
				warning("The effective multiplicity is zero", multipl,
					null, WRONG_MULTIPLICITY);
			}
		}
	}

}
