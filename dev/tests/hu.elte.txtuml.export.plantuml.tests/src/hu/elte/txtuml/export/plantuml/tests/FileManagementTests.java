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

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;

public class FileManagementTests {
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

		ArrayList<String> SeqDiagName = new ArrayList<String>();
		SeqDiagName.add("hu.elte.txtuml.export.plantuml.tests.sequences.SequenceBasic");
		exporter = new PlantUmlExporter("hu.elte.txtuml.export.plantuml.tests", "gen", SeqDiagName);
		try {
			exporter.generatePlantUmlOutput(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testFolderCreation() {
		Assert.assertTrue(genFolder.exists());
	}

	@Test
	public void testFileCreation() {
		Assert.assertTrue(genFolder.exists());
		Assert.assertEquals(1, exporter.expotedCount());

		int fileCount = 0;
		try {
			for (IResource res : genFolder.members()) {
				if (res.getType() == IResource.FILE) {
					fileCount++;
				}
			}
		} catch (CoreException e) {
			e.printStackTrace();
			Assert.assertFalse(true);
		}

		IFile outfile = genFolder.getFile("SequenceBasic.txt");

		Assert.assertEquals(1, fileCount);
		Assert.assertTrue(outfile.exists());
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
