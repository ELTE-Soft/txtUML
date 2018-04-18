package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.InterfaceNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates.HeaderInfo;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class InterfaceTemplates {

	private static String ReqPostFix = "ReqInf";
	private static String ProvPostFix = "ProvInf";
	private static String RequiredBase = "RequiredInterfaceBase";
	private static String ProvidedBase = "ProvidedInterfaceBase";
	
	enum ReceptionType {
		Send, Recive;		
	};

	public static String createInterface(String infName, List<Signal> receptionSignals) {
		StringBuilder source = new StringBuilder("");
		StringBuilder sendFunctions = new StringBuilder("");
		StringBuilder reciveFunctions = new StringBuilder("");

		for (Signal sign : receptionSignals) {
			sendFunctions.append(infReceptionDef(ReceptionType.Send, sign.getName()));
			reciveFunctions.append(infReceptionDef(ReceptionType.Recive, sign.getName()));
		}

		source.append(infPartDecl(infName, sendFunctions.toString(), ReceptionType.Send));
		source.append(infPartDecl(infName, reciveFunctions.toString(), ReceptionType.Recive));
		source.append(integratedInfDef(infName));
		return source.toString();
	}

	private static String infPartDecl(String name, String receptions, ReceptionType type) {
		
		String partName = "";
		String baseClass = "";
		switch(type) {
		case Send:
			partName = name + ReqPostFix;
			baseClass = RequiredBase;
			break;
		case Recive:
			partName = name + ProvPostFix;
			baseClass = ProvidedBase;
			break;
		default:
			break;
		
		}
		return HeaderTemplates.classHeader(Arrays.asList(baseClass), 
				Collections.emptyList(), receptions, "", 
				"", new HeaderInfo(partName, new HeaderTemplates.RawClassHeaderType()));
	}

	private static String infReceptionDef(ReceptionType type, String singalName) {
		String name = "";
		String anyName = "";
		String singalParam = "s";
		switch (type) {
		case Send:
			name = InterfaceNames.SendReceptionName;
			anyName = InterfaceNames.CommonSendAnySignalName;
			break;
		case Recive:
			name = InterfaceNames.ReciveReceptionName;
			anyName = InterfaceNames.CommonReciveAnySignalName;
			break;
		default:
			assert false : "Unhandled reception type";
			break;
		}

		return ModifierNames.NoReturn + " " + name + "(" + EventTemplates.eventPtr(singalName) + " " + singalParam + ")"
				+ "{" + anyName + "(" + singalParam + ");" + "}\n";

	}

	private static String integratedInfDef(String infName) {
		List<String> templateParameters = new ArrayList<String>();
		templateParameters.add(infName + ReqPostFix);
		templateParameters.add(infName + ProvPostFix);
		return GenerationTemplates.usingTemplateType(infName, InterfaceNames.IntegratedBaseTemplateName,
				templateParameters);
	}

}
