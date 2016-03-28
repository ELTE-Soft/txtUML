package hu.elte.txtuml.export.papyrus;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.papyrus.infra.core.editor.IMultiDiagramEditor;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.progress.IProgressService;

import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.AbstractPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.DefaultPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.papyrusmodelmanagers.TxtUMLPapyrusModelManager;
import hu.elte.txtuml.export.papyrus.utils.EditorOpener;

/**
 * A simple registry for registering implementation for the diagram creation process
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
	public static AbstractPapyrusModelManager getPapyrusModelManager(ServicesRegistry registry){
			/*
		IProgressService progressService = PlatformUI.getWorkbench()
				.getProgressService();

			progressService.runInUI(progressService, new IRunnableWithProgress() {
				
				@Override
				public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
				
				}
			}, ResourcesPlugin.getWorkspace().getRoot());
*/
		
		return new TxtUMLPapyrusModelManager(registry);
	}
}
