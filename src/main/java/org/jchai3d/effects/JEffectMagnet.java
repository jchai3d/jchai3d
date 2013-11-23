
package org.jchai3d.effects;

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
 *   author    João Cerqueira
 *   version   1.0.0
 */

import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JGenericObject;

public class JEffectMagnet extends JGenericEffect {

    /**
     * Constructor of JEffectMagnet.
     */
    public JEffectMagnet(JGenericObject aParent) {
        super(aParent);
    }

    /**
     * Compute resulting force.
     */
    @Override
    public boolean computeForce(
            final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int aToolID,
            JVector3d aReactionForce) {

        /**
         * compute distance from object to tool
         */
        double distance = JMaths.jDistance(aToolPos, parent.getInteractionProjectedPoint());

        /**
         * get parameters of magnet
         */
        double magnetMaxForce = parent.getMaterial().getMagnetMaxForce();
        double magnetMaxDistance = parent.getMaterial().getMagnetMaxDistance();
        double stiffness = parent.getMaterial().getStiffness();
        double forceMagnitude = 0;

        if ((distance > 0) && (distance < magnetMaxDistance) && (stiffness > 0)) {
            double limitLinearModel = (magnetMaxForce / stiffness);
            JMaths.jClamp(limitLinearModel, 0.0f, 0.5f * distance);

            if (distance < limitLinearModel) {
                /**
                 * apply local linear model near magnet
                 */
                forceMagnitude = stiffness * distance;
            } else {
                /**
                 * compute quadratic model
                 */
                JMatrix3d sys = new JMatrix3d();
                sys.m[0][0] = limitLinearModel * limitLinearModel;
                sys.m[0][1] = limitLinearModel;
                sys.m[0][2] = 1.0;
                sys.m[1][0] = magnetMaxDistance * magnetMaxDistance;
                sys.m[1][1] = magnetMaxDistance;
                sys.m[1][2] = 1.0;
                sys.m[2][0] = 2.0 * limitLinearModel;
                sys.m[2][1] = 1.0;
                sys.m[2][2] = 0.0;
                sys.invert();

                JVector3d param = new JVector3d();
                sys.mulr(new JVector3d(magnetMaxForce, 0.0, -1.0), param);

                /**
                 * apply quadratic model
                 */
                double val = distance - limitLinearModel;
                forceMagnitude = param.x * val * val + param.y * val + param.z;
            }

            /**
             * compute magnetic force
             */
            aReactionForce.copyFrom(JMaths.jMul(forceMagnitude, JMaths.jNormalize(JMaths.jSub(parent.getInteractionProjectedPoint(), aToolPos))));

            // add damping component
            double viscosity = parent.getMaterial().getViscosity();
            JVector3d viscousForce = JMaths.jMul(-viscosity, aToolVel);
            aReactionForce.add(viscousForce);

            return (true);

        } else {
            /**
             * the tool is located outside the magnet zone
             */
            aReactionForce.zero();
            return (false);
        }

    }
}
