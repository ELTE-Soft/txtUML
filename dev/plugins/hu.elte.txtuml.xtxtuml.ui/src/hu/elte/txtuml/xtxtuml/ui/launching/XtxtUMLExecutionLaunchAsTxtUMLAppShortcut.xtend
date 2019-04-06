package hu.elte.txtuml.xtxtuml.ui.launching;

import org.eclipse.debug.core.DebugPlugin

class XtxtUMLExecutionLaunchAsTxtUMLAppShortcut extends XtxtUMLExecutionLaunchShortcut {
    
    override getConfigurationType(){
    	return  DebugPlugin.getDefault().getLaunchManager().getLaunchConfigurationTypes().get(23);
    }

}