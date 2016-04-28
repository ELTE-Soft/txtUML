package hu.elte.txtuml.xtxtuml.common;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.core.search.SearchPattern;
import org.eclipse.jdt.core.search.TypeNameRequestor;
import org.eclipse.xtext.common.types.access.jdt.IJdtTypeProvider;
import org.eclipse.xtext.common.types.xtext.ui.JdtBasedSimpleTypeScope;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.resource.EObjectDescription;
import org.eclipse.xtext.resource.IEObjectDescription;

import com.google.common.base.Predicate;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.inject.Inject;

public class XtxtUMLReferenceProposalTypeScope extends JdtBasedSimpleTypeScope {

	private static List<String> allowedJavaClassTypes = Arrays.asList("Boolean", "Double", "Integer", "String");

	@Inject
	IQualifiedNameProvider qualifiedNameProvider;

	public XtxtUMLReferenceProposalTypeScope(IJdtTypeProvider typeProvider,
			IQualifiedNameConverter qualifiedNameConverter, Predicate<IEObjectDescription> filter) {
		super(typeProvider, qualifiedNameConverter, filter);
	}

	/**
	 * Returns an appropriate scope consisting of allowed Java class types
	 * {@link Boolean}, {@link Double}, {@link Integer} and {@link String}.
	 */
	@Override
	protected Iterable<IEObjectDescription> internalGetAllElements() {
		IJavaProject javaProject = getTypeProvider().getJavaProject();
		if (javaProject == null)
			return Collections.emptyList();
		final List<IEObjectDescription> allScopedElements = Lists.newArrayListWithExpectedSize(25000);
		try {
			IJavaSearchScope searchScope = SearchEngine.createJavaSearchScope(new IJavaElement[] { javaProject });

			// don't add primitives, we handle them as keywords

			TypeNameRequestor nameMatchRequestor = new TypeNameRequestor() {
				@Override
				public void acceptType(int modifiers, char[] packageName, char[] simpleTypeName,
						char[][] enclosingTypeNames, String path) {
					StringBuilder fqName = new StringBuilder(packageName.length + simpleTypeName.length + 1);
					if (packageName.length != 0) {
						fqName.append(packageName);
						fqName.append('.');
					}
					for (char[] enclosingType : enclosingTypeNames) {
						fqName.append(enclosingType);
						fqName.append('.');
					}
					fqName.append(simpleTypeName);
					String fullyQualifiedName = fqName.toString();
					InternalEObject proxy = createProxy(fullyQualifiedName);
					Map<String, String> userData = null;
					if (enclosingTypeNames.length == 0) {
						userData = ImmutableMap.of("flags", String.valueOf(modifiers));
					} else {
						userData = ImmutableMap.of("flags", String.valueOf(modifiers), "inner", "true");
					}
					IEObjectDescription eObjectDescription = EObjectDescription
							.create(getQualifiedNameConverter().toQualifiedName(fullyQualifiedName), proxy, userData);
					if (eObjectDescription != null)
						allScopedElements.add(eObjectDescription);
				}
			};

			// start of modified code

			for (String allowedType : allowedJavaClassTypes) {
				new SearchEngine().searchAllTypeNames("java.lang".toCharArray(), SearchPattern.R_EXACT_MATCH,
						allowedType.toCharArray(), SearchPattern.R_EXACT_MATCH, IJavaSearchConstants.CLASS, searchScope,
						nameMatchRequestor, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, new NullProgressMonitor());
			}

			// end of modified code

		} catch (JavaModelException e) {
			// ignore
		}
		return allScopedElements;
	}

}
