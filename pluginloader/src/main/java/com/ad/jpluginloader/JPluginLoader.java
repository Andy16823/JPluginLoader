package com.ad.jpluginloader;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Vector;
import java.util.jar.JarFile;

/**
 * Hello world!
 */
public class JPluginLoader {
	private String pluginDir;
    
	public JPluginLoader(String pluginDir) {
		this.pluginDir = pluginDir;
	}
	
	/**
	 * Loads plugins from the specified plugin directory.
	 * @return A list of loaded AppModule instances.
	 * @throws IOException If there is an error reading the plugin files.
	 */
	public <T> Vector<T> loadPlugins(Class<T> refClass) throws IOException {
		// Create new list for modules and define the plugin directory
		Vector<T> modules = new Vector<>();
		File dir = new File(pluginDir);
		
		// Check if the plugin directory exists
		if (!dir.exists() || !dir.isDirectory()) {
			throw new IllegalArgumentException("Invalid plugin directory: " + pluginDir);
		}
		
		// List all jar fils in the plugin directory
		File[] files = dir.listFiles((d, name) -> name.endsWith(".jar"));
		if (files == null || files.length == 0) {
			return modules; // Return empty list if no jars found
		}
		
		// Iterate over each jar file
		for (File jarFile : files) {			
			// Load the jar file using the URLClassLoader
			URL jarUrl = jarFile.toURI().toURL();
			URLClassLoader classLoader = new URLClassLoader(new URL[] { jarUrl }, this.getClass().getClassLoader());
		
			// Create an JarFile object to read the contents
			JarFile jar = new JarFile(jarFile);
			
			// Find the class that implements AppModule
			Vector<?> moduleClasses = findAppModuleClasses(jar, refClass, classLoader);
			// Check if we found a suitable class
			for (Object moduleClass : moduleClasses) {
				try {
					// Instantiate the module and add to the list
					@SuppressWarnings("unchecked")
					T moduleInstance = (T) ((Class<?>)moduleClass).getDeclaredConstructor().newInstance();
					modules.add(moduleInstance);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			classLoader.close();
		}
		return modules;
	}
	
	/**
	 * Finds all classes in the jar that implement the specified reference interface/class.
	 * @param jar The JarFile to search.
	 * @param refClass The reference interface/class to look for.
	 * @param loader The ClassLoader to use for loading classes.
	 * @return A vector of classes that implement the reference interface/class.
	 */
	private <T> Vector<Class<? extends T>> findAppModuleClasses(JarFile jar, Class<T> refClass, URLClassLoader loader) {
		// Define an array to hold the found class
	    Vector<Class<? extends T>> foundClasses = new Vector<>();
	    
	    // Open the jar file and iterate over the entries an jar is like an zip file
	    jar.stream()
	    	// We only care about class files
	       .filter(e -> e.getName().endsWith(".class"))
	       // Iterate all class files
	       .forEach(e -> {
	    	   // Convert the file path to an blank class name
	           String className = e.getName().replace("/", ".").replace(".class", "");
	           try {
	        	   // Create the class object
	               Class<?> cls = Class.forName(className, true, loader);
	               // Check if the class implements the AppMopdule interface and its not an abstract class or interface
	               if (refClass.isAssignableFrom(cls) && !cls.isInterface() &&
	                   !java.lang.reflect.Modifier.isAbstract(cls.getModifiers())) {
	            	   // If found we have our class to load
	            	   foundClasses.add(cls.asSubclass(refClass));
	                   // return from the lambda loop
	                   return;
	               }
	           } catch (ClassNotFoundException ex) {
	               ex.printStackTrace();
	           }
	       });

	    // Return the found class or null if none found
	    return foundClasses;
	}
	
}
