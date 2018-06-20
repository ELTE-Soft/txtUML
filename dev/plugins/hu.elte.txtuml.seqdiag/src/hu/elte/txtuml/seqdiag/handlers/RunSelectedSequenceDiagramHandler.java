package hu.elte.txtuml.seqdiag.handlers;

import java.lang.reflect.Constructor;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.ITypeHierarchy;
import org.eclipse.jdt.core.JavaModelException;
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
import hu.elte.txtuml.api.model.execution.seqdiag.error.ErrorLevel;
import hu.elte.txtuml.api.model.execution.seqdiag.error.SequenceDiagramProblem;
import hu.elte.txtuml.api.model.seqdiag.SequenceDiagram;
import hu.elte.txtuml.utils.eclipse.ClassLoaderProvider;
import hu.elte.txtuml.utils.eclipse.Dialogs;

public class RunSelectedSequenceDiagramHandler extends AbstractHandler {

	/**
	 * Finds the selected sequence diagrams and executes them.
	 */
	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		Job job = new Job("Sequence diagram execution") {

			@Override
			protected IStatus run(IProgressMonitor monitor) {
				monitor.beginTask("Execution", IProgressMonitor.UNKNOWN);
				MessageConsole console = findConsole();
				console.clearConsole();
				ISelection selection = HandlerUtil.getCurrentSelection(event);
				IStructuredSelection strSelection = (IStructuredSelection) selection;

				for (Object selected : strSelection.toArray()) {
					ICompilationUnit selectedCompilationUnit = ((IAdaptable) selected)
							.getAdapter(ICompilationUnit.class);
					try {
						IType[] types = selectedCompilationUnit.getTypes();
						List<IType> sequenceDiagrams = Stream.of(types).filter(type -> {
							ITypeHierarchy tyHierarchy = null;
							try {
								tyHierarchy = type.newSupertypeHierarchy(null);
							} catch (JavaModelException ex) {
								return false;
							}
							return Stream.of(tyHierarchy.getAllSupertypes(type)).anyMatch(superTy -> superTy
									.getFullyQualifiedName().equals(SequenceDiagram.class.getCanonicalName()));
						}).collect(Collectors.toList());
						for (IType sequenceDiagram : sequenceDiagrams) {
							runSequenceDiagram(sequenceDiagram, console, monitor);
						}
					} catch (InterruptedException e) {
						return Status.CANCEL_STATUS;
					} catch (Exception e) {
						Display.getDefault().syncExec(() -> {
							Dialogs.errorMsgb("Error", "Error occured during sequence diagram execution.", e);
						});
						return Status.CANCEL_STATUS;
					}
				}
				return Status.OK_STATUS;
			}
		};

		job.setUser(true);
		job.schedule();
		return null;
	}

	/**
	 * Loads the the given sequence diagram with URLClassloader, runs it with a
	 * {@link SequenceDiagramExecutor}, waits for its termination and writes the
	 * result to the given console.
	 * <p>
	 * The user can cancel the execution.
	 */
	@SuppressWarnings("unchecked")
	private void runSequenceDiagram(IType sequenceDiagramType, MessageConsole console, IProgressMonitor monitor)
			throws Exception {
		monitor.subTask("Execution of " + sequenceDiagramType.getElementName());
		String projectName = sequenceDiagramType.getJavaProject().getElementName();
		URLClassLoader classLoader = ClassLoaderProvider.getClassLoaderForProject(projectName,
				this.getClass().getClassLoader());
		Class<? extends SequenceDiagram> myClass = (Class<? extends SequenceDiagram>) classLoader
				.loadClass(sequenceDiagramType.getFullyQualifiedName());
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
		writeToConsole(console, executor.getErrors(), sequenceDiagramType.getElementName());
	}

	/**
	 * Searches for a console with the name "Sequence Diagram Console" and
	 * returns it if it was found. If the console was not found creates a new
	 * one and returns it.
	 */
	private MessageConsole findConsole() {
		final String seqDiagConsoleName = "Sequence Diagram Console";
		ConsolePlugin plugin = ConsolePlugin.getDefault();
		IConsoleManager conMan = plugin.getConsoleManager();
		IConsole[] existing = conMan.getConsoles();
		Optional<IConsole> seqConsole = Stream.of(existing)
				.filter(console -> console.getName().equals(seqDiagConsoleName)).findFirst();

		if (seqConsole.isPresent()) {
			return (MessageConsole) seqConsole.get();
		} else {
			MessageConsole newSeqConsole = new MessageConsole(seqDiagConsoleName, null);
			conMan.addConsoles(new IConsole[] { newSeqConsole });
			return newSeqConsole;
		}
	}

	/**
	 * Writes the results into a console.
	 */
	private void writeToConsole(MessageConsole console, List<SequenceDiagramProblem> problems,
			String sequenceDiagramClassName) {
		Display.getDefault().syncExec(() -> {
			MessageConsoleStream out = console.newMessageStream();
			IWorkbench wb = PlatformUI.getWorkbench();
			IWorkbenchWindow window = wb.getActiveWorkbenchWindow();
			IWorkbenchPage page = window.getActivePage();
			String id = IConsoleConstants.ID_CONSOLE_VIEW;

			IConsoleView view;
			try {
				view = (IConsoleView) page.showView(id);
				view.display(console);
			} catch (PartInitException e) {
				Dialogs.errorMsgb("Error", "Error occured while trying to open output console.", e);
			}

			List<SequenceDiagramProblem> errors = problems.stream()
					.filter(pr -> pr.getErrorLevel().equals(ErrorLevel.ERROR)).collect(Collectors.toList());
			List<SequenceDiagramProblem> warnings = problems.stream()
					.filter(pr -> pr.getErrorLevel().equals(ErrorLevel.WARNING)).collect(Collectors.toList());

			out.println("------------- " + sequenceDiagramClassName + " test results -------------");
			if (errors.isEmpty()) {
				out.println("PASSED.");
			} else {
				out.println("FAILED.");
			}
			out.println("Errors:   " + errors.size());
			out.println("Warnings: " + warnings.size());
			out.print("-----------------------------------------");
			out.println(IntStream.range(0, sequenceDiagramClassName.length()).mapToObj(i -> "-")
					.collect(Collectors.joining()));
			
			if (!errors.isEmpty()) {
				out.println();
				out.println("Errors: ");
			}
			errors.stream().forEach(error -> {
				out.println();
				out.println(Stream.of(error.getMessage().split("\n")).map(line -> "  " + line)
						.collect(Collectors.joining("\n")));
			});
			
			if (!warnings.isEmpty()) {
				out.println();
				out.println("Warnings: ");
			}
			warnings.stream().forEach(warning -> {
				out.println();
				out.println(Stream.of(warning.getMessage().split("\n")).map(line -> "  " + line)
						.collect(Collectors.joining("\n")));
			});
		});
	}
}