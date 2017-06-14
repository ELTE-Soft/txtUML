package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.List;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;

public class SubStateMachineTemplates {

	public static String subStateMachineClassFixPrivateParts(String parentClass) {
		return GenerationNames.pointerType(parentClass) + " " + GenerationNames.ParentSmMemberName + ";\n";
	}

	static String subMachineFriendDecls(List<String> subMachines) {
		StringBuilder source = new StringBuilder("");
		for (String subMachine : subMachines) {
			source.append(GenerationNames.friendClassDecl(subMachine));
		}
		return source.toString();
	}

}
