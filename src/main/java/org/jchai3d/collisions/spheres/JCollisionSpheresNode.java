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
import java.util.Collections;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.graphics.JDraw3D;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheresNode extends JCollisionSpheresSphere {

    /**
     * A "sufficiently small" number; zero within tolerated precision.
     */
    private static final double LITTLE = 1e-10;
    /**
     * A "sufficiently large" number; effectively infinity.
     */
    private static final double LARGE = 1e10;
    /**
     * The left subtree
     */
    JCollisionSpheresSphere left;
    /**
     * The right subtree
     */
    JCollisionSpheresSphere right;

    /**
     * Constructor of JCollisionSpheresNode.
     *
     * @param a_primList List of shape primitives to be enclosed in the subtree
     * rooted at this internal node.
     * @param a_parent Pointer to the parent of this node in the tree.
     */
    public JCollisionSpheresNode(ArrayList<JCollisionSpheresGenericShape> primList,
            JCollisionSpheresSphere parent) {
        super(parent);
        // set this node's parent pointer
        this.parent = parent;

        // create the left and right subtrees of this node
        constructChildren(primList);
    }

    /**
     * Constructor of JCollisionSpheresNode.
     *
     * @param a_tris Pointer to vector of triangles to use for collision
     * detection.
     * @param a_parent Pointer to the parent of this node in sphere tree.
     * @param a_extendedRadius Bounding radius.
     */
    public JCollisionSpheresNode(ArrayList<JTriangle> tris,
            JCollisionSpheresSphere parent,
            double extendedRadius) {
        
        super(parent);
        
        // create JCollisionSpheresTri primitive object for each cTriangle object
        ArrayList<JCollisionSpheresGenericShape> primList = new ArrayList<JCollisionSpheresGenericShape>();
        for (int i = 0; i < tris.size(); i++) {
            // create JCollisionSpheresPoint primitive object for first point
            JVector3d vpos1 = tris.get(i).getVertex0().getPosition();
            JVector3d vpos2 = tris.get(i).getVertex1().getPosition();
            JVector3d vpos3 = tris.get(i).getVertex2().getPosition();

            JCollisionSpheresTri t = new JCollisionSpheresTri(vpos1,
                    vpos2,
                    vpos3,
                    extendedRadius);

            t.setOriginal(tris.get(i));

            // insert new object in primitives list
            primList.add(t);

        }

        // set parent pointer
        this.parent = parent;

        // create left and right subtrees of this node
        constructChildren(primList);
    }

    private void constructChildren(ArrayList<JCollisionSpheresGenericShape> primList) {
        // ensure that there are at least two primitives so that it makes sense
        // to split them into left and right subtrees

        assert (primList.size() >= 2);

        // declare and initialize local variables for splitting primitives
        ArrayList<JCollisionSpheresGenericShape> leftList, rightList;
        leftList = new ArrayList<JCollisionSpheresGenericShape>();
        rightList = new ArrayList<JCollisionSpheresGenericShape>();
        double min[] = {LARGE, LARGE, LARGE};
        double max[] = {-LARGE, -LARGE, -LARGE};

        // find minimum and maximum values for each coordinate of primitves' centers
        for (JCollisionSpheresGenericShape cur : primList) {

            JVector3d center = cur.getCenter();
            for (int i = 0; i < 3; i++) {
                if (center.get(i) < min[i]) {
                    min[i] = center.get(i);
                }
                if (center.get(i) > max[i]) {
                    max[i] = center.get(i);
                }
            }
        }

        // find the coordinate index with the largest range (max to min)
        int split = 0;
        if ((max[1] - min[1]) > (max[split] - min[split])) {
            split = 1;
        }
        if ((max[2] - min[2]) > (max[split] - min[split])) {
            split = 2;
        }

        // sort the primitives according to the coordinate with largest range
        JCollisionSpheresGenericShape.split = split;
        Collections.sort(primList);

        // put first half in left subtree and second half in right subtree
        int s;
        for (s = 0; s < primList.size() / 2; s++) {
            leftList.add(primList.get(s));
        }

        for (s = primList.size() / 2; s < primList.size(); s++) {
            rightList.add(primList.get(s));
        }

        // if the left subtree is empty, transfer one from the right list
        if (leftList.isEmpty()) {
            leftList.add(0, rightList.get(0));
            rightList.remove(0);
        }

        // create new internal nodes as roots for left and right subtree lists, or
        // a leaf node if the subtree list has only one primitive
        if (leftList.size() == 1) {
            this.left = new JCollisionSpheresLeaf(leftList.get(0), this);
        } else {
            this.left = new JCollisionSpheresNode(leftList, this);
        }
        if (rightList.size() == 1) {
            this.right = new JCollisionSpheresLeaf(rightList.get(0), this);
        } else {
            this.right = new JCollisionSpheresNode(rightList, this);
        }

        // get centers and radii of left and right children
        final JVector3d lc = this.left.center;
        final JVector3d rc = this.right.center;
        double lr = this.left.getRadius();
        double rr = this.right.getRadius();

        // compute new radius as one-half the sum of the distance between the two
        // childrens' centers and the two childrens' radii
        double dist = lc.distance(rc);
        this.radius = (dist + lr + rr) / 2.0;

        // compute new center along line between childrens' centers
        if (dist != 0) {
            double lambda = (this.radius - lr) / dist;
            this.center = new JVector3d();
            this.center.x = lc.x + lambda * (rc.x - lc.x);
            this.center.y = lc.y + lambda * (rc.y - lc.y);
            this.center.z = lc.z + lambda * (rc.z - lc.z);
        } // if the left and right children have the same center, use this as the
        // new center
        else {
            this.center = lc;
        }

        // if one sphere is entirely contained within the other, set this sphere's
        // new center and radius equal to those of the larger one
        if (lr > dist + rr) {
            this.center = lc;
            this.radius = lr;
        }
        if (rr > dist + lr) {
            this.center = rc;
            this.radius = rr;
        }

        this.radius *= 1.001;
    }

    @Override
    public boolean isLeaf() {
        return false;
    }

    /**
     * Draw the collision sphere if at the given depth.
     *
     * @param depth Only draw nodes at this depth in the tree. depth = -1
     * renders the complete tree.
     */
    @Override
    public void draw(int depth) {
        // only render the sphere if this node is at the given depth
        if (((depth < 0) && (Math.abs(depth) >= this.depth)) || depth == this.depth) {
            GL2 gl = GLContext.getCurrent().getGL().getGL2();
            gl.glPushMatrix();
            gl.glTranslated(center.x, center.y, center.z);
            JDraw3D.jDrawSphere(radius, 10, 10);
            gl.glPopMatrix();
        }

        // do not go any further if the target depth has been reached
        if (depth >= 0 && depth == this.depth) {
            return;
        }

        // recursively call left and right subtrees
        if (left != null) {
            left.draw(depth);
        }
        if (right != null) {
            right.draw(depth);
        }
    }

    /**
     * Determine whether there is any intersection between the primitives (line
     * and triangles) in the collision subtrees rooted at the two given
     * collision spheres. If so, return (in the output parameters) information
     * about the intersected triangle of the mesh closest to the segment origin.
     *
     * @param sa Root of one sphere tree to check for collision.
     * @param sb Root of other sphere tree to check for collision.
     * @param recorder Stores all collision events
     * @param settings Contains collision settings information.
     * @return Return whether any primitives within the two sphere trees
     * collide.
     */
    public static boolean computeCollision(JCollisionSpheresNode sa,
            JCollisionSpheresSphere sb,
            JCollisionRecorder recorder,
            JCollisionSettings settings) {
        // if both nodes are internal nodes, arrange that the larger one is first
        if (!sb.isLeaf() && (sa.radius < sb.radius)) {
            JCollisionSpheresNode tmp = sa;
            sa = (JCollisionSpheresNode) sb;
            sb = tmp;
        }

        // if spheres don't overlap, there can be no collision
        double minSep = (sa.center).distance(sb.center);
        if ((minSep - (sa.radius + sb.radius)) >= LITTLE) {
            return false;
        }

        // check for overlap of larger sphere's left subtree with smaller sphere
        boolean l_result = JCollisionSpheresSphere.computeCollision(sa.left, sb, recorder, settings);

        // check for overlap of larger sphere's right subtree with smaller sphere
        boolean r_result = JCollisionSpheresSphere.computeCollision(sa.right, sb, recorder, settings);

        // return result
        return (l_result || r_result);
    }
}
