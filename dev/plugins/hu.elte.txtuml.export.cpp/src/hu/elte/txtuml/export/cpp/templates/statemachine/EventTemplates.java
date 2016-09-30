package hu.elte.txtuml.export.cpp.templates.statemachine;

import java.util.List;
import java.util.Set;

import org.eclipse.uml2.uml.Property;
import org.eclipse.uml2.uml.SignalEvent;

import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.Options;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.VariableTemplates;
import hu.elte.txtuml.utils.Pair;

public class EventTemplates {

	public static String eventBase(Options options) {
		StringBuilder eventBase = new StringBuilder("");

		eventBase.append(RuntimeTemplates.rtEventHeaderInclude()).append("\n");

		eventBase.append(GenerationNames.ClassType + " " + GenerationNames.EventBaseName);
		if (options.isAddRuntime()) {
			eventBase.append(":" + RuntimeTemplates.EventIName);
		}
		eventBase.append("\n{\n" + GenerationNames.EventBaseName + "(");
		eventBase.append("int t_):");

		eventBase.append("t(t_){}\nint t;\n};\ntypedef const " + GenerationNames.EventBaseName + "& "
				+ GenerationNames.EventBaseRefName + ";\n\n");
		return eventBase.toString();
	}

	public static StringBuilder eventClass(String className, List<Pair<String, String>> params, String constructorBody,
			List<Property> properites) {
		StringBuilder source = new StringBuilder(
				GenerationNames.ClassType + " " + GenerationNames.eventClassName(className) + ":public "
						+ GenerationNames.EventBaseName + "\n{\n" + GenerationNames.eventClassName(className) + "(");
		String paramList = PrivateFunctionalTemplates.paramList(params);
		if (paramList != "") {
			source.append(paramList);
		}
		source.append("):" + GenerationNames.EventBaseName + "(");
		source.append(GenerationNames.eventEnumName(className) + ")");
		StringBuilder body = new StringBuilder("\n{\n" + constructorBody + "}\n");

		for (Property property : properites) {
			body.append(VariableTemplates.variableDecl(property.getType().getName(), property.getName(), null, false));
		}
		source.append(body).append("};\n\n");
		body.setLength(0);
		return source;
	}

	// TODO works only with signal events! (Time,Change,.. not handled) - not
	// supported..
	public static String eventEnum(Set<SignalEvent> events) {
		StringBuilder eventList = new StringBuilder("enum ");
		eventList.append(GenerationNames.EventsEnumName);
		eventList.append("{");
		eventList.append(GenerationNames.eventEnumName(EventTemplates.InitSignal) + ",");

		if (events != null && !events.isEmpty()) {
			for (SignalEvent item : events) {
				eventList.append(GenerationNames.eventEnumName(item.getSignal().getName()) + ",");
			}
		}

		return eventList.substring(0, eventList.length() - 1) + "};\n";
	}

	public static String eventHeaderGuard(String source) {
		return HeaderTemplates.headerGuard(source, GenerationNames.EventHeaderName);
	}

	public static String eventParamName() {
		return GenerationNames.formatIncomingParamName(GenerationNames.EventParamName);
	}

	public static final String EventHeader = GenerationNames.EventHeaderName + "." + GenerationNames.HeaderExtension;
	public static final String InitSignal = GenerationNames.InitialEventName;

}
