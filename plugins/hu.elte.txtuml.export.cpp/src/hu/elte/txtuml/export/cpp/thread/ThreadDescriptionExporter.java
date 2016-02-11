package hu.elte.txtuml.export.cpp.thread;

import hu.elte.txtuml.api.model.ModelClass;
import hu.elte.txtuml.api.deployment.Configuration;
import hu.elte.txtuml.api.deployment.Group;
import hu.elte.txtuml.api.deployment.GroupContainer;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ThreadDescriptionExporter {

	private Map<String, ThreadPoolConfiguration> configMap;
	private boolean descriptionExported = false;

	List<String> warningList;
	List<String> errorList;

	int numberOfConfigurations;

	Set<String> exportedClasses;

	public ThreadDescriptionExporter() {
		configMap = new HashMap<String, ThreadPoolConfiguration>();
		exportedClasses = new HashSet<String>();
		numberOfConfigurations = 0;

		warningList = new ArrayList<String>();
		errorList = new ArrayList<String>();
	}

	public Map<String, ThreadPoolConfiguration> getConfigMap() {
		return configMap;
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

			} else {
				warningList.add("Only Group annotations are allowed to use.");
			}
		}

		descriptionExported = true;

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
		numberOfConfigurations = numberOfConfigurations + 1;

		if (group.gradient() < 0 || group.gradient() > 1) {
			warningList.add("The gradient of linear function should be beetween 0 and 1.");
		}

		if (group.constant() < 0) {
			warningList.add("The constant of linear function should be higher than 0.");
		}

		ThreadPoolConfiguration config = new ThreadPoolConfiguration(numberOfConfigurations, group.gradient(),
				group.constant());
		config.setMaxThreads(group.max());

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

	private void checkEmptyGroup(Class<? extends ModelClass>[] classes) {
		if (classes.length == 0) {
			warningList.add("Group annotation is empty.");
		}

	}
}
