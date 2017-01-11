package hu.elte.txtuml.xtxtuml.imports;

import com.google.inject.Inject
import org.eclipse.xtext.conversion.IValueConverter
import org.eclipse.xtext.formatting.IWhitespaceInformationProvider
import org.eclipse.xtext.resource.XtextResource
import org.eclipse.xtext.xbase.conversion.XbaseQualifiedNameValueConverter
import org.eclipse.xtext.xbase.imports.IImportsConfiguration
import org.eclipse.xtext.xbase.imports.ImportSectionRegionUtil
import org.eclipse.xtext.xbase.imports.RewritableImportSection
import org.eclipse.xtext.xtype.XImportDeclaration
import org.eclipse.xtext.xtype.XImportSection

/**
 * A specialized {@link RewritableImportSection}, which appends a ';' to
 * its import declarations.
 */
class XtxtUMLRewritableImportSection extends RewritableImportSection {

	public static class Factory extends RewritableImportSection.Factory {

		@Inject IImportsConfiguration importsConfiguration;
		@Inject IWhitespaceInformationProvider whitespaceInformationProvider;
		@Inject ImportSectionRegionUtil regionUtil;
		@Inject XbaseQualifiedNameValueConverter nameValueConverter;

		override parse(XtextResource resource) {
			new XtxtUMLRewritableImportSection(resource, importsConfiguration,
				importsConfiguration.getImportSection(resource),
				whitespaceInformationProvider.getLineSeparatorInformation(resource.URI).lineSeparator, regionUtil,
				nameValueConverter)
		}

		override createNewEmpty(XtextResource resource) {
			val xtxtUMLRewritableImportSection = new XtxtUMLRewritableImportSection(resource, importsConfiguration,
				null, whitespaceInformationProvider.getLineSeparatorInformation(resource.URI).lineSeparator,
				regionUtil, nameValueConverter);
			xtxtUMLRewritableImportSection.sort = true;
			return xtxtUMLRewritableImportSection;
		}
	}

	new(XtextResource resource, IImportsConfiguration importsConfiguration, XImportSection originalImportSection,
		String lineSeparator, ImportSectionRegionUtil regionUtil, IValueConverter<String> nameConverter) {
		super(resource, importsConfiguration, originalImportSection, lineSeparator, regionUtil, nameConverter);
	}

	override protected appendImport(StringBuilder builder, XImportDeclaration newImportDeclaration) {
		super.appendImport(builder, newImportDeclaration);
		builder.insert(builder.toString.replaceAll("\\s+$", "").length, ';');
	}

}
