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

import javax.media.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JVector3d;

/**
 *
 * JCollisionAABBInternal contains methods to set up internal nodes of an AABB
 * tree and to use them to detect for collision with a line.
 *
 * @author Chris Sewell (original author)
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (Java adaptation)
 */
public class JCollisionAABBInternal extends JCollisionAABBNode {

    /**
     * The root of this node's left subtree.
     */
    JCollisionAABBNode leftSubTree;
    /**
     * The root of this node's right subtree.
     */
    JCollisionAABBNode rightSubTree;
    /**
     * Test box for collision with ray itself (as compared to line's box)?
     */
    boolean testLineBox;

    /**
     * Default constructor of cCollisionAABBInternal.
     */
    public JCollisionAABBInternal() {
        super(JAABBNodeType.AABB_NODE_INTERNAL, 0);
    }

    /**
     * Initializes this internal node
     *
     * @param leaves the reference to the array of leaves created on
     * JCollisionAABB
     * @param offset the position of the leaf that this node will read from.
     * @param length the number of leaves, from offset, that will be readed.
     * @param depth the depth of this node.
     */
    public void initialize(JCollisionAABBLeaf[] leaves, int offset, int length, int depth) {

        // set the depth of this node and initialize
        this.depth = depth;
        this.leftSubTree = null;
        this.rightSubTree = null;
        this.testLineBox = true;

        // the real final position in the leaves array that this instance will
        // read
        int end = offset + length;

        // create a box to enclose all the leafs below this internal node
        bbox.setEmpty();
        for (int j = offset; j < (end); ++j) {
            bbox.enclose(leaves[j].bbox);
        }

        // move leafs with smaller coordinates (on the longest axis) towards the
        // beginning of the array and leaves with larger coordinates towards the
        // end of the array
        ////System.out.println("["+depth+"] offset:"+offset+"\t length:"+length);
        int axis = bbox.longestAxis();
        int i = offset;
        int mid = end;
        while (i < mid) {
            if (leaves[i].bbox.getCenter().get(axis) < bbox.getCenter().get(axis)) {
                ++i;
            } else {
                mid--;
                JCollisionAABBLeaf tmp = leaves[i];
                leaves[i] = leaves[mid];
                leaves[mid] = tmp;

            }
        }

        // we expect mid, used as the right iterator in the "insertion sort" style
        // rearrangement above, to have moved roughly to the middle of the array;
        // however, if it never moved left or moved all the way left, set it to
        // the middle of the array so that neither the left nor right subtree will
        // be empty
        if (mid == offset || mid == (end)) {
            mid = length / 2;
        } else {
            mid -= offset;
        }
        // if the right subtree contains multiple triangles, create new internal node
        if (mid >= 2) {
            rightSubTree = new JCollisionAABBInternal();
            ((JCollisionAABBInternal) rightSubTree).initialize(leaves, offset, mid, this.depth + 1);
        } else {
            rightSubTree = leaves[offset];
            if (rightSubTree != null) {
                rightSubTree.depth = this.depth + 1;
            }
        }

        // if the left subtree contains multiple triangles, create new internal node
        if (length - mid >= 2) {

            leftSubTree = new JCollisionAABBInternal();
            ((JCollisionAABBInternal) leftSubTree).initialize(leaves, offset + mid, length - mid, depth + 1);
        } else {
            leftSubTree = leaves[offset + mid];
            if (leftSubTree != null) {
                leftSubTree.depth = depth + 1;
            }
        }
    }

    @Override
    public void fitBBox(double a_radius) {
        bbox.enclose(leftSubTree.bbox, rightSubTree.bbox);
    }

    /**
     * <p>Draw the edges of the bounding box for an internal tree node if it is
     * at depth <b>depth</b> in the tree, and call the draw function for its
     * children.</p>
     *
     * <p>Onlydraw nodes at this level in the tree. <b>depth</b> less than 0
     * render <b>up to</b> this level.</p>
     */
    @Override
    public void render(int depth) {
        // render current node
        if (((depth < 0) && (Math.abs(depth) >= this.depth)) || depth == this.depth) {
            if (depth < 0) {
                GLContext.getCurrent().getGL().getGL2().glColor4f(1.0f, 0.0f, 0.0f, 1.0f);
            }
            bbox.render();
        }

        if (leftSubTree != null) // render left sub tree
        {
            leftSubTree.render(depth);
        }

        if (rightSubTree != null) // render right sub tree
        {
            rightSubTree.render(depth);
        }
    }

    /**
     * Determine whether the given line intersects the mesh covered by the AABB
     * Tree rooted at this internal node. If so, return (in the output
     * parameters) information about the intersected triangle of the mesh
     * closest to the segment origin.
     *
     * @param segmentPointA Initial point of segment.
     * @param segmentPointB End point of segment.
     * @param lineBox A bounding box for the incoming segment, for quick
     * discarding of collision tests.
     * @param recorder Stores all collision events.
     * @param settings Contains collision settings information.
     * @return true if line segment intersects a triangle in the subtree.
     */
    @Override
    public boolean computeCollision(JVector3d segmentPointA, JVector3d segmentPointB, JCollisionAABBBox lineBox, JCollisionRecorder recorder, JCollisionSettings settings) {
        //System.out.println("["+depth+"]a: "+segmentPointA);
        //System.out.println("["+depth+"]b: "+segmentPointB);
        // if a line's bounding box does not intersect the node's bounding box,
        // there can be no intersection
        if (!JCollisionAABB.intersect(bbox, lineBox)) {
            return (false);
        }
        
        //System.out.println("intersect: "+depth);

        if (testLineBox) {
            if (!JCollisionAABB.hitBoundingBox(
                    (bbox.min.toArray()),
                    (bbox.max.toArray()),
                    segmentPointA.toArray(),
                    segmentPointB.toArray())) {
                return (false);
            }
        }

        // check collision between line and left subtree node; it will only
        // return true if the distance between the segment origin and this
        // triangle is less than the current closest intersecting triangle
        // (whose distance squared is in l_colSquareDistance)
        boolean leftResult = (leftSubTree != null && leftSubTree.computeCollision(
                segmentPointA, segmentPointB, lineBox, recorder, settings));

        // check collision between line and right subtree node; it will only
        // return true if the distance between the segment origin and this
        // triangle is less than the current closest intersecting triangle
        // (whose distance squared is in r_colSquareDistance)
        boolean rightResult = (rightSubTree != null && rightSubTree.computeCollision(
                segmentPointA, segmentPointB, lineBox, recorder, settings));

        // return result
        return (leftResult || rightResult);
    }

    /**
     * Return whether this node contains the specified triangle tag.
     *
     * \fn void cCollisionAABBInternal::contains_triangle(int tag) \param tag
     * Tag to inquire about
     */
    @Override
    public boolean containsTriangle(int tag) {
        return (leftSubTree.containsTriangle(tag)
                || rightSubTree.containsTriangle(tag));
    }

    /**
     * Sets this node's parent pointer and optionally propagate assignments to
     * its children (setting their parent pointers to this node).
     *
     * \fn void cCollisionAABBInternal::setParent(cCollisionAABBNode* a_parent,
     * int a_recursive); \param a_parent Pointer to this node's parent. \param
     * a_recursive Propagate assignment down the tree?
     */
    @Override
    public void setParent(JCollisionAABBNode parent, boolean recursive) {

        this.parent = parent;
        if (leftSubTree != null && recursive) {
            leftSubTree.setParent(this, true);
        }
        if (rightSubTree != null && recursive) {
            rightSubTree.setParent(this, true);
        }
    }
}
