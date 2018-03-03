package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;

public class PlantUmlExportTestBase {

	protected static PlantUmlExporter exporter;
	protected static IFolder genFolder;
	protected static IProject project;

	@BeforeClass
	public static void setUp() throws Exception {
		project = PlantUmlExportTestUtils.getModelsProject();
		genFolder = project.getFolder("gen");
	}

	@AfterClass
	public static void tearDown() throws Exception {
		exporter = null;
		genFolder.delete(true, new NullProgressMonitor());
		genFolder = null;
		project = null;
	}

	protected void assertOutput(String diagramName, List<String> expected) {
		List<String> seqDiagNames = new ArrayList<>();
		seqDiagNames.add(project.getName().toString() + ".sequences." + diagramName);
		exporter = new PlantUmlExporter(project, "gen", seqDiagNames);
		String output = null;
		try {
			exporter.generatePlantUmlOutput(null);
			IFile outfile = genFolder.getFile(diagramName + ".txt");
			output = PlantUmlExportTestUtils.getOutput(outfile);
		} catch (Exception e) {
			Assert.assertFalse("Exception: " + e.getMessage(), true);
		}

		try (Scanner rd = new Scanner(output)) {
			expected.forEach(line -> Assert.assertEquals(line, rd.nextLine()));
		}
	}

}
