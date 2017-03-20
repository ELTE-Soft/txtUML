package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.eclipse.uml2.uml.DataType;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;

public class DataTypeExporter extends StructuredElementExporter<DataType> {

	public DataTypeExporter() {}

	@Override
	public void exportStructuredElement(DataType structuredElement, String sourceDestination)
			throws FileNotFoundException, UnsupportedEncodingException {
		super.setStructuredElement(structuredElement);
		exportDataType(sourceDestination);
	}

	private void exportDataType(String destiation) throws FileNotFoundException, UnsupportedEncodingException {

		String attributes = super.createPublicAttributes();
		Shared.writeOutSource(destiation, GenerationTemplates.headerName(name),
				HeaderTemplates.headerGuard(dependencyExporter.createDependencyHeaderIncludeCode()
						+ GenerationTemplates.dataType(name, attributes.toString()), name));
	}

}
