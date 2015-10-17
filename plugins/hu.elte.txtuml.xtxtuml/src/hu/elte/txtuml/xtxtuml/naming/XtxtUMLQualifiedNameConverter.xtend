package hu.elte.txtuml.xtxtuml.naming

import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.QualifiedName
import java.util.regex.Pattern

class XtxtUMLQualifiedNameConverter extends IQualifiedNameConverter.DefaultImpl {
	
	private static String assocEndDelimiterRegex = "\\:\\:|\\.";
	
	override toQualifiedName(String name) {
		val regex = Pattern.compile(assocEndDelimiterRegex);
		val matcher = regex.matcher(name);
		if (name != null && matcher.find) {
			QualifiedName.create(name.split(assocEndDelimiterRegex));
		} else {
			super.toQualifiedName(name);
		}
	}
	
}