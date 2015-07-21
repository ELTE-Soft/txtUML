package hu.elte.txtuml.export.papyrus.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import hu.elte.txtuml.eclipseutils.ProjectUtils;
import hu.elte.txtuml.export.papyrus.PapyrusModelCreator;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.papyrus.infra.gmfdiag.common.model.NotationModel;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.UMLFactory;
import org.eclipse.uml2.uml.UMLPackage;
import org.eclipse.uml2.uml.resource.UMLResource;
import org.junit.Before;
import org.junit.Test;

/**
 * Unit test for {@link PapyrusModelCreator}
 *
 * @author András Dobreff
 * 
 * <p>
 * <b>Attention:</b>
 * This test should be run as a JUnit Plug-in test
 * </p>
 */
public class PapyrusModelCreatorTest {

	public PapyrusModelCreator creator;
	public IProject project;
	public static final String modelPath = "TestProject/test"; 
	
	@Before
	public void setUp() throws Exception {
		cleanWorkspace();
		project = ProjectUtils.createProject("TestProject");
		ProjectUtils.openProject(project);
		
		creator = new PapyrusModelCreator(modelPath);
	}

	
	private void cleanWorkspace() throws Exception {
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for(int i = 0; i<projects.length; i++){
			try {
				projects[i].delete(true, new NullProgressMonitor());
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	@Test
	public void testDiExists() {
		assertFalse(creator.diExists());
		creator.createPapyrusModel();
		assertTrue(creator.diExists());
	}

	@Test
	public void testGetDi() {
		creator.createPapyrusModel();
		assertTrue(creator.diExists());
		IFile diFile = creator.getDi();
		assertTrue(diFile.getFileExtension().equals("di"));
	}	
	
	@Test
	public void testSetUpUML() {
		IProject sourceproject = ProjectUtils.createProject("sourceProject");
		
		String modelFilename = "dummy";
		
		String tmpfolder = sourceproject.getLocation().toString();

        URI uri = URI.createURI(tmpfolder).appendSegment(modelFilename).appendFileExtension(UMLResource.FILE_EXTENSION);
        URI urifile = URI.createFileURI(uri.toString());
        String modelname = "TestModel";
		createUMLFile(urifile, modelname);
		
		creator.setUpUML(urifile.toString());
		
		IFile file = project.getFile("test."+UMLResource.FILE_EXTENSION);
		
		assertTrue(file.exists());
		assertTrue(file.getFileExtension().equals(UMLResource.FILE_EXTENSION));
		/* uml model asserts */
		

		ResourceSetImpl RESOURCE_SET = new ResourceSetImpl();
		Resource resource = RESOURCE_SET.getResource(urifile, true);
		
		org.eclipse.uml2.uml.Package package_ = (org.eclipse.uml2.uml.Package) EcoreUtil
												.getObjectByType(resource.getContents(), UMLPackage.Literals.PACKAGE);
		assertTrue(package_ instanceof Model);
		assertTrue(package_.getName().equals(modelname));
	}
	
	private void createUMLFile(URI uri, String modelname){
		Model model = UMLFactory.eINSTANCE.createModel();
        
		model.setName(modelname);

    	ResourceSet resourceSet = new ResourceSetImpl();
  		resourceSet.getPackageRegistry().put(UMLPackage.eNS_URI, UMLPackage.eINSTANCE);
        
        resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put(
  				UMLResource.FILE_EXTENSION,
  				UMLResource.Factory.INSTANCE
  			);
        
        Resource modelResource = resourceSet.createResource(uri);
        modelResource.getContents().add(model);
        try {
			modelResource.save(null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testCreatePapyrusModel() {
		IFile UMLfile = project.getFile("test."+UMLResource.FILE_EXTENSION);
		IFile Difile = project.getFile("test."+"di");
		IFile Notationfile = project.getFile("test."+NotationModel.NOTATION_FILE_EXTENSION);
		
		assertFalse(UMLfile.exists());
		assertFalse(Difile.exists());
		assertFalse(Notationfile.exists());
		
		creator.createPapyrusModel();
		
		assertTrue(UMLfile.exists());
		assertTrue(Difile.exists());
		assertTrue(Notationfile.exists());
		assertEquals("test.uml", UMLfile.getName());
		assertEquals("test.di", Difile.getName());
		assertEquals("test.notation", Notationfile.getName());
	}
}
