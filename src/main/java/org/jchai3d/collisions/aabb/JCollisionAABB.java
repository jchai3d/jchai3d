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

import java.util.ArrayList;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.collisions.JGenericCollision;
import org.jchai3d.graphics.JDraw3D;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;

/**
 *
 * JCollisionAABB provides methods to create an Axis-Aligned Bounding Box
 * collision detection tree, and to use this tree to check for the intersection
 * of a line segment with a mesh.
 *
 * @author Chris Sewell (original author)
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (Java adaptation)
 */
public class JCollisionAABB extends JGenericCollision {

    /**
     * Pointer to the list of triangles in the mesh.
     */
    protected ArrayList<JTriangle> triangles;
    /**
     * Pointer to an array of leaf nodes for the AABB Tree.
     */
    protected JCollisionAABBLeaf leaves[];
    /**
     * Pointer to array of internal nodes.
     */
    protected JCollisionAABBInternal[] internalNodes;
    /**
     * Pointer to the root of the AABB Tree.
     */
    protected JCollisionAABBNode root;
    /**
     * The number of triangles in the mesh.
     */
    protected int numTriangles;
    /**
     * Triangle returned by last successful collision test.
     */
    protected JTriangle lastCollision;
    /**
     * Use list of triangles' neighbors to speed up collision detection?
     */
    protected boolean neighborsEnabled;

    /**
     * Constructor of JCollisionAABB.
     *
     * @param a_triangles Pointer to array of triangles.
     * @param a_useNeighbors Use neighbor lists to speed up collision detection?
     */
    public JCollisionAABB(ArrayList<JTriangle> triangles, boolean useNeighbors) {
        // list of triangles used when building the tree
        this.triangles = triangles;

        // initialize members
        this.internalNodes = null;
        this.root = null;
        this.leaves = null;
        this.numTriangles = 0;
        this.neighborsEnabled = useNeighbors;
    }

    /**
     * Build the Axis-Aligned Bounding Box collision-detection tree. Each leaf
     * is associated with one triangle and with a bounding box of minimal
     * dimensions such that it fully encloses the triangle and is aligned with
     * the coordinate axes (no rotations). Each internal node is associated with
     * a bounding box of minimal dimensions such that it fully encloses the
     * bounding boxes of its two children and is aligned with the axes.
     *
     * @param radius radius to add around the triangles.
     */
    @Override
    public void initialize(double radius) {
        lastCollision = null;

        // erase previous tree
        root = null;

        //reset triangle counter 
        numTriangles = 0;

        // count the number of allocated triangles
        for (int i = 0; i < triangles.size(); ++i) {
            if (triangles.get(i).isAllocated()) {
                numTriangles++;
            }
        }

        // check if there is no allocated triangle
        if (numTriangles == 0) {
            return;
        }

        // create a leaf node for each triangle
        leaves = new JCollisionAABBLeaf[numTriangles];
        int j = 0;
        for (int i = 0; i < triangles.size(); ++i) {
            if (triangles.get(i).isAllocated()) {
                leaves[j] = new JCollisionAABBLeaf();
                leaves[j].initialize(triangles.get(i), radius);
                j++;
            }
        }

        // allocate an array to hold all internal nodes of the binary tree
        if (numTriangles >= 2) {
            
            // create the root node, that will contain *all* sub nodes.
            root = new JCollisionAABBInternal();
            ((JCollisionAABBInternal) root).initialize(leaves,0,numTriangles, 0);
        }
        // there is only one triangle, so the tree consists of just one triangle
        else {
            root = leaves[0];
        }
        
        //assign parent relationship in the tree
        root.setParent(null, true);

    }

    JVector3d a = new JVector3d();
    JVector3d b = new JVector3d();
    /**
     * Draw the bounding boxes in OpenGL2.
     */
    @Override
    public void render() {
        if (root != null) {
            GL2 gl = GLContext.getCurrent().getGL().getGL2();
            // set rendering settings
            gl.glDisable(GL2.GL_LIGHTING);
            gl.glLineWidth(1.0f);
            gl.glColor3f(0.2f, 0.2f, 0.2f);
            
            //JDraw3D.jDrawWireBox(a.x, a.y, a.z, b.x, b.y, b.z);
            // render tree by calling the root, which recursively calls the children
            root.render(displayDepth);

            // restore lighting settings
            gl.glEnable(GL2.GL_LIGHTING);
        }
    }

    /**
     * Check if the given line segment intersects any triangle of the mesh. If
     * so, return true, as well as (through the output parameters) pointers to
     * the intersected triangle, the mesh of which this triangle is a part, the
     * point of intersection, and the distance from the origin of the segment to
     * the collision point. If more than one triangle is intersected, return the
     * one closest to the origin of the segment. The method uses the
     * pre-computed AABB boxes, starting at the root and recursing through the
     * tree, breaking the recursion along any path in which the bounding box of
     * the line segment does not intersect the bounding box of the node. At the
     * leafs, triangle-segment intersection testing is called.
     *
     * @param segmentPointA Initial point of segment.
     * @param segmentPointB End point of segment.
     * @param recorder Stores all collision events
     * @param settings Contains collision settings information.
     * @return Return true if a collision event has occurred.
     */
    @Override
    public boolean computeCollision(JVector3d segmentPointA, JVector3d segmentPointB,
            JCollisionRecorder recorder, JCollisionSettings settings) {
        // if the root is null, the tree is empty, so there can be no collision
        if (root == null) {
            return (false);
        }

        // create an axis-aligned bounding box for the line
        JCollisionAABBBox lineBox = new JCollisionAABBBox();
        lineBox.setEmpty();
        lineBox.enclose(segmentPointA);
        lineBox.enclose(segmentPointB);
        
        a.copyFrom(segmentPointA);
        b.copyFrom(segmentPointB);
        // test for intersection between the line segment and the root of the
        // collision tree; the root will recursively call children down the tree
        //System.out.println("Testing collision");
        return root.computeCollision(segmentPointA,
                segmentPointB,
                lineBox,
                recorder, settings);

    }
    
    /**
     * Return the root node of the collision tree.
     *
     * @return the root node of the collision tree.
     */
    public JCollisionAABBNode getRoot() {
        return (root);
    }

    /**
     * Determine whether the two given boxes intersect each other.
     *
     * @param a0 First box; may intersect with second box.
     * @param a1 Second box; may intersect with first box.
     * @return Return whether there is any overlap of the two boxes.
     */
    public static boolean intersect(JCollisionAABBBox a0, JCollisionAABBBox a1) {
        // check for overlap along each axis
        if (a0.getLowerX() > a1.getUpperX()) {
            return false;
        }
        if (a0.getLowerY() > a1.getUpperY()) {
            return false;
        }
        if (a0.getLowerZ() > a1.getUpperZ()) {
            return false;
        }
        if (a1.getLowerX() > a0.getUpperX()) {
            return false;
        }
        if (a1.getLowerY() > a0.getUpperY()) {
            return false;
        }
        if (a1.getLowerZ() > a0.getUpperZ()) {
            return false;
        }

        // if the boxes are not separated along any axis, a collision has occurred
        return true;
    }

    /**
     * Determine whether the given ray intersects the bounding box. Based on
     * code by Andrew Woo from "Graphics Gems", Academic Press, 1990.
     *
     * @param a_minB[3] Minimum coordinates (along each axis) of bounding box.
     * @param a_maxB[3] Maximum coordinates (along each axis) of bounding box.
     * @param a_origin[3] Origin of the ray.
     * @param a_dir[3] Direction of the ray.
     * @return Return true if line segment intersects the bounding box.
     */
    public static boolean hitBoundingBox(double minB[], double maxB[], double origin[], double end[]) {

        double[] coord = new double[]{0.00,0.00,0.00};
        boolean inside = true;
        char[] quadrant = new char[]{0,0,0};
        int i;
        int whichPlane;
        double[] maxT = new double[]{0.00,0.00,0.00};
        double[] candidatePlane = new double[]{0.00,0.00,0.00};
        double[] dir = new double[]{0.00,0.00,0.00};
        dir[0] = end[0] - origin[0];
        dir[1] = end[1] - origin[1];
        dir[2] = end[2] - origin[2];

        // Find candidate planes; this loop can be avoided if
        // rays cast all from the eye (assume perspective view)
        for (i = 0; i < 3; i++) {
            if (origin[i] < minB[i]) {
                quadrant[i] = LEFT;
                candidatePlane[i] = minB[i];
                inside = false;
            } else if (origin[i] > maxB[i]) {
                quadrant[i] = RIGHT;
                candidatePlane[i] = maxB[i];
                inside = false;
            } else {
                quadrant[i] = MIDDLE;
            }
        }

        // Ray origin inside bounding box
        if (inside) {
            //coord = origin;
            return (true);
        }

        // Calculate T distances to candidate planes
        for (i = 0; i < 3; i++) {
            if (quadrant[i] != MIDDLE && dir[i] != 0.) {
                maxT[i] = (candidatePlane[i] - origin[i]) / dir[i];
            } else {
                maxT[i] = -1.;
            }
        }

        // Get largest of the maxT's for final choice of intersection
        whichPlane = 0;
        for (i = 1; i < 3; i++) {
            if (maxT[whichPlane] < maxT[i]) {
                whichPlane = i;
            }
        }

        // Check final candidate actually inside box
        if (maxT[whichPlane] < 0.) {
            return (false);
        }
        for (i = 0; i < 3; i++) {
            if (whichPlane != i) {
                coord[i] = origin[i] + maxT[whichPlane] * dir[i];
                if (coord[i] < minB[i] || coord[i] > maxB[i]) {
                    return (false);
                }
            } else {
                coord[i] = candidatePlane[i];
            }
        }

        // Ray hits box...
        return (true);
    }
   
    private static final int RIGHT = 0;
    private static final int LEFT = 1;
    private static final int MIDDLE = 2;
}
