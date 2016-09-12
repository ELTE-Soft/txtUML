package hu.elte.txtuml.export.plantuml.tests;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;
import hu.elte.txtuml.export.plantuml.exceptions.SequenceDiagramStructuralException;

public class SequenceDiagramFragmentsTests {
	static PlantUmlExporter exporter;
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	static IFolder genFolder;
	static IProject project;

	@BeforeClass
	public static void setUp() {
		try {
			project = PlantUmlExportTestUtils.getSelfProject();
			project.open(new NullProgressMonitor());
		} catch (Exception e) {
			e.printStackTrace();
		}

		genFolder = project.getFolder("gen");
	}

	@Test
	public void testSeqFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.SeqFragment");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);

			exporter.generatePlantUmlOutput(new NullProgressMonitor());

			IFile outfile = genFolder.getFile("SeqFragment.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("group SEQ", rd.nextLine());
			rd.nextLine();
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());

			rd.close();
			// outfile.delete(true, null);

		} catch (CoreException | SequenceDiagramStructuralException | IOException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@Test
	public void testStrictFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.StrictFragment");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);

			exporter.generatePlantUmlOutput(new NullProgressMonitor());

			IFile outfile = genFolder.getFile("StrictFragment.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("group STRICT", rd.nextLine());
			rd.nextLine();
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());

			rd.close();
			// outfile.delete(true, null);

		} catch (CoreException | SequenceDiagramStructuralException | IOException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@Test
	public void testOptFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.OPTFragment");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);

			exporter.generatePlantUmlOutput(new NullProgressMonitor());

			IFile outfile = genFolder.getFile("OPTFragment.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("opt true", rd.nextLine());
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());

			rd.close();
			// outfile.delete(true, null);

		} catch (CoreException | SequenceDiagramStructuralException | IOException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@Test
	public void testAltFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.ALTFragment");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);

			exporter.generatePlantUmlOutput(new NullProgressMonitor());

			IFile outfile = genFolder.getFile("ALTFragment.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("alt true", rd.nextLine());
			Assert.assertEquals("else true && true", rd.nextLine());
			Assert.assertEquals("else", rd.nextLine());
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());

			rd.close();

		} catch (CoreException | SequenceDiagramStructuralException | IOException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@Test
	public void testLoopFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.LoopFragment");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);

			exporter.generatePlantUmlOutput(new NullProgressMonitor());

			IFile outfile = genFolder.getFile("LoopFragment.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("loop from i=1 to i < 1 by i+1", rd.nextLine());
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("loop while i == 2", rd.nextLine());
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("loop while i == 2", rd.nextLine());
			Assert.assertEquals("end", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());

		} catch (CoreException | SequenceDiagramStructuralException | IOException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@AfterClass
	public static void tearDown() {
		exporter = null;

		try {
			genFolder.delete(true, new NullProgressMonitor());
		} catch (CoreException e) {
			e.printStackTrace();
		}

		project = null;
		genFolder = null;
	}
}
