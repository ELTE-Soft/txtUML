package hu.elte.txtuml.export.uml2.restructured.structural

import hu.elte.txtuml.export.uml2.restructured.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import java.io.File
import java.util.regex.Pattern
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Package
import hu.elte.txtuml.export.uml2.restructured.BaseExporter

abstract class AbstractPackageExporter<T extends Package> extends Exporter<IPackageFragment, IPackageFragment, T> {

	new() {
	}

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	override exportContents(IPackageFragment packageFragment) {
		packageFragment.children.forEach[exportCompUnit(it as ICompilationUnit)]
		val packageRoot = packageFragment.parent as IPackageFragmentRoot
		val subPackages = packageRoot.children.map[it as IPackageFragment].filter [
			elementName.startsWith(packageFragment.elementName + ".")
		]
		subPackages.forEach[exportPackage[result.nestedPackages += it]]
	}

	def exportCompUnit(ICompilationUnit compUnit) {
		parseCompUnit(compUnit).types.forEach[exportType]
	}

	def dispatch exportType(TypeDeclaration decl) {
		switch decl {
			case ElementTypeTeller.isModelClass(decl):
				exportClass(result, decl)[result.packagedElements += it]
			case ElementTypeTeller.isAssociation(decl):
				exportAssociation(decl)[result.packagedElements += it]
			case ElementTypeTeller.isSignal(decl): {
				exportSignal(decl)[result.packagedElements += it]
				exportSignalEvent(decl)[result.packagedElements += it]
			}
			case ElementTypeTeller.isDataType(decl.resolveBinding): {
				exportDataType(decl)[result.packagedElements += it]
			}
			default:
				throw new IllegalArgumentException("Illegal type declaration: " + decl.toString)
		}
	}

	def dispatch exportType(AbstractTypeDeclaration decl) {
		throw new IllegalArgumentException(decl.toString)
	}

	def parseCompUnit(ICompilationUnit compUnit) {
		SharedUtils.parseJavaSource(new File(compUnit.resource.locationURI), compUnit.javaProject)
	}
}

class PackageExporter extends AbstractPackageExporter<Package> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IPackageFragment pf) { factory.createPackage }

	override exportContents(IPackageFragment s) {
		result.name = s.elementName.split(Pattern.quote(".")).last
		super.exportContents(s)
	}
}