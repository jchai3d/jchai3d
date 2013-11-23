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
 * JPotentialFieldForceAlgo is an abstract class for algorithms that compute
 * single point force contacts.
 *
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (java implementation)
 */
public class JPotentialFieldForceAlgo extends JGenericPointForceAlgo {

    /**
     * Interactions recorder settings.
     */
    JInteractionSettings interactionSettings;
    /**
     * Interactions recorder.
     */
    public JInteractionRecorder interactionRecorder;
    /**
     * Identification number for this force algorithm.
     */
    private int id;
    /**
     * IDN counter for all.
     */
    public static int FORCE_ALGO_ID = 0;

    /**
     * Constructor of cPotentialFieldForceAlgo.
     */
    public JPotentialFieldForceAlgo() {
        interactionRecorder = new JInteractionRecorder();

        id = FORCE_ALGO_ID;

        // increment counter
        FORCE_ALGO_ID++;

        // define default settings
        interactionSettings = new JInteractionSettings(true, true);
    }

    /**
     * Initialize the algorithm by passing the initial position of the device.
     */
    @Override
    public void initialize(JWorld aWorld, JVector3d aInitialPos) {
        parentWorld = aWorld;
    }

    /**
     * Compute the next force given the updated position of the device.
     */
    @Override
    public JVector3d computeForces(JVector3d aToolPos, JVector3d aToolVel) {

        // initialize force
        JVector3d force = new JVector3d();
        force.zero();
        interactionRecorder.clear();

        // compute force feedback for all potential field based objects located
        // in the world
        if (parentWorld != null) {
            force = parentWorld.computeInteractions(aToolPos, aToolVel, id, interactionRecorder, interactionSettings);
        }

        // return result
        return (force);
    }
}
