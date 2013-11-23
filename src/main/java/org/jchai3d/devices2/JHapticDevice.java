/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.devices2;

import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public abstract class JHapticDevice extends JDevice{
    
    /**
     * 
     */
    JHapticDeviceSpecifications deviceProperties;
    
    /**
     * Retrieves the properties of this device
     * @return the properties of this device
     */
    public JHapticDeviceSpecifications getHapticDeviceProperties() {
        return deviceProperties;
    }
    
    /**
     * Reads the position of the device
     * @return 
     */
    public abstract JVector3d getPosition();
        
    /**
     * Reads the rotation of the device
     */
    public abstract JMatrix3d getRotation();
    
    /**
     * Reads the gripper angle of the device
     */
    public abstract double getGripperAngle();
    
    /**
     * 
     */
    public abstract JVector3d getForce();
    
    /**
     * 
     */
    public abstract void setForce(JVector3d force);
    
    /**
     * 
     */
    public abstract JVector3d getTorque();
    
    /**
     * 
     */
    public abstract void setTorque(JVector3d torque);
    
    /**
     * 
     */
    public abstract JVector3d getGripperForce();
    
    /**
     * 
     */
    public abstract void setGripperForce(JVector3d force);
    
    /**
     * 
     */
    public abstract JVector3d getGripperTorque();
    
    /**
     * 
     */
    public abstract void setGripperTorque(JVector3d torque);
    
    /**
     * 
     */
    public abstract boolean isSwitchPressed(int index);
}
