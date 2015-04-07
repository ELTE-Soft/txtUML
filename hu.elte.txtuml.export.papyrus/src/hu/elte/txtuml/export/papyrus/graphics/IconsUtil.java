package hu.elte.txtuml.export.papyrus.graphics;

import java.net.MalformedURLException;
import java.net.URL;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;

/**
 * A collection of papyrus icons
 *
 * @author András Dobreff
 */
@SuppressWarnings("javadoc")
public class IconsUtil {
	
	public static String CLASS_DIAGRAM_16 = "platform:/plugin/org.eclipse.papyrus.uml.diagram.icons/resource/obj16/Diagram_Class.gif";
	public static String ACTIVITY_DIAGRAM_16 = "platform:/plugin/org.eclipse.papyrus.uml.diagram.icons/resource/obj16/Diagram_Activity.gif";
	public static String STATEMACHINE_DIAGRAM_16 = "platform:/plugin/org.eclipse.papyrus.uml.diagram.icons/resource/obj16/Diagram_StateMachine.gif";
	
	public static String CONSTRAINT_16 = "platform:/plugin/org.eclipse.papyrus.uml.icons/resource/gif/Constraint.gif";
	public static String SIGNAL_16 = "platform:/plugin/org.eclipse.papyrus.uml.icons/resource/gif/Signal.gif";
	public static String COMMENT_16 = "platform:/plugin/org.eclipse.papyrus.uml.icons/resource/gif/Comment.gif";
	
	/**
	 * Gets an Image from the given URL
	 * @param IconURL - The URL
	 * @return Returns an Image
	 */
	public static Image getIcon(String IconURL){
			Image img;
			try{
				img = ImageDescriptor.createFromURL(new URL(IconURL)).createImage(); 
			}catch(MalformedURLException e){
				img = new Image(Display.getCurrent(),0,0);
			}
		return img;
	}
}
