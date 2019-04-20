package hu.elte.txtuml.xtxtuml.ui.launching;

import org.eclipse.debug.core.DebugPlugin
import hu.elte.txtuml.diagnostics.launching.ILaunchConstants

class XtxtUMLExecutionLaunchAsTxtUMLAppShortcut extends XtxtUMLExecutionLaunchShortcut {
    
    override getConfigurationType(){
    	return DebugPlugin.getDefault().getLaunchManager()
    	         .getLaunchConfigurationType(ILaunchConstants.ID_TXTUML_APPLICATION);
    }

}