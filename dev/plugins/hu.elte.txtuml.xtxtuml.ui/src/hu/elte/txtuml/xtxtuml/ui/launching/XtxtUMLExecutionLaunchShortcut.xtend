package hu.elte.txtuml.xtxtuml.ui.launching;

import org.eclipse.jdt.debug.ui.launchConfigurations.JavaApplicationLaunchShortcut
import org.eclipse.jface.viewers.ISelection
import org.eclipse.jface.viewers.IStructuredSelection
import org.eclipse.jface.viewers.StructuredSelection
import org.eclipse.ui.IEditorPart
import org.eclipse.xtext.xbase.ui.launching.JavaElementDelegate
import org.eclipse.xtext.xbase.ui.launching.LaunchShortcutUtil

class XtxtUMLExecutionLaunchShortcut extends JavaApplicationLaunchShortcut {

	/**
	 * Overrides the default implementation to use XtxtUML delegates.
	 * @see XtxtUMLJavaElementDelegateMainLaunch
	 */
	override launch(ISelection selection, String mode) {
		if (selection instanceof IStructuredSelection) {
			val newSelection = LaunchShortcutUtil.replaceWithJavaElementDelegates(selection,
				XtxtUMLJavaElementDelegateMainLaunch);
			super.launch(newSelection, mode);
		}
	}

	/**
	 * Overrides the default implementation to use XtxtUML delegates.
	 * @see XtxtUMLJavaElementDelegateMainLaunch
	 */
	override launch(IEditorPart editor, String mode) {
		val javaElementDelegate = (editor as JavaElementDelegate).getAdapter(XtxtUMLJavaElementDelegateMainLaunch);
		if (javaElementDelegate != null) {
			launch(new StructuredSelection(javaElementDelegate), mode);
		} else {
			super.launch(editor, mode);
		}
	}

}