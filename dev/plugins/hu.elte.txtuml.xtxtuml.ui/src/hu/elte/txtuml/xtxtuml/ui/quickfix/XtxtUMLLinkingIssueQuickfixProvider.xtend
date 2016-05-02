package hu.elte.txtuml.xtxtuml.ui.quickfix

import com.google.inject.Inject
import hu.elte.txtuml.xtxtuml.common.XtxtUMLUtils
import hu.elte.txtuml.xtxtuml.xtxtUML.TUAssociation
import hu.elte.txtuml.xtxtuml.xtxtUML.TUClass
import hu.elte.txtuml.xtxtuml.xtxtUML.TUComposition
import hu.elte.txtuml.xtxtuml.xtxtUML.TUConnector
import hu.elte.txtuml.xtxtuml.xtxtUML.XtxtUMLPackage
import org.eclipse.core.runtime.NullProgressMonitor
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.jdt.core.compiler.IProblem
import org.eclipse.jdt.core.search.IJavaSearchConstants
import org.eclipse.jdt.core.search.IJavaSearchScope
import org.eclipse.jdt.core.search.SearchPattern
import org.eclipse.jdt.internal.compiler.env.AccessRestriction
import org.eclipse.jdt.internal.core.search.BasicSearchEngine
import org.eclipse.jdt.internal.core.search.IRestrictedAccessTypeRequestor
import org.eclipse.xtext.common.types.JvmDeclaredType
import org.eclipse.xtext.common.types.TypesPackage
import org.eclipse.xtext.common.types.xtext.ui.JdtTypeRelevance
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.scoping.IScopeProvider
import org.eclipse.xtext.ui.editor.model.IXtextDocument
import org.eclipse.xtext.ui.editor.model.edit.IModificationContext
import org.eclipse.xtext.ui.editor.quickfix.IssueResolutionAcceptor
import org.eclipse.xtext.validation.Issue
import org.eclipse.xtext.xbase.XbasePackage
import org.eclipse.xtext.xbase.imports.IImportsConfiguration
import org.eclipse.xtext.xbase.typesystem.util.CommonTypeComputationServices
import org.eclipse.xtext.xbase.ui.contentassist.ReplacingAppendable
import org.eclipse.xtext.xbase.ui.quickfix.JavaTypeQuickfixes

import static org.eclipse.xtext.util.Strings.isEmpty

class XtxtUMLLinkingIssueQuickfixProvider extends JavaTypeQuickfixes {

	@Inject extension IImportsConfiguration;
	@Inject extension XtxtUMLUtils;

	@Inject ReplacingAppendable.Factory appendableFactory;
	@Inject JdtTypeRelevance jdtTypeRelevance;
	@Inject IScopeProvider scopeProvider;
	@Inject CommonTypeComputationServices services;
	@Inject XtxtUMLTypeNameGuesser typeNameGuesser;

	/**
	 * Adds relevant XtxtUML import proposals.
	 */
	override addQuickfixes(Issue issue, IssueResolutionAcceptor issueResolutionAcceptor, IXtextDocument xtextDocument,
		XtextResource resource, EObject referenceOwner, EReference unresolvedReference) {
		val referenceString = xtextDocument.get(issue.offset, issue.length);

		if (unresolvedReference == TypesPackage.Literals.JVM_PARAMETERIZED_TYPE_REFERENCE__TYPE ||
			unresolvedReference == XbasePackage.Literals.XABSTRACT_FEATURE_CALL__FEATURE ||
			unresolvedReference == XbasePackage.Literals.XCONSTRUCTOR_CALL__CONSTRUCTOR) {
			addJvmTypeImportQuickfixes(issue, referenceString, issueResolutionAcceptor, referenceOwner,
				unresolvedReference);
		} else if (unresolvedReference.EReferenceType.EPackage.nsURI == XtxtUMLPackage.eNS_URI) {
			addXtxtUMLImportQuickfixes(issue, referenceString, issueResolutionAcceptor, referenceOwner,
				unresolvedReference);
		}
	}

	def private addJvmTypeImportQuickfixes(Issue issue, String referenceString,
		IssueResolutionAcceptor issueResolutionAcceptor, EObject referenceOwner, EReference unresolvedReference) {
		val javaSearchScope = referenceOwner.javaSearchScope;
		val contextType = referenceOwner.contextJvmDeclaredType;
		createImportProposals(contextType, issue, referenceString, javaSearchScope, issueResolutionAcceptor);
	}

	def private addXtxtUMLImportQuickfixes(Issue issue, String referenceString,
		IssueResolutionAcceptor issueResolutionAcceptor, EObject referenceOwner, EReference unresolvedReference) {

		val referenceStringSegments = referenceString.split("\\.");
		if (referenceStringSegments.size > 2) { // we do not deal with partially package-qualified names
			return;
		}

		// don't use java search here, the relevant entities are already in the scope
		for (scopeElement : scopeProvider.getScope(referenceOwner, unresolvedReference).allElements) {
			val qualifiedName = scopeElement.qualifiedName.toString;

			if (qualifiedName.endsWith("." + referenceString)) {
				var String importName = null;
				if (referenceStringSegments.size == 1) {
					importName = qualifiedName;
				} else {
					val containerDescription = scopeElement.EContainerDescription;
					val container = containerDescription?.EObjectOrProxy;

					if (container instanceof TUAssociation || container instanceof TUClass ||
						container instanceof TUComposition || container instanceof TUConnector) { // check our potential nested "classes"
						importName = containerDescription?.qualifiedName?.toString;
					}
				}

				if (importName != null) { // create the proposal
					val simpleName = importName.split("\\.").last;
					val label = '''Import '«simpleName»' («importName») '''
					val importNameFinal = importName; // to make it referrable inside the local class below
					if (!issueResolutionAcceptor.issueResolutions.exists[it.label == label]) {
						issueResolutionAcceptor.accept(issue, label, label,
							"impc_obj.gif", [ EObject element, IModificationContext context |
								val importAppendable = appendableFactory.create(context.getXtextDocument(),(
									element.eResource as XtextResource), 0, 0);

								importAppendable.importSection.addImport(importNameFinal);
								importAppendable.insertNewImports();
							], jdtTypeRelevance.getRelevance(qualifiedName, referenceString) + 100);
					}
				}
			}
		}
	}

	/**
	 * Overrides the super behavior to make import proposals case sensitive.
	 */
	override protected createImportProposals(JvmDeclaredType contextType, Issue issue, String typeName,
		IJavaSearchScope searchScope, IssueResolutionAcceptor acceptor) {
		if (contextType != null) {
			val visibilityHelper = contextType.visibilityHelper;
			val packageAndType = typeNameGuesser.guessPackageAndTypeName(contextType, typeName);
			val wantedPackageName = packageAndType.first;

			val searchEngine = new BasicSearchEngine();
			val wantedPackageChars = if(isEmpty(wantedPackageName)) null else wantedPackageName.toCharArray;
			val wantedTypeName = packageAndType.second;

			searchEngine.searchAllTypeNames(wantedPackageChars, SearchPattern.R_EXACT_MATCH, wantedTypeName.toCharArray,
				SearchPattern.R_EXACT_MATCH.bitwiseOr(SearchPattern.R_CASE_SENSITIVE), IJavaSearchConstants.TYPE, // the only modification is the added case sensitivity
				searchScope,
				new IRestrictedAccessTypeRequestor {

					override acceptType(int modifiers, char[] packageName, char[] simpleTypeName,
						char[][] enclosingTypeNames, String path, AccessRestriction access) {
						val qualifiedTypeName = getQualifiedTypeName(packageName, enclosingTypeNames, simpleTypeName);
						if (access == null ||
							(access.problemId != IProblem.ForbiddenReference && !access.ignoreIfBetter)) {
							val importType = services.typeReferences.findDeclaredType(qualifiedTypeName, contextType);

							if (importType instanceof JvmDeclaredType &&
								visibilityHelper.isVisible(importType as JvmDeclaredType)) {
								val label = new StringBuilder("Import '");
								label.append(simpleTypeName);
								label.append("' (");
								label.append(packageName);

								if (enclosingTypeNames != null && enclosingTypeNames.length > 0) {
									for (enclosingTypeName : enclosingTypeNames) {
										label.append(".");
										label.append(enclosingTypeName);
									}
								}

								label.append(")");
								acceptor.accept(issue, label.toString, label.toString,
									"impc_obj.gif", [ EObject element, IModificationContext context |
										val appendable = appendableFactory.create(context.xtextDocument,
											element.eResource as XtextResource, 0, 0);
										appendable.append(
											services.typeReferences.findDeclaredType(qualifiedTypeName, element));
										appendable.insertNewImports;

									], jdtTypeRelevance.getRelevance(qualifiedTypeName, wantedTypeName) + 100);
							}
						}
					}
				}, IJavaSearchConstants.WAIT_UNTIL_READY_TO_SEARCH, new NullProgressMonitor);
		}
	}

}
