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

package org.jchai3d.extras;

/**
 *
 * @author jairo
 */
public abstract class JGlobals {

    //! maximum length of a path
    public static final int CHAI_SIZE_PATH = 255;
    //! maximum length of a object name
    public static final int CHAI_SIZE_NAME = 64;
    //! a large double
    public static final int CHAI_DBL_MAX = 9999999;

    public static  boolean JCHAI_DEBUG = false;

    // debug process
    public static final void CHAI_DEBUG_PRINT(String format, Object... args) {
        System.out.printf(format, args);
    }
}
