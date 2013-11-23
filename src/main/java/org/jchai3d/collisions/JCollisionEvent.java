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
package org.jchai3d.collisions;

import org.jchai3d.extras.JGlobals;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JGenericObject;

/**
 *
 * @author João Cerqueira
 * @author Marcos da Silva Ramos version 1.0.0
 */
public class JCollisionEvent {

    /**
     * If the position of segment A is modified to take into account motion
     */
    /**
     * (see m_adjustObjectMotion in cCollisionSettings), the value is stored
     * hare.
     */
    protected JGenericObject object;
    protected JTriangle triangle;
    protected JVector3d localPosition;
    protected JVector3d globalPosition;
    protected JVector3d localNormal;
    protected JVector3d globalNormal;
    protected double squareDistance;
    protected JVector3d adjustedSegmentAPoint;

    public JCollisionEvent() {
        localPosition = new JVector3d();
        globalPosition = new JVector3d();
        localNormal = new JVector3d();
        globalNormal = new JVector3d();
        adjustedSegmentAPoint = new JVector3d();
        clear();
    }

    /**
     * initialize all data
     */
    public final void clear() {
        object = null;
        triangle = null;
        localPosition.zero();
        globalPosition.zero();
        localNormal.zero();
        globalNormal.zero();
        squareDistance = JGlobals.CHAI_DBL_MAX;
    }

    /**
     * @return the object
     */
    public JGenericObject getObject() {
        return object;
    }

    /**
     * @param object the object to set
     */
    public void setObject(JGenericObject object) {
        this.object = object;
    }

    /**
     * @return the triangle
     */
    public JTriangle getTriangle() {
        return triangle;
    }

    /**
     * @param triangle the triangle to set
     */
    public void setTriangle(JTriangle triangle) {
        this.triangle = triangle;
    }

    /**
     * @return the localPos
     */
    public JVector3d getLocalPosition() {
        return localPosition;
    }

    /**
     * @param localPos the localPos to set
     */
    public void setLocalPosition(JVector3d localPos) {
        this.localPosition.copyFrom(localPos);
    }

    /**
     * @return the globalPos
     */
    public JVector3d getGlobalPosition() {
        return globalPosition;
    }

    /**
     * @param globalPos the globalPos to set
     */
    public void setGlobalPosition(JVector3d globalPos) {
        this.globalPosition.copyFrom(globalPos);
    }

    /**
     * @return the localNormal
     */
    public JVector3d getLocalNormal() {
        return localNormal;
    }

    /**
     * @param localNormal the localNormal to set
     */
    public void setLocalNormal(JVector3d localNormal) {
        this.localNormal.copyFrom(localNormal);
    }

    /**
     * @return the globalNormal
     */
    public JVector3d getGlobalNormal() {
        return globalNormal;
    }

    /**
     * @param globalNormal the globalNormal to set
     */
    public void setGlobalNormal(JVector3d globalNormal) {
        this.globalNormal.copyFrom(globalNormal);
    }

    /**
     * @return the squareDistance
     */
    public double getSquareDistance() {
        return squareDistance;
    }

    /**
     * @param squareDistance the squareDistance to set
     */
    public void setSquareDistance(double squareDistance) {
        this.squareDistance = squareDistance;
    }

    /**
     * @return the adjustedSegmentAPoint
     */
    public JVector3d getAdjustedSegmentAPoint() {
        return adjustedSegmentAPoint;
    }

    /**
     * @param adjustedSegmentAPoint the adjustedSegmentAPoint to set
     */
    public void setAdjustedSegmentAPoint(JVector3d adjustedSegmentAPoint) {
        this.adjustedSegmentAPoint.copyFrom(adjustedSegmentAPoint);
    }

    public void copyFrom(JCollisionEvent event) {
        this.object = event.object;
        this.triangle = event.triangle;
        this.localPosition.copyFrom(event.localPosition);
        this.globalPosition.copyFrom(event.globalPosition);
        this.localNormal.copyFrom(event.localNormal);
        this.globalNormal.copyFrom(event.globalNormal);
        this.squareDistance = event.squareDistance;
        this.adjustedSegmentAPoint.copyFrom(event.adjustedSegmentAPoint);
    }
}
