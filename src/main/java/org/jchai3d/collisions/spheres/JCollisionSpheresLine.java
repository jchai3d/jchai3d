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
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheresLine extends JCollisionSpheresGenericShape {

    /**
     * The center of the line.
     */
    JVector3d center;
    /**
     * The radius of the line.
     */
    double radius;
    /**
     * The first endpoint of the line.
     */
    protected JVector3d segmentPointA;
    /**
     * The first endpoint of the line.
     */
    protected JVector3d segmentPointB;

    /**
     * Constructor of cCollisionSpheresLine.
     *
     * @param a_segmentPointA First endpoint of the line segment.
     * @param a_segmentPointB Second endpoint of the line segment.
     */
    JCollisionSpheresLine(JVector3d segmentPointA,
            JVector3d segmentPointB) {
        // calculate the center of the line segment
        center = JMaths.jMul(0.5, JMaths.jAdd(segmentPointA, segmentPointB));

        // calculate the radius of the bounding sphere as the distance from the
        // center of the segment (calculated above) to an endpoint
        radius = JMaths.jDistance(center, segmentPointA);

        // store segment
        this.segmentPointA = segmentPointA;
        this.segmentPointB = segmentPointB;
    }

    @Override
    public JVector3d getCenter() {
        return center;
    }

    @Override
    public double getRadius() {
        return radius;
    }

    /**
     * Determine whether there is any intersection between the primitives (this
     * line and the given triangle) by calling the collision detection method of
     * the triangle primitive.
     *
     * @param a_other The triangle primitive to check for intersection.
     * @param a_recorder Stores all collision events.
     * @param a_settings Contains collision settings information.
     * @return Return whether the given triangle intersects this line.
     */
    @Override
    public boolean computeCollision(JCollisionSpheresGenericShape other, JCollisionRecorder recorder, JCollisionSettings settings) {
        // check for a collision between the primitives (one a triangle and the
        // other a line segment, we assume) by calling the collision detection
        // method of the triangle primitive; it will only return true if the
        // distance between the segment origin and the triangle is less than the
        // current closest intersecting triangle (whose distance squared is kept
        // in a_colSquareDistance)
        return other.computeCollision(this, recorder, settings);
    }

    /**
     * @return the segmentPointA
     */
    public JVector3d getSegmentPointA() {
        return segmentPointA;
    }

    /**
     * @return the segmentPointB
     */
    public JVector3d getSegmentPointB() {
        return segmentPointB;
    }
}
