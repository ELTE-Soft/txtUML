package hu.elte.txtuml.xtxtuml;

import org.eclipse.xtext.generator.IGenerator;
import org.eclipse.xtext.naming.IQualifiedNameConverter;
import org.eclipse.xtext.scoping.IScopeProvider;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.validation.ConfigurableIssueCodesProvider;
import org.eclipse.xtext.xbase.compiler.XbaseCompiler;
import org.eclipse.xtext.xbase.scoping.batch.ImplicitlyImportedFeatures;
import org.eclipse.xtext.xbase.typesystem.computation.ITypeComputer;
import org.eclipse.xtext.xbase.typesystem.internal.DefaultReentrantTypeResolver;
import org.eclipse.xtext.xbase.typesystem.util.ExtendedEarlyExitComputer;
import org.eclipse.xtext.xbase.util.XExpressionHelper;

import com.google.inject.Binder;
import com.google.inject.name.Names;

import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLCompiler;
import hu.elte.txtuml.xtxtuml.compiler.XtxtUMLGenerator;
import hu.elte.txtuml.xtxtuml.jvmmodel.XtxtUMLTypesBuilder;
import hu.elte.txtuml.xtxtuml.naming.IPackageNameCalculator;
import hu.elte.txtuml.xtxtuml.naming.XtxtUMLPackageNameCalculator;
import hu.elte.txtuml.xtxtuml.naming.XtxtUMLQualifiedNameConverter;
import hu.elte.txtuml.xtxtuml.scoping.XtxtUMLImplicitlyImportedFeatures;
import hu.elte.txtuml.xtxtuml.scoping.XtxtUMLXImportSectionNamespaceScopeProvider;
import hu.elte.txtuml.xtxtuml.typesystem.XtxtUMLEarlyExitComputer;
import hu.elte.txtuml.xtxtuml.typesystem.XtxtUMLReentrantTypeResolver;
import hu.elte.txtuml.xtxtuml.typesystem.XtxtUMLTypeComputer;
import hu.elte.txtuml.xtxtuml.validation.XtxtUMLConfigurableIssueCodes;
import hu.elte.txtuml.xtxtuml.validation.XtxtUMLExpressionHelper;

/**
 * Use this class to register components to be used at runtime / without the
 * Equinox extension registry.
 */
public class XtxtUMLRuntimeModule extends AbstractXtxtUMLRuntimeModule {

	public Class<? extends ImplicitlyImportedFeatures> bindImplicitlyImportedFeatures() {
		return XtxtUMLImplicitlyImportedFeatures.class;
	}

	public void configureIScopeProviderDelegate(Binder binder) {
		binder.bind(IScopeProvider.class).annotatedWith(Names.named(AbstractDeclarativeScopeProvider.NAMED_DELEGATE))
				.to(XtxtUMLXImportSectionNamespaceScopeProvider.class);
	}

	public Class<? extends XbaseCompiler> bindXbaseCompiler() {
		return XtxtUMLCompiler.class;
	}

	public Class<? extends ITypeComputer> bindITypeComputer() {
		return XtxtUMLTypeComputer.class;
	}

	public Class<? extends ExtendedEarlyExitComputer> bindExtendedEarlyExitComputer() {
		return XtxtUMLEarlyExitComputer.class;
	}

	public Class<? extends IQualifiedNameConverter> bindIQualifiedNameConverter() {
		return XtxtUMLQualifiedNameConverter.class;
	}

	public Class<? extends XtxtUMLTypesBuilder> bindXtxtUMLTypesBuilder() {
		return XtxtUMLTypesBuilder.class;
	}

	public Class<? extends IGenerator> bindIGenerator() {
		return XtxtUMLGenerator.class;
	}

	public Class<? extends IPackageNameCalculator> bindIPackageNameCalculator() {
		return XtxtUMLPackageNameCalculator.class;
	}

	@Override
	public Class<? extends DefaultReentrantTypeResolver> bindDefaultReentrantTypeResolver() {
		return XtxtUMLReentrantTypeResolver.class;
	}

	public Class<? extends XExpressionHelper> bindXExpressionHelper() {
		return XtxtUMLExpressionHelper.class;
	}

	@Override
	public Class<? extends ConfigurableIssueCodesProvider> bindConfigurableIssueCodesProvider() {
		return XtxtUMLConfigurableIssueCodes.class;
	}
}
