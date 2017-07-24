package hu.elte.txtuml.export.cpp.thread;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.utils.Pair;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.GroupContainer;
import hu.elte.txtuml.api.deployment.Runtime;
import hu.elte.txtuml.api.deployment.RuntimeType;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadDescriptionExporter {

	private Map<String, ThreadPoolConfiguration> configMap;
	private RuntimeType runtime;
	private boolean descriptionExported = false;
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

	public Pair<RuntimeType, Map<String, ThreadPoolConfiguration>> getExportedConfiguration() {
		return new Pair<>(runtime,configMap);
	}

	public void exportDescription(Class<? extends Configuration> description) {

		if (descriptionExported)
			return;
		
		
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

		checkConfigurationOptions(group.gradient(), group.constant(), group.max());

		ThreadPoolConfiguration config = createNewPoolConfiguration(group.gradient(), group.constant(), group.max());

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

	private ThreadPoolConfiguration createNewPoolConfiguration(double gradient, int constant, int max) {
		ThreadPoolConfiguration config = new ThreadPoolConfiguration(numberOfConfigurations, gradient, constant, max);
		numberOfConfigurations++;

		return config;
	}

	private void exportDefaultConfiguration() {

		if (allModelClassName.size() != exportedClasses.size()) {
			Set<String> nonExportedClasses = new HashSet<String>();
			nonExportedClasses.addAll(allModelClassName);
			nonExportedClasses.removeAll(exportedClasses);

			ThreadPoolConfiguration config = createNewPoolConfiguration(0, 1, 1);
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

	private void checkConfigurationOptions(double gradient, int constant, int max) {
		if (gradient < 0 || gradient > 1) {
			warningList.add("The gradient of linear function should be between 0 and 1: " + "conversion to 0.");
		}

		if (constant < 1) {
			warningList.add("The constant of linear function should be higher than 0: " + "conversion to 1.");
		}

		if (max < 1) {
			warningList.add("The maximum number of threads should be higher than 0: " + "conversion to 1.");
		}

		if (max < constant) {
			warningList.add(
					"The maximum number of threads should more or equal to constant: " + "conversion to the value of constant.");
		}
	}
}
