package hu.elte.txtuml.export.uml2.restructured

import hu.elte.txtuml.utils.jdt.ElementTypeTeller
import hu.elte.txtuml.utils.jdt.SharedUtils
import java.io.File
import java.util.List
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

	new(ExporterRegistry registry, Class<T> cls) {
		super(registry, cls);
	}

	def dispatch exportChild(List<Element> stack, T pack, IPackageFragment subPackage) {
		packageExporter.export(stack, subPackage)
	}

	def dispatch exportChild(List<Element> stack, T pack, ICompilationUnit compUnit) {
		val unit = parseCompUnit(compUnit)
		unit.types.forEach[t|exportType(stack, pack, t)]
	}

	def dispatch exportChild(List<Element> stack, T pack, IJavaElement other) {
		throw new IllegalArgumentException(other.toString);
	}

	def parseCompUnit(ICompilationUnit compUnit) {
		SharedUtils.parseJavaSource(new File(compUnit.resource.locationURI), compUnit.javaProject)
	}

	def exportType(List<Element> stack, T pack, TypeDeclaration typeDecl) {
		switch typeDecl {
			case ElementTypeTeller.isModelClass(typeDecl): classExporter.export(stack, typeDecl)
			default: throw new IllegalArgumentException(typeDecl.toString)
		}
	}

}

class PackageExporter extends AbstractPackageExporter<Package> {

	new(ExporterRegistry registry) {
		super(registry, Package)
	}

	override create() { factory.createPackage }

	override exportContents(Package pack, IPackageFragment s) {
		pack.name = s.elementName.split(Pattern.quote(".")).last
	}

	override tryStore(Package pkg, Element contained) {
		switch contained {
			Type: pkg.ownedTypes.add(contained)
			Package: pkg.nestedPackages.add(contained)
			PackageableElement: pkg.packagedElements.add(contained)
			default: return false
		}
		return true;
	}

}