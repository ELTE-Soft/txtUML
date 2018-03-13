package hu.elte.txtuml.utils.jdt;

import java.util.stream.Stream;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageDeclaration;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldAccess;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IAnnotationBinding;
import org.eclipse.jdt.core.dom.IBinding;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.IVariableBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.api.model.Association;
import hu.elte.txtuml.api.model.AssociationEnd;
import hu.elte.txtuml.api.model.AssociationEnd.Container;
import hu.elte.txtuml.api.model.BehaviorPort;
import hu.elte.txtuml.api.model.Composition;
import hu.elte.txtuml.api.model.ConnectorBase;
import hu.elte.txtuml.api.model.ConnectorBase.ConnectorEnd;
import hu.elte.txtuml.api.model.DataType;
import hu.elte.txtuml.api.model.Delegation;
import hu.elte.txtuml.api.model.External;
import hu.elte.txtuml.api.model.ExternalBody;
import hu.elte.txtuml.api.model.GeneralCollection;
import hu.elte.txtuml.api.model.Interface;
import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.model.ModelClass.InPort;
import hu.elte.txtuml.api.model.ModelClass.OutPort;
import hu.elte.txtuml.api.model.ModelClass.Port;
import hu.elte.txtuml.api.model.ModelEnum;
import hu.elte.txtuml.api.model.Signal;
import hu.elte.txtuml.api.model.StateMachine.Choice;
import hu.elte.txtuml.api.model.StateMachine.CompositeState;
import hu.elte.txtuml.api.model.StateMachine.Initial;
import hu.elte.txtuml.api.model.StateMachine.State;
import hu.elte.txtuml.api.model.StateMachine.Transition;
import hu.elte.txtuml.api.model.StateMachine.Vertex;

/**
 * This class provides utilities for telling the types of txtUML model elements.
 */
public final class ElementTypeTeller {

	private ElementTypeTeller() {
	}

	public static boolean isModelElement(CompilationUnit unit) {
		if (unit.getPackage() == null) {
			// the model cannot be in default package
			return false;
		}
		return isModelPackage((IPackageFragment) unit.getPackage().resolveBinding().getJavaElement());
	}

	/**
	 * Checks a package if it belong to an existing model. Searches for a
	 * package-info.java compilation unit in the package or one of the ancestor
	 * packages and checks if it has the {@link Model} annotation.
	 */
	public static boolean isModelPackage(IPackageFragment pack) {
		try {
			IJavaProject javaProject = pack.getJavaProject();
			String packageName = pack.getElementName();
			for (IPackageFragmentRoot pfRoot : javaProject.getPackageFragmentRoots()) {
				if (!pfRoot.isExternal()) {
					if (isModelPackage(pfRoot, packageName)) {
						return true;
					}
				}
			}
		} catch (JavaModelException e) {
			// TODO: use PluginLogWrapper
			e.printStackTrace();
		}
		return false;
	}

	public static boolean isModelPackage(IPackageFragmentRoot pfRoot, String packageName) throws JavaModelException {
		IPackageFragment pack;
		while (!packageName.isEmpty()) {
			pack = pfRoot.getPackageFragment(packageName);
			if (pack.exists() && isModelRootPackage(pack)) {
				return true;
			}
			int lastDot = packageName.lastIndexOf(".");
			if (lastDot == -1) {
				lastDot = 0;
			}
			packageName = packageName.substring(0, lastDot);
		}
		return false;
	}

	private static boolean isModelRootPackage(IPackageFragment pack) throws JavaModelException {
		ICompilationUnit[] compilationUnits = pack.getCompilationUnits();
		for (ICompilationUnit compUnit : compilationUnits) {
			if (compUnit.getElementName().equals("package-info.java")) {
				if (checkPackageInfo(compUnit)) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean checkPackageInfo(ICompilationUnit compUnit) throws JavaModelException {
		for (IPackageDeclaration packDecl : compUnit.getPackageDeclarations()) {
			for (IAnnotation annot : packDecl.getAnnotations()) {
				// Because names are not resolved in IJavaElement AST
				// representation, we have to manually check if a given
				// annotation is really the Model annotation.
				if (isImportedNameResolvedTo(compUnit, annot.getElementName(), Model.class.getCanonicalName())) {
					return true;
				}
			}
		}
		return false;
	}

	private static boolean isImportedNameResolvedTo(ICompilationUnit compUnit, String elementName,
			String qualifiedName) {
		if (qualifiedName.equals(elementName)) {
			return true;
		}
		if (!qualifiedName.endsWith(elementName)) {
			return false;
		}
		int lastSection = qualifiedName.lastIndexOf(".");
		String pack = qualifiedName.substring(0, lastSection);
		return (compUnit.getImport(qualifiedName).exists() || compUnit.getImport(pack + ".*").exists());
	}

	public static boolean isModelClass(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, ModelClass.class);
	}

	public static boolean isModelClass(ITypeBinding type) {
		return hasSuperClass(type, ModelClass.class.getCanonicalName());
	}

	public static boolean isDataType(ITypeBinding type) {
		return hasSuperClass(type, DataType.class.getCanonicalName());
	}

	public static boolean isVertex(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Vertex.class);
	}

	public static boolean isVertex(ITypeBinding type) {
		return hasSuperClass(type, Vertex.class.getCanonicalName());
	}

	public static boolean isInitialPseudoState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Initial.class);
	}

	public static boolean isInitialPseudoState(ITypeBinding type) {
		return hasSuperClass(type, Initial.class.getCanonicalName());
	}

	public static boolean isChoicePseudoState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Choice.class);
	}

	public static boolean isChoicePseudoState(ITypeBinding type) {
		return hasSuperClass(type, Choice.class.getCanonicalName());
	}

	public static boolean isState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, State.class);
	}

	public static boolean isState(ITypeBinding value) {
		return hasSuperClass(value, State.class.getCanonicalName());
	}

	public static boolean isCompositeState(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, CompositeState.class);
	}

	public static boolean isSimpleState(TypeDeclaration typeDeclaration) {
		return isState(typeDeclaration) && !isCompositeState(typeDeclaration);
	}

	public static boolean isTransition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Transition.class);
	}

	public static boolean isTransition(ITypeBinding value) {
		return hasSuperClass(value, Transition.class.getCanonicalName());
	}

	public static boolean isSignal(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Signal.class);
	}

	public static boolean isSignal(ITypeBinding value) {
		return hasSuperClass(value, Signal.class.getCanonicalName());
	}

	public static boolean isAssociation(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Association.class);
	}

	public static boolean isAssociation(ITypeBinding binding) {
		return hasSuperClass(binding, Association.class.getCanonicalName());
	}

	public static boolean isAssociationeEnd(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, AssociationEnd.class);
	}

	public static boolean isAssociationEnd(ITypeBinding value) {
		return hasSuperClass(value, AssociationEnd.class.getCanonicalName());
	}

	public static boolean isConnectorEnd(ITypeBinding value) {
		return hasSuperClass(value, ConnectorEnd.class.getCanonicalName());
	}

	public static boolean isComposition(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Composition.class);
	}

	public static boolean isContainer(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Container.class);
	}
	
	public static boolean isContainer(ITypeBinding typeBinding) {
		return SharedUtils.typeIsAssignableFrom(typeBinding, Container.class);
	}

	@SuppressWarnings("unchecked")
	public static boolean isContained(TypeDeclaration declaration) {
		TypeDeclaration parent = (TypeDeclaration) declaration.getParent();
		return parent.bodyDeclarations().stream().filter(d -> d != declaration)
				.anyMatch(d -> isContainer((TypeDeclaration) d));
	}

	public static boolean isContained(ITypeBinding value) {
		ITypeBinding parent = (ITypeBinding) value.getDeclaringClass();
		return Stream.of(parent.getDeclaredTypes()).filter(d -> d != value)
				.anyMatch(d -> isContainer(d));
	}
	
	public static boolean isPort(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Port.class);
	}

	public static boolean isPort(ITypeBinding value) {
		return hasSuperClass(value, Port.class.getCanonicalName());
	}
	
	public static boolean isInPort(ITypeBinding binding) {
		return hasSuperClass(binding, InPort.class.getCanonicalName());
	}
	
	public static boolean isOutPort(ITypeBinding binding) {
		return hasSuperClass(binding, OutPort.class.getCanonicalName());
	}

	public static boolean isInterface(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, Interface.class);
	}

	public static boolean isInterface(ITypeBinding bnd) {
		return SharedUtils.typeIsAssignableFrom(bnd, Interface.class);
	}

	public static boolean isConnector(TypeDeclaration typeDeclaration) {
		return SharedUtils.typeIsAssignableFrom(typeDeclaration, ConnectorBase.class);
	}

	public static boolean isConnector(ITypeBinding typeDeclaration) {
		return hasSuperClass(typeDeclaration, ConnectorBase.class.getCanonicalName());
	}

	public static boolean isSpecificClassifier(TypeDeclaration classifierDeclaration) {
		ITypeBinding superclassBinding = classifierDeclaration.resolveBinding().getSuperclass();
		if (superclassBinding == null) {
			return false;
		}
		String superclassQualifiedName = superclassBinding.getQualifiedName();
		boolean extendsModelClass = superclassQualifiedName.equals(ModelClass.class.getCanonicalName());
		boolean extendsSignal = superclassQualifiedName.equals(Signal.class.getCanonicalName());

		return !extendsModelClass && !extendsSignal;
	}

	public static boolean isAbstract(ITypeBinding type) {
		return (type.getModifiers() & Modifier.ABSTRACT) != 0;
	}

	public static boolean isAbstract(TypeDeclaration typeDeclaration) {
		for (Object elem : typeDeclaration.modifiers()) {
			IExtendedModifier extendedModifier = (IExtendedModifier) elem;
			if (extendedModifier.isModifier()) {
				Modifier modifier = (Modifier) extendedModifier;
				if (modifier.isAbstract()) {
					return true;
				}
			}
		}
		return false;
	}

	/*public static boolean isExternalClass(ITypeBinding type) {
		return isModelClass(type) && containsExternalAnnotation(type.getAnnotations());
	}
	private static boolean containsExternalAnnotation(IAnnotationBinding[] annotations) {
		return Stream.of(annotations).anyMatch(a -> a.getAnnotationType().getErasure().getQualifiedName().equals(External.class.getCanonicalName()));
	}*/
	public static boolean isModelEnum(EnumDeclaration enumDeclaration) {
		return SharedUtils.typeIsAssignableFrom(enumDeclaration, ModelEnum.class);
	}

	public static boolean isModelEnum(ITypeBinding binding) {
		return binding.isEnum() && SharedUtils.typeIsAssignableFrom(binding, ModelEnum.class);
	}

	public static boolean isEffect(MethodDeclaration method) {
		return method.getName().toString().equals("effect");
	}

	public static boolean isGuard(MethodDeclaration method) {
		return method.getName().toString().equals("guard");
	}

	public static boolean isFinal(FieldDeclaration node) {
		for (Object modifier : node.modifiers()) {
			if (modifier instanceof Modifier && ((Modifier) modifier).isFinal()) {
				return true;
			}
		}
		return false;
	}

	public static boolean isExternal(ITypeBinding typeBinding) {
		return SharedUtils.obtainAnnotation(typeBinding, External.class) != null;
	}
	
	public static boolean isExternal(IVariableBinding varBinding) {
		return SharedUtils.obtainAnnotation(varBinding, External.class) != null;
	}
	
	public static boolean isExternal(BodyDeclaration declaration) {
		return SharedUtils.obtainAnnotation(declaration, External.class) != null;
	}

	public static boolean hasExternalBody(MethodDeclaration declaration) {
		return SharedUtils.obtainAnnotation(declaration, ExternalBody.class) != null;
	}

	public static boolean hasSuperInterface(ITypeBinding type, String superInterfaceName) {
		if (type.getQualifiedName().equals(superInterfaceName)) {
			return true;
		} else {
			for (ITypeBinding ifaceType : type.getInterfaces()) {
				if (hasSuperInterface(ifaceType, superInterfaceName)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean hasSuperClass(ITypeBinding type, String superClassName) {
		while (type != null && !type.getErasure().getQualifiedName().equals(superClassName)) {
			type = type.getSuperclass();
		}
		return type != null;
	}

	public static boolean isBehavioralPort(TypeDeclaration typeDeclaration) {
		for (IAnnotationBinding annot : typeDeclaration.resolveBinding().getAnnotations()) {
			if (annot.getAnnotationType().getQualifiedName().equals(BehaviorPort.class.getCanonicalName())) {
				return true;
			}
		}
		return false;
	}

	public static boolean isVariable(Expression expr) {
		if (!(expr instanceof Name)) {
			return false;
		}
		IBinding binding = ((Name) expr).resolveBinding();
		if (!(binding instanceof IVariableBinding)) {
			return false;
		}
		IVariableBinding varBinding = (IVariableBinding) binding;
		return !varBinding.isField();
	}

	public static boolean isFieldAccess(Expression expr) {
		if (expr instanceof FieldAccess) {
			return true;
		}
		if (!(expr instanceof Name)) {
			return false;
		}
		IBinding binding = ((Name) expr).resolveBinding();
		if (!(binding instanceof IVariableBinding)) {
			return false;
		}
		IVariableBinding varBinding = (IVariableBinding) binding;
		return varBinding.isField();
	}

	public static boolean isDelegation(ITypeBinding binding) {
		return SharedUtils.typeIsAssignableFrom(binding, Delegation.class);
	}

	public static boolean isCollection(TypeDeclaration binding) {
		return SharedUtils.typeIsAssignableFrom(binding, GeneralCollection.class);
	}

}
