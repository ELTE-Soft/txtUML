package hu.elte.txtuml.export.cpp.templates.structual;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.FileNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class PortTemplates {
	public final static String PORT_ENUM_NAME = "PortTypes";
	public static final String PortTypeName = "Port";
	public static final String BehaviorPortTypeName = "BehaviorPort";
	public final static String NO_PORT = "1";
	public final static String PORT_ENUM_EXTENSION = "_PE";
	public static final String PORT_FILE_NAME = "ports";
	public final static String PORT_HEADER = PORT_FILE_NAME + "." + FileNames.HeaderExtension;
	public static final String PORTS_INITIALIZER_FUNCTION_NAME = GenerationNames.InitiliazetFixFunctionNames.InitPorts;
	
	public static String portHeaderGuard (String source) {
		return HeaderTemplates.headerGuard(source, PORT_FILE_NAME);
	}
	
	public static String portsHeaderInclude () {
		return PrivateFunctionalTemplates.include(PortTemplates.PORT_FILE_NAME);
	}
	
	public static String ponrtEnumName (String port) {
		return port + PORT_ENUM_EXTENSION;
	}
}
