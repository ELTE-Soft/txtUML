package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;

public class BasicSeqDiagElementTests {

	static PlantUmlExporter exporter;
	static IFolder genFolder;
	static IProject project;

	@BeforeClass
	public static void setUp() {
		try {
			project = PlantUmlExportTestUtils.getSelfProject();
		} catch (Exception e) {
			e.printStackTrace();
		}

		genFolder = project.getFolder("gen");
	}

	@Test
	public void testLifelines() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.SequenceBasic");

			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);
			exporter.generatePlantUmlOutput(null);
			IFile outfile = genFolder.getFile("SequenceBasic.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());
		} catch (Exception e) {
			Assert.assertFalse(true);
		} finally {
			if (rd != null) {
				rd.close();
			}
		}
	}

	@Test
	public void testMessaging() {
		Scanner rd = null;
		try {
			ArrayList<String> SeqDiagName = new ArrayList<String>();
			SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.SequenceMessaging");
			exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);
			exporter.generatePlantUmlOutput(null);
			IFile outfile = genFolder.getFile("SequenceMessaging.txt");

			String output = PlantUmlExportTestUtils.getOutput(outfile);

			rd = new Scanner(output);

			Assert.assertEquals("@startuml", rd.nextLine());
			Assert.assertEquals("participant lifeline1", rd.nextLine());
			Assert.assertEquals("participant lifeline3", rd.nextLine());
			Assert.assertEquals("participant lifeline2", rd.nextLine());
			Assert.assertEquals("activate lifeline1", rd.nextLine());
			Assert.assertEquals("activate lifeline2", rd.nextLine());
			Assert.assertEquals("lifeline1->lifeline2 : hu.elte.txtuml.export.plantuml.tests.testModel.TestSig",
					rd.nextLine());
			Assert.assertEquals("activate lifeline3", rd.nextLine());
			Assert.assertEquals("lifeline2->lifeline3 : hu.elte.txtuml.export.plantuml.tests.testModel.TestSig",
					rd.nextLine());
			Assert.assertEquals("lifeline3->lifeline2 : hu.elte.txtuml.export.plantuml.tests.testModel.TestSig",
					rd.nextLine());
			Assert.assertEquals("lifeline2->lifeline1 : hu.elte.txtuml.export.plantuml.tests.testModel.TestSig",
					rd.nextLine());
			Assert.assertEquals("deactivate lifeline1", rd.nextLine());
			Assert.assertEquals("deactivate lifeline2", rd.nextLine());
			Assert.assertEquals("deactivate lifeline3", rd.nextLine());
			Assert.assertEquals("@enduml", rd.nextLine());
		} catch (Exception e) {

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

		genFolder = null;
		project = null;
	}
}
