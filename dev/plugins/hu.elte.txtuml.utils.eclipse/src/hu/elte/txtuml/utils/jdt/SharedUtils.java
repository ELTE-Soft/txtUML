package hu.elte.txtuml.utils.jdt;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.stream.Stream;

import org.eclipse.core.resources.IResource;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.Annotation;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SingleMemberAnnotation;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import hu.elte.txtuml.utils.Sneaky;

public final class SharedUtils {

	private SharedUtils() {
	}

	/**
	 * Decides if the given type declaration declares a type that is assignable
	 * from the specified class.
	 * 
	 * @param typeDeclaration
	 *            The given type declaration.
	 * @param specifiedClass
	 *            The specified class.
	 * @return The decision.
	 */
	public static boolean typeIsAssignableFrom(TypeDeclaration typeDeclaration, Class<?> specifiedClass) {
		return typeIsAssignableFrom(typeDeclaration.resolveBinding(), specifiedClass);
	}

	public static boolean typeIsAssignableFrom(ITypeBinding typeBinding, Class<?> specifiedClass) {
		String className = specifiedClass.getCanonicalName();
		while (typeBinding != null) {
			if (className.equals(typeBinding.getErasure().getQualifiedName())) {
				return true;
			}
			if (typeImplementsInterfaceDirectly(typeBinding, className)) {
				return true;
			}

			typeBinding = typeBinding.getSuperclass();
		}
		return false;
	}

	private static boolean typeImplementsInterfaceDirectly(ITypeBinding type, String interfaceName) {
		for (ITypeBinding implementedInterface : type.getInterfaces()) {
			if (interfaceName.equals(implementedInterface.getErasure().getQualifiedName())) {
				return true;
			}
		}
		return false;
	}

	public static Expression obtainSingleMemberAnnotationValue(BodyDeclaration declaration, Class<?> annotationClass) {
		for (Object mod : declaration.modifiers()) {
			IExtendedModifier modifier = (IExtendedModifier) mod;
			if (modifier.isAnnotation()) {
				Annotation annotation = (Annotation) modifier;
				if (annotation.isSingleMemberAnnotation() && identicalAnnotations(annotation, annotationClass)) {
					SingleMemberAnnotation singleMemberAnnot = (SingleMemberAnnotation) annotation;
					return singleMemberAnnot.getValue();
				}
			}
		}
		return null;
	}

	private static boolean identicalAnnotations(Annotation annotation, Class<?> annotationClass) {
		return annotation.resolveAnnotationBinding().getAnnotationType().getQualifiedName()
				.equals(annotationClass.getCanonicalName());
	}

	public static MethodDeclaration findMethodDeclarationByName(TypeDeclaration owner, String name) {
		for (MethodDeclaration methodDeclaration : owner.getMethods()) {
			if (methodDeclaration.getName().getFullyQualifiedName().equals(name)) {
				return methodDeclaration;
			}
		}
		return null;
	}

	public static CompilationUnit[] parseICompilationUnitStream(Stream<ICompilationUnit> stream,
			IJavaProject javaProject) throws IOException, JavaModelException {

		// Sneaky.<JavaModelException> Throw();
		// Sneaky.<IOException> Throw();
		return stream.map(ICompilationUnit::getResource).map(IResource::getLocationURI).map(File::new)
				.map(Sneaky.unchecked(f -> SharedUtils.parseJavaSource(f, javaProject))).filter(Objects::nonNull)
				.toArray(CompilationUnit[]::new);
	}

	/**
	 * Parses the specified Java source file located in the given Java project.
	 * 
	 * @param sourceFile
	 *            The specified Java source file.
	 * @param project
	 *            The given Java project.
	 * @return The parsed compilation unit.
	 * @throws IOException
	 *             Thrown when I/O error occurs during reading the file.
	 * @throws JavaModelException
	 */
	public static CompilationUnit parseJavaSource(File sourceFile, IJavaProject project)
			throws IOException, JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		char[] content = SharedUtils.getFileContents(sourceFile);

		parser.setSource(content);
		parser.setProject(project);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setUnitName(sourceFile.getName());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		CompilationUnit compilationUnit = (CompilationUnit) parser.createAST(null);

		return compilationUnit;
	}

	/**
	 * Gets the contents of the specified source file.
	 * 
	 * @param sourceFile
	 *            The specified source file.
	 * @return The contents of the specified source file stored in a character
	 *         array.
	 * @throws IOException
	 *             Thrown when I/O error occurs during reading the file.
	 */
	public static char[] getFileContents(File sourceFile) throws IOException {
		Path path = Paths.get(sourceFile.getAbsolutePath());
		return new String(Files.readAllBytes(path)).toCharArray();
	}

	public static boolean isActionCall(MethodInvocation methodInvocation) {
		IMethodBinding methodBinding = methodInvocation.resolveMethodBinding();
		ITypeBinding declaringClass = methodBinding.getDeclaringClass();
		return typeIsAssignableFrom(declaringClass, hu.elte.txtuml.api.model.Action.class);
	}

	public static String qualifiedName(TypeDeclaration decl) {
		String name = decl.getName().getIdentifier();
		ASTNode parent = decl.getParent();
		// resolve full name e.g.: A.B
		while (parent != null && parent.getClass() == TypeDeclaration.class) {
			name = ((TypeDeclaration) parent).getName().getIdentifier() + "." + name;
			parent = parent.getParent();
		}
		// resolve fully qualified name e.g.: some.package.A.B
		if (decl.getRoot().getClass() == CompilationUnit.class) {
			CompilationUnit root = (CompilationUnit) decl.getRoot();
			if (root.getPackage() != null) {
				PackageDeclaration pack = root.getPackage();
				name = pack.getName().getFullyQualifiedName() + "." + name;
			}
		}
		return name;
	}
}
