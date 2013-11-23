package org.jchai3d.effects;

/*
 * This file is part of the JCHAI 3D visualization and haptics libraries.
 * Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License("GPL") version 2 as published by
 * the Free Software Foundation.
 *
 * For using the JCHAI 3D libraries with software that can not be combined with
 * the GNU GPL, and for taking advantage of the additional benefits of our
 * support services, please contact CHAI 3D about acquiring a Professional
 * Edition License.
 *
 * project <https://sourceforge.net/projects/jchai3d> 
 * version 1.0.0
 */
import org.jchai3d.scenegraph.JGenericObject;
import org.jchai3d.math.JVector3d;

/**
 * 
 * @author João Cerqueira
 */
public class JGenericEffect {

    public final int CHAI_EFFECT_MAX_IDN = 16;
    /**
     * Last computed force.
     */
    protected JVector3d lastComputedForce;
    /**
     * Is the current effect enabled.
     */
    boolean enabled;
    /**
     * Object to which the force effects applies.
     */
    public JGenericObject parent;

    /**
     * Constructor of JGenericEffect.
     */
    public JGenericEffect(JGenericObject aParent) {

        /**
         * set parent object
         */
        parent = aParent;

        /**
         * no force has been computed yet
         */
        lastComputedForce = new JVector3d(0,0,0);

        /**
         * effect is enabled by default
         */
        enabled = true;
    }

    /**
     * Compute a resulting force.
     */
    public boolean computeForce(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int aToolID,
            JVector3d aReactionForce) {
        aReactionForce.zero();
        return (false);
    }

    /**
     * Initialize effect model.
     */
    protected void initialize() {};

    /**
     * Read last computed force.
     */
    public JVector3d getLastComputedForce() {
        return (lastComputedForce);
    }

    /**
     * Enable or disable current effect.
     */
    public void enable(boolean aStatus) {
        enabled = aStatus;
    }

    /**
     * Is the current effect enabled.
     */
    public boolean isEnabled() {
        return (enabled);
    }
}
