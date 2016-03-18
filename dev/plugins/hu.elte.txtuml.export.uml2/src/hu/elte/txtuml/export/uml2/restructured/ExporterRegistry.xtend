package hu.elte.txtuml.export.uml2.restructured

import org.eclipse.uml2.uml.Model
import org.eclipse.uml2.uml.Package
import org.eclipse.uml2.uml.Class

class ExporterRegistry {
	
	val modelExporter = new ModelExporter(this)
	val packageExporter = new PackageExporter(this)
	val classExporter = new ClassExporter(this)
	
	def getModelExporter() { modelExporter }
	def getPackageExporter() { packageExporter }
	def getClassExporter() { classExporter }
	
	def <T> getSourceExporter(T cls) {
		switch cls {
			case Model: modelExporter
			case Package: packageExporter
			case Class: classExporter
		}
	}
	
}