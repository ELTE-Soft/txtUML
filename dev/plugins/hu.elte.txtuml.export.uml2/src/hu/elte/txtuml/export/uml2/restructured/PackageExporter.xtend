package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import java.io.File
import java.util.regex.Pattern
import org.eclipse.jdt.core.ICompilationUnit
import org.eclipse.jdt.core.IJavaElement
import org.eclipse.jdt.core.IPackageFragment
import org.eclipse.jdt.core.dom.TypeDeclaration
import org.eclipse.uml2.uml.Element
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.PackageableElement
import org.eclipse.uml2.uml.Type

abstract class AbstractPackageExporter<T extends Element> extends Exporter<IPackageFragment, T> {

	new() {}

	new(Exporter<?,?> parent) {
		super(parent);
	}

	def dispatch exportChild(IPackageFragment subPackage) {
		exportPackage(subPackage)
	}

	def dispatch exportChild(ICompilationUnit compUnit) {
		val unit = parseCompUnit(compUnit)
		unit.types.forEach[exportType]
	}

	def dispatch exportChild(IJavaElement other) {
		throw new IllegalArgumentException(other.toString);
	}

	def parseCompUnit(ICompilationUnit compUnit) {
		SharedUtils.parseJavaSource(new File(compUnit.resource.locationURI), compUnit.javaProject)
	}

	def exportType(TypeDeclaration typeDecl) {
		switch typeDecl {
			case ElementTypeTeller.isModelClass(typeDecl): exportClass(typeDecl)
			default: throw new IllegalArgumentException(typeDecl.toString)
		}
	}

}

class PackageExporter extends AbstractPackageExporter<Package> {

	new(Exporter<?,?> parent) {
		super(parent)
	}

	override create() { factory.createPackage }

	override exportContents(IPackageFragment s) {
		result.name = s.elementName.split(Pattern.quote(".")).last
	}

	override tryStore(Element contained) {
		switch contained {
			Type: result.ownedTypes.add(contained)
			Package: result.nestedPackages.add(contained)
			PackageableElement: result.packagedElements.add(contained)
			default: return false
		}
		return true;
	}

}