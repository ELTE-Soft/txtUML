package hu.elte.txtuml.export.papyrus;

import hu.elte.txtuml.export.Uml2Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.impl.BasicEObjectImpl;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.sasheditor.contentprovider.IPageManager;
import org.eclipse.papyrus.infra.core.services.ExtensionServicesRegistry;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.uml.diagram.wizards.category.NewPapyrusModelCommand;
import org.eclipse.uml2.uml.Model;
import org.eclipse.uml2.uml.Profile;

/**
 * Controls the creation of a Papyrus Model
 *
 * @author András Dobreff
 */
public class PapyrusModelCreator {
	private IFile diFile;
	private IFile umlFile;
	private String diFilePath;
	private String umlFilePath;
	private ServicesRegistry registry;

	/**
	 * The Constructor
	 * @param modelpath
	 */
	public PapyrusModelCreator(String modelpath){
		diFilePath = modelpath+".di";
		umlFilePath = modelpath+".uml";
		
		diFile = fileFromPath(diFilePath);
		umlFile = fileFromPath(umlFilePath);
	}
	
	/**
	 * Checks if the .di file exists
	 * @return Returns true if the .di exists
	 */
	public boolean diExists(){
		return diFile.exists();
	}
	
	/**
	 * Returns the .di File
	 * @return Returns the .di File 
	 */
	public IFile getDi(){
		return diFile;
	}

	/**
	 * sets the UML file of the papyrus model that will be created.
	 * @param sourceUMLPath
	 */
	public void setUpUML(String sourceUMLPath){
		if(!umlFile.exists()){
			setUpUMLFile(sourceUMLPath);
		}
	}
	/**
	 * Copies the content of the sourceUMLPath to the umlFile
	 * The referenced Profile files will be also copied
	 * @param sourceUMLPath - The path of the source .uml File
	 */
	private void setUpUMLFile(String sourceUMLPath){
			copyFile(sourceUMLPath, umlFile);
			
			Model m = getUmlModel();
			EList<Profile> profiles = m.getAllAppliedProfiles();
			for(Profile profile : profiles){
						String filestring = ((BasicEObjectImpl) profile).eProxyURI().toPlatformString(false);
						Path profileFilepath = new Path(filestring);
						
						String oldFolder = new Path(new Path(sourceUMLPath).toFile().getParent().toString()).toString();
						String oldFileName = oldFolder+Path.SEPARATOR+profileFilepath.toFile().getName();
						
						int index = umlFilePath.lastIndexOf(Path.SEPARATOR);
						String newFileName = umlFilePath.substring(0, index)+Path.SEPARATOR+profileFilepath.toFile().getName();
						IFile newFile = fileFromPath(newFileName);
						
						copyFile(oldFileName, newFile);
			}
	}
	
	/**
	 * Returns the UML model handled by the instance
	 * @return The {@link Model}
	 */
	public Model getUmlModel(){
		return Uml2Utils.loadModel(umlFilePath);
	}
	
	private void copyFile(String sourcepath, IFile newFile){
		try{
			File oldFile = new File(java.net.URI.create(sourcepath));
			FileInputStream is2 = new FileInputStream(oldFile);
			newFile.create(null, true, null);
			newFile.setContents(is2, true, false, new NullProgressMonitor());
		}catch(CoreException | FileNotFoundException e){
			throw new RuntimeException(e);
		}
	}

	
	/**
	 * Creates an empty Papyrus Model
	 */
	public void createPapyrusModel(){
		ModelSet modelSet = createModelSet();
		URI diPResURI =  URI.createPlatformResourceURI(diFilePath, true);
		RecordingCommand command = new NewPapyrusModelCommand(modelSet, diPResURI);
		modelSet.getTransactionalEditingDomain().getCommandStack().execute(command);
		initRegistry(registry);
		
		try {
			modelSet.save(new NullProgressMonitor());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Gets the ModelSet
	 */
	private ModelSet createModelSet(){
		try{
			registry = createServicesRegistry();
			return registry.getService(ModelSet.class);
		}catch(ServiceException e){
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Initializes a registry
	 * @param registry - The ServiceRegistry to be Initialized 
	 */
	private void initRegistry(ServicesRegistry registry){
		try {
			registry.startRegistry();
		} catch (ServiceException ex) {
			// Ignore this exception: some services may not have been loaded,
			// which is probably normal at this point
		}
		
		try{
			registry.getService(IPageManager.class);
		}catch (ServiceException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets an IFile from the given String path
	 * @param path - The path of the File
	 * @return - The File
	 */
	private IFile fileFromPath(String path){
		URI FileURI =  URI.createFileURI(path);
		URI resolvedFile = CommonPlugin.resolve(FileURI);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(resolvedFile.toFileString()));
		return file;
	}
	
	/**
	 * Creates a ServicesRegistry with ModelSet class key.
	 * @return The ServicesRegistry
	 * @throws ServiceException
	 */
	private ServicesRegistry createServicesRegistry() throws ServiceException {
		ServicesRegistry result = null;
		result = new ExtensionServicesRegistry(org.eclipse.papyrus.infra.core.Activator.PLUGIN_ID);
		
		try {
			// have to create the model set and populate it with the DI model
			// before initializing other services that actually need the DI
			// model, such as the SashModel Manager service
			result.startServicesByClassKeys(ModelSet.class);
		} catch (ServiceException ex) {
			// Ignore this exception: some services may not have been loaded,
			// which is probably normal at this point
		}
		return result;
	}
	
}
