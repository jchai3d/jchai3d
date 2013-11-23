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

import org.jchai3d.graphics.JTriangle;
import java.util.ArrayList;
import org.jchai3d.math.JVector3d;

/**
 * JCollisionBrute provides methods to check for the intersection of a line
 * segment with a mesh by checking all triangles in the mesh.
 *
 * @author Igor Gabriel
 * @author Marcos da Silva Ramos version 1.0.0
 */
public class JCollisionBrute extends JGenericCollision {

    // Pointer to the list of triangles in the mesh.
    protected ArrayList<JTriangle> mTriangles;

    // Constructor of JCollisionBrute.
    public JCollisionBrute(ArrayList<JTriangle> aTriangles) {
        mTriangles = aTriangles;
    }

    // No initialization is necessary for the brute force method.
    @Override
    public void initialize(double aRadius) {
    }

    // There isn't really a useful "visualization" of "check all triangles".
    @Override
    public void render() {
    }

    /**
     * Check if the given line segment intersects any triangle of the mesh. This
     * method is called "brute force" because all triangles are checked by
     * invoking their collision-detection methods. This method is simple but
     * very inefficient.
     *
     * @param aSegmentPointA Initial point of segment.
     * @param aSegmentPointB End point of segment.
     * @param aRecorder Returns pointer to nearest collided object.
     * @param aSettings Structure which contains some rules about how the
     * collision detection should be performed.
     * @return Return true if the line segment intersects one or more triangles.
     */
    @Override
    public boolean computeCollision(JVector3d aSegmentPointA, JVector3d aSegmentPointB, JCollisionRecorder aRecorder, JCollisionSettings aSettings) {
        // temp variables
        boolean hit = false;

        // check all triangles for collision
        int numTriangles = mTriangles.size();
        for (int i = 0; i < numTriangles; i++) {

            // check for a collision between this triangle and the segment by
            // calling the triangle's collision detection method
            if (mTriangles.get(i).computeCollision(aSegmentPointA, aSegmentPointB, aRecorder, aSettings)) {
                hit = true;
            }
        }

        // return result
        return (hit);
    }
}
