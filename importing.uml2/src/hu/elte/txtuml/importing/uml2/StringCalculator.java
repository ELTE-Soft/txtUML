package hu.elte.txtuml.importing.uml2;

import org.eclipse.uml2.uml.NamedElement;

public class StringCalculator {
	public static String fromQualifiedName(String name) {
		String newName = "";
		try {
			newName = name.replaceAll("::", ".");
		} catch (Exception e) {
		}

		String[] parts = newName.split("\\.");
		newName = parts[0];
		for (int i = 1; i < parts.length; ++i) {
			if (!parts[i].equals(parts[i - 1])) {
				newName += "." + parts[i];
			}
		}

		return newName;
	}

	public static String toFieldName(String name) {
		int index = 0;

		for (int i = 0; i < name.length(); ++i) {
			if (Character.isUpperCase(name.charAt(i))) {
				index = i;
				break;
			}
		}

		return name.substring(index);
	}

	public static boolean isMatch(org.w3c.dom.Element eElement, NamedElement e) {
		return toFieldName(eElement.getAttribute("name")).equals(
				fromQualifiedName(e.getQualifiedName()));
	}
}
