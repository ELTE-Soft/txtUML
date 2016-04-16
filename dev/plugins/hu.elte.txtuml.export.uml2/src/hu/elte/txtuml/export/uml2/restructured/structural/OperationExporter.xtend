package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import hu.elte.txtuml.export.uml2.restructured.Exporter
import org.eclipse.jdt.core.dom.IMethodBinding
import org.eclipse.jdt.core.dom.MethodDeclaration
import org.eclipse.jdt.core.dom.SingleVariableDeclaration
import org.eclipse.uml2.uml.Operation
import org.eclipse.uml2.uml.ParameterDirectionKind
import org.eclipse.uml2.uml.Stereotype
import org.eclipse.jdt.core.dom.Modifier
import org.eclipse.jdt.core.dom.ITypeBinding
import hu.elte.txtuml.api.model.ModelClass

class OperationExporter extends Exporter<MethodDeclaration, IMethodBinding, Operation> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IMethodBinding s) {
		factory.createOperation
	}

	override exportContents(MethodDeclaration decl) {
		val binding = decl.resolveBinding
		result.name = binding.name
		if (binding.returnType.qualifiedName != void.canonicalName) {
			val retParam = factory.createParameter
			retParam.type = fetchType(binding.returnType)
			retParam.direction = ParameterDirectionKind.RETURN_LITERAL
			retParam.name = 'return'
			result.ownedParameters += retParam
		}
		result.redefinedOperations += binding.overridden.map[fetchElement as Operation]
		result.methods += fetchElement(decl.resolveBinding, new MethodActivityExporter(this))
		result.ownedParameters += decl.parameters.map [
			exportParameter((it as SingleVariableDeclaration).resolveBinding)
		]
		result.isAbstract = Modifier.isAbstract(binding.modifiers)
		result.isStatic = Modifier.isStatic(binding.getModifiers)
		if (decl.isConstructor) {
			result.applyStereotype(getImportedElement("Create") as Stereotype)
		}
	}

	def overridden(IMethodBinding meth) { getOverridden(meth, meth.declaringClass.superclass) }

	def Iterable<IMethodBinding> getOverridden(IMethodBinding meth, ITypeBinding cls) {
		if (cls == null || cls.qualifiedName == ModelClass.canonicalName) {
			return #[]
		}
		cls.declaredMethods.filter[meth.overrides(it)] + getOverridden(meth, cls.superclass)
	}

}