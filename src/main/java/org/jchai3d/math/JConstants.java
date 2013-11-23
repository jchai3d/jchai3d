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

package org.jchai3d.math;

/**
 *
 * @author jairo
 */
public interface JConstants {

    //---------------------------------------------------------------------------
    // MEMBERS: SCALARS
    //---------------------------------------------------------------------------
    /**
     * PI constant.
     */
    double CHAI_PI = 3.14159265358979323846;
    /**
     * PI constant divided by two.
     */
    double CHAI_PI_DIV_2 = 1.57079632679489661923;
    /**
     * Conversion from degrees to radians
     */
    double CHAI_DEG2RAD = 0.01745329252;
    
    /**
     * Conversion from radians to degrees
     */
    double CHAI_RAD2DEG = 57.2957795131;
    /**
     * Smallest value near zero for a double
     */
    double CHAI_TINY = 1e-49;
    /**
     * Small value near zero
     */
    double CHAI_SMALL = 0.000000001;
    /**
     * Biggest value for a double
     */
    double CHAI_LARGE = 1e+49;
    /**
     *Biggest value for a float
     */
    double CHAI_LARGE_FLOAT = 1e37;

    //! Biggest value for a double
    double DBL_MAX = 1e+49;
    //---------------------------------------------------------------------------
    // MEMBERS:VECTORS
    //---------------------------------------------------------------------------
    /**
     * Zero vector (0,0,0)
     */
    JVector3d CHAI_VECTOR_ZERO = new JVector3d(0,0,0);
    /**
     * Unit vector along Axis X (1,0,0)
     */
    JVector3d CHAI_VECTOR_X = new JVector3d(1,0,0);
    /**
     * Unit vector along Axis Y (0,1,0)
     */
    JVector3d CHAI_VECTOR_Y = new JVector3d(0,1,0);
    /**
     * Unit vector along Axis Z (0,0,1)
     */
    JVector3d CHAI_VECTOR_Z = new JVector3d(0,0,1);
    /**
     * Origin (0,0,0)
     */
    JVector3d CHAI_ORIGIN = new JVector3d(0,0,0);
}
