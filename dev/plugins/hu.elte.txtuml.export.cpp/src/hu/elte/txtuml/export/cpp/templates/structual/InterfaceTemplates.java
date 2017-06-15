package hu.elte.txtuml.export.cpp.templates.structual;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.templates.GenerationNames.InterfaceNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.ModifierNames;
import hu.elte.txtuml.export.cpp.templates.GenerationNames.TypeDelcreationKeywords;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

public class InterfaceTemplates {
	
	private static String ReqPostFix = "ReqInf";
	private static String ProvPostFix = "ProvInf";
	
	enum ReceptionType {Send, Recive};
	
	public static String createInterface(String infName, List<Signal> receptionSignals) {
		StringBuilder source = new StringBuilder("");
		StringBuilder sendFunctions = new StringBuilder("");
		StringBuilder reciveFunctions = new StringBuilder("");
		
		
		for (Signal sign : receptionSignals) {
			sendFunctions.append(infReceptionDef(ReceptionType.Send, sign.getName()));
			reciveFunctions.append(infReceptionDef(ReceptionType.Recive, sign.getName()));
		}
		String singalParam = "s";
		String sendAny = "virtual " + 
				ModifierNames.NoReturn + " " + 
				InterfaceNames.CommonSendAnySignalName + 
				"(" + EventTemplates.EventPointerType + " " + singalParam  + ") = 0;\n";
		
		String reciveAny = "virtual " + 
				ModifierNames.NoReturn + " " + 
				InterfaceNames.ReciveReceptionName + 
				"(" + EventTemplates.EventPointerType + " " + singalParam  + ") = 0;\n";
		
		source.append(infPartDecl(infName + ReqPostFix, sendFunctions.toString(), sendAny));
		source.append(infPartDecl(infName + ProvPostFix, reciveFunctions.toString(), reciveAny));
		source.append(integratedInfDef(infName));
		return source.toString();
	}
	private static String infPartDecl(String partName, String receptions, String anyReception) {
		return TypeDelcreationKeywords.InterfacType +  " " + 
					partName + "\n" + 
					"{\n" + "public: \n" + receptions +
					"private: \n" + anyReception + "\n};\n";
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
		
		return ModifierNames.NoReturn + " " + name + 
		"(" + EventTemplates.eventPtr(singalName) + " " + singalParam + ")" + 
		"{" + anyName + "(" + singalParam + ");" +  "}\n";
		
		
	}
	
	private static String integratedInfDef(String infName) {
		List<String> templateParameters = new ArrayList<String>();
		templateParameters.add(infName + ReqPostFix);
		templateParameters.add(infName + ProvPostFix);
		return GenerationTemplates.usingTemplateType
				(infName, 
				 InterfaceNames.IntegratedBaseTemplateName, 
				 templateParameters);
	}

}
