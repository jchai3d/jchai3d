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
import org.jchai3d.scenegraph.JGenericObject;

/**
 * JInteractionEvent stores all information related to the intersection between
 * a point and an object.
 *
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (java implementation)
 */
public class JInteractionEvent {

    /**
     * Pointer to the interaction object.
     */
    protected JGenericObject object;
    /**
     * Is the pointer located inside the object.
     */
    protected boolean inside;
    /**
     * Position of the interaction point in reference to the objects coordinate
     * frame (local coordinates).
     */
    protected JVector3d localPosition;
    /**
     * Resulting force in local coordinates.
     */
    protected JVector3d localForce;
    /**
     * Nearest point to the object surface in local coordinates
     */
    protected JVector3d localSurfacePosition;

    /**
     * Default constructor of JInteractionEvent
     */
    public JInteractionEvent() {
        clear();
    }

    /**
     * Initialize all data contained in current event.
     */
    void clear() {
        object = null;
        inside = false;
        localPosition = new JVector3d();
        localForce = new JVector3d();
        localSurfacePosition = new JVector3d();
    }

    /**
     * @return the mObject
     */
    public JGenericObject getObject() {
        return object;
    }

    /**
     * @param mObject the mObject to set
     */
    public void setObject(JGenericObject mObject) {
        this.object = mObject;
    }

    /**
     * @return the mIsInside
     */
    public boolean isInside() {
        return inside;
    }

    /**
     * @param mIsInside the mIsInside to set
     */
    public void setInside(boolean mIsInside) {
        this.inside = mIsInside;
    }

    /**
     * @return the mLocalPos
     */
    public JVector3d getLocalPosition() {
        return localPosition;
    }

    /**
     * @param mLocalPos the mLocalPos to set
     */
    public void setLocalPosition(JVector3d mLocalPos) {
        this.localPosition.copyFrom(mLocalPos);
    }

    /**
     * @return the mLocalForce
     */
    public JVector3d getLocalForce() {
        return localForce;
    }

    /**
     * @param mLocalForce the mLocalForce to set
     */
    public void setLocalForce(JVector3d mLocalForce) {
        this.localForce.copyFrom(mLocalForce);
    }

    /**
     * @return the mLocalSurfacePos
     */
    public JVector3d getLocalSurfacePosition() {
        return localSurfacePosition;
    }

    /**
     * @param mLocalSurfacePos the mLocalSurfacePos to set
     */
    public void setLocalSurfacePosition(JVector3d mLocalSurfacePos) {
        this.localSurfacePosition.copyFrom(mLocalSurfacePos);
    }
}
