package hu.elte.txtuml.api.model.execution.handlers;

import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import hu.elte.txtuml.api.model.execution.ModelExecutor;
import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.execution.seqdiag.error.MessageError;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.Logger;
import hu.elte.txtuml.utils.eclipse.Dialogs;

public class RunSelectedSequenceDiagramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Job job = new Job("Sequence diagram execution") {
			
			@Override
			protected IStatus run(IProgressMonitor monitor) {
				ISelection selection = HandlerUtil.getCurrentSelection(event);
				IStructuredSelection strSelection = (IStructuredSelection) selection;
	
				ICompilationUnit selectedCompilationUnit = ((IAdaptable)strSelection.getFirstElement()).getAdapter(ICompilationUnit.class);
				try {
					IType[] types = selectedCompilationUnit.getTypes();
					List<IType> sequenceDiagrams = Stream.of(types).filter(type -> {
						ITypeHierarchy tyHierarchy = null;
						try {
							tyHierarchy = type.newSupertypeHierarchy(null);
						} catch (JavaModelException ex) {
							return false;
						}
						return Stream.of(tyHierarchy.getAllSupertypes(type))
								.anyMatch(superTy -> superTy.getFullyQualifiedName().equals(SequenceDiagram.class.getCanonicalName()));
					}).collect(Collectors.toList());
					for (IType sequenceDiagram : sequenceDiagrams) {
						runSequenceDiagram(sequenceDiagram, monitor);
					}
				} catch (Exception e) {
					Logger.sys.error(e.getMessage());
					return Status.CANCEL_STATUS;
				}
				return Status.OK_STATUS;
			}	
		};
		
		job.setUser(true);
		job.schedule();
		return null;
	}

	@SuppressWarnings("unchecked")
	private void runSequenceDiagram(IType sequenceDiagramType, IProgressMonitor monitor) throws Exception {
		IJavaProject project = sequenceDiagramType.getJavaProject();
		String[] classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(project);
		List<URL> urlList = new ArrayList<URL>();
		for (int i = 0; i < classPathEntries.length; i++) {
			String entry = classPathEntries[i];
			IPath path = new Path(entry);
			URL url = path.toFile().toURI().toURL();
			urlList.add(url);
		}
		URL[] urls = (URL[]) urlList.toArray(new URL[urlList.size()]);
		URLClassLoader classLoader = new URLClassLoader(urls, this.getClass().getClassLoader());
		Class<? extends SequenceDiagram> myClass = (Class<? extends SequenceDiagram>) classLoader.loadClass(sequenceDiagramType.getFullyQualifiedName());

		SequenceDiagramExecutor executor = SequenceDiagramExecutor.create();
		Constructor<? extends SequenceDiagram> constructor = myClass.getDeclaredConstructor();
		
		if (!constructor.isAccessible())
			constructor.setAccessible(true);
		
		executor.setDiagram(constructor.newInstance());
		executor.startNoWait().shutdown();
		while (executor.getStatus().equals(ModelExecutor.Status.ACTIVE)) {
			if (monitor.isCanceled()) {
				executor.shutdownNow().awaitTermination();
				classLoader.close();
				throw new InterruptedException("Job was canceled.");
			}
		}
		classLoader.close();
		writeToConsole(executor.getErrors());
	}

	private MessageConsole findConsole() {
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		Optional<IConsole> seqConsole = Stream.of(existing)
				.filter(console -> console.getName().equals("Sequence Diagram Console"))
				.findFirst();
		
		if (seqConsole.isPresent()) {
			return (MessageConsole) seqConsole.get();
		} else {
			MessageConsole myConsole = new MessageConsole("Sequence Diagram Console", null);
			conMan.addConsoles(new IConsole[]{myConsole});
			return myConsole;
		}
	}
	
	private void writeToConsole(List<MessageError> errors) {
		Display.getDefault().syncExec(() -> {
			MessageConsole console = findConsole();
			MessageConsoleStream out = console.newMessageStream();
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			IConsoleView view;
				try {
					view = (IConsoleView) page.showView(id);
					view.display(console);
				} catch (PartInitException e) {
					Dialogs.errorMsgb("Error",
							"Error occured while trying to open output console.", e);
				}
			errors.stream().forEach(error -> out.println(error.getMessage()));
			
			if (errors.size() != 0) {
				out.println("-------------End of errors-------------");
				out.println();
			} else {
				out.println("-------------There were no errors-------------");
				out.println();
			}
		});
	}
}