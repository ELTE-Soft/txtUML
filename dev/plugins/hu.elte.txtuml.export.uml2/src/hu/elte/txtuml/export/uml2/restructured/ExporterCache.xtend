package hu.elte.txtuml.export.uml2.restructured

import java.util.HashMap
import java.util.Map
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.ITypeBinding
import org.eclipse.jdt.core.dom.IVariableBinding
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.UMLFactory
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.ASTNode

class ExporterCache {

	val Map<Pair<Class<?>, Object>, Element> cache = new HashMap
	val Map<Pair<Class<?>, Object>, Element> fetchMap = new HashMap

	protected val factory = UMLFactory.eINSTANCE

	def <S, A, R extends Element> R export(Exporter<S, A, R> exporter, S source, A access) {
		val exported = cache.get(new Pair(exporter.class,source))
		if (exported != null) {
			return exported as R
		}
		val accessKey = generateAccessKey(exporter.class, access)
		val fetched = fetchMap.get(accessKey) as R;
		if (fetched != null) {
			exporter.alreadyExists(fetched)
			exporter.exportContents(source)
			return fetched
		}
		val justExported = exporter.createResult(access)
		if (justExported != null) {
			cache.put(new Pair(exporter.class,source), justExported)
			fetchMap.put(accessKey, justExported)
			exporter.exportContents(source)
		}
		return justExported
	}

	def <S, A, R extends Element> R fetch(Exporter<S, A, R> exporter, A access) {
		val accessKey = generateAccessKey(exporter.class, access)
		if (fetchMap.containsKey(accessKey)) {
			return fetchMap.get(accessKey) as R
		} else {
			val exported = exporter.createResult(access)
			fetchMap.put(accessKey, exported)
			return exported
		}
	}
	
	def generateAccessKey(Class<?> exporterClass, Object key) {
		new Pair(exporterClass, generateSourceAccessKey(key))
	}
	
	def dispatch Object generateSourceAccessKey(IVariableBinding access) {
		val method = access.declaringMethod
		if (method != null) {
			generateSourceAccessKey(method) + access.name
		} else {
			val cls = access.declaringClass
			generateSourceAccessKey(cls) + access.name
		}
	}
	
	def dispatch Object generateSourceAccessKey(IMethodBinding access) {
		val cls = access.declaringClass
		if (cls != null) {
			generateSourceAccessKey(cls) + access.name
		}
	}
	
	def dispatch Object generateSourceAccessKey(ITypeBinding access) {
		access.qualifiedName
	}
	
	def dispatch Object generateSourceAccessKey(IPackageFragment pack) {
		pack.elementName
	}
	
	def dispatch Object generateSourceAccessKey(ASTNode node) { node }
	
	def dispatch Object generateSourceAccessKey(Object obj) {
		throw new IllegalArgumentException(obj.toString)
	}

}