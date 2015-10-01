package hu.elte.txtuml.diagnostics.session;

import java.io.File;
import java.io.FilenameFilter;
import java.util.HashMap;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import hu.elte.txtuml.api.diagnostics.protocol.MessageType;
import hu.elte.txtuml.api.diagnostics.protocol.ModelEvent;
import hu.elte.txtuml.diagnostics.Activator;
import hu.elte.txtuml.export.papyrus.preferences.PreferencesManager;
import hu.elte.txtuml.export.uml2.mapping.ModelMapException;
import hu.elte.txtuml.export.uml2.mapping.ModelMapProvider;

import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.papyrus.moka.ui.presentation.AnimationUtils;

/**
 * Animates diagrams according to events coming from clients.
 * @author gerazo
 */
public class Animator {
	
	private static final int ANIMATION_TIMER = 1000;
	private static final String MAPPING_FILE_EXTENSION = ModelMapProvider.MAPPING_FILE_EXTENSION;
	private static final String MAPPING_FILE_EXTENSION_TOKEN = "." + MAPPING_FILE_EXTENSION;
	private static final String MAPPING_DIRECTORY_PATH =
			PreferencesManager.getString(PreferencesManager.TXTUML_VISUALIZE_DESTINATION_FOLDER);
	
	private AnimationUtils animationUtils;
	private NavigableMap<String, ModelMapProvider> modelMapProviders = new TreeMap<String, ModelMapProvider>();
	private Map<String, String> classToInstance = new HashMap<String, String>();
	private int faultTolerance = 37;
	private Map<String,EObject> animated = new HashMap<String,EObject>();
	
	Animator(String projectName) {
		animationUtils = AnimationUtils.getInstance();
		AnimationUtils.init();
		
		for (IProject project : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			if (project.isOpen() && (projectName.equals("") || project.getName().equals(projectName))) {
				IFolder mappingFolder = project.getFolder(MAPPING_DIRECTORY_PATH);
				if (mappingFolder.exists()) {
					File mappingDirectory = new File(mappingFolder.getLocation().toOSString());
					URI uri = URI.createFileURI(mappingFolder.getLocation().toOSString());
					if (mappingDirectory.exists() && mappingDirectory.isDirectory() && mappingDirectory.canRead()) {
						for (String mappingFilename : mappingDirectory.list(new FilenameFilter() {
							@Override
							public boolean accept(File dir, String name) {
								return name.endsWith(MAPPING_FILE_EXTENSION_TOKEN);
							}})) {
							ModelMapProvider modelMapProvider = null;
							mappingFilename = mappingFilename.substring(0, mappingFilename.lastIndexOf(MAPPING_FILE_EXTENSION_TOKEN));
							try {
								modelMapProvider = new ModelMapProvider(uri, mappingFilename);
							} catch (ModelMapException ex) {
								Activator.getDefault().getLog().log(new Status(IStatus.ERROR, Activator.PLUGIN_ID, "Symbol mapping error: " + ex));
								assert false;
							}
							if (modelMapProvider != null) {
								modelMapProviders.put(mappingFilename, modelMapProvider);
							}
						}
					}
				}
			}
		}
	}
	
	void dispose() {
		animationUtils.removeAllAnimationMarker();
		animationUtils = null;
		modelMapProviders.clear();
		modelMapProviders = null;
		classToInstance.clear();
		classToInstance = null;
		animated = null;
	}
	
	void animate(ModelEvent event) {
		if (event.messageType == MessageType.PROCESSING_SIGNAL) {
			return;
		}
		
		String uniqueInstanceID = getUniqueInstanceID(event);
		String previousInstanceID = classToInstance.putIfAbsent(event.modelClassName, uniqueInstanceID);
		if (previousInstanceID != null && !previousInstanceID.equals(uniqueInstanceID)) {
			// other object which we are not visualizing currently
			// TODO Make this choosable from interface!
			return;
		}
		
		ModelMapProvider modelMapProvider = null;
		Map.Entry<String, ModelMapProvider> entry = modelMapProviders.floorEntry(event.modelClassName);
		if (entry != null && event.modelClassName.startsWith(entry.getKey())) {
			modelMapProvider = entry.getValue();
		}
		
		if (modelMapProvider != null) {
			EObject eobject = modelMapProvider.getByName(event.eventTargetClassName);
			removeMarkerFromClass(event.modelClassName);
			// animationUtils.removeAllAnimationMarker();
			if (eobject != null) {
				animationUtils.addAnimationMarker(eobject);
				animated.put(event.modelClassName, eobject);
			} else {
				if (faultTolerance > 0) {
					Activator.getDefault().getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Mapping failure for element " + event.eventTargetClassName + " " + event.messageType));
					faultTolerance--;
				}
			}
			try {
				Thread.sleep(ANIMATION_TIMER);
			} catch (InterruptedException ex) {}
		} else {
			if (faultTolerance > 0) {
				Activator.getDefault().getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Mapping failure for model " + event.modelClassName));
				faultTolerance--;
			}
		}
		
		if (faultTolerance == 0) {
			Activator.getDefault().getLog().log(new Status(IStatus.WARNING, Activator.PLUGIN_ID, "Too much mapping failures, no more reported..."));
			faultTolerance = -1;
		}
	}
	
	private static String getUniqueInstanceID(ModelEvent event) {
		return event.modelClassInstanceID + "@" + event.clientID;
	}
	
	private void removeMarkerFromClass(String modelClassName) {
		EObject animatedObj = animated.get(modelClassName); 
		if(animatedObj != null) {
			animationUtils.removeAnimationMarker(animatedObj);
		}
	}

}
