package hu.elte.txtuml.export.uml2.restructured

import java.util.HashMap
import java.util.Map
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.UMLFactory
import org.eclipse.jdt.core.IPackageFragment

class ExporterCache {

	val Map<Object, Element> cache = new HashMap
	val Map<String, Element> fetchMap = new HashMap

	protected val factory = UMLFactory.eINSTANCE

	def <S, A, R extends Element> R export(Exporter<S, A, R> exporter, S source, A access) {
		val exported = cache.get(source)
		if (exported != null) {
			return exported as R
		}
		val accessKey = generateAccessKey(access)
		val fetched = fetchMap.get(accessKey) as R;
		if (fetched != null) {
			exporter.alreadyExists(fetched)
			exporter.exportContents(source)
			exporter.store()
			return fetched
		}
		val justExported = exporter.createResult(access)
		if (justExported != null) {
			cache.put(source, justExported)
			fetchMap.put(accessKey, justExported)
			exporter.exportContents(source)
			exporter.store()
		}
		return justExported
	}

	def <S, A, R extends Element> R fetch(Exporter<S, A, R> exporter, A access) {
		val accessKey = generateAccessKey(access)
		if (fetchMap.containsKey(accessKey)) {
			return fetchMap.get(accessKey) as R
		} else {
			val exported = exporter.createResult(access)
			fetchMap.put(accessKey, exported)
			return exported
		}
	}
	
	def dispatch String generateAccessKey(IVariableBinding access) {
		val method = access.declaringMethod
		if (method != null) {
			generateAccessKey(method) + access.name
		} else {
			val cls = access.declaringClass
			generateAccessKey(cls) + access.name
		}
	}
	
	def dispatch String generateAccessKey(IMethodBinding access) {
		val cls = access.declaringClass
		if (cls != null) {
			generateAccessKey(cls) + access.name
		}
	}
	
	def dispatch String generateAccessKey(ITypeBinding access) {
		access.qualifiedName
	}
	
	def dispatch String generateAccessKey(IPackageFragment pack) {
		pack.elementName
	}
	
	def dispatch String generateAccessKey(Object obj) {
		throw new IllegalArgumentException(obj.toString)
	}

}