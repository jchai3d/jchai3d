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
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheresTri extends JCollisionSpheresGenericShape {

    //! The vertices of the triangle.
    JCollisionSpheresPoint corner[];
    //! The edges of the triangle.
    JCollisionSpheresEdge side[];
    //! The center of the triangle.
    JVector3d center;
    //! The radius of the triangle.
    double radius;
    //! The JTriangle object in the mesh associated with this triangle.
    JTriangle original;

    /**
     * Constructor of cCollisionSpheresTri, to enclose a single triangle in a
     * sphere.
     *
     * \fn cCollisionSpheresTri::cCollisionSpheresTri(cVector3d a, cVector3d b,
     * cVector3d c, double a_extendedRadius) \param a First vertex of the
     * triangle. \param b Second vertex of the triangle. \param c Third vertex
     * of the triangle. \param a_extendedRadius Additional radius to add to
     * sphere. \return Return a pointer to new cCollisionSpheresTri instance.
     */
    public JCollisionSpheresTri(JVector3d a,
            JVector3d b,
            JVector3d c,
            double extendedRadius) {
        // Calculate the center of the circumscribing sphere for this triangle:
        // First compute the normal to the plane of this triangle
        JVector3d plane_normal = JMaths.jCross(JMaths.jSub(a, b), JMaths.jSub(a, c));

        // Compute the perpendicular bisector of the edge between points a and b
        JVector3d bisector1_dir = JMaths.jCross(JMaths.jSub(a, b), plane_normal);
        JVector3d bisector1_pt = JMaths.jDiv(2.0, (JMaths.jAdd(a, b)));

        // Compute the perpendicular bisector of the edge between points b and c
        JVector3d bisector2_dir = JMaths.jCross(JMaths.jSub(b, c), plane_normal);
        JVector3d bisector2_pt = JMaths.jDiv(2.0, (JMaths.jAdd(b, c)));

        // Find the intersection of the perpendicular bisectors to find the center
        // of the circumscribed sphere, using the formula for 3D line-line
        // intersection given at 
        // http://cglab.snu.ac.kr/research/seminar/data01-1/RealTimeRendering10_1.ppt 
        JVector3d[] mat = new JVector3d[3];
        mat[0] = JMaths.jSub(bisector2_pt, bisector1_pt);
        mat[1] = bisector2_dir;
        mat[2] = JMaths.jCross(bisector1_dir, bisector2_dir);
        float det = (float) (mat[0].x * mat[1].y * mat[2].z + mat[1].x * mat[2].y * mat[0].z
                + mat[2].x * mat[0].y * mat[1].z - mat[0].z * mat[1].y * mat[2].z
                - mat[1].z * mat[2].y * mat[0].x - mat[2].z * mat[0].y * mat[1].x);
        JVector3d cp = JMaths.jCross(bisector1_dir, bisector2_dir);
        if (cp.lengthsq() > JConstants.CHAI_SMALL) {
            float s = (float) (det / cp.lengthsq());
            this.center = JMaths.jAdd(bisector1_pt, JMaths.jMul(s, bisector1_dir));
        } else {
            center = JMaths.jDiv(2.0, (JMaths.jAdd(a, b)));
        }

        // set the vertices (corners) of the triangle
        corner = new JCollisionSpheresPoint[3];
        corner[0] = new JCollisionSpheresPoint(a);
        corner[1] = new JCollisionSpheresPoint(b);
        corner[2] = new JCollisionSpheresPoint(c);

        // Calculate a radius of the bounding sphere as the largest distance between
        // the sphere center calculated above and any vertex of the triangle
        radius = 0;
        int i, j, k;
        for (i = 0; i < 3; i++) {
            double curRadius = corner[i].position.distance(center);
            if (curRadius > radius) {
                radius = curRadius;
            }
        }

        // See if we could get a smaller bounding sphere by just taking one of the edges
        // of the triangle as a diameter (this may be better for long, skinny triangles)
        for (i = 0; i < 3; i++) {
            for (j = i; j < 3; j++) {
                // Calculate the center for this edge, and determine necessary sphere radius
                JVector3d candidate_center = new JVector3d();
                candidate_center.x = (corner[i].position.x + corner[j].position.x) / 2.0;
                candidate_center.y = (corner[i].position.y + corner[j].position.y) / 2.0;
                candidate_center.z = (corner[i].position.z + corner[j].position.z) / 2.0;
                double candidate_radius = 0.0;
                for (k = 0; k < 3; k++) {
                    double curRad = corner[k].position.distance(candidate_center);
                    if (curRad > candidate_radius) {
                        candidate_radius = curRad;
                    }
                }

                // If this results in a smaller sphere, use it
                if (candidate_radius < radius) {
                    radius = candidate_radius;
                    center = candidate_center;
                }
            }
        }

        // add external radius to sphere
        radius = radius + extendedRadius;
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
     * triangle and the given line) by calling the collision detection method of
     * the cTriangle object associated with this triangle primitive.
     *
     *
     * @param a_recorder Stores all collision events.
     * @param a_settings Contains collision settings information.
     */
    @Override
    public boolean computeCollision(JCollisionSpheresGenericShape other, JCollisionRecorder recorder, JCollisionSettings settings) {
        // cast the "other" shape to a line primitive; collision detection is
        // currently only set up to handle line segment - triangle intersections
        JCollisionSpheresLine line = (JCollisionSpheresLine) other;

        // check for a collision between the primitives (one a triangle and the
        // other a line segment, we assume) by calling the collision detection
        // method of the cTriangle object associated with this triangle primitive;
        // it will only return true if the distance between the segment origin and
        // the triangle is less than the current closest intersecting triangle
        // (whose distance squared is kept in a_colSquareDistance)

        JVector3d segA = line.getSegmentPointA();
        JVector3d segB = line.getSegmentPointB();

        return (original.computeCollision(segA, segB,
                recorder,
                settings));
    }

    public void setOriginal(JTriangle original) {
        this.original = original;
    }
}
