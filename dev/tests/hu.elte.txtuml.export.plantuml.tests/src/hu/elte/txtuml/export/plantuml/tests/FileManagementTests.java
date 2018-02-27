package hu.elte.txtuml.export.plantuml.tests;

import java.util.ArrayList;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.export.plantuml.PlantUmlExporter;

public class FileManagementTests extends PlantUmlExportTestBase {

	@BeforeClass
	public static void setUp() throws Exception {
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
			Assert.assertFalse("Exception: " + e.getMessage(), true);
		}

		IFile outfile = genFolder.getFile("SequenceBasic.txt");

		Assert.assertEquals(1, fileCount);
		Assert.assertTrue("Output file does not exists", outfile.exists());
	}

}
