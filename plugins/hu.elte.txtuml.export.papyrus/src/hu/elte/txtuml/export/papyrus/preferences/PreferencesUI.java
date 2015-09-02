package hu.elte.txtuml.export.papyrus.preferences;

import hu.elte.txtuml.export.papyrus.graphics.IconsUtil;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

/**
 * Creates the controls for the preferences
 *
 * @author András Dobreff
 */
public class PreferencesUI {

	/**
	 * Value-Objects that hold the checkboxes and the preferences together
	 *
	 * @author András Dobreff
	 */
	private class CheckBoxRecord{
		TreeItem chbx;
		String preference;
		
		public CheckBoxRecord(TreeItem parent, String text, String icon, String preference){
			this.chbx = new TreeItem(parent, 0);
			init(text, icon, preference);
		}
		
		public CheckBoxRecord(Tree parent, String text, String icon, String preference){
			this.chbx = new TreeItem(parent, 0);
			init(text, icon, preference);
		}
		
		private void init(String text, String icon, String preference){
			this.chbx.setText(text);
			this.chbx.setImage(IconsUtil.getIcon(icon));
			this.preference = preference;
		}
	}

	private Map<String, CheckBoxRecord> checkboxes; 

	/**
	 * The Constructor
	 */
	public PreferencesUI(){
		checkboxes = new HashMap<String, CheckBoxRecord>();
	}
	
	/**
	 * Initializes the UI
	 * @param parent - The parent of the controls
	 * @param preferencesManager - The PreferencesManager where the preferences are get/set
	 */
	public void init(Composite parent){
		final Tree tree = new Tree(parent, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL);
		
		checkboxes.put("ClassDiagramCHBX", new CheckBoxRecord(tree, "Class Diagram",
								IconsUtil.CLASS_DIAGRAM_16,	PreferencesManager.CLASS_DIAGRAM_PREF));
		checkboxes.put("ActivityDiagramCHBX", new CheckBoxRecord(tree, "Activity Diagram",
								IconsUtil.ACTIVITY_DIAGRAM_16, PreferencesManager.ACTIVITY_DIAGRAM_PREF));
		checkboxes.put("StateMachineDiagramCHBX", new CheckBoxRecord(tree, "StateMachine Diagram",
								IconsUtil.STATEMACHINE_DIAGRAM_16, PreferencesManager.STATEMACHINE_DIAGRAM_PREF));
		
		checkboxes.put("CDConstraintCHBX", new CheckBoxRecord(checkboxes.get("ClassDiagramCHBX").chbx, "Constraint",
								IconsUtil.CONSTRAINT_16, PreferencesManager.CLASS_DIAGRAM_CONSTRAINT_PREF));
		checkboxes.put("CDSignalCHBX", new CheckBoxRecord(checkboxes.get("ClassDiagramCHBX").chbx, "Signal",
								IconsUtil.SIGNAL_16, PreferencesManager.CLASS_DIAGRAM_SIGNAL_PREF));
		checkboxes.put("CDCommentCHBX", new CheckBoxRecord(checkboxes.get("ClassDiagramCHBX").chbx, "Comment",
								IconsUtil.COMMENT_16, PreferencesManager.CLASS_DIAGRAM_COMMENT_PREF));
		
		checkboxes.put("ADCommentCHBX", new CheckBoxRecord(checkboxes.get("ActivityDiagramCHBX").chbx, "Comment",
								IconsUtil.COMMENT_16, PreferencesManager.ACTIVITY_DIAGRAM_COMMENT_PREF));
		
		checkboxes.put("SMDCommentCHBX", new CheckBoxRecord(checkboxes.get("StateMachineDiagramCHBX").chbx, "Comment",
								IconsUtil.COMMENT_16, PreferencesManager.STATEMACHINE_DIAGRAM_COMMENT_PREF));
		checkboxes.put("SMDConstraintCHBX", new CheckBoxRecord(checkboxes.get("StateMachineDiagramCHBX").chbx, "Constraint",
								IconsUtil.CONSTRAINT_16, PreferencesManager.STATEMACHINE_DIAGRAM_CONSTRAINT_PREF));
		
		this.refresh();
	}
	
	/**
	 * Gives a Map<String, Object> which contains the prefence name-value pairs, that should be saved.
	 * @return Map<String, Object> of name-value pairs of the swt widgets on the UI. 
	 */
	public Map<String, Object> getValues(){
		Map<String, Object> map = new HashMap<String, Object>();
		
		Iterator<Map.Entry<String, CheckBoxRecord>> it = checkboxes.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, CheckBoxRecord> pairs = it.next();
	        CheckBoxRecord rec = pairs.getValue();
	        
	        String pref = rec.preference;
	        boolean value = rec.chbx.getChecked();
	        map.put(pref, value);
	    }
		
		return map;
	}
	
	/**
	 * Refreshes the states of the swt widgets
	 */
	public void refresh(){
		Iterator<Map.Entry<String, CheckBoxRecord>> it = checkboxes.entrySet().iterator();
		while (it.hasNext()) {
	        Map.Entry<String, CheckBoxRecord> pairs = it.next();
	        CheckBoxRecord rec = pairs.getValue();
        	rec.chbx.setChecked(PreferencesManager.getBoolean(rec.preference));
	    }
	}
}
