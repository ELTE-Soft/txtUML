package hu.elte.txtuml.seqdiag.handlers;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.core.expressions.PropertyTester;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;

import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;

public class SequenceDiagramDescriptionTester extends PropertyTester {

	@Override
	public boolean test(Object receiver, String property, Object[] args, Object expectedValue) {
		ICompilationUnit compilationUnit = (ICompilationUnit) receiver;
		if ("hasDescriptionType".equals(property)) {
			List<IType> types = null;
			try {
				types = Arrays.asList(compilationUnit.getAllTypes());
			} catch (JavaModelException ex) {
				return false;
			}
			boolean hasDiagramType = types.stream().anyMatch(ty -> {
				ITypeHierarchy tyHierarchy = null;
				try {
					tyHierarchy = ty.newSupertypeHierarchy(null);
				} catch (JavaModelException ex) {
					return false;
				}
				return Stream.of(tyHierarchy.getAllSupertypes(ty)).anyMatch(
						superTy -> superTy.getFullyQualifiedName().equals(SequenceDiagram.class.getCanonicalName()));
			});

			return expectedValue == null ? hasDiagramType : hasDiagramType == ((Boolean) expectedValue).booleanValue();
		}
		return false;
	}

}
