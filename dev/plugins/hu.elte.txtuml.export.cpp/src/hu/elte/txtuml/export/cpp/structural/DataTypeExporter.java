package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.DataType;
import org.eclipse.uml2.uml.Property;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class DataTypeExporter {
	private DataType dataType;
	private String name;
	private String destiation;
	private List<String> dependecies;
	
	public DataTypeExporter (){}

	public DataTypeExporter(DataType dataType, String name, String destination) {
		init(dataType,name,destination);
	}
	
	public void init(DataType dataType, String name, String destination) {
		dependecies = new ArrayList<String>();
		this.dataType = dataType;
		this.name = name;
		this.destiation = destination;
	}

	public void exportDataType() throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder attributes = new StringBuilder("");
		for (Property attribute : dataType.getOwnedAttributes()) {
			String type = attribute.getType().getName();
			attributes.append(GenerationTemplates.variableDecl(type, attribute.getName(), 1));
			if(!Shared.isBasicType(type)) {
				dependecies.add(type);
			}

		}

		Shared.writeOutSource(destiation, GenerationTemplates.headerName(name), GenerationTemplates.headerGuard(createDependencyIncudesCode() + 
				GenerationTemplates.dataType(name, attributes.toString()), name));
	}
	
	private String createDependencyIncudesCode() {
		StringBuilder includes = new StringBuilder("");
		for(String type : dependecies) {
			includes.append(GenerationTemplates.cppInclude(type));
		}
		
		return includes.toString();
	}

}
