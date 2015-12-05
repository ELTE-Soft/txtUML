package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.DefaultPapyrusModelManager;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;

/**
 * A simple registry for registering implementation for the diagram creation process
 * @author András Dobreff
 */
public class SettingsRegistry {
	private static Class<? extends AbstractPapyrusModelManager> manager;
	
	/**
	 * sets Implementation for the PapyrusModelManager
	 * @param manager
	 */
	public static void setPapyrusModelManager(Class<? extends AbstractPapyrusModelManager> manager){
		SettingsRegistry.manager = manager;
	}
	
	/**
	 * Clears the registry
	 */
	public static void clear(){
		manager = null;
	}
	
	/**
	 * Returns an instance of the registered Papyrus model manager. If there were no registration
	 * an instance of {@link DefaultPapyrusModelManager} will be returned.
	 * @param editor - Constructor parameter of manager
	 * @return Instance of the registered derivateive of {@link AbstractPapyrusModelManager}
	 */
	public static AbstractPapyrusModelManager getPapyrusModelManager(IMultiDiagramEditor editor){
		if(manager == null) return new DefaultPapyrusModelManager(editor);	
		try {
			return manager.getConstructor(IMultiDiagramEditor.class).newInstance(editor);
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			throw new RuntimeException(e);
		}
	}
}
