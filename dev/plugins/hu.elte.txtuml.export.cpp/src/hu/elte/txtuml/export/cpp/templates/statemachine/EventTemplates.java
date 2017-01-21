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
	
	public static final String EventHeader = EventTemplates.EventHeaderName + "." + GenerationNames.HeaderExtension;
	public static final String InitSignal = GenerationNames.InitialEventName;
	public static final String ProcessEventFunctionName = "processEventVirtual";
	public static final String EventFParamName = GenerationNames.formatIncomingParamName(EventTemplates.EventParamName);
	public static final String EventParamName = "e";
	public static final String EventHeaderName = "event";
	public static final String EventBaseName = "EventBase";
	public static final String EventBaseRefName = EventBaseName + "CRef";
	public static final String EventsEnumName = "Events";

	public static String eventBase(Options options) {
		StringBuilder eventBase = new StringBuilder("");

		eventBase.append(RuntimeTemplates.eventHeaderInclude()).append("\n");

		eventBase.append(GenerationNames.ClassType + " " + EventTemplates.EventBaseName);
		if (options.isAddRuntime()) {
			eventBase.append(":" + RuntimeTemplates.EventIName);
		}
		eventBase.append("\n{\n" + EventTemplates.EventBaseName + "(");
		eventBase.append("int t_):");

		eventBase.append("t(t_){}\nint t;\n};\ntypedef const " + EventTemplates.EventBaseName + "& "
				+ EventTemplates.EventBaseRefName + ";\n\n");
		return eventBase.toString();
	}

	public static String eventClass(String className, List<Pair<String, String>> params, String constructorBody,
			List<Property> properites) {
		StringBuilder source = new StringBuilder(
				GenerationNames.ClassType + " " + GenerationNames.eventClassName(className) + ":public "
						+ EventTemplates.EventBaseName + "\n{\n" + GenerationNames.eventClassName(className) + "(");
		String paramList = PrivateFunctionalTemplates.paramList(params);
		if (paramList != "") {
			source.append(paramList);
		}
		source.append("):" + EventTemplates.EventBaseName + "(");
		source.append(GenerationNames.eventEnumName(className) + ")");
		StringBuilder body = new StringBuilder("\n{\n" + constructorBody + "}\n");

		for (Property property : properites) {
			body.append(VariableTemplates.variableDecl(property.getType().getName(), property.getName(), null, false));
		}
		source.append(body).append("};\n\n");
		body.setLength(0);
		return source.toString();
	}

	// TODO works only with signal events! (Time,Change,.. not handled) - not
	// supported..
	public static String eventEnum(Set<SignalEvent> events) {
		StringBuilder eventList = new StringBuilder("enum ");
		eventList.append(EventTemplates.EventsEnumName);
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
		return HeaderTemplates.headerGuard(source, EventTemplates.EventHeaderName);
	}

	public static String eventParamName() {
		return GenerationNames.formatIncomingParamName(EventTemplates.EventParamName);
	}



}
