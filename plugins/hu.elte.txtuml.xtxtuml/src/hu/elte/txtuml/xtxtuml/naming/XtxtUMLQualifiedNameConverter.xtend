package hu.elte.txtuml.xtxtuml.naming

import org.eclipse.xtext.naming.IQualifiedNameConverter
import org.eclipse.xtext.naming.QualifiedName

class XtxtUMLQualifiedNameConverter extends IQualifiedNameConverter.DefaultImpl {

	override toQualifiedName(String name) {
		var lastWasColon = false
		val l = name.length
		var i = 0

		val current = new StringBuffer()
		val segments = <String>newArrayList

		while (i < l) {
			switch (c : name.charAt(i).toString) {
				case '.': {
					lastWasColon = false
					segments.add(current.toString)
					current.length = 0
				}
				case ':':
					if (lastWasColon) {
						lastWasColon = false
						segments.add(current.toString)
						current.length = 0
					} else {
						lastWasColon = true
					}
				default: {
					lastWasColon = false
					current.append(c)
				}
			}
			i++
		}
		segments.add(current.toString)

		return QualifiedName.create(segments)
	}
}
