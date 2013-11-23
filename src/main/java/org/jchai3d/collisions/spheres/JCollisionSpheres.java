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

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.collisions.JGenericCollision;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheres extends JGenericCollision {
    //! Pointer to the sphere at the root of the sphere tree.

    JCollisionSpheresSphere root;
    //! Pointer to the list of triangles in the mesh.
    ArrayList<JTriangle> triangles;
    //! Triangle returned by last successful collision test.
    JTriangle lastCollision;
    //! Use neighbor list to speed up collision detection?
    boolean useNeighbors;
    //! For internal and debug usage.
    JTriangle secret;

    /**
     * Constructor of cCollisionSpheres.
     *
     * @param triangles Pointer to array of triangles.
     * @param useNeighbors Use neighbor lists to speed up collision detection?
     */
    public JCollisionSpheres(ArrayList<JTriangle> triangles, boolean useNeighbors) {
        this.triangles = triangles;
        this.useNeighbors = useNeighbors;
        root = null;

        treeColor = new JColorf(01f, 0.3f, 0.1f, 0.3f);
    }

    @Override
    public void initialize(double aRadius) {
        secret = null;

        // initialize number of triangles, root pointer, and last intersected triangle
        int numTriangles = this.triangles.size();
        this.root = null;
        this.lastCollision = null;

        // if there are triangles, build the tree
        if (numTriangles > 0) {
            // if there is more than one triangle, allocate internal nodes
            if (numTriangles > 1) {
                this.root = new JCollisionSpheresNode(triangles, null, aRadius);
            } // if there is only one triangle, just allocate one leaf node and
            // set the root to point to it
            else {
                this.root = new JCollisionSpheresLeaf(this.triangles.get(0));
            }
        } // if there are no triangles, just set the root to null
        else {
            this.root = null;
        }
    }

    @Override
    public void render() {
        if (root == null) {
            return;
        }

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        boolean transparency = treeColor.getA() >= 1;

        // set up transparency if we need it...
        if (transparency) {
            gl.glEnable(GL2.GL_BLEND);
            gl.glDepthMask(false);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
        }

        // set rendering settings
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glLineWidth(1.0f);
        gl.glColor4fv(treeColor.color,0);

        // render tree
        root.draw(displayDepth);

        // turn off transparency if we used it...
        if (transparency) {
            gl.glDepthMask(true);
            gl.glDisable(GL2.GL_BLEND);
        }
    }

    /**
     * Check if the given line segment intersects any triangle of the mesh. If
     * so, return true, as well as (through the output parameters) pointers to
     * the intersected triangle, the mesh of which this triangle is a part, the
     * point of intersection, and the distance from the origin of the segment to
     * the collision point. If more than one triangle is intersected, return the
     * one closest to the origin of the segment. The method uses the
     * pre-computed sphere tree, starting at the root and recursing through the
     * tree, breaking the recursion along any path in which the sphere bounding
     * the line segment does not intersect the sphere of the node. At the leafs,
     * triangle-segment intersection testing is called.
     *
     * @param segmentPointA Initial point of segment.
     * @param segmentPointB End point of segment.
     * @param recorder Stores all collision events
     * @param ttings Contains collision settings information.
     * @return Return \b true if a collision event has occurred.
     */
    @Override
    public boolean computeCollision(JVector3d segmentPointA, JVector3d segmentPointB, JCollisionRecorder recorder, JCollisionSettings settings) {
        // if this is a subsequent call from the proxy algorithm after detecting
        // an initial collision, and if the flag to use neighbor checking is set,
        // only neighbors of the triangle from the first collision detection
        // need to be checked
        if ((useNeighbors) && (root != null)
                && (lastCollision != null) && (lastCollision.neighbors != null)) {
            // check each neighbor, and find the closest for which there is a
            // collision, if any
            for (int i = 0; i < lastCollision.neighbors.size(); i++) {
                lastCollision.neighbors.get(i).computeCollision(
                        segmentPointA, segmentPointB, recorder, settings);
            }

            // if at least one neighbor triangle was intersected, return true
            if (recorder.getCollisions().size() > 0) {
                return true;
            }

            // otherwise there was no collision; return false
            lastCollision = null;
            return false;
        }

        // otherwise, if this is the first call in an iteration of the proxy
        // algorithm (or a call from any other algorithm), check the sphere tree

        // if the root is null, the tree is empty, so there can be no collision
        if (root == null) {
            lastCollision = null;
            return false;
        }

        // create a JCollisionSpheresLine object and enclose it in a sphere leaf
        JCollisionSpheresLine curLine = new JCollisionSpheresLine(segmentPointA, segmentPointB);
        JCollisionSpheresLeaf lineSphere = new JCollisionSpheresLeaf(curLine);

        // test for intersection between the line segment and the root of the
        // collision tree; the root will recursively call children down the tree
        boolean result = JCollisionSpheresSphere.computeCollision(root,
                lineSphere,
                recorder,
                settings);

        // This prevents the destructor from deleting a stack-allocated SpheresLine
        // object
        lineSphere.primitive = null;

        // return whether there was an intersection
        return result;
    }
}
