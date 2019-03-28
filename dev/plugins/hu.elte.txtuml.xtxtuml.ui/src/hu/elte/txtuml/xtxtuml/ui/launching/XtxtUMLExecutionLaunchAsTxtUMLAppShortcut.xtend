package hu.elte.txtuml.xtxtuml.ui.launching;

import org.eclipse.core.runtime.CoreException
import org.eclipse.debug.core.DebugPlugin
import org.eclipse.debug.core.ILaunchConfiguration
import org.eclipse.jdt.core.IType

class XtxtUMLExecutionLaunchAsTxtUMLAppShortcut extends XtxtUMLExecutionLaunchShortcut {
	
	override launch(IType type, String mode) {
        try {
	        val ILaunchConfiguration config = createConfiguration(type);
	        if (config != null) {
	         config.launch(mode, null);
	        }
            
        } catch (CoreException e) {
            //TODO
        }
    }
    
    override getConfigurationType(){
    	return  DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationTypes().get(23);
    }

}