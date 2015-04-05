package hu.elte.txtuml.export.papyrus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.common.CommonPlugin;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.papyrus.infra.core.resource.ModelMultiException;
import org.eclipse.papyrus.infra.core.resource.ModelSet;
import org.eclipse.papyrus.infra.core.sasheditor.contentprovider.IPageManager;
import org.eclipse.papyrus.infra.core.services.ExtensionServicesRegistry;
import org.eclipse.papyrus.infra.core.services.ServiceException;
import org.eclipse.papyrus.infra.core.services.ServicesRegistry;
import org.eclipse.papyrus.uml.diagram.wizards.category.NewPapyrusModelCommand;

public class PapyrusModelCreator {
	private IFile diFile;
	private IFile umlFile;
	private String diFilePath;
	private String umlFilePath;
	private ServicesRegistry registry;

	
	
	public boolean diExists(){
		return diFile.exists();
	}
	
	public IFile getDi(){
		return diFile;
	}


	public void init(String modelpath, String sourceUMLPath) throws FileNotFoundException, CoreException {
		diFilePath = modelpath+".di";
		umlFilePath = modelpath+".uml";
		
		diFile = fileFromPath(diFilePath);
		umlFile = fileFromPath(umlFilePath);
		if(!umlFile.exists()){
			copyUmlToProject(sourceUMLPath);
		}
	}
	
	
	private void copyUmlToProject(String SourceUMLPath) throws FileNotFoundException, CoreException {
			InputStream  is = new FileInputStream(new File(java.net.URI.create(SourceUMLPath)));
			umlFile.create(null, true, null);
			umlFile.setContents(is, true, false, new NullProgressMonitor());
	}
	
	public void createPapyrusModel() throws ServiceException, IOException{
		ModelSet modelSet = getModelSet();
		URI diPResURI =  URI.createPlatformResourceURI(diFilePath, true);
		RecordingCommand command = new NewPapyrusModelCommand(modelSet, diPResURI);
		modelSet.getTransactionalEditingDomain().getCommandStack().execute(command);
		initRegistry(registry);
		modelSet.save(new NullProgressMonitor());
	}
	
	public void loadPapyrusModel() throws ServiceException, ModelMultiException{
		ModelSet modelSet = getModelSet();
		URI diPResURI = URI.createPlatformResourceURI(umlFilePath, true);
		modelSet.loadModels(diPResURI);
	}
	
	private ModelSet getModelSet() throws ServiceException{
		registry = createServicesRegistry();
		ModelSet modelSet = registry.getService(ModelSet.class);
		return modelSet;
	}
	
	private void initRegistry(ServicesRegistry registry) throws ServiceException{
		try {
			registry.startRegistry();
		} catch (ServiceException ex) {
			// Ignore this exception: some services may not have been loaded,
			// which is probably normal at this point
		}
		registry.getService(IPageManager.class);
	}

	private IFile fileFromPath(String path){
		URI FileURI =  URI.createFileURI(path);
		URI resolvedFile = CommonPlugin.resolve(FileURI);
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(new Path(resolvedFile.toFileString()));
		return file;
	}
	
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
