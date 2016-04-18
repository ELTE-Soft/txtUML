package hu.elte.txtuml.export.uml2.restructured.activity.apicalls

import hu.elte.txtuml.export.uml2.restructured.BaseExporter
import org.eclipse.jdt.core.dom.Expression
import org.eclipse.jdt.core.dom.MethodInvocation
import org.eclipse.jdt.core.dom.TypeLiteral
import org.eclipse.uml2.uml.CreateLinkAction
import org.eclipse.uml2.uml.LinkEndData
import hu.elte.txtuml.export.uml2.restructured.activity.ActionExporter
import org.eclipse.uml2.uml.ConnectorEnd
import org.eclipse.uml2.uml.Property
import org.eclipse.jdt.core.dom.ITypeBinding

class ConnectActionExporter extends ActionExporter<MethodInvocation, CreateLinkAction> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(MethodInvocation access) {
		if (isApiMethodInvocation(access.resolveMethodBinding) && access.resolveMethodBinding.name == "connect")
			factory.createCreateLinkAction
	}

	override exportContents(MethodInvocation source) {
		var LinkEndData leftEnd
		var LinkEndData rightEnd
		if (source.arguments.size == 4) {
			val leftLit = source.arguments.get(0) as TypeLiteral
			leftEnd = createEnd(factory.createLinkEndCreationData, leftLit.type.resolveBinding,
				source.arguments.get(1) as Expression)
			val rightLit = source.arguments.get(2) as TypeLiteral
			rightEnd = createEnd(factory.createLinkEndCreationData, rightLit.type.resolveBinding,
				source.arguments.get(3) as Expression)
		} else {
			val childLit = source.arguments.get(1) as TypeLiteral
			val childEndType = childLit.type.resolveBinding
			val parentEndType = childEndType.declaringClass.declaredTypes.findFirst[it != childEndType]
			leftEnd = createEnd(factory.createLinkEndCreationData, parentEndType, source.arguments.get(0) as Expression)
			rightEnd = createEnd(factory.createLinkEndCreationData, childEndType, source.arguments.get(2) as Expression)
		}
		result.endData += #[leftEnd, rightEnd]
		result.name = '''connect «leftEnd.value.name» to «rightEnd.value.name»'''
	}

	protected def createEnd(LinkEndData end, ITypeBinding endType, Expression endValue) {
		end.end = fetchElement(endType.superclass.typeArguments.get(0)) as Property
		if (endValue != null) {
			if (endValue instanceof MethodInvocation) {
				val endInvoke = endValue as MethodInvocation
				if (endInvoke.resolveMethodBinding.name == "port") {
					val expr = exportExpression(endInvoke.expression) ?:
						thisRef(endInvoke.resolveMethodBinding.declaringClass.fetchType)
					end.value = result.createInputValue(expr.name, end.end.type)
					expr.objectFlow(end.value)
					return end
				}
			}
			throw new IllegalArgumentException("Bad Action.connect format")
		}
		return end
	}

}