package hu.elte.txtuml.xtxtuml.ui.quickfix;

import org.eclipse.emf.ecore.EObject
import org.eclipse.xtext.util.Tuples
import org.eclipse.xtext.xbase.ui.quickfix.TypeNameGuesser

class XtxtUMLTypeNameGuesser extends TypeNameGuesser {

	/**
	 * Overrides the super implementation such that even one letter long capital name
	 * segments are considered to be classes.
	 */
	override guessPackageAndTypeName(EObject context, String referenceString) {
		val simpleNames = referenceString.split("(\\.|::|\\$)");
		val packageName = new StringBuilder;

		var isFirstSegment = true;
		for (simpleName : simpleNames) {
			if (Character.isUpperCase(simpleName.charAt(0))) {
				return Tuples.create(packageName.toString(), simpleName);
			}

			if (isFirstSegment) {
				isFirstSegment = false;
			} else {
				packageName.append(".");
			}

			isFirstSegment = false;
			packageName.append(simpleName);
		}

		return Tuples.create(packageName.toString(), "");
	}

}
