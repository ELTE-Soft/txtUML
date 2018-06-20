package hu.elte.txtuml.seqdiag.tests;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.IType;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import hu.elte.txtuml.seqdiag.export.plantuml.PlantUmlExporter;

public class FileManagementTests extends PlantUmlExportTestBase {

	@BeforeClass
	public static void initialize() throws Exception {
		List<IType> seqDiags = new ArrayList<>();
		seqDiags.add(javaProject.findType(project.getName().toString() + ".sequences.SequenceBasic"));
		exporter = new PlantUmlExporter(project, "gen", seqDiags);
		exporter.generatePlantUmlOutput(new NullProgressMonitor());
	}

	@Test
	public void testFolderCreation() {
		Assert.assertTrue("gen folder does not exists", genFolder.exists());
	}

	@Test
	public void testFileCreation() {
		Assert.assertTrue("gen folder does not exists", genFolder.exists());

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
