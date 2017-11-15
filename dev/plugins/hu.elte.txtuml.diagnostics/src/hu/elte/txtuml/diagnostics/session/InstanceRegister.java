package hu.elte.txtuml.diagnostics.session;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Arrays;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.InstanceEvent;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.Message;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.utils.Logger;

/**
 * Analyzes events for errors, keeps track of service and class instances
 */
public class InstanceRegister {
	// Usually only 1 or 2 instances will be in this container so a
	// TreeSet performs better as its memory is more compact than HashSet's
	private Set<Integer> aliveServiceInstances = new TreeSet<>();
	// aliveClassInstances should be a Set but stupid Java does not have
	// a Set operation to find something by value
	private Map<String, ArrayList<UniqueInstance>> aliveClassInstances = new HashMap<String, ArrayList<UniqueInstance>>();

	InstanceRegister() {
	}
	
	void dispose() {
		for (String modelClasses : aliveClassInstances.keySet()){
			for (UniqueInstance instance : aliveClassInstances.get(modelClasses)) {
				Logger.sys.warn("Instance" + instance + " of class " + instance.getModelClassName() + " was not destructed");
			}	
		}

		for (int clientID : aliveServiceInstances) {
			Logger.sys.warn("Service instance 0x" + Integer.toHexString(clientID) + " was not shut down properly");
		}

		aliveClassInstances.clear();
		aliveClassInstances = null;
		aliveServiceInstances.clear();
		aliveServiceInstances = null;
	}
	
	public UniqueInstance getInstance(String modelClassName, String classInstanceID, int serviceInstanceID) {
		return getInstanceClassIfAlive(modelClassName, new UniqueInstance(classInstanceID, serviceInstanceID));
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
			Logger.sys.error("Protocol error: inappropriate message type");
			assert false;
		}
		if (event.messageType != MessageType.CHECKOUT && !aliveServiceInstances.contains(event.serviceInstanceID)) {
			Logger.sys.warn("Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has not checked in");
		}
	}
	
	private void processPlainMessage(Message event) {
		if (event.messageType == MessageType.CHECKIN) {
			boolean isNew = aliveServiceInstances.add(event.serviceInstanceID);
			if (!isNew) {
				Logger.sys.warn("Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has already checked in");
			}
		}
		else if (event.messageType == MessageType.CHECKOUT) {
			boolean wasHere = aliveServiceInstances.remove(event.serviceInstanceID);
			if (!wasHere) {
				Logger.sys.warn("Service instance 0x" + Integer.toHexString(event.serviceInstanceID) + " has checked out before or was never checked in");
			}
		}
	}
	
	private void processClassInstanceEvent(InstanceEvent event) {
		UniqueInstance instance = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
		if (event.messageType == MessageType.INSTANCE_CREATION) {
			instance.setModelClassName(event.modelClassName);
			boolean isNew = insertIntoAliveClasses(instance);
			if (!isNew) {
				Logger.sys.warn("Instance " + instance + " of class " + instance.getModelClassName() + " was already created");
			}
		}
		else if (event.messageType == MessageType.INSTANCE_DESTRUCTION) {
			boolean wasHere = removeFromAliveClasses(instance);
			if (!wasHere) {
				Logger.sys.warn("Instance " + instance + " of class " + instance.getModelClassName() + " was already destroyed");
			}
		}
	}

	private void processModelEvent(ModelEvent event) {
		UniqueInstance instanceID = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
		// here instanceID only contains the ID but instance has additional data
		UniqueInstance instance = getInstanceClassIfAlive(event.modelClassName, instanceID);
		if (instance == null) {
			Logger.sys.warn("Instance " + instanceID + " of class " + event.modelClassName + " was never created and is currently active");
			instance = new UniqueInstance(event.modelClassInstanceID, event.serviceInstanceID);
			instance.setModelClassName(event.modelClassName);
			insertIntoAliveClasses(instance);
			return;
		}
		instance.setModelClassName(event.modelClassName); // to test if class did not change
	}
	
	/**
	 * @param instanceID
	 * @param modelClassName
	 * @return the instance if is in the map of aliveClassInstances.
	 */
	UniqueInstance getInstanceClassIfAlive(String modelClassName, UniqueInstance instanceID) {
		if(!aliveClassInstances.containsKey(modelClassName)) {return null;};
		ArrayList<UniqueInstance> list = aliveClassInstances.get(modelClassName);
		if (list.contains(instanceID)){
			return instanceID;
		};
		return null;
	}

	/**
	 * @param instance
	 * required: instance.modelClassName not null.
	 * @return True if the instance wasn't in map (is new)
	 * 			False otherwise.
	 */
	Boolean insertIntoAliveClasses(UniqueInstance instance){
		String modelClassName = instance.getModelClassName();
		if (aliveClassInstances.putIfAbsent(modelClassName, new ArrayList<UniqueInstance>(Arrays.asList(instance))) == null){
			return true;
		}
		ArrayList<UniqueInstance> list = aliveClassInstances.get(modelClassName);
		if (!list.contains(instance)){
			list.add(instance);
			return true;
		};
		return false;
	}
	
	/**
	 * @param instance
	 * @return True if the instance was in map (was there)
	 * 			False otherwise.
	 */
	Boolean removeFromAliveClasses(UniqueInstance instance){
		String modelClassName = instance.getModelClassName();
		if (!aliveClassInstances.containsKey(modelClassName)){
			return false;
		}
		ArrayList<UniqueInstance> list = aliveClassInstances.get(modelClassName);
		if (list.contains(instance)){
			list.remove(instance);
			if(list.isEmpty()) {aliveClassInstances.remove(modelClassName);}
			return true;
		};
		return false;
	}
	
}
