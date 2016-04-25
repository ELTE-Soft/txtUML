package hu.elte.txtuml.project;

import java.io.ByteArrayInputStream;
import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.wizard.IWizardContainer;
import org.eclipse.ui.actions.WorkspaceModifyOperation;

import hu.elte.txtuml.api.model.Model;
import hu.elte.txtuml.utils.Logger;

/**
 * Creates the root package of the model and the model file.
 */
public class ModelCreator {

	private IResource resource;

	public IFile createModelFile(IWizardContainer context, IPackageFragmentRoot packageFragmentRoot,
			IPackageFragment packageFragment, String typeName, boolean isXtxtUml) {
		try {
			String filename;
			StringBuilder contentBuilder = new StringBuilder();
			if (isXtxtUml) {
				filename = buildXtxtUmlFileContent(packageFragment, typeName, contentBuilder);
			} else {
				filename = buildJavaFileContent(packageFragment, typeName, contentBuilder);
			}

			if (createFile(context, packageFragmentRoot, packageFragment, contentBuilder.toString(), filename)) {
				return (IFile) resource;
			}
		} catch (Throwable e) {
			Logger.sys.error("Error while creating package/model-info file", e);
		}
		return null;
	}

	private String buildJavaFileContent(IPackageFragment packageFragment, String typeName,
			StringBuilder contentBuilder) {
		String filename;
		filename = "package-info.java";
		contentBuilder.append("@Model(\"");
		contentBuilder.append(typeName);
		contentBuilder.append("\")\npackage ");
		contentBuilder.append(packageFragment.getElementName());
		contentBuilder.append(";\n\n");
		contentBuilder.append("import ");
		contentBuilder.append(Model.class.getCanonicalName());
		contentBuilder.append(";\n");
		return filename;
	}

	private String buildXtxtUmlFileContent(IPackageFragment packageFragment, String typeName,
			StringBuilder contentBuilder) {
		String filename;
		filename = "model-info.xtxtuml";
		contentBuilder.append("model-package ");
		contentBuilder.append(packageFragment.getElementName());
		contentBuilder.append(" as \"");
		contentBuilder.append(typeName);
		contentBuilder.append("\";\n");
		return filename;
	}

	private boolean createFile(IWizardContainer context, IPackageFragmentRoot packageFragmentRoot,
			IPackageFragment packageFragment, String content, String filename) {
		IRunnableWithProgress op = new WorkspaceModifyOperation() {

			@Override
			protected void execute(IProgressMonitor monitor)
					throws CoreException, InvocationTargetException, InterruptedException {
				if (monitor == null) {
					monitor = new NullProgressMonitor();
				}

				try {
					if (!packageFragment.exists()) {
						try {
							packageFragmentRoot.createPackageFragment(packageFragment.getElementName(), true, monitor);
						} catch (JavaModelException e) {
							e.printStackTrace();
						}
					}
					IResource res = packageFragment.getResource();
					IFile txtUMLFile = ((IFolder) res).getFile(filename);
					txtUMLFile.create(new ByteArrayInputStream(content.getBytes()), true, monitor);
					resource = txtUMLFile;
				} catch (OperationCanceledException e) {
					throw new InterruptedException();
				} catch (Exception e) {
					throw new InvocationTargetException(e);
				} finally {
					monitor.done();
				}
			}

		};
		try {
			context.run(true, true, op);
		} catch (InterruptedException e) {
			// cancelled by user
			return false;
		} catch (InvocationTargetException e) {
			Logger.sys.error("Error during file creation", e);
		}
		return true;
	}

}
