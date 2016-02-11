package hu.elte.txtuml.export.uml2.utils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
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
	 *
	 * @author Adam Ancsin
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
	 *
	 * @author �d�m Ancsin
	 */
	public static CompilationUnit parseJavaSource(File sourceFile, IJavaProject project)
			throws IOException, JavaModelException {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		char[] content = SharedUtils.getFileContents(sourceFile);
		String[] classpath = new String[0];
		String[] sourcepath = { sourceFile.getParent() };
		String[] encodings = { "UTF-8" };

		parser.setSource(content);
		parser.setProject(project);
		Map<?, ?> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);

		IClasspathEntry[] classpathEntries = project.getResolvedClasspath(true);
		String[] variableNames = new String[classpathEntries.length];
		IPath[] paths = new IPath[classpathEntries.length];
		int i = 0;
		for (IClasspathEntry entry : classpathEntries) {
			paths[i] = entry.getPath();
			variableNames[i] = project.encodeClasspathEntry(entry);
			++i;
		}
		JavaCore.setClasspathVariables(variableNames, paths, null);
		parser.setCompilerOptions(options);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		parser.setUnitName(sourceFile.getName());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setEnvironment(classpath, sourcepath, encodings, false);

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
	 *
	 * @author �d�m Ancsin
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
