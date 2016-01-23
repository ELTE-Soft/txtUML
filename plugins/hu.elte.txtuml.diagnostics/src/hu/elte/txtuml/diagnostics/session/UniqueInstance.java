package hu.elte.txtuml.diagnostics.session;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;

import hu.elte.txtuml.diagnostics.Activator;
import hu.elte.txtuml.diagnostics.PluginLogWrapper;

/**
 * Uniquely identifies an object instance on the other side and keeps its status.
 * It is not a POD in the sense that while (classInstanceID, serviceInstanceID)
 * uniquely identifies the object, additional data can be present about the
 * object in it.
 * @author gerazo
 */
public class UniqueInstance {
	// unique ID
	final String classInstanceID;
	final int serviceInstanceID;

	// Status members
	private String modelClassName;
	
	UniqueInstance(String classInstanceID, int serviceInstanceID) {
		this.classInstanceID = classInstanceID.intern();
		this.serviceInstanceID = serviceInstanceID;
		this.modelClassName = null;
	}

	String getModelClassName() {
		return modelClassName;
	}
	
	void setModelClassName(String modelClassName) {
		if (this.modelClassName == null) {
			this.modelClassName = modelClassName.intern();
		}
		else if (!this.modelClassName.equals(modelClassName)) {
			PluginLogWrapper.getInstance().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "ModelClassName cannot change for " + this + " from " + this.modelClassName + " to " + modelClassName));
		}
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof UniqueInstance)) return false;
		UniqueInstance o = (UniqueInstance)obj;
		return this.classInstanceID == o.classInstanceID && this.serviceInstanceID == o.serviceInstanceID;
	}
	
	@Override
	public int hashCode() {
		return classInstanceID.hashCode() + serviceInstanceID;
	}
	
	@Override
	public String toString() {
		return classInstanceID + "@0x" + Integer.toHexString(serviceInstanceID);
	}

}
