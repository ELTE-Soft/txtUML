package hu.elte.txtuml.diagnostics.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import hu.elte.txtuml.api.diagnostics.protocol.InstanceEvent;
import hu.elte.txtuml.api.diagnostics.protocol.Message;
import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.Activator;
import hu.elte.txtuml.diagnostics.PluginLogWrapper;

/**
 * Analyzes events for errors, keeps track of service and class instances
 */
public class InstanceRegister {
	// Usually only 1 or 2 instances will be in this container so a
	// TreeSet performs better as its memory is more compact than HashSet's
	private Set<Integer> aliveServiceInstances = new TreeSet<>();
	// aliveClassInstances should be a Set but stupid Java does not have
	// a Set operation to find something by value
	private Map<UniqueInstance, UniqueInstance> aliveClassInstances = new HashMap<UniqueInstance, UniqueInstance>();

	InstanceRegister() {
	}
	
	void dispose() {
		for (UniqueInstance instance : aliveClassInstances.values()) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Instance" + instance + " of class " + instance.getModelClassName() + " was not destructed"));
		}
		for (int clientID : aliveServiceInstances) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Service instance 0x" + Integer.toHexString(clientID) + " was not shut down properly"));
		}

		aliveClassInstances.clear();
		aliveClassInstances = null;
		aliveServiceInstances.clear();
		aliveServiceInstances = null;
	}
	
	public UniqueInstance getInstance(String classInstanceID, int serviceInstanceID) {
		return aliveClassInstances.get(new UniqueInstance(classInstanceID, serviceInstanceID));
	}
	
	void processMessage(Message event) {
		boolean protocolKept = true;
		try {
			switch (event.messageType) {
			case ACKNOWLEDGED:
				protocolKept = false;
				break;
			case CHECKIN:
			case CHECKOUT:
				processPlainMessage(event);
				break;
			case INSTANCE_CREATION:
			case INSTANCE_DESTRUCTION:
				InstanceEvent classInstanceEvent = (InstanceEvent) event;
				processClassInstanceEvent(classInstanceEvent);
				break;
			case PROCESSING_SIGNAL:
			case USING_TRANSITION:
			case ENTERING_VERTEX:
			case LEAVING_VERTEX:
				ModelEvent modelEvent = (ModelEvent) event;
				processModelEvent(modelEvent);
				break;
			}
		} catch (ClassCastException ex) {
			protocolKept = false;
		}
		if (!protocolKept) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Protocol error: inappropriate message type"));
			assert false;
		}
		if (event.messageType != MessageType.CHECKOUT && !aliveServiceInstances.contains(event.serviceInstanceID)) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has not checked in"));
		}
	}
	
	private void processPlainMessage(Message event) {
		if (event.messageType == MessageType.CHECKIN) {
			boolean isNew = aliveServiceInstances.add(event.serviceInstanceID);
			if (!isNew) {
				PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has already checked in"));
			}
		}
		else if (event.messageType == MessageType.CHECKOUT) {
			boolean wasHere = aliveServiceInstances.remove(event.serviceInstanceID);
			if (!wasHere) {
				PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has checked out before or was never checked in"));
			}
		}
	}
	
	private void processClassInstanceEvent(InstanceEvent event) {
		UniqueInstance instance = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
		if (event.messageType == MessageType.INSTANCE_CREATION) {
			instance.setModelClassName(event.modelClassName);
			boolean isNew = (aliveClassInstances.putIfAbsent(instance, instance) == null);
			if (!isNew) {
				PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Instance " + instance + " of class " + instance.getModelClassName() + " was already created"));
			}
		}
		else if (event.messageType == MessageType.INSTANCE_DESTRUCTION) {
			boolean wasHere = (aliveClassInstances.remove(instance) != null);
			if (!wasHere) {
				PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Instance " + instance + " of class " + instance.getModelClassName() + " was already destroyed"));
			}
		}
	}

	private void processModelEvent(ModelEvent event) {
		UniqueInstance instanceID = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
		// here instanceID only contains the ID but instance has additional data
		UniqueInstance instance = aliveClassInstances.get(instanceID);
		if (instance == null) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Instance " + instanceID + " of class " + event.modelClassName + " was never created and is currently active"));
			instance = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
			instance.setModelClassName(event.modelClassName);
			aliveClassInstances.putIfAbsent(instance, instance);
			return;
		}
		instance.setModelClassName(event.modelClassName); // to test if class did not change
	}

}
