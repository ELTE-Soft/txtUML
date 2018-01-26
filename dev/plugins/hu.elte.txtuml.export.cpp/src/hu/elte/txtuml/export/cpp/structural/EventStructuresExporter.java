package hu.elte.txtuml.export.cpp.structural;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.eclipse.uml2.uml.Operation;
import org.eclipse.uml2.uml.Parameter;
import org.eclipse.uml2.uml.Signal;

import hu.elte.txtuml.export.cpp.ActivityExportResult;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.ICppCompilationUnit;
import hu.elte.txtuml.export.cpp.activity.ActivityExporter;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.statemachine.EventTemplates;
import hu.elte.txtuml.utils.Pair;

public class EventStructuresExporter implements ICppCompilationUnit {
	
	private static final String ENUM_EXTENSION = "_EE";

	
	private Map<Signal, Operation> eventListWithConstructors;
	private ActivityExporter activityExporter;
	
	public EventStructuresExporter(Map<Signal,Operation> eventListWithConstructors) {
		this.eventListWithConstructors = eventListWithConstructors;
		
		activityExporter = new ActivityExporter(this);
	}
	
	public void createEventStructureSources(String outputDirectory) throws FileNotFoundException, UnsupportedEncodingException {
		StringBuilder forwardDecl = new StringBuilder("");
		StringBuilder events = new StringBuilder("");
		StringBuilder source = new StringBuilder("");
		List<Pair<String, String>> allParam = new LinkedList<Pair<String, String>>();
		for (Entry<Signal, Operation> signal : eventListWithConstructors.entrySet()) {
			List<Pair<String, String>> currentParams = getSignalParams(signal);
			ActivityExportResult ctrBody =  activityExporter.createFunctionBody(CppExporterUtils.getOperationActivity(signal.getValue())); 
			allParam.addAll(currentParams);
			source.append(
					EventTemplates.eventClass(signal.getKey().getName(), currentParams, ctrBody.getActivitySource(), signal.getKey().getOwnedAttributes()));
			events.append(signal.getKey().getName() + ENUM_EXTENSION + ",");
		}

		DependencyExporter dependencyEporter = new DependencyExporter();
		for (Pair<String, String> param : allParam) {
			dependencyEporter.addDependency(param.getSecond());
		}
		forwardDecl.append(dependencyEporter.createDependencyHeaderIncludeCode());
		forwardDecl.append(RuntimeTemplates.eventHeaderInclude());
		forwardDecl.append("enum Events {" + CppExporterUtils.cutOffTheLastCharcter(events.toString()) + "};\n");
		forwardDecl.append(source);
		CppExporterUtils.writeOutSource(outputDirectory, (EventTemplates.EventHeader),
				CppExporterUtils.format(
						EventTemplates.eventHeaderGuard(RuntimeTemplates.eventHeaderInclude() + GenerationTemplates
								.putNamespace(forwardDecl.toString(), GenerationNames.Namespaces.ModelNamespace))));
	}
	

	private List<Pair<String, String>> getSignalParams(Entry<Signal, Operation> signal) {
		List<Pair<String, String>> ret = new ArrayList<Pair<String, String>>();
		for (Parameter param :  signal.getValue().getOwnedParameters().stream().filter(p -> !p.getType().getName().equals(signal.getKey().getName())).collect(Collectors.toList())) {
			ret.add(new Pair<String, String>(param.getType().getName(), param.getName()));
		}
		return ret;
	}
	
	@Override
	public String getUnitName() {
		return ""; // not using anywhere
	}

	@Override
	public void addDependency(String type) {
		// TODO Auto-generated method stub
		
	}

}
