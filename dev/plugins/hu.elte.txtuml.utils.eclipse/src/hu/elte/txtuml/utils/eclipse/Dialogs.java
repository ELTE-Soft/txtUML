package hu.elte.txtuml.utils.eclipse;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.MultiStatus;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;

public class Dialogs {
	/**
	 * Opens an {@link Dialogs}
	 * @param title - The title  (if null it will be "Error")
	 * @param body - The message (if null it will be e.getLocalizedMessage() )
	 * @param e - A Throwable
	 */
	public static void errorMsgb(String title, String body, Throwable e){
		Plugin plugin = ResourcesPlugin.getPlugin();
		if(title == null){
			title = "Error";
		}
		
		if(body == null){
			body = e.getLocalizedMessage();
		}
		
		StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    e.printStackTrace(pw);

	    final String trace = sw.toString(); // stack trace as a string

	    // Temp holder of child statuses
	    List<Status> childStatuses = new ArrayList<>();

	    // Split output by OS-independend new-line
	    for (String line : trace.split(System.getProperty("line.separator"))) {
	        // build & add status
	        childStatuses.add(new Status(IStatus.ERROR, plugin.getBundle().getSymbolicName(), line));
	    }

	    MultiStatus ms = new MultiStatus(plugin.getBundle().getSymbolicName(), IStatus.ERROR,
	            childStatuses.toArray(new Status[] {}), // convert to array of statuses
	            e.getLocalizedMessage(), e);
	    final String dialogTitle = title;
	    final String dialogBody = body;
	    Display.getDefault().syncExec(() -> {
	    	ErrorDialog.openError(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), dialogTitle, dialogBody, ms);
	    });
	}
	
	
	/**
	 * Opens a MessageBox with the given title and message.
	 * @param title The title of the MessageBox window
	 * @param body The Message
	 */
	public static void MessageBox(String title, String body){
		MessageDialog.openInformation(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),title,body);
	}
	
	public static boolean WarningConfirm(String title, String body){
		MessageDialog dialog = new MessageDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(),
				title, null,  body, MessageDialog.WARNING, new String[] { "Yes", "No"}, 0);
		return dialog.open() == 0;
	}
}
