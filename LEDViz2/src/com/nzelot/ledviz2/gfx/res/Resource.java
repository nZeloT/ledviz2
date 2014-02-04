package com.nzelot.ledviz2.gfx.res;


/**
 * Clas holding one resource, and its specific type
 * @author Leon
 *
 */
public class Resource {

    /**
     * The Data Type
     */
    private Class<?> type;
    
    /**
     * The Resource Data
     */
    private Object data;
    
    /**
     * Constructor used to create a new instance
     * @param d The Data
     */
    public Resource(Object d) {
	type = d.getClass();
	data = d;
    }
    
    
    /**
     * Getter
     * @return Return the current Data
     */
    @SuppressWarnings("unchecked")
    public <T> T getData(){
	return (T) data;
    }
    
    
    /**
     * Getter
     * @return Return the current type
     */
    public Class<?> getType() {
	return type;
    }
}
