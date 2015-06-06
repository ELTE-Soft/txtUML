package hu.elte.txtuml.export.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.launching.JavaRuntime;

public class ClassLoaderProvider {

	/**
	 * Returns a ClassLoader for the Project attached to the parent.
	 * @param projectName
	 * @param parent
	 * @return
	 * @author András Dobreff
	 */
	public static URLClassLoader getClassLoaderForProject(String projectName, ClassLoader parent){
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
		return getClassLoaderForProject(project, parent);
	}
	
	/**
	 * Returns a ClassLoader for the Project attached to the parent.
	 * @param project
	 * @param parent
	 * @return
	 * @author András Dobreff
	 */
	public static URLClassLoader getClassLoaderForProject(IProject project, ClassLoader parent){
		String[] classPathEntries;
		
		try{
			IJavaProject javaproject = JavaCore.create(project);
			classPathEntries = JavaRuntime.computeDefaultRuntimeClassPath(javaproject);
		}catch(CoreException e){
			classPathEntries = new String[]{};
		}
		
		URL[] urls = getClassPathEntyUrls(classPathEntries);
		URLClassLoader classLoader  = new  URLClassLoader(urls, parent);
		return classLoader;
	}
	
	/**
	 * Converts ClassPathEntries StringArray to URL Array
	 * @param classPathEntries
	 * @return
	 * @author András Dobreff
	 */
	private static URL[] getClassPathEntyUrls(String[] classPathEntries){
		try{
			List<URL> urlList = new ArrayList<URL>();
			for (String entry: classPathEntries) {
			 IPath path = new Path(entry);
			 URL url = path.toFile().toURI().toURL();
			 urlList.add(url);
			}
			
			return urlList.toArray(new URL[urlList.size()]);
		}catch(MalformedURLException e){
			e.printStackTrace();
			return new URL[]{};
		}
	}
}
