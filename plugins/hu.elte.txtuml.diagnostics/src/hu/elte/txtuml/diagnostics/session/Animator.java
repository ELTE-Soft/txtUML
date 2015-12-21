package hu.elte.txtuml.diagnostics.session;

import java.util.HashMap;
import java.util.Map;

import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.services.markerlistener.IPapyrusMarker;
import org.eclipse.papyrus.infra.services.markerlistener.PapyrusMarkerAdapter;

/**
 * Animates diagrams
 * @author gerazo
 */
class Animator {
	private static final int ANIMATION_TIMER = 1000;
	private static final String ANIMATED_MARKER_ID = "hu.elte.txtuml.diagnostics.animatedmarker";

	
	private InstanceRegister instanceRegister;
	private ModelMapper modelMapper;
	private Map<String, UniqueInstance> classNameToAnimatedInstance = new HashMap<String, UniqueInstance>();
	private Map<String, EObject> classNameToAnimatedElement = new HashMap<String, EObject>();
	private Map<EObject, IPapyrusMarker> eobjectToMarker = new HashMap<EObject, IPapyrusMarker>();
	
	Animator(InstanceRegister instanceRegister, ModelMapper modelMapper) {
		this.instanceRegister = instanceRegister;
		this.modelMapper = modelMapper;
	}
	
	void dispose() {
		removeAllAnimationMarkers();
		eobjectToMarker.clear();
		eobjectToMarker = null;
		classNameToAnimatedElement.clear();
		classNameToAnimatedElement = null;
		classNameToAnimatedInstance.clear();
		classNameToAnimatedInstance = null;
		modelMapper = null;
		instanceRegister = null;
	}
	
	void animateEvent(ModelEvent event) {
		if (event.messageType == MessageType.PROCESSING_SIGNAL) {
			return;
		}
		
		UniqueInstance instance = instanceRegister.getInstance(event.modelClassInstanceID, event.serviceInstanceID);
		UniqueInstance currentInstance = classNameToAnimatedInstance.putIfAbsent(event.modelClassName, instance);
		if (currentInstance != null && !currentInstance.equals(instance)) {
			// TODO Make this choosable from interface!
			return;
		}
		
		EObject eobject = modelMapper.getEObjectForModelAndElement(event.modelClassName, event.eventTargetClassName);
		removeMarkerFromClass(event.modelClassName);
		// animationUtils.removeAllAnimationMarker();
		if (eobject != null) {
			addAnimationMarker(eobject);
			classNameToAnimatedElement.put(event.modelClassName, eobject);
		}

		try {
			Thread.sleep(ANIMATION_TIMER);
		} catch (InterruptedException ex) {}
	}
		
	private void removeMarkerFromClass(String modelClassName) {
		EObject animatedObj = classNameToAnimatedElement.get(modelClassName); 
		if(animatedObj != null) {
			removeAnimationMarker(animatedObj);
		}
	}
	
	private void removeAllAnimationMarkers() {
		for (EObject eobject : eobjectToMarker.keySet()) {
			removeAnimationMarker(eobject);
		}
	}
	
	private void addAnimationMarker(EObject eobject) {
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put(EValidator.URI_ATTRIBUTE, EcoreUtil.getURI(eobject).toString());
		IResource resource = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		if (workspace != null) {
			resource = workspace.getRoot().getFile(new Path(eobject.eResource().getURI().toPlatformString(true)));
		}
		if (resource != null) {
			IMarker imarker = null;
			try {
				imarker = resource.createMarker(ANIMATED_MARKER_ID);
				imarker.setAttributes(attributes);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (imarker != null) {
				IPapyrusMarker marker = PapyrusMarkerAdapter.wrap(eobject.eResource(), imarker, attributes);
				eobjectToMarker.put(eobject, marker);
			}
		}
	}

	private void removeAnimationMarker(EObject eobject) {
		IPapyrusMarker marker = eobjectToMarker.get(eobject);
		if (marker != null) {
			try {
				marker.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			eobjectToMarker.remove(eobject);
		}
	}

}
