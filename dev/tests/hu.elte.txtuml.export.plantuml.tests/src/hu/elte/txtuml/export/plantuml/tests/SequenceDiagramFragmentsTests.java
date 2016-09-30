package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;

public class SequenceDiagramFragmentsTests {
	static PlantUmlExporter exporter;
	IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
	static IFolder genFolder;
	static IProject project;

	@BeforeClass
	public static void setUp() throws Exception {
		project = PlantUmlExportTestUtils.getSelfProject();
		genFolder = project.getFolder("gen");
	}

	@Test
	public void testSeqFragment() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagNames = new ArrayList<String>();
			SeqDiagNames.add(project.getName().toString() + ".sequences.SeqFragment");

			exporter = new PlantUmlExporter(project.getName().toString(), "gen", SeqDiagNames);

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

		} catch (Exception e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
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
			ArrayList<String> SeqDiagNames = new ArrayList<String>();
			SeqDiagNames.add(project.getName().toString() + ".sequences.StrictFragment");

			exporter = new PlantUmlExporter(project.getName().toString(), "gen", SeqDiagNames);

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

		} catch (Exception e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
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
			ArrayList<String> SeqDiagNames = new ArrayList<String>();
			SeqDiagNames.add(project.getName().toString() + ".sequences.OPTFragment");

			exporter = new PlantUmlExporter(project.getName().toString(), "gen", SeqDiagNames);

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

		} catch (Exception e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
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
			ArrayList<String> SeqDiagNames = new ArrayList<String>();
			SeqDiagNames.add(project.getName().toString() + ".sequences.ALTFragment");

			exporter = new PlantUmlExporter(project.getName().toString(), "gen", SeqDiagNames);

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

		} catch (Exception e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
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
			ArrayList<String> SeqDiagNames = new ArrayList<String>();
			SeqDiagNames.add(project.getName().toString() + ".sequences.LoopFragment");

			exporter = new PlantUmlExporter(project.getName().toString(), "gen", SeqDiagNames);

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

		} catch (Exception e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@AfterClass
	public static void tearDown() throws Exception {
		exporter = null;
		genFolder.delete(true, new NullProgressMonitor());

		project = null;
		genFolder = null;
	}
}
