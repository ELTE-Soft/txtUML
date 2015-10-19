package hu.elte.txtuml.diagnostics.session;

import java.util.HashMap;
import java.util.Map;

import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.moka.ui.presentation.AnimationUtils;

/**
 * Animates diagrams
 * @author gerazo
 */
class Animator {
	private static final int ANIMATION_TIMER = 1000;
	
	private InstanceRegister instanceRegister;
	private ModelMapper modelMapper;
	private AnimationUtils animationUtils;
	private Map<String, UniqueInstance> classNameToAnimatedInstance = new HashMap<String, UniqueInstance>();
	private Map<String, EObject> classNameToAnimatedElement = new HashMap<String, EObject>();
	
	Animator(InstanceRegister instanceRegister, ModelMapper modelMapper) {
		this.instanceRegister = instanceRegister;
		this.modelMapper = modelMapper;
		animationUtils = AnimationUtils.getInstance();
		AnimationUtils.init();
	}
	
	void dispose() {
		classNameToAnimatedElement.clear();
		classNameToAnimatedElement = null;
		classNameToAnimatedInstance.clear();
		classNameToAnimatedInstance = null;
		animationUtils.removeAllAnimationMarker();
		animationUtils = null;
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
			animationUtils.addAnimationMarker(eobject);
			classNameToAnimatedElement.put(event.modelClassName, eobject);
			
		}

		try {
			Thread.sleep(ANIMATION_TIMER);
		} catch (InterruptedException ex) {}
	}
		
	private void removeMarkerFromClass(String modelClassName) {
		EObject animatedObj = classNameToAnimatedElement.get(modelClassName); 
		if(animatedObj != null) {
			animationUtils.removeAnimationMarker(animatedObj);
		}
	}

}
