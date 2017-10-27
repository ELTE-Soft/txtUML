package hu.elte.txtuml.export.cpp.thread;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.elte.txtuml.export.cpp.CppExporterUtils;
import hu.elte.txtuml.export.cpp.templates.GenerationNames;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;
import hu.elte.txtuml.export.cpp.templates.PrivateFunctionalTemplates;
import hu.elte.txtuml.export.cpp.templates.RuntimeTemplates;
import hu.elte.txtuml.export.cpp.templates.activity.ActivityTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.FunctionTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.HeaderTemplates;
import hu.elte.txtuml.export.cpp.thread.ThreadPoolConfiguration.LinearFunction;
import hu.elte.txtuml.utils.Pair;

public class ThreadHandlingManager {

	private Map<String, Integer> threadDescription;
	private String runtimeTypeName;
	private Set<ThreadPoolConfiguration> pools;
	
	private static final String ConfigurationStructName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "Configuration";
	private static final String ThreadConfigurationArray = GenerationNames.Containers.FixContainer + "<" + GenerationNames.sharedPtrType(ConfigurationStructName) + ">";
	private static final String ConfigurationObjectVariableName = "conf";
	private static final String ConfigurationFile = "deployment";
	private static final String ThreadPoolClassName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "StateMachineThreadPool";
	private static final String FunctionName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "LinearFunction";
	private static final String NamespaceName = "deployment";
	private static final String ConfiguratedThreadedRuntimeName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "ConfiguredThreadedRT";
	private static final String SingleRuntimeName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "SingleThreadRT";
	private static final String SetConfigurationMethod = "configure";
	private static final String CreatorFunction = "initRuntime";

	int numberOfThreads;

	public ThreadHandlingManager(Pair<RuntimeType, Map<String, ThreadPoolConfiguration>> config) {
		threadDescription = new HashMap<>();
		for (Entry<String, ThreadPoolConfiguration> conf : config.getSecond().entrySet()) {
			threadDescription.put(conf.getKey(), conf.getValue().getId());
		}
		
		numberOfThreads = threadDescription.size();

		Collection<ThreadPoolConfiguration> poolsCollection = config.getSecond().values();
		pools = new LinkedHashSet<ThreadPoolConfiguration>();
		pools.addAll(poolsCollection);
		runtimeTypeName = getRuntimeTypeName(config.getFirst());
	}

	public Integer getConfiguratedPoolId(String className) {
		return threadDescription.get(className);
	}

	public void createConfigurationSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {

		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RuntimeHeaderName));
		source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath));
		source.append("\n\n");

		List<String> templateParams = new ArrayList<String>();
		templateParams.add(runtimeTypeName);
		source.append(GenerationTemplates.usingTemplateType(RuntimeTemplates.UsingRuntimePtr,
				RuntimeTemplates.RuntimePtrType, templateParams));
		source.append(GenerationTemplates.usingTemplateType(RuntimeTemplates.UsingRuntimeType,
				RuntimeTemplates.RuntimeInterfaceName, templateParams));
		source.append("\n\n");

		source.append(GenerationTemplates.putNamespace(
				FunctionTemplates.simpleFunctionDecl(RuntimeTemplates.UsingRuntimePtr, CreatorFunction) + ";",
				NamespaceName));

		CppExporterUtils.writeOutSource(dest, GenerationTemplates.headerName(ConfigurationFile),
				CppExporterUtils.format(HeaderTemplates.headerGuard(source.toString(), ConfigurationFile)));

		CppExporterUtils.writeOutSource(dest, GenerationTemplates.sourceName(ConfigurationFile),
				CppExporterUtils.format(createDeploymentFunctionDefinition()));

	}

	private String createDeploymentFunctionDefinition() {
		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.include(ConfigurationFile));
		source.append(GenerationTemplates.putNamespace(
				FunctionTemplates.simpleFunctionDef(RuntimeTemplates.UsingRuntimePtr, CreatorFunction,
						createConfiguration() + createThreadedRuntime(), RuntimeTemplates.RuntimeParameterName),
				NamespaceName));

		return source.toString();
	}

	private String createThreadedRuntime() {
		StringBuilder source = new StringBuilder("");
		source.append(
				GenerationTemplates.staticCreate(RuntimeTemplates.UsingRuntimeType, RuntimeTemplates.UsingRuntimePtr,
						RuntimeTemplates.RuntimeParameterName, RuntimeTemplates.RuntimeInsanceGetter));
		List<String> params = new ArrayList<String>();
		params.add(ConfigurationObjectVariableName);
		source.append(ActivityTemplates.blockStatement(ActivityTemplates.operationCallOnPointerVariable(
				RuntimeTemplates.RuntimeParameterName, SetConfigurationMethod, params)));
		return source.toString();
	}

	private String createConfiguration() {
		StringBuilder source = new StringBuilder("");
		List<String> parameters = new ArrayList<String>();
		parameters.add(new Integer(pools.size()).toString());
		source.append(ThreadConfigurationArray + " " +  ConfigurationObjectVariableName + "(" + new Integer(pools.size()).toString() + ");\n");

		for (ThreadPoolConfiguration pool : pools) {
			parameters.clear();
			parameters.add(allocatePoolObject(pool));
			parameters.add(allocateFunctionObject(pool.getFunction()));
			parameters.add(new Integer(pool.getMaxThread()).toString());

			source.append(insertToConfiguration(pool.getId(),
					GenerationTemplates.allocateObject(ConfigurationStructName, parameters, true)));
		}

		return source.toString();
	}

	private String insertToConfiguration(Integer id, String configuration) {


		return ActivityTemplates.blockStatement(
				ConfigurationObjectVariableName + "[" + id +  "] = " + configuration);
	}

	private String allocateFunctionObject(LinearFunction function) {
		List<String> params = new ArrayList<String>();
		params.add(new Integer(function.getConstant()).toString());
		params.add(new Double(function.getGradient()).toString());

		return GenerationTemplates.allocateObject(FunctionName, params, true);
	}

	private String allocatePoolObject(ThreadPoolConfiguration pool) {
		List<String> params = new ArrayList<String>();
		return GenerationTemplates.allocateObject(ThreadPoolClassName, params, true);
	}
	
	private String getRuntimeTypeName(RuntimeType runtimeType) {
		String runtimeTypeName = "MISSING RUNTIME TYPE";
		switch (runtimeType) {
		case SINGLE:
			runtimeTypeName = SingleRuntimeName;
			break;
		case THREADED:
			runtimeTypeName = ConfiguratedThreadedRuntimeName;
			break;
		default:
			assert(false);
			break;
		
		}
		
		return runtimeTypeName;
	}

}
