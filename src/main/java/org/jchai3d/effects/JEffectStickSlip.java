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
import org.jchai3d.math.JVector3d;
import org.jchai3d.math.JMaths;
import org.jchai3d.scenegraph.JGenericObject;

public class JEffectStickSlip extends JGenericEffect {

    /**
     * store the algorithm history for each IDN calling this effect.
     */
    protected JStickSlipStatus[] history;

    /**
     * Constructor of JEffectStickSlip.
     */
    public JEffectStickSlip(JGenericObject aParent) {
        super(aParent);
        history = new JStickSlipStatus[CHAI_EFFECT_MAX_IDN];

        for (int i = 0; i < CHAI_EFFECT_MAX_IDN; i++) {
            history[i] = new JStickSlipStatus();
        }
    }

    /**
     * Compute resulting force.
     */
    @Override
    public boolean computeForce(final JVector3d aToolPos,
            final JVector3d aToolVel,
            final int aToolID,
            JVector3d aReactionForce) {

        /**
         * check if history for this IDN exists
         */
        if (aToolID < CHAI_EFFECT_MAX_IDN) {
            if (parent.isInteractionInside()) {
                /**
                 * check if a recent valid point has been stored previously
                 */
                if (!history[aToolID].valid) {
                    history[aToolID].setCurrentStickPosition(aToolPos);
                    history[aToolID].valid = true;
                }

                /**
                 * read parameters for stick and slip model
                 */
                double stiffness = parent.getMaterial().getStickSlipStiffness();
                double forceMax = parent.getMaterial().getStickSlipForceMax();

                /**
                 * compute current force between last stick position and current
                 * tool position
                 */
                double distance = JMaths.jDistance(aToolPos, history[aToolID].currentStickPosition);
                double forceMag = distance * stiffness;

                if (forceMag > 0) {
                    /**
                     * if force above threshold, slip...
                     */
                    if (forceMag > forceMax) {
                        history[aToolID].currentStickPosition = aToolPos;
                        aReactionForce.zero();
                    } /**
                     * ...otherwise stick
                     */
                    else {
                        aReactionForce.copyFrom(JMaths.jSub(history[aToolID].currentStickPosition, aToolPos));
                        aReactionForce.operatorMul((forceMag / distance));
                    }
                } else {
                    aReactionForce.zero();
                }

                return (true);
            } else {
                /**
                 * the tool is located outside the object, so zero reaction
                 * force
                 */
                history[aToolID].valid = false;
                aReactionForce.zero();
                return (false);
            }
        } else {
            aReactionForce.zero();
            return (false);
        }
    }
}

/**
 * Provides some history information to class cEffectStickSlip for a given
 * haptic tool.
 */
class JStickSlipStatus {

    /**
     * current stick position
     */
    JVector3d currentStickPosition;
    /**
     * is the stick position valid?
     */
    boolean valid;

    public JStickSlipStatus() {
        currentStickPosition = new JVector3d();
    }
    
    public JVector3d getCurrentStickPosition() {
        return currentStickPosition;
    }

    public void setCurrentStickPosition(JVector3d mCurrentStickPosition) {
        this.currentStickPosition.copyFrom(mCurrentStickPosition);
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean mValid) {
        this.valid = mValid;
    }
}
