package org.jchai3d.devices2;

/**
 *
 * @author Marcos da Silva Ramos
 */
public abstract class JDevice {
    
    /**
     * Open communication with the device
     */
    public abstract void open();
    
    /**
     * Closes communication with the device
     */
    public abstract void close();
    
    /**
     * Is this device ready for communication?
     */
    public abstract boolean isReady();
}
