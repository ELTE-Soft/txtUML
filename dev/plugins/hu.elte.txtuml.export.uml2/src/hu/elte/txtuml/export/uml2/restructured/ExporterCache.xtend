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
import java.util.function.Consumer

/**
 * The exporter cache keeps tabs on which elements have been partially or fully exported. For identifying the
 * element it stores both the access key and the exporter (multiple exporters can export the same element).
 */
class ExporterCache {

	/** Fully exported elements */
	val Map<Pair<Class<?>, Object>, Element> cache = new HashMap
	
	/** Partially exported elements */
	// QUESTION: do we have to keep elements that have been partially exported?
	val Map<Pair<Class<?>, Object>, Element> fetchMap = new HashMap

	protected val factory = UMLFactory.eINSTANCE

	/** 
	 * Exports the given element if it hadn't been exported. Checks if it is already partially or fully 
	 * exported and completes the process.
	 * 
	 * @param store Pre-store action, called before exportContents.
	 */
	def <S, A, R extends Element> R export(Exporter<S, A, R> exporter, S source, A access, Consumer<? super R> store) {
		val exported = cache.get(new Pair(exporter.class, source))
		if (exported != null) {
			return exported as R
		}
		val accessKey = generateAccessKey(exporter.class, access)
		val fetched = fetchMap.get(accessKey) as R;
		if (fetched != null) {
			exporter.alreadyExists(fetched)
			store.accept(fetched)
			exporter.exportContents(source)
			return fetched
		}
		val justExported = exporter.createResult(access)
		if (justExported != null) {
			cache.put(new Pair(exporter.class, source), justExported)
			fetchMap.put(accessKey, justExported)
			store.accept(justExported)
			exporter.exportContents(source)
		}
		return justExported
	}

	/** Fetches the given element if it is already partially exported, or partially exports it otherwise. */
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

	protected def generateAccessKey(Class<?> exporterClass, Object key) {
		new Pair(exporterClass, generateSourceAccessKey(key))
	}

	protected def dispatch Object generateSourceAccessKey(IVariableBinding access) {
		val method = access.declaringMethod
		if (method != null) {
			generateSourceAccessKey(method) + access.name
		} else {
			val cls = access.declaringClass
			generateSourceAccessKey(cls) + access.name
		}
	}

	protected def dispatch Object generateSourceAccessKey(IMethodBinding access) {
		val cls = access.declaringClass
		if (cls != null) {
			generateSourceAccessKey(cls) + access.name
		}
	}

	protected def dispatch Object generateSourceAccessKey(ITypeBinding access) {
		access.qualifiedName
	}

	protected def dispatch Object generateSourceAccessKey(IPackageFragment pack) {
		pack.elementName
	}

	protected def dispatch Object generateSourceAccessKey(ASTNode node) { node }

	protected def dispatch Object generateSourceAccessKey(Object obj) {
		throw new IllegalArgumentException(obj.toString)
	}

}