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
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JGenericObject;
import org.jchai3d.timers.JPrecisionClock;

public class JEffectVibration extends JGenericEffect {

    /**
     * Vibration signal clock.
     */
    protected JPrecisionClock clock;

    /**
     * Constructor of cEffectVibration.
     */
    public JEffectVibration(JGenericObject aParent) {
        super(aParent);
        clock = new JPrecisionClock();
        clock.start(true);
    }

    /**
     * Compute resulting force.
     */
    @Override
    public boolean computeForce(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int aToolID,
            JVector3d aReactionForce) {

        if (parent.isInteractionInside()) {
            /**
             * read vibration parameters
             */
            double vibrationFrequency = parent.getMaterial().getVibrationFrequency();
            double vibrationAmplitude = parent.getMaterial().getVibrationAmplitude();

            /**
             * read time
             */
            double time = clock.getCurrentTimeSeconds();

            /**
             * compute force magnitude
             */
            double forceMag = vibrationAmplitude * Math.sin(2.0 * JConstants.CHAI_PI * vibrationFrequency * time);

            aReactionForce.copyFrom(JMaths.jMul(forceMag, new JVector3d(1, 0, 0)));
            return (true);
        } else {

            /**
             * the tool is located outside the object, so zero reaction force
             */
            aReactionForce.zero();
            return (false);
        }
    }
}
