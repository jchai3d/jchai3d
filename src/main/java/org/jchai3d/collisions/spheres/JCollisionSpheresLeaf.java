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
public class JCollisionSpheresLeaf extends JCollisionSpheresSphere {

    protected JCollisionSpheresGenericShape primitive;

    //! Constructor of cCollisionSpheresLeaf.
    public JCollisionSpheresLeaf(JCollisionSpheresGenericShape prim) {
        this(prim, null);
    }

    /**
     * Constructor of cCollisionSpheresLeaf.
     *
     * @param prim Pointer to a shape primitive to be enclosed by the new sphere
     * leaf node.
     * @param parent Pointer to the parent of this node in sphere tree.
     */
    public JCollisionSpheresLeaf(JCollisionSpheresGenericShape prim, JCollisionSpheresSphere parent) {
        super(parent);
        // set this node's primitive pointer to the received shape primitive
        assert (prim != null);
        this.primitive = prim;

        // set the primitive's sphere pointer to this node
        primitive.setSphere(this);

        // set the center and radius of the bounding sphere to enclose the primitive
        radius = prim.getRadius();
        center = prim.getCenter();

        // set this node's parent pointer
        this.parent = parent;

    }

    public JCollisionSpheresLeaf(JTriangle tri) {
        this(tri, null, 0);
    }

    /**
     * Constructor of cCollisionSpheresLeaf.
     *
     * @param tri Pointer to triangle to be enclosed by new sphere leaf.
     * @param parent Pointer to the parent of this node in the sphere tree.
     * @param extendedRadius Bounding radius.
     */
    public JCollisionSpheresLeaf(JTriangle tri, JCollisionSpheresSphere parent, double extendedRadius) {
        // create cCollisionSpheresPoint primitive object for first point
        JVector3d vpos0 = tri.getVertex0().getPosition();
        JVector3d vpos1 = tri.getVertex1().getPosition();
        JVector3d vpos2 = tri.getVertex2().getPosition();

        JCollisionSpheresTri t = new JCollisionSpheresTri(vpos0,
                vpos1,
                vpos2,
                extendedRadius);

        // set pointers
        t.setOriginal(tri);
        primitive = t;
        primitive.setSphere(this);
        this.parent = parent;

        // set the center and radius of the bounding sphere to enclose the primitive
        radius = primitive.getRadius();
        center = primitive.getCenter();
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

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
    }

    /**
     * Determine whether there is any intersection between the primitives (line
     * and triangle) of the two given collision spheres by calling the
     * primitive's collision detection method.
     *
     *
     * @param sa One sphere tree leaf to check for collision.
     * @param sb Other sphere tree leaf to check for collision.
     * @param recorder Stores all collision events
     * @param settings Contains collision settings information.
     */
    public static boolean computeCollision(JCollisionSpheresLeaf sa,
            JCollisionSpheresLeaf sb,
            JCollisionRecorder recorder,
            JCollisionSettings settings) {
        // check for a collision between the primitives (one a triangle and the
        // other a line segment, we assume) of these two leafs; it will only
        // return true if the distance between the segment origin and the
        // triangle is less than the current closest intersecting triangle
        // (whose distance squared is kept in colSquareDistance)
        return sa.primitive.computeCollision(sb.primitive,
                recorder,
                settings);
    }
}
