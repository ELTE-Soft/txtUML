package hu.elte.txtuml.export.uml2.restructured

import java.util.HashMap
import java.util.Map
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.UMLFactory

class ExporterCache {

	val Map<Object, Element> cache = new HashMap
	val Map<Object, Element> fetchMap = new HashMap

	protected val factory = UMLFactory.eINSTANCE

	def <S, A, R extends Element> R export(Exporter<S, A, R> exporter, S source, A access) {
		val exported = cache.get(source)
		if (exported != null) {
			return exported as R
		}
		val fetched = fetchMap.get(access) as R;
		if (fetched != null) {
			exporter.exportExisting(source, fetched)
			return fetched
		}
		val justExported = exporter.export(source, access)
		cache.put(source, justExported)
		fetchMap.put(access, justExported)
		return justExported
	}

	def <S, A, R extends Element> R fetch(Exporter<S, A, R> exporter, A access) {
		if (fetchMap.containsKey(access)) {
			return fetchMap.get(access) as R
		} else {
			val exported = exporter.create(access)
			fetchMap.put(access, exported)
			return exported
		}
	}

}