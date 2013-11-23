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
 */
package org.jchai3d.forces;

import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JWorld;

/**
 *
 * Force Model Base Class.
 *
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (java implementation)
 */
public abstract class JGenericPointForceAlgo {

    /**
     *
     */
    protected JWorld parentWorld;

    /**
     * Constructor of cGenericPointForceAlgo.
     */
    public JGenericPointForceAlgo() {
        parentWorld = null;
    }

    /**
     * Get a pointer to the world in which the force algorithm is operating.
     */
    public JWorld getParentWorld() {
        return (parentWorld);
    }

    /**
     * Get a pointer to the world in which the force algorithm is operating.
     */
    public void setParentWorld(JWorld w) {
        this.parentWorld = w;
    }

    /*
     * Initialize the algorithm by passing the initial position of the device.
     */
    public abstract void initialize(JWorld aWorld, JVector3d aInitialPos);

    /*
     * Compute the next force given the updated position of the device.
     */
    public abstract JVector3d computeForces(JVector3d aToolPos, JVector3d aToolVel);
}
