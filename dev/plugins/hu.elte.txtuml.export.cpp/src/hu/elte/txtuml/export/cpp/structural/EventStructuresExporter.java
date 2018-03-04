package hu.elte.txtuml.export.cpp.structural;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.utils.Pair;

public class EventStructuresExporter implements ICppCompilationUnit {

	private static final String ENUM_EXTENSION = "_EE";

	private Map<Signal, Operation> eventListWithConstructors;
	private ActivityExporter activityExporter;
	DependencyExporter dependencyExporter;
	private String outputDirectory;

	public EventStructuresExporter(Map<Signal, Operation> eventListWithConstructors, String outputDirectory) {
		this.eventListWithConstructors = eventListWithConstructors;

		activityExporter = new ActivityExporter(Optional.empty());
		dependencyExporter = new DependencyExporter();

		this.outputDirectory = outputDirectory;
	}

	private List<Pair<String, String>> getSignalParams(Entry<Signal, Operation> signal) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param : signal.getValue().getOwnedParameters().stream()
				.filter(p -> !p.getType().getName().equals(signal.getKey().getName())).collect(Collectors.toList())) {
			ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
		}
		return ret;
	}

	@Override
	public String getUnitName() {
		return EventTemplates.EventHeaderName;
	}

	@Override
	public String getUnitNamespace() {
		return GenerationNames.Namespaces.ModelNamespace;
	}

	@Override
	public String createUnitCppCode() {
		// empty
		return "";
	}

	@Override
	public String createUnitHeaderCode() {
		StringBuilder source = new StringBuilder("");
		StringBuilder eventClasses = new StringBuilder("");
		StringBuilder events = new StringBuilder("");
		List<Pair<String, String>> allParam = new LinkedList<Pair<String, String>>();
		for (Entry<Signal, Operation> signal : eventListWithConstructors.entrySet()) {
			List<Pair<String, String>> currentParams = getSignalParams(signal);
			ActivityExportResult ctrBody = activityExporter
					.createFunctionBody(CppExporterUtils.getOperationActivity(signal.getValue()));
			allParam.addAll(currentParams);
			eventClasses.append(EventTemplates.eventClass(signal.getKey().getName(), currentParams,
					ctrBody.getActivitySource(), signal.getKey().getOwnedAttributes()));
			events.append(signal.getKey().getName() + ENUM_EXTENSION + ",");
		}

		dependencyExporter = new DependencyExporter();
		for (Pair<String, String> param : allParam) {
			dependencyExporter.addDependency(param.getSecond());
		}

		source.append("enum Events {" + CppExporterUtils.cutOffTheLastCharacter(events.toString()) + "};\n");
		source.append(eventClasses);

		return source.toString();
	}

	@Override
	public String getUnitDependencies(UnitType type) {
		return RuntimeTemplates.eventHeaderInclude()
				+ dependencyExporter.createDependencyHeaderIncludeCode(GenerationNames.Namespaces.ModelNamespace);
	}

	@Override
	public String getDestination() {
		return outputDirectory;
	}

}
