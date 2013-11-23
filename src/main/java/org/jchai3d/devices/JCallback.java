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


package org.jchai3d.devices;

/**
 * JCallback is an abstract class that allows subclasses
 *   to define a single callback function, for example to be called by
 *   a device when it's time to compute haptic forces.  This feature is _not_
 *   supported by all devices; see setCallback()
 * @author jairo
 */
public abstract class JCallback {

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    public JCallback(){}

    //-----------------------------------------------------------------------
    // CALL BACK FUNCTION:
    //-----------------------------------------------------------------------
    
    abstract void callback();
}
