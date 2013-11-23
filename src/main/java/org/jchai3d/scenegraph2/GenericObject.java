package org.jchai3d.scenegraph2;

/**
 * 
 * Abstract class for all scenegraph-related subclasses.
 * 
 * @author Marcos da Silva Ramos
 */
public abstract class GenericObject extends Object{
    
    /**
     * The name of this object
     */
    protected String name;
    
    /**
     * Users may use this field to associate external objects with this one.
     */
    protected Object userData;

    /**
     * Returns the name of this object.
     * @return the name of this object.
     */
    public String getName() {
        return name;
    }

    /**
     * Updates the name of this object.
     * 
     * @param name the new name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the arbitrary user data stored in this object.
     * @return the user data associated with this object.
     */
    public Object getUserData() {
        return userData;
    }

    /**
     * Updates the user data associated with this object.
     * 
     * @param userData the new user data to set
     */
    public void setUserData(Object userData) {
        this.userData = userData;
    }
}
