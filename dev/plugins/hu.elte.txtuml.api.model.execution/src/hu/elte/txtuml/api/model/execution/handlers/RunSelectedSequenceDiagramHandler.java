package hu.elte.txtuml.api.model.execution.handlers;

import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.launching.JavaRuntime;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.console.IConsoleManager;
import org.eclipse.ui.console.IConsoleView;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;
import org.eclipse.ui.handlers.HandlerUtil;

import hu.elte.txtuml.api.model.execution.SequenceDiagramExecutor;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.Logger;

public class RunSelectedSequenceDiagramHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IStructuredSelection strSelection = (IStructuredSelection) selection;

		ICompilationUnit selectedCompilationUnit = ((IAdaptable)strSelection.getFirstElement()).getAdapter(ICompilationUnit.class);
		try {
			IType[] types = selectedCompilationUnit.getTypes();
			Stream.of(types).filter(type -> {
				ITypeHierarchy tyHierarchy = null;
				try {
					tyHierarchy = type.newSupertypeHierarchy(null);
				} catch (JavaModelException ex) {
					return false;
				}
				return Stream.of(tyHierarchy.getAllSupertypes(type))
						.anyMatch(superTy -> superTy.getFullyQualifiedName().equals(SequenceDiagram.class.getCanonicalName()));
			}).forEach(this::runSequenceDiagram);
		} catch (JavaModelException e) {
			Logger.sys.error(e.getMessage());
		}	
		return null;
	}

	@SuppressWarnings("unchecked")
	private void runSequenceDiagram(IType sequenceDiagramType) {
		try {
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
			executor.setDiagram(myClass.newInstance());
			executor.run();
			classLoader.close();
			MessageConsole console = findConsole();
			MessageConsoleStream out = console.newMessageStream();
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow win = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = win.getActivePage();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;
			IConsoleView view = (IConsoleView) page.showView(id);
			view.display(console);

			executor.getErrors().stream().forEach(error -> out.println(error.getMessage()));
		} catch (Exception e) {
			Logger.sys.error(e.getMessage());
		}
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
}