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
 * project <https://sourceforge.net/projects/jchai3d> author João Cerqueira
 * version 1.0.0
 */
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JGenericObject;

public class JEffectViscosity extends JGenericEffect {

    /**
     * Constructor of JEffectViscosity.
     */
    public JEffectViscosity(JGenericObject aParent) {
        super(aParent);

    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Compute resulting force
     */
    @Override
    public boolean computeForce(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int aToolID,
            JVector3d aReactionForce) {

        if (parent.isInteractionInside()) {

            /**
             * the tool is located inside the object.
             */
            double viscosity = parent.getMaterial().getViscosity();
            aReactionForce.copyFrom(JMaths.jMul(-viscosity, aToolVel));
            return (true);
        } else {

            /**
             * the tool is located outside the object, so zero reaction force.
             */
            aReactionForce.zero();
            return (false);
        }
    }
}
