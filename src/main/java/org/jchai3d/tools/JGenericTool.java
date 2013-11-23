/*
 *   This file is part of the JCHAI 3D visualization and haptics libraries.
 *   Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 *   This library is free software; you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License("GPL") version 2
 *   as published by the Free Software Foundation.
 *
 *   For using the JCHAI 3D libraries with software that can not be combined
 *   with the GNU GPL, and for taking advantage of the additional benefits
 *   of our support services, please contact CHAI 3D about acquiring a
 *   Professional Edition License.
 *
 *   project   <https://sourceforge.net/projects/jchai3d>
 *   version   1.0.0
 */

package org.jchai3d.tools;

import org.jchai3d.devices.JGenericHapticDevice;
import org.jchai3d.scenegraph.JGenericObject;

/**
 * describes a generic class to create virtual tools inside a
 * virtual environment (cWorld) and connecting them to haptic devices
 * @author jairo
 */
public class JGenericTool extends JGenericObject {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    //! Handle to the haptic device driver.
    protected JGenericHapticDevice hapticDevice;
    //! Status of the user switches of the device attached to this tool.
    private int mUserSwitches;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    /**
     * Constructor of JGenericTool.
     */
    public JGenericTool() {
        // no device is currently connected to this tool
        hapticDevice = null;

        // initialize variable which stores the status of the user switches of a
        // the device
        mUserSwitches = 0;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Connect this tool to a haptic device.
     * @param aDevice
     */
    public void setHapticDevice(JGenericHapticDevice aDevice) {
        if (aDevice != null) {
            hapticDevice = aDevice;
        }
    }

    /**
     * Get the handle of haptic device to wich this tool is connected to.
     * @return
     */
    public JGenericHapticDevice getHapticDevice() {
        return (hapticDevice);
    }
    

    /**
     * Update Position, orientation, velocity and other degree of freedoms of tool.
     */
    public void updatePose() {
    }

    

    /**
     * Compute interaction forces with environment.
     */
    public void computeInteractionForces() {
    }

    

    /**
     * Apply latest forces to device.
     */
    public void applyForces() {
    }

    

    /**
     * Start communication with the device connected to the tool (0 indicates success).
     * @return
     */
    public int start() {
        return (-1);
    }

    /**
     * Stop communication with the device connected to the tool (0 indicates success).
     * @return
     */
    public int stop() {
        return (-1);
    }

    /*
     * Initialize encoders on device connected to the tool (0 indicates success).
     */
    public int initialize(final boolean a_resetEncoders) {
        return (-1);
    }

    /**
     * Toggle forces \b ON.
     * @return
     */
    public int setForcesON() {
        return (-1);
    }

    /**
     * Toggle forces \b OFF.
     * @return
     */
    public int setForcesOFF() {
        return (-1);
    }

    /**
     * Read the status of one of the switches on this device.
     * @param aSwitchIndex
     * @return
     */
    public boolean getUserSwitch(int aSwitchIndex) {
        // read selected user switch
        boolean userSwitch = false;

        // check switch
        if (hapticDevice != null) {
            hapticDevice.getUserSwitch(aSwitchIndex, userSwitch);
        }

        // return result
        return (userSwitch);
    }

    /**
     * Check if the tool is touching a particular object.
     * @param a_object
     * @return
     */
    public boolean isInContact(JGenericObject a_object) {
        return (false);
    }
}
