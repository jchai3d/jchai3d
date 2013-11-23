/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.files.l3ds;

/**
 *
 * @author Marcos
 */
public class LObject {

    /**
     * the name of the object
     */
    protected String name;

    /**
     * the default constructor, initializes the m_name here
     */
    LObject() {
        name ="";
    }

    /**
     * call this to get the name of the object
     */
    public final String getName() {
        return this.name;
    }

    /**
     * this methods should not be used by the "user", they're used internally to
     * fill the class with valid data when reading from file. If you're about to
     * add an importer for another format you'LL have to use these methods call
     * this to set the name of the object
     */
    public void setName(final String value) {
        this.name = value;
    }

    /**
     * returns true if the object's name is the name passed as parameter
     */
    boolean isObject(final String name) {
        return this.name.equals(name);
    }
}
