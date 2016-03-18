package hu.elte.txtuml.export.uml2.restructured

import java.util.HashMap
import java.util.Map
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.UMLFactory

class ExporterCache {
	
	val Map<Object, Element> cache = new HashMap();
	protected val factory = UMLFactory.eINSTANCE
	
	def <S,R extends Element> R export(Exporter<S,R> exporter, S source) {
		if (cache.containsKey(source)) {
			return cache.get(source) as R
		} else {
			exporter.export(source)
		}
	}
		
}