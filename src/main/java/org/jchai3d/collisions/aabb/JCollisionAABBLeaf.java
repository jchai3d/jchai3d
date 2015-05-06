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
package org.jchai3d.collisions.aabb;

import com.jogamp.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;

/**
 * JCollisionAABBLeaf contains methods to set up leaf nodes of an AABB tree and
 * to use them to detect for collision with a line.
 *
 * @author Chris Sewell (original author)
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (Java adaptation)
 */
public class JCollisionAABBLeaf extends JCollisionAABBNode {

    /*
     * The triangle bounded by the leaf.
     */
    JTriangle triangle;

    /**
     * Default constructor of cCollisionAABBLeaf. *
     */
    public JCollisionAABBLeaf() {
        super(JAABBNodeType.AABB_NODE_LEAF, 0);
    }

    /**
     * Initialize internal node.
     */
    public void initialize(JTriangle triangle, double radius) {
        this.triangle = triangle;
        fitBBox(radius);
    }

    /**
     * Create a bounding box to enclose triangle belonging to this leaf node.
     */
    @Override
    public void fitBBox(double radius) {
        // empty box
        bbox.setEmpty();

        // enclose all three vertices of triangle
        if (triangle != null) {
            radius = 2*radius;
            bbox.enclose(triangle.getVertex0().getPosition());
            bbox.enclose(triangle.getVertex1().getPosition());
            bbox.enclose(triangle.getVertex2().getPosition());
            JVector3d min = new JVector3d(bbox.min);
            JVector3d max = new JVector3d(bbox.max);
            min.sub(radius, radius, radius);
            max.add(radius, radius, radius);
            bbox.setValue(min, max);
            //System.out.println(":: "+((float)bbox.getCenter().get(bbox.longestAxis())));
        }
    }

    /**
     * Draw the edges of the bounding box for this leaf if it is at depth
     * a_depth.
     */
    @Override
    public void render(int depth) {
        if (((depth < 0) && (Math.abs(depth) >= this.depth)) || depth == this.depth) {
            if (depth < 0) {
                GLContext.getCurrent().getGL().getGL2().glColor4f(1f, 0f, 0f, 1f);
            }
            bbox.render();
        }
    }

    /**
     * Determine whether the given line intersects the triangle belonging to
     * this leaf node by calling the triangle's collision detection method.
     *
     * \fn bool cCollisionAABBLeaf::computeCollision(cVector3d& a_segmentPointA,
     * cVector3d& a_segmentPointB, cCollisionAABBBox& a_lineBox,
     * cCollisionRecorder& a_recorder, cCollisionSettings& a_settings) \param
     * a_segmentPointA Initial point of segment. \param a_segmentPointB End
     * point of segment. \param a_lineBox A bounding box for the incoming
     * segment, for quick discarding of collision tests. \param a_recorder
     * Stores all collision events. \param a_settings Contains collision
     * settings information. \return Return \b true if the line segment
     * intersects the leaf's triangle.
     */
    @Override
    public boolean computeCollision(JVector3d segmentPointA,
            JVector3d segmentPointB,
            JCollisionAABBBox lineBox,
            JCollisionRecorder recorder,
            JCollisionSettings settings) {
        // check for a collision between this leaf's triangle and the segment by
        // calling the triangle's collision detection method; it will only
        // return true if the distance between the segment origin and this
        // triangle is less than the current closest intersecting triangle
        // (whose distance squared is kept in colSquareDistance)

        return triangle.computeCollision(segmentPointA,
                segmentPointB,
                recorder,
                settings);
    }

    /**
     * Return true if this node contains the specified triangle tag.
     */
    @Override
    public boolean containsTriangle(int tag) {
        return (triangle != null && triangle.getTag() == tag);
    }

    /**
     * Return parent of this node.
     */
    @Override
    public void setParent(JCollisionAABBNode parent, boolean recusive) {
        this.parent = parent;
    }
}