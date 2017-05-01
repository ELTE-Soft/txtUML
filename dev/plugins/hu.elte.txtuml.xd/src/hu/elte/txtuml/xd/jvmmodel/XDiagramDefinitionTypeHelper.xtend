package hu.elte.txtuml.xd.jvmmodel

import hu.elte.txtuml.xd.xDiagramDefinition.DiagramSignature
import hu.elte.txtuml.xd.xDiagramDefinition.PackageDeclaration
import org.eclipse.core.runtime.Assert
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.JvmGenericType
import org.eclipse.xtext.common.types.JvmIdentifiableElement
import org.eclipse.xtext.xbase.typesystem.IBatchTypeResolver
import org.eclipse.xtext.xbase.typesystem.references.LightweightTypeReference

class XDiagramDefinitionTypeHelper {
	public IBatchTypeResolver currentTypeResolver;
	public DiagramSignature currentSignature;
	public PackageDeclaration currentPackageDecl;
	
	def LightweightTypeReference getActualType(JvmIdentifiableElement identifiable){
		if (identifiable == null) return null;
		if (currentTypeResolver == null) return null;
		val resolvedTypes = currentTypeResolver.resolveTypes(identifiable);
		if (resolvedTypes == null) return null;
		return resolvedTypes.getActualType(identifiable);
	}

	def boolean checkSuperTypes(JvmGenericType type, Class<?>... classes) {
		Assert.isNotNull(classes);
		val sup = classes.findFirst[type?.actualType?.getSuperType(it) != null];
		return sup != null;
	}

	def boolean checkDeclaringType(JvmGenericType type, String declaringTypeName) {
		Assert.isNotNull(type);
		if (declaringTypeName == null) {
			return type.declaringType == null;
		}

		var targetType = type as JvmDeclaredType;
		while (targetType != null) {
			if(targetType.qualifiedName.equals(declaringTypeName)) return true;
			targetType = targetType.declaringType;
		}
		return false;
	}

}
