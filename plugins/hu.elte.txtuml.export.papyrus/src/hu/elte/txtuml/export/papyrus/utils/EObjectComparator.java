package hu.elte.txtuml.export.papyrus.utils;

import java.util.Comparator;

import org.eclipse.emf.ecore.EObject;

/**
 * Compares two {@link EObject}
 * @author András Dobreff -
 * 	reference: http://jorgemanrubia.net/blog/wp-content/uploads/2008/07/emfcomparator.java
 */
public class EObjectComparator implements Comparator<EObject> {
	@Override
	public int compare(EObject object1, EObject object2) {
		String targetString1 = extractComparisonString(object1);
		String targetString2 = extractComparisonString(object2);

		return targetString1.compareTo(targetString2);
	}

	private String extractComparisonString(EObject object) {
		return object.toString().replaceAll(
				object.getClass().getName(), "").replaceAll(
				Integer.toHexString(object.hashCode()), "");
	}
}
