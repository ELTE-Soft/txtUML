package hu.elte.txtuml.seqdiag.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;

import hu.elte.txtuml.seqdiag.export.plantuml.PlantUmlExporter;

public class PlantUmlExportTestBase {

	protected static PlantUmlExporter exporter;
	protected static IFolder genFolder;
	protected static IProject project;
	protected static IJavaProject javaProject;

	@BeforeClass
	public static void setUp() throws Exception {
		project = PlantUmlExportTestUtils.getModelsProject();
		javaProject = PlantUmlExportTestUtils.getJavaModelsProject();
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
		List<IType> seqDiags = new ArrayList<>();
		String output = null;
		try {
			seqDiags.add(javaProject.findType(project.getName().toString() + ".sequences." + diagramName));
			exporter = new PlantUmlExporter(project, "gen", seqDiags);

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
