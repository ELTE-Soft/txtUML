package hu.elte.txtuml.export.cpp.thread;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
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
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates;
import hu.elte.txtuml.export.cpp.templates.structual.ObjectDeclDefTemplates.AllocateType;

public class ThreadHandlingManager {

	private Map<String, Integer> threadDescription;
	private String runtimeTypeName;
	private Set<ThreadPoolConfiguration> pools;
	
	private static final String ConfigurationStructName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "Configuration";
	private static final String ConfigurationFile = "deployment";
	private static final String ThreadPoolClassName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "StateMachineThreadPool";
	private static final String NamespaceName = "deployment";
	private static final String ConfiguratedThreadedRuntimeName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "ConfiguredThreadedRT";
	private static final String SingleRuntimeName = GenerationNames.Namespaces.ExecutionNamesapce + "::" + "SingleThreadRT";
	private static final String SetConfigurationMethod = "configure";
	private static final String CreatorFunction = "initRuntime";


	public ThreadHandlingManager(ThreadDescription config) {
		threadDescription = new HashMap<>();
		for (Entry<String, ThreadPoolConfiguration> conf : config.groups.entrySet()) {
			threadDescription.put(conf.getKey(), conf.getValue().getId());
		}
		

		Collection<ThreadPoolConfiguration> poolsCollection = config.groups.values();
		pools = new LinkedHashSet<ThreadPoolConfiguration>();
		pools.addAll(poolsCollection);
		runtimeTypeName = getRuntimeTypeName(config.type, pools.size());
		
	}

	public Integer getConfiguratedPoolId(String className) {
		return threadDescription.get(className);
	}

	public void createConfigurationSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {

		StringBuilder source = new StringBuilder("");
		source.append(PrivateFunctionalTemplates.include(RuntimeTemplates.RuntimeHeaderName));
		source.append(PrivateFunctionalTemplates.include(GenerationNames.FileNames.TypesFilePath));
		source.append("\n\n");

		List<String> templateParams = Arrays.asList(runtimeTypeName, new Integer(pools.size()).toString());
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
						 createThreadedRuntime(), RuntimeTemplates.RuntimeParameterName),
				NamespaceName));

		return source.toString();
	}

	private String createThreadedRuntime() {
		StringBuilder source = new StringBuilder("");
		source.append(
				GenerationTemplates.staticCreate(RuntimeTemplates.UsingRuntimeType, RuntimeTemplates.UsingRuntimePtr,
						RuntimeTemplates.RuntimeParameterName, RuntimeTemplates.RuntimeInsanceGetter));
		
		source.append(ActivityTemplates.blockStatement(ActivityTemplates.operationCallOnPointerVariable(
				RuntimeTemplates.RuntimeParameterName, SetConfigurationMethod, Arrays.asList(createConfiguration()))));
		return source.toString();
	}

	private String createConfiguration() {		
		StringBuilder confgis = new StringBuilder("");
		for (ThreadPoolConfiguration pool : pools) {
			confgis.append(ObjectDeclDefTemplates.allocateObject(ConfigurationStructName, 
					Optional.of(Arrays.asList(allocatePoolObject(pool), new Double(pool.getNumberOfExecutors()).toString())),
					AllocateType.Temporary));
			confgis.append(",");
			
		}
		
		return "{" + CppExporterUtils.cutOffTheLastCharacter(confgis.toString()) + "}";
	}

	private String allocatePoolObject(ThreadPoolConfiguration pool) {
		return ObjectDeclDefTemplates.allocateObject(ThreadPoolClassName, Optional.empty(), AllocateType.SharedPtr);
	}
	
	private String getRuntimeTypeName(RuntimeType runtimeType, Integer numberOgConfigs) {
		String runtimeTypeName = "MISSING RUNTIME TYPE";
		String numberOgConfigTemplateParam = "<" + numberOgConfigs + ">";
		switch (runtimeType) {
		case SINGLE:
			runtimeTypeName = SingleRuntimeName + numberOgConfigTemplateParam;
			break;
		case THREADED:
			runtimeTypeName = ConfiguratedThreadedRuntimeName + numberOgConfigTemplateParam;
			break;
		default:
			assert(false);
			break;
		
		}
		
		return runtimeTypeName;
	}

}
