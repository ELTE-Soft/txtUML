package hu.elte.txtuml.export.cpp.templates.structual;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class PortTemplates {
	public static final String PortTypeName = "Port";
	public static final String BehaviorPortTypeName = "BehaviorPort";
	public final static String ANY_PORT = "PortType::AnyPort";
	public static final String PORT_FILE_NAME = "ports";
	public final static String PORT_HEADER = PORT_FILE_NAME + "." + FileNames.HeaderExtension;
	public static final String PORTS_INITIALIZER_FUNCTION_NAME = GenerationNames.InitiliazetFixFunctionNames.InitPorts;
	public static final String PORT_TYPE_CLASS_INFO_NAME = "PortType";
	public static final Integer ANY_PORT_DEFAULT_VALUE = 1;
	
	public static String portHeaderGuard (String source) {
		return HeaderTemplates.headerGuard(source, PORT_FILE_NAME);
	}
	
	public static String portsHeaderInclude () {
		return PrivateFunctionalTemplates.include(PortTemplates.PORT_FILE_NAME);
	}
	
	public static String portTypeInfoClassName(String ownerClassName) {
		return  ownerClassName + PORT_TYPE_CLASS_INFO_NAME;
	}
	
	public static String portTypeInfoName(String portName, String ownerClassName) {
		return portTypeInfoClassName(ownerClassName) + "::" + portName;
	}
	
	public static String portTypeInfoDecl(String portName, String ownerClassName) {
		return ObjectDeclDefTemplates.staticPropertyDecl(portTypeInfoClassName(ownerClassName), portName);
	}
	
	public static String portTypeInfoDef(String portName, String ownerClassName, int typeId) {
		return ObjectDeclDefTemplates.staticPropertyDef(ownerClassName + "::" + portTypeInfoClassName(ownerClassName), ownerClassName, 
				portTypeInfoClassName(ownerClassName) + "::" + portName, ownerClassName + "::" + portTypeInfoClassName(ownerClassName) + "(" + typeId + ")");
	}
	
	public static String portTypeInfoClassConstructor(String ownerName) {
		String portIdParamName = "portId";
		return portTypeInfoClassName(ownerName) + "(int " + portIdParamName + ") :" + PORT_TYPE_CLASS_INFO_NAME + "(" + portIdParamName + ") {}\n";
	}
}
