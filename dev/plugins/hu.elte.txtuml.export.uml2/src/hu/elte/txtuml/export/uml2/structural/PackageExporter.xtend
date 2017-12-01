package hu.elte.txtuml.export.uml2.structural

import hu.elte.txtuml.export.uml2.BaseExporter
import hu.elte.txtuml.export.uml2.ExportMode
import hu.elte.txtuml.export.uml2.Exporter
import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import java.io.File
import java.util.regex.Pattern
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.IPackageFragmentRoot
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Class
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.PackageableElement

abstract class AbstractPackageExporter<S, T extends Package> extends Exporter<S, S, T> {

	new(ExportMode mode) {
		super(mode)
	}

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	def exportPackageFragment(IPackageFragment packageFragment) {
		packageFragment.children.forEach[exportCompUnit(it as ICompilationUnit)]
		val packageRoot = packageFragment.parent as IPackageFragmentRoot
		val subPackages = packageRoot.children.map[it as IPackageFragment].filter [
			elementName.startsWith(packageFragment.elementName + ".")
		]
		subPackages.forEach[exportPackage[result.nestedPackages += it]]
	}

	def exportCompUnit(ICompilationUnit compUnit) {
		parseCompUnit(compUnit).types.filter[t | !ElementTypeTeller.isExternal(t as TypeDeclaration)].forEach[exportType]
	}

	override storePackaged(PackageableElement pkg) {
		result.packagedElements += pkg
	}

	def dispatch exportType(TypeDeclaration decl) {
		switch decl {
			case ElementTypeTeller.isModelClass(decl):
				exportClass(decl)[result.packagedElements += it]
			case ElementTypeTeller.isAssociation(decl):
				exportAssociation(decl)[result.packagedElements += it]
			case ElementTypeTeller.isSignal(decl): {
				exportSignal(decl)[result.packagedElements += it]
				exportSignalEvent(decl)[result.packagedElements += it]
				exportSignalFactory(decl)[result.packagedElements += it]
			}
			case ElementTypeTeller.isDataType(decl.resolveBinding): {
				exportDataType(decl)[result.packagedElements += it]
			}
			case ElementTypeTeller.isConnector(decl): {
				val end = decl.bodyDeclarations.filter[it instanceof TypeDeclaration].map[it as TypeDeclaration].
					findFirst [
						val binding = it.resolveBinding
						ElementTypeTeller.isContained(binding.superclass.typeArguments.get(0))
					]

				val otherEnd = end.resolveBinding.superclass.typeArguments.get(0).declaringClass.declaredTypes.findFirst [
					it != end
				]

				val owner = otherEnd.superclass.typeArguments.get(0)

				val connector = exportConnector(decl)[(owner.fetchType as Class).ownedConnectors += it]
				val connectorType = exportConnectorType(decl)[result.packagedElements += it]
				connector.type = connectorType
			}
			case ElementTypeTeller.isInterface(decl): {
				exportInterface(decl)[result.packagedElements += it]
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

class PackageExporter extends AbstractPackageExporter<IPackageFragment, Package> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(IPackageFragment pf) { factory.createPackage }

	override exportContents(IPackageFragment s) {
		result.name = s.elementName.split(Pattern.quote(".")).last
		exportPackageFragment(s)
	}
}
