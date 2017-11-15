package hu.elte.txtuml.diagnostics.animation;

import java.util.ArrayList;
import java.util.Arrays;
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
import org.eclipse.gmf.runtime.common.core.util.Log;
import org.eclipse.papyrus.infra.services.markerlistener.IPapyrusMarker;
import org.eclipse.papyrus.infra.services.markerlistener.PapyrusMarkerAdapter;

import hu.elte.txtuml.api.model.execution.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.model.execution.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.session.InstanceRegister;
import hu.elte.txtuml.diagnostics.session.ModelMapper;
import hu.elte.txtuml.diagnostics.session.UniqueInstance;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.Pair;
/**
 * Animates diagrams
 */
public class Animator {
	private InstanceRegister instanceRegister;
	private ModelMapper modelMapper;
	private Map<String, ArrayList<UniqueInstance>> classNameToAnimatedInstance = new HashMap<String, ArrayList<UniqueInstance>>();
	private Map<Pair<String, String>, EObject> classNameToAnimatedElement = new HashMap<Pair<String, String>, EObject>();
	private Map<Pair<String, EObject>, IPapyrusMarker> eobjectToMarker = new HashMap<Pair<String, EObject>, IPapyrusMarker>();
	
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
		
		UniqueInstance instance = instanceRegister.getInstance(event.modelClassName, event.modelClassInstanceID, event.serviceInstanceID);
		UniqueInstance currentInstance = putIfAbbsentIntoClassNameToAnimateMap(event.modelClassName, instance);
		if (currentInstance != null && !currentInstance.equals(instance)) {
			// TODO Make this choosable from interface!
			return;
		}
		
		EObject eobject = modelMapper.getEObjectForModelAndElement(event.modelClassName, event.eventTargetClassName);
		removeMarkerFromClass(event.modelClassName, event.modelClassInstanceID);
		// animationUtils.removeAllAnimationMarker();
		if (eobject != null) {
			addAnimationMarker(eobject, event.modelClassInstanceID);
			classNameToAnimatedElement.put(new Pair<String, String>(event.modelClassName, event.modelClassInstanceID), eobject);
		}

		try {
			Thread.sleep(AnimationConfig.ANIMATION_TIMER);
		} catch (InterruptedException ex) {}
	}
		
	private void removeMarkerFromClass(String modelClassName, String instanceClassID) {
		EObject animatedObj = classNameToAnimatedElement.get(new Pair<String, String>(modelClassName, instanceClassID));
		if(animatedObj != null) {
			removeAnimationMarker(new Pair<String, EObject>(instanceClassID, animatedObj));
			keepOtherMarkersforEObject(animatedObj);
		}
	}
	
	private void keepOtherMarkersforEObject(EObject animatedObj) {
		for (Pair<String, EObject> pair: eobjectToMarker.keySet()) {
			if(pair.getSecond().equals(animatedObj)){
				IResource rs = getResourceFor(animatedObj);
				IMarker imarker;
				try {
					imarker = rs.createMarker(AnimationConfig.TXTUML_ANIMATION_MARKER_ID);
					String al = URI.createPlatformResourceURI(rs.getFullPath().toString(), true) + "#" + EcoreUtil.getURI(animatedObj).fragment();
					imarker.setAttribute(EValidator.URI_ATTRIBUTE, al);
				} catch (CoreException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private void removeAllAnimationMarkers() {
		for (Pair<String, EObject> pair: eobjectToMarker.keySet()) {
			removeAnimationMarker(pair, false);
		}
		eobjectToMarker.clear();
	}
	
	IResource getResourceFor(EObject eobject){
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
		return resource;
		
	}
	
	private void addAnimationMarker(EObject eobject, String modelClassID) {
		IResource resource = getResourceFor(eobject);
		if (resource != null && resource.exists()) {
			IMarker imarker = null;
			try {
				imarker = resource.createMarker(AnimationConfig.TXTUML_ANIMATION_MARKER_ID);
				String uri = URI.createPlatformResourceURI(resource.getFullPath().toString(), true) + "#" + EcoreUtil.getURI(eobject).fragment();
				imarker.setAttribute(EValidator.URI_ATTRIBUTE, uri);
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (imarker != null) {
				IPapyrusMarker marker = PapyrusMarkerAdapter.wrap(eobject.eResource(), imarker);
				eobjectToMarker.put(new Pair<String, EObject>(modelClassID, eobject), marker);
			}
		}
	}

	private void removeAnimationMarker(Pair<String, EObject> key, boolean removeFromMap) {
		IPapyrusMarker marker = eobjectToMarker.get(key);
		if (marker != null) {
			try {
				marker.delete();
			} catch (CoreException e) {
				e.printStackTrace();
			}
			if (removeFromMap) {
				eobjectToMarker.remove(key);
			}
		}
	}

	private void removeAnimationMarker(Pair<String, EObject> key) {
		removeAnimationMarker(key, true);
	}
	 /** @param instance
	 * required: instance.modelClassName not null.
	 * @return instance if is already in map (is new)
	 * 			Null otherwise.
	 */
	private UniqueInstance putIfAbbsentIntoClassNameToAnimateMap(String modelClassName, UniqueInstance instance){
		if (classNameToAnimatedInstance.putIfAbsent(modelClassName, new ArrayList<UniqueInstance>(Arrays.asList(instance))) == null){
			return null;
		}
		ArrayList<UniqueInstance> list = classNameToAnimatedInstance.get(modelClassName);
		if (!list.contains(instance)){
			list.add(instance);
			return null;
		};
		return instance;
	}
		
}
