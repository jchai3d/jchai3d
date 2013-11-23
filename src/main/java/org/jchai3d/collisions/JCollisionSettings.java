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

/**
 *  JCollisionSettings class
 *   @author João Cerqueira
 *   @author Marcos da Silva Ramos
 *   version   1.0.0
 */
public class JCollisionSettings {

    /**
     * Radius of the virtual tool or cursor.
     */
    private boolean checkForNearestCollisionOnly;
    private boolean returnMinimalCollisionData;
    private boolean checkVisibleObjectsOnly;
    private boolean checkHapticObjectsOnly;
    private boolean checkBothSidesOfTriangles;
    private boolean adjustObjectMotion;
    private double collisionRadius;

    /**
     * @return the checkForNearestCollisionOnly
     */
    public boolean isCheckForNearestCollisionOnly() {
        return checkForNearestCollisionOnly;
    }

    /**
     * @param checkForNearestCollisionOnly the checkForNearestCollisionOnly to set
     */
    public void setCheckForNearestCollisionOnly(boolean checkForNearestCollisionOnly) {
        this.checkForNearestCollisionOnly = checkForNearestCollisionOnly;
    }

    /**
     * @return the returnMinimalCollisionData
     */
    public boolean isReturnMinimalCollisionData() {
        return returnMinimalCollisionData;
    }

    /**
     * @param returnMinimalCollisionData the returnMinimalCollisionData to set
     */
    public void setReturnMinimalCollisionData(boolean returnMinimalCollisionData) {
        this.returnMinimalCollisionData = returnMinimalCollisionData;
    }

    /**
     * @return the checkVisibleObjectsOnly
     */
    public boolean isCheckVisibleObjectsOnly() {
        return checkVisibleObjectsOnly;
    }

    /**
     * @param checkVisibleObjectsOnly the checkVisibleObjectsOnly to set
     */
    public void setCheckVisibleObjectsOnly(boolean checkVisibleObjectsOnly) {
        this.checkVisibleObjectsOnly = checkVisibleObjectsOnly;
    }

    /**
     * @return the checkHapticObjectsOnly
     */
    public boolean isCheckHapticObjectsOnly() {
        return checkHapticObjectsOnly;
    }

    /**
     * @param checkHapticObjectsOnly the checkHapticObjectsOnly to set
     */
    public void setCheckHapticObjectsOnly(boolean checkHapticObjectsOnly) {
        this.checkHapticObjectsOnly = checkHapticObjectsOnly;
    }

    /**
     * @return the checkBothSidesOfTriangles
     */
    public boolean isCheckBothSidesOfTriangles() {
        return checkBothSidesOfTriangles;
    }

    /**
     * @param checkBothSidesOfTriangles the checkBothSidesOfTriangles to set
     */
    public void setCheckBothSidesOfTriangles(boolean checkBothSidesOfTriangles) {
        this.checkBothSidesOfTriangles = checkBothSidesOfTriangles;
    }

    /**
     * @return the adjustObjectMotion
     */
    public boolean isAdjustObjectMotion() {
        return adjustObjectMotion;
    }

    /**
     * @param adjustObjectMotion the adjustObjectMotion to set
     */
    public void setAdjustObjectMotion(boolean adjustObjectMotion) {
        this.adjustObjectMotion = adjustObjectMotion;
    }

    /**
     * @return the collisionRadius
     */
    public double getCollisionRadius() {
        return collisionRadius;
    }

    /**
     * @param collisionRadius the collisionRadius to set
     */
    public void setCollisionRadius(double collisionRadius) {
        this.collisionRadius = collisionRadius;
    }
    
}
