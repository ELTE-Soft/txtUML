package hu.elte.txtuml.export.cpp.thread;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.GroupContainer;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;
import hu.elte.txtuml.api.model.ModelClass;


public class ThreadDescriptionExporter {

	private Map<String, ThreadPoolConfiguration> configMap;
	private RuntimeType runtime;
	private int allThread;
	
	private boolean descriptionExported = false;
	private double sumRate;
	private boolean runtimeTypeIsPresent = false;

	private List<String> warningList;
	private List<String> errorList;

	private int numberOfConfigurations;

	private Set<String> exportedClasses;
	private Set<String> allModelClassName;

	public ThreadDescriptionExporter(Set<String> allModelClassName) {
		configMap = new HashMap<String, ThreadPoolConfiguration>();

		this.allModelClassName = allModelClassName;
		exportedClasses = new HashSet<String>();
		numberOfConfigurations = 0;

		warningList = new ArrayList<String>();
		errorList = new ArrayList<String>();
	}

	public ThreadDescription getExportedConfiguration() {
		return new ThreadDescription(runtime,configMap);
	}

	public void exportDescription(Class<? extends Configuration> description) {

		if (descriptionExported)
			return;
		
		sumRate = 0;
		allThread = java.lang.Runtime.getRuntime().availableProcessors() + 1;
		for (Annotation annotaion : description.getAnnotations()) {
			if (annotaion instanceof GroupContainer) {

				for (Annotation group : ((GroupContainer) annotaion).value()) {

					exportGroup((Group) group);
				}
			} else if (annotaion instanceof Group) {

				exportGroup((Group) annotaion);

			} else if (annotaion instanceof Runtime) {
				exportRuntimeType((Runtime) annotaion);
				
			} else {
				warningList.add("Only Group and Runtime annotations are allowed to use.");
			}
		}
		
		if (!runtimeTypeIsPresent) {
			warningList.add("You haven't specified the runtime type!\n "
					+ "The default runtime type is: MultiThreadedRT");
			runtime = RuntimeType.THREADED;
		}
		exportDefaultConfiguration();
		
		if(sumRate > 1) {
			warningList.add("The sum of all groups rate is greater than zero.\n "
					+ "More thread will be created then number of cores.");
		}
		

		descriptionExported = true;

	}

	private void exportRuntimeType(Runtime annotaion) {
		if (!runtimeTypeIsPresent) {
			runtimeTypeIsPresent = true;
			runtime = annotaion.value();
		} else {
			warningList.add("Runtime type is configured multiple times!");
		}
	}

	public boolean isSuccessfulExportation() {
		if (!descriptionExported) {
			return false;
		} else {
			return errorList.isEmpty();
		}
	}

	public boolean warningListIsEmpty() {
		return warningList.isEmpty();
	}

	public List<String> getErrors() {
		return errorList;
	}

	public List<String> getWarnings() {
		return warningList;
	}

	private void exportGroup(Group group) {

		checkConfigurationOptions(group);
		double rate = group.rate();
		sumRate += rate;
		ThreadPoolConfiguration config = createNewPoolConfiguration((int) Math.max(1, allThread  * rate));

		checkEmptyGroup(group.contains());

		for (Class<? extends ModelClass> cls : group.contains()) {
			if (!exportedClasses.contains(cls.getSimpleName())) {
				exportedClasses.add(cls.getSimpleName());
				configMap.put(cls.getSimpleName(), config);
			} else {
				warningList.add(cls.getSimpleName() + " is configured multiple times!");
			}

		}
	}

	private ThreadPoolConfiguration createNewPoolConfiguration(int numberOfExecutors) {
		ThreadPoolConfiguration config = new ThreadPoolConfiguration(numberOfConfigurations, numberOfExecutors);
		numberOfConfigurations++;

		return config;
	}

	private void exportDefaultConfiguration() {

		if (allModelClassName.size() != exportedClasses.size()) {
			Set<String> nonExportedClasses = new HashSet<String>();
			nonExportedClasses.addAll(allModelClassName);
			nonExportedClasses.removeAll(exportedClasses);

			ThreadPoolConfiguration config = createNewPoolConfiguration(1);
			for (String uncategorizedClassName : nonExportedClasses) {
				configMap.put(uncategorizedClassName, config);
			}
		}

	}

	private void checkEmptyGroup(Class<? extends ModelClass>[] classes) {
		if (classes.length == 0) {
			warningList.add("Group annotation is empty.");
		}

	}

	private void checkConfigurationOptions(Group group) {
		if (group.rate() < 0 || group.rate() > 1) {
			warningList.add("The rate should be between 0 and 1: " + "conversion to 1.");
		}

	}
}
