package hu.elte.txtuml.export.cpp.thread;

import hu.elte.txtuml.export.cpp.Shared;
import hu.elte.txtuml.export.cpp.templates.GenerationTemplates;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.uml2.uml.Class;

public class ThreadHandlingManager {

	private Map<String, ThreadPoolConfiguration> threadDescription;
	private Set<ThreadPoolConfiguration> pools;

	private static final String ConfigurationClassName = "ThreadConfiguration";
	private static final String ConfigurationFile = "conf.hpp";
	private static final String PoolsMapName = "id_matching_map";
	private static final String ThreadPool = "StateMachineThreadPool";
	private static final String FunctionName = "LinearFunction";
	private static final String FunctionTypePointer = FunctionName + "*";
	private static final String IDType = "id_type";
	private static final String ThreadPoolTypePointer = ThreadPool + "*";
	private static final String FunctionMapName = "function_matching_map";
	private static final String MaximumThreadsMapName = "maximum_thread_map";

	int numberOfThreads;

	public ThreadHandlingManager(List<Class> classList, Map<String, ThreadPoolConfiguration> description) {

		this.threadDescription = description;
		numberOfThreads = threadDescription.size();
		Collection<ThreadPoolConfiguration> poolsCollection = threadDescription.values();

		Set<String> configuratedClasses = threadDescription.keySet();
		Set<String> nonConfiguratedClasses = new HashSet<String>();
		for (Class cls : classList) {
			nonConfiguratedClasses.add(cls.getName());
		}
		nonConfiguratedClasses.removeAll(configuratedClasses);

		pools = new LinkedHashSet<ThreadPoolConfiguration>();
		pools.addAll(poolsCollection);

		if (classList.size() != threadDescription.size()) {
			ThreadPoolConfiguration defaultConfig = new ThreadPoolConfiguration(0, 0, 1);
			defaultConfig.setMaxThreads(1);

			for (String clsName : nonConfiguratedClasses) {
				threadDescription.put(clsName, defaultConfig);
			}

			pools.add(defaultConfig);

		}
	}

	public Map<String, ThreadPoolConfiguration> getDescription() {
		return threadDescription;
	}
	
	

	public void createConfigurationSource(String dest) throws FileNotFoundException, UnsupportedEncodingException {

		String source = createConfiguratioSource();
		Shared.writeOutSource(dest, ConfigurationFile, source);

	}
	
	private void createConfiguration() {
	    //TODO create a configuration object and configure it 
	}

	private String createConfiguratioSource() {
		String source = "";

		source = source + GenerationTemplates.cppInclude(ConfigurationClassName.toLowerCase());
		source = source + createConstructorHead();
		//source = source + createConstructorBody();

		return source;
	}

	private String createConstructorHead() {
		return ConfigurationClassName + "::" + ConfigurationClassName + "()\n";
	}

	private String createConstructorBody() {
		String body = "";
		body = "{\n" + setPoolsMap() + setFunctionMap() + maximumThreadMap() + "}\n\n";
		

		return body;
	}

	private String maximumThreadMap() {
		String source = "";

		for (ThreadPoolConfiguration pool : pools) {
			source = source + "\t" + insertToConfigurationMap(MaximumThreadsMapName, IDType, "int",
					new Integer(pool.getId()).toString(), new Integer(pool.getMaxThread()).toString()) + ";\n";
		}

		return source;

	}

	private String setFunctionMap() {

		String source = "";

		for (ThreadPoolConfiguration pool : pools) {
			List<String> params = new ArrayList<String>();

			params.add(new Integer(pool.getFunction().getConstant()).toString());
			params.add(new Double(pool.getFunction().getGradient()).toString());
			source = source + "\t"
					+ insertToConfigurationMap(FunctionMapName, IDType, FunctionTypePointer,
							new Integer(pool.getId()).toString(),
							GenerationTemplates.allocateObject(FunctionName, params))
					+ ";\n";
		}

		return source;
	}

	private String setPoolsMap() {

		String source = "";

		for (ThreadPoolConfiguration pool : pools) {

			List<String> params = new ArrayList<String>();

			params.add(new Integer(pool.getFunction().getConstant()).toString());
			source = source + "\t"
					+ insertToConfigurationMap(PoolsMapName, IDType, ThreadPoolTypePointer,
							new Integer(pool.getId()).toString(),
							GenerationTemplates.allocateObject(ThreadPool, params))
					+ ";\n";
		}

		return source;
	}

	private String insertToConfigurationMap(String mapName, String keyType, String valueType, String key,
			String value) {
		return mapName + ".insert(std::pair<" + keyType + "," + valueType + ">(" + key + "," + value + ") )";
	}

}
