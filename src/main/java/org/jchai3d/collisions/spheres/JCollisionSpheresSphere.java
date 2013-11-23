/**
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
 */
package org.jchai3d.collisions.spheres;

import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public abstract class JCollisionSpheresSphere {

    protected JCollisionSpheresSphere parent;
    protected JVector3d center;
    protected double radius;
    protected int depth;
    protected int num;

    /**
     * Default constructor of JCollisionSpheresSphere.
     */
    public JCollisionSpheresSphere() {
        center = new JVector3d();
        parent = null;
        depth = 0;
    }

    
    /**
     * Constructor of JCollisionSpheresSphere.
     *
     * @param parent Pointer to parent of this node in the sphere tree.
     */
    public JCollisionSpheresSphere(JCollisionSpheresSphere parent) {
        this.parent = parent;
        // set the depth of this node to be one below its parent
        if (parent != null) {
            depth = parent.depth + 1;
        }
    }

    /**
     *
     * @return
     */
    public abstract boolean isLeaf();

    /**
     *
     * Draw the collision sphere for this node, if at the given depth.
     *
     * @param depth the depth of the draw
     */
    public abstract void draw(int depth);
    
    /**
     * Draw the collision sphere for this node, with depth -1;
     */
    public void draw() {
        draw(-1);
    }


    /**
     * Determine whether there is any intersection between the primitives (line
     * and triangles) in the collision subtrees rooted at the two given
     * collision spheres.
     *
     * @param sa Node 0.
     * @param sb Node 1.
     * @param recorder Stores all collision events
     * @param settings Contains collision settings information.
     * @return Return true if a collision event has occurred.
     */
    public static boolean computeCollision(JCollisionSpheresSphere sa,
            JCollisionSpheresSphere sb,
            JCollisionRecorder recorder,
            JCollisionSettings settings) {
        // if first sphere is an internal node, call internal node collision function
        if (!sa.isLeaf()) {
            return JCollisionSpheresNode.computeCollision((JCollisionSpheresNode) sa,
                    sb, recorder, settings);
        }

        // if second sphere is an internal node, call internal node collision function
        if (!sb.isLeaf()) {
            return JCollisionSpheresNode.computeCollision((JCollisionSpheresNode) sb,
                    sa, recorder, settings);
        }

        // if both spheres are leaves, call leaf collision function
        return JCollisionSpheresLeaf.computeCollision((JCollisionSpheresLeaf) sa,
                (JCollisionSpheresLeaf) sb, recorder, settings);
    }

    /**
     * @return the parent
     */
    public JCollisionSpheresSphere getParent() {
        return parent;
    }

    /**
     * @param parent the parent to set
     */
    public void setParent(JCollisionSpheresSphere parent) {
        this.parent = parent;
    }

    /**
     * @return the center
     */
    public JVector3d getCenter() {
        return center;
    }

    /**
     * @return the radius
     */
    public double getRadius() {
        return radius;
    }

    /**
     * @return the depth
     */
    public int getDepth() {
        return depth;
    }
}
