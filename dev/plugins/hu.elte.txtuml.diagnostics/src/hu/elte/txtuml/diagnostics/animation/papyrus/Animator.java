package hu.elte.txtuml.diagnostics.animation.papyrus;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.services.markerlistener.IPapyrusMarker;
import org.eclipse.papyrus.infra.services.markerlistener.PapyrusMarkerAdapter;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.session.InstanceRegister;
import hu.elte.txtuml.diagnostics.session.ModelMapper;
import hu.elte.txtuml.diagnostics.session.UniqueInstance;

/**
 * Animates diagrams
 */
public class Animator {
	private InstanceRegister instanceRegister;
	private ModelMapper modelMapper;
	private Map<String, UniqueInstance> classNameToAnimatedInstance = new HashMap<String, UniqueInstance>();
	private Map<String, EObject> classNameToAnimatedElement = new HashMap<String, EObject>();
	private Map<EObject, IPapyrusMarker> eobjectToMarker = new HashMap<EObject, IPapyrusMarker>();
	
	public Animator(InstanceRegister instanceRegister, ModelMapper modelMapper) {
		this.instanceRegister = instanceRegister;
		this.modelMapper = modelMapper;
	}
	
	public void dispose() {
		removeAllAnimationMarkers();
		eobjectToMarker = null;
		classNameToAnimatedElement.clear();
		classNameToAnimatedElement = null;
		classNameToAnimatedInstance.clear();
		classNameToAnimatedInstance = null;
		modelMapper = null;
		instanceRegister = null;
	}
	
	public void animateEvent(ModelEvent event) {
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
	}
		
	private void removeMarkerFromClass(String modelClassName) {
		EObject animatedObj = classNameToAnimatedElement.get(modelClassName); 
		if(animatedObj != null) {
			removeAnimationMarker(animatedObj);
		}
	}
	
	private void removeAllAnimationMarkers() {
		for (EObject eobject : eobjectToMarker.keySet()) {
			removeAnimationMarker(eobject, false);
		}
		eobjectToMarker.clear();
	}
	
	private void addAnimationMarker(EObject eobject) {
		IResource resource = null;
		IWorkspace workspace = ResourcesPlugin.getWorkspace(); 
		if (workspace != null) {
			URI uri = eobject.eResource().getURI();
			if (uri.isPlatform()) {
				resource = workspace.getRoot().getFile(new Path(uri.toPlatformString(true)));
			} else {
				IPath fs = new Path(uri.toFileString());
				for (IProject proj : workspace.getRoot().getProjects()) {
					if (proj.getLocation().isPrefixOf(fs)) {
						IPath relative = fs.makeRelativeTo(proj.getLocation());
						resource = proj.findMember(relative);
					}
				}
			}
		}
		if (resource != null && resource.exists()) {
			IMarker imarker = null;
			try {
				imarker = resource.createMarker(AnimationConfig.TXTUML_ANIMATION_MARKER_ID);
				imarker.setAttribute(EValidator.URI_ATTRIBUTE, URI.createPlatformResourceURI(resource.getFullPath().toString(), true) + "#" + EcoreUtil.getURI(eobject).fragment());
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (imarker != null) {
				IPapyrusMarker marker = PapyrusMarkerAdapter.wrap(eobject.eResource(), imarker);
				eobjectToMarker.put(eobject, marker);
			}
		}
	}

	private void removeAnimationMarker(EObject eobject, boolean removeFromMap) {
		IPapyrusMarker marker = eobjectToMarker.get(eobject);
		if (marker != null) {
			try {
				marker.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (removeFromMap) {
				eobjectToMarker.remove(eobject);
			}
		}
	}

	private void removeAnimationMarker(EObject eobject) {
		removeAnimationMarker(eobject, true);
	}
}
