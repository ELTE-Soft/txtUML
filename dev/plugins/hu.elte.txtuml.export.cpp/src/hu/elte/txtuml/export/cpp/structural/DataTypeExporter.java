package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import org.eclipse.uml2.uml.DataType;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class DataTypeExporter extends StructuredElementExporter<DataType>{

	private String destiation;
	
	public DataTypeExporter (){}
	public DataTypeExporter(DataType dataType, String name, String destination) {
		init(dataType,name,destination);
	}
	
	public void init(DataType dataType, String name, String destination) {
		super.init(dataType, name);
		this.destiation = destination;
	}

	public void exportDataType() throws FileNotFoundException, UnsupportedEncodingException {
		
		String attributes = super.createPublicAttributes();
		
		Shared.writeOutSource(destiation, GenerationTemplates.headerName(name), GenerationTemplates.headerGuard(super.createDependencyIncudesCode(true) + 
				GenerationTemplates.dataType(name, attributes.toString()), name));
	}

}
