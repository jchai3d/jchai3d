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

import java.util.ArrayList;

/**
 * JCollisionRecorder stores a list of collision events.
 *
 * @author Igor Gabriel
 * @author Marcos da Silva Ramos
 * @version 1.0.0
 */
public class JCollisionRecorder {

    /**
     * List of collisions.
     */
    private JCollisionEvent nearestCollision;
    private ArrayList<JCollisionEvent> collisions;

    /**
     * Constructor of JCollisionRecorder
     */
    public JCollisionRecorder() {
        nearestCollision = new JCollisionEvent();
        collisions = new ArrayList<JCollisionEvent>();
    }

    /**
     * Clear all records.
     */
    public void clear() {
        nearestCollision.clear();
        collisions.clear();
    }

    /**
     * @return the nearestCollision
     */
    public JCollisionEvent getNearestCollision() {
        return nearestCollision;
    }

    /**
     * @return the collisions
     */
    public ArrayList<JCollisionEvent> getCollisions() {
        return collisions;
    }

    /**
     * @param collisions the collisions to set
     */
    public void setCollisions(ArrayList<JCollisionEvent> collisions) {
        this.collisions = collisions;
    }
}
