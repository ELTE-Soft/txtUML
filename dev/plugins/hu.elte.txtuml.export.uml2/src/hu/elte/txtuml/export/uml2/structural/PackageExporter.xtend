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
import java.util.List
import java.util.Collections

abstract class AbstractPackageExporter<S, T extends Package> extends Exporter<S, S, T> {

	new(ExportMode mode) {
		super(mode)
	}

	new(BaseExporter<?, ?, ?> parent) {
		super(parent);
	}

	def exportPackageFragment(List<IPackageFragment> packageFragments) {
		if(packageFragments.empty) {
			return
		}
			
		packageFragments.forEach[packageFragment | 
			packageFragment.children.forEach[exportCompUnit(it as ICompilationUnit)]
		]
		
		if(packageFragments.size == 1) {
			val packageFregement = packageFragments.head
			val subPackages = getSubPackages(packageFregement)
			subPackages.forEach[p | exportPackage(#[p])[result.nestedPackages += it]]
				
		} else if(packageFragments.size == 2) {
			val subPackages1 = getSubPackages(packageFragments.get(0))
			val subPackages2 = getSubPackages(packageFragments.get(1))
			Collections.sort(subPackages1, [p1, p2 | p1.elementName.compareTo(p2.elementName)])
			Collections.sort(subPackages2, [p1, p2 | p1.elementName.compareTo(p2.elementName)])	
				
							
			var i = 0
			var j = 0			
			while(i < subPackages1.size || j < subPackages2.size) {
				
				if(j >= subPackages2.size || 
					(i < subPackages1.size && subPackages1.get(i).elementName < subPackages2.get(j).elementName)
				) {
					val packageFragment1 = subPackages1.get(i)
					exportPackage(#[packageFragment1])[result.nestedPackages += it]
					i++
				}
				else if(i >= subPackages1.size || 
					(j < subPackages2.size && subPackages1.get(i).elementName > subPackages2.get(j).elementName)
				) {
					val packageFragment2 = subPackages2.get(j)
					exportPackage(#[packageFragment2])[result.nestedPackages += it]
					j++
					
				} else if(i < subPackages1.size && j < subPackages2.size && 
					subPackages1.get(i).elementName == subPackages2.get(j).elementName
				) {
					val packageFragment1 = subPackages1.get(i)
					val packageFragment2 = subPackages2.get(j)
					exportPackage(#[packageFragment1, packageFragment2])[result.nestedPackages += it]
					i++
					j++
				}
			}
		}

		
	}
	
	def List<IPackageFragment> getSubPackages(IPackageFragment packageFregement) {
			val packageRoot = packageFregement.parent as IPackageFragmentRoot
			val subPackages = packageRoot.children.map[it as IPackageFragment].filter [
					elementName.startsWith(packageFregement.elementName + ".")
			]
			subPackages.toList
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
			case ElementTypeTeller.isCollection(decl): {
				//TODO handle collection types
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

class PackageExporter extends AbstractPackageExporter<List<IPackageFragment>, Package> {

	new(BaseExporter<?, ?, ?> parent) {
		super(parent)
	}

	override create(List<IPackageFragment> pf) { if(!pf.empty) factory.createPackage }

	override exportContents(List<IPackageFragment> s) {
		result.name = s.head.elementName.split(Pattern.quote(".")).last
		exportPackageFragment(s)
	}
}
