package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.seqdiag.export.plantuml.PlantUmlExporter;

public class FileManagementTests {
	private static PlantUmlExporter exporter;
	private static IFolder genFolder;
	private static IProject project;

	@BeforeClass
	public static void setUp() throws Exception {
		project = PlantUmlExportTestUtils.getModelsProject();

		genFolder = project.getFolder("gen");

		ArrayList<String> SeqDiagNames = new ArrayList<String>();
		SeqDiagNames.add(project.getName().toString() + ".sequences.SequenceBasic");
		exporter = new PlantUmlExporter(project, "gen", SeqDiagNames);
		exporter.generatePlantUmlOutput(new NullProgressMonitor());
	}

	@Test
	public void testFolderCreation() {
		Assert.assertTrue("gen folder does not exists", genFolder.exists());
	}

	@Test
	public void testFileCreation() {
		Assert.assertTrue("gen folder does not exists", genFolder.exists());
		Assert.assertEquals(1, exporter.expotedCount());

		int fileCount = 0;
		try {
			for (IResource res : genFolder.members()) {
				if (res.getType() == IResource.FILE) {
					fileCount++;
				}
			}
		} catch (CoreException e) {
			Assert.assertFalse("Exception:" + e.getMessage(), true);
		}

		IFile outfile = genFolder.getFile("SequenceBasic.txt");

		Assert.assertEquals(1, fileCount);
		Assert.assertTrue("Output file does not exists", outfile.exists());
	}

	@AfterClass
	public static void tearDown() throws Exception {
		exporter = null;
		genFolder.delete(true, new NullProgressMonitor());

		genFolder = null;
		project = null;
	}
}
