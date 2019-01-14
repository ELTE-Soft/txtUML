package hu.elte.txtuml.export.cpp.thread;

import java.util.Map;

import hu.elte.txtuml.api.deployment.RuntimeType;

public class ThreadDescription {
	public ThreadDescription(RuntimeType type, Map<String, ThreadPoolConfiguration> groups) {
		super();
		this.type = type;
		this.groups = groups;
	}
	public RuntimeType type;
	public Map<String, ThreadPoolConfiguration> groups;
}
