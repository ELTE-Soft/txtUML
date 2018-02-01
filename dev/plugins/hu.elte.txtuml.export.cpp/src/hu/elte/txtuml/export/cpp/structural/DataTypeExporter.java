package hu.elte.txtuml.export.cpp.structural;


import org.eclipse.uml2.uml.DataType;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class DataTypeExporter extends StructuredElementExporter<DataType> {

	public DataTypeExporter(DataType structuredElement, String name, String sourceDestination) {
		super(structuredElement, name, sourceDestination);
		super.init();
	}

	@Override
	public String getUnitNamespace() {
		return GenerationNames.Namespaces.ModelNamespace;
	}

	@Override
	public String createUnitCppCode() {
		String attributes = super.createPublicAttributes();
		return GenerationTemplates.dataType(name, attributes.toString());
	}

	@Override
	public String createUnitHeaderCode() {
		return "";
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		return dependencyExporter.createDependencyHeaderIncludeCode();
	}

}
