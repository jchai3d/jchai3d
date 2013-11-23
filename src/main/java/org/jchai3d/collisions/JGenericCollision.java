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
 *   author    Igor Gabriel, Marcos da Silva Ramos
 *   version   1.0.0
 */

package org.jchai3d.collisions;

import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JMaterial;
import org.jchai3d.math.JVector3d;

/**
 * JGenericCollision is an abstract class for collision-detection
 * algorithms for meshes with line segments.
 */
public abstract class JGenericCollision {

    // Constructor of JGenericCollision.
    public JGenericCollision() {
        this.treeColor = new JColorf(1.0f, 1.0f, 1.0f);

        // set default value for display depth (level 0 = root)
        this.displayDepth = 3;
    }
    // Color properties of the collision object.
    protected JColorf treeColor;
    /**
     * Level of collision tree to render... negative values force rendering
     * up to and including this level, positive values render _just_ this level.
     */
    protected int displayDepth;
    

    // Do any necessary initialization, such as building trees.
    public void initialize() {
        initialize(0.0);
    }

    // Do any necessary initialization, such as building trees.
    public abstract void initialize(double aRadius);

    // Provide a visual representation of the method.
    public abstract void render();

    // Return the triangles intersected by the given segment, if any.
    public abstract boolean computeCollision(JVector3d aSegmentPointA, JVector3d aSegmentPointB, JCollisionRecorder aRecorder, JCollisionSettings aSettings);

    // Set level of collision tree to display.
    public void setDisplayDepth(int aDepth) {
        displayDepth = aDepth;
    }

    // Read level of collision tree being displayed.
    public double getDisplayDepth() {
        return (displayDepth);
    }

    /**
     * @return the material
     */
    public JColorf getTreeColor() {
        return treeColor;
    }

    /**
     * @param material the material to set
     */
    public void setTreeColor(JColorf color) {
        this.treeColor = color;
    }
}
