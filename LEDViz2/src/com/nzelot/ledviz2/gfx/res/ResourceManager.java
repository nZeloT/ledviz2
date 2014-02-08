package com.nzelot.ledviz2.gfx.res;

import java.io.File;
import java.util.HashMap;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The ResourceLoader automatically loads all files from all sub-packages.<br>
 * The Resources are accessible through a HashMap.<br>
 * The Resource Loader uses the Singleton programming style.<br>
 * @author Leon
 *
 */
public class ResourceManager {
    
    private final Logger l = LoggerFactory.getLogger(ResourceManager.class);

    /**
     * The instance of the Singleton
     */
    private static ResourceManager inst;


    /**
     * A HashMap to Store all resources by their fileName
     */
    private HashMap<String, Resource>  resources;

    /**
     * A Hashmap to define which Resource to load with which ResourceLoader
     */
    private HashMap<String, ResourceLoader> loader;


    /**
     * Method to get a loaded Resource
     * @param name the Name of the Resource
     * @return the requested Resource
     */
    public static Resource getResource(String name){
	init();

	if(inst.resources.containsKey(name))
	    return inst.resources.get(name);
	else{
	    System.err.println("Could not find Resource: " + name + "!");
	    return null;
	}
    }


    /**
     * Add a resource Loader
     * @param extension
     * @param l
     */
    public static void addLoader(String extension, ResourceLoader l){
	init();

	inst.loader.put(extension, l);
    }


    /**
     * Remove a resource loader
     * @param extension
     * @return
     */
    public static ResourceLoader removeLoader(String extension){
	init();

	return inst.loader.remove(extension);
    }


    /**
     * Load all resources after specifying the resource loaders
     */
    public static void loadResources(){
	init();

	inst.initResources();
    }


    /**
     * A private, constructor, to prevent instancing
     */
    private ResourceManager(){
	resources = new HashMap<String, Resource>();
	loader = new HashMap<String, ResourceLoader>();
    }


    /**
     * A method to init the instance field of the class
     */
    private static void init(){
	if(inst == null)
	    inst = new ResourceManager();
    }


    /**
     * A Method to read all Resources into the HashMap
     */
    private void initResources(){
	HashMap<String, String> files = getResourceFileNames("./res", "");

	for(Entry<String, String> e : files.entrySet()){

	    int lastIdx = e.getKey().lastIndexOf(".")+1;

	    if(lastIdx > 0){
		String extension = e.getKey().substring(lastIdx).toLowerCase();

		if(loader.containsKey(extension)){

		    String key = e.getValue().substring(2, e.getValue().lastIndexOf("."));

		    if(!resources.containsKey(key)){

			Object data = loader.get(extension).load(e.getKey());

			if(data != null){
			    resources.put(key, new Resource(data));
			    l.debug("Loaded: " + resources.get(key).getType().getCanonicalName() + " " + key);
			}

		    }else{
			l.error("Could not load Resource!\n"
				+ "Key: " + key + " already exists!");
		    }
		}
	    }
	}
    }


    /**
     * Build a List of all Resource Files
     * @param parentDir The directory to scan
     * @return the List of recource files
     */
    private HashMap<String, String> getResourceFileNames(String parentDir, String prefix){

	File f = null;

	f = new File(prefix + parentDir);

	HashMap<String, String> list = new HashMap<String, String>();

	File[] files = f.listFiles();
	for(int i = 0; i < files.length; i++){
	    if(files[i].isDirectory()){
		list.putAll(getResourceFileNames(files[i].getName(), prefix + parentDir + "/"));
	    }else{
		list.put(files[i].getAbsolutePath(), prefix + parentDir + "/" + files[i].getName());
	    }
	}

	return list;
    }
}
