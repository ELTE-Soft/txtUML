package hu.elte.txtuml.project.buildpath;

import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.ui.wizards.IClasspathContainerPage;
import org.eclipse.jdt.ui.wizards.NewElementWizardPage;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;

import hu.elte.txtuml.project.Messages;

/**
 * Shows an informational page about the executor runtime library.
 */
public class RuntimeLibraryContainerWizardPage extends NewElementWizardPage
		implements IClasspathContainerPage {

	private IClasspathEntry containerEntry;

	@SuppressWarnings("restriction")
	public RuntimeLibraryContainerWizardPage() {
		super("RuntimeLibraryContainer"); //$NON-NLS-1$
		setTitle(Messages.RuntimeLibraryContainerWizardPage_classpathLibraryTitle);
		setImageDescriptor(org.eclipse.jdt.internal.ui.JavaPluginImages.DESC_WIZBAN_ADD_LIBRARY);
		setDescription(Messages.RuntimeLibraryContainerWizardPage_classpathLibraryDescription);
		this.containerEntry = JavaCore
				.newContainerEntry(RuntimeLibraryContainerInitializer.LIBRARY_PATH);
	}

	@Override
	public void createControl(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		composite.setLayout(new FillLayout());
		Label label = new Label(composite, SWT.NONE);
		String aboutText = NLS
				.bind(Messages.RuntimeLibraryContainerWizardPage_classpathLibraryText,
						containedBundles());
		label.setText(aboutText);
		setControl(composite);
	}

	private String containedBundles() {
		StringBuilder builder = new StringBuilder();
		for (String bundleId : RuntimeLibraryContainer.BUNDLE_IDS_TO_INCLUDE) {
			builder.append("\t").append(bundleId).append("\n"); //$NON-NLS-1$ //$NON-NLS-2$
		}
		return builder.toString();
	}

	@Override
	public boolean finish() {
		return true;
	}

	@Override
	public IClasspathEntry getSelection() {
		return containerEntry;

	}

	@Override
	public void setSelection(IClasspathEntry containerEntry) {
		// intentionally left blank
	}
}
