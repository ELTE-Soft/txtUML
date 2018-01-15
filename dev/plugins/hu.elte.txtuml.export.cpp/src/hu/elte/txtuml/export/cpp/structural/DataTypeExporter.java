package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import org.eclipse.uml2.uml.DataType;

import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;

public class DataTypeExporter extends StructuredElementExporter<DataType> {

	public DataTypeExporter() {
	}

	@Override
	public void exportStructuredElement(DataType structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {
		super.init();
		super.setStructuredElement(structuredElement);
		exportDataType(sourceDestination);
	}

	private void exportDataType(String destination) throws FileNotFoundException, UnsupportedEncodingException {

		String attributes = super.createPublicAttributes();
		String source = dependencyExporter.createDependencyHeaderIncludeCode()
				+ GenerationTemplates.dataType(name, attributes.toString());
		
		CppExporterUtils.writeOutSource(destination,
				GenerationTemplates.headerName(name),
				HeaderTemplates.headerGuard( GenerationTemplates.putNamespace(
						source, 
						GenerationNames.Namespaces.ModelNamespace), 
						name));
	}

}
