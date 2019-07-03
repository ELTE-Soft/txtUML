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
		val multipl = assocEnd.collection.multiplicity;
		if (assocEnd.container && multipl != null) { // container end with explicit multiplicity
			warning("The multiplicity of container end " + assocEnd.name +
				" is implicitly 0..1 – the specified multiplicity will be ignored", assocEnd.collection,
				TU_ASSOCIATION_END_COLLECTION__MULTIPLICITY, WRONG_ASSOCIATION_END_MULTIPLICITY);
		}
	}

	
	@Check
	def checkOrderingUniqnessAtMaxOneMultiplicity(TUAssociationEnd assocEnd) {
		val it = assocEnd.collection.multiplicity;
		if ((assocEnd.collection.modifiers.ordered || assocEnd.collection.modifiers.unique) 
			&& (it == null	|| (lower == 1 && !upperSet || upper == 1))
		) {
			error("The multiplicity of association end " + assocEnd.name + " is invalid – "
				+ "in case of ordered or unique collection upper multiplicity must be at least 2",
				assocEnd.collection, TU_ASSOCIATION_END_COLLECTION__MULTIPLICITY, ORDERED_UNIQUE_ONE_MULTIPLICITY);
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
