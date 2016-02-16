package hu.elte.txtuml.project;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "hu.elte.txtuml.project.messages"; //$NON-NLS-1$
	public static String RuntimeLibraryContainer_libraryDescription;
	public static String RuntimeLibraryContainerWizardPage_classpathLibraryDescription;
	public static String RuntimeLibraryContainerWizardPage_classpathLibraryText;
	public static String RuntimeLibraryContainerWizardPage_classpathLibraryTitle;
	public static String WizardNewtxtUMLProjectCreationPage_ErrorMessageProjectName;
	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
