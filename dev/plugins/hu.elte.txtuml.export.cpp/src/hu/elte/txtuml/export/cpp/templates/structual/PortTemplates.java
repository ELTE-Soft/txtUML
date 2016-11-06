package hu.elte.txtuml.export.cpp.templates.structual;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;

public class PortTemplates {
	public final static String PORT_ENUM_NAME = "Port";
	public final static String NO_PORT = "NoPort";
	public final static String PORT_ENUM_EXTENSION = "_PE";
	public static final String PORT_FILE_NAME = "ports";
	public final static String PORT_HEADER = PORT_FILE_NAME + "." + GenerationNames.HeaderExtension;
	
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
