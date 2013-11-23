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
 *   version   1.0.0
 */
package org.jchai3d.graphics;

import java.util.ArrayList;
import org.jchai3d.collisions.JCollisionEvent;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JMesh;

/**
 *
 * @author Jairo Melo
 * @author Igor Gabriel
 */
public class JTriangle implements Comparable<JTriangle> {

    /**
     * Index number of vertex 0 (defines a location in my owning mesh's vertex
     * array)
     */
    protected int indexVertex0;
    /**
     * Index number of vertex 1 (defines a location in my owning mesh's vertex
     * array).
     */
    protected int indexVertex1;
    /**
     * Index number of vertex 2 (defines a location in my owning mesh's vertex
     * array).
     */
    protected int indexVertex2;
    /**
     * Index number of this triangle (defines a location in my owning mesh's
     * triangle array).
     */
    protected int index;
    /**
     * The mesh that owns me.
     */
    protected JMesh parentMesh;
    /**
     * Is this triangle still active?
     */
    protected boolean allocated;
    /**
     * For custom use. No specific purpose.
     */
    protected int tag;
    /**
     * A mesh can be organized into a network of neighboring triangles, which
     * are stored here...
     */
    public ArrayList<JTriangle> neighbors;

    /**
     * Constructor of JTriangle.
     *
     * @param aParent
     * @param aIndexVertex0
     * @param aIndexVertex1
     * @param aIndexVertex2
     */
    public JTriangle(JMesh aParent, final int aIndexVertex0,
            final int aIndexVertex1, final int aIndexVertex2) {
        indexVertex0 = aIndexVertex0;
        indexVertex1 = aIndexVertex1;
        indexVertex2 = aIndexVertex2;
        parentMesh = aParent;
        allocated = false;
        tag = 0;
        neighbors = new ArrayList<JTriangle>();
        index = 0;
    }

    /**
     * Default constructor of JTriangle.
     */
    public JTriangle() {
        indexVertex0 = 0;
        indexVertex1 = 0;
        indexVertex2 = 0;
        parentMesh = null;
        allocated = false;
        tag = 0;
        neighbors = new ArrayList<JTriangle>();
        index = 0;
    }

    /**
     * Set the vertices of the triangle by passing the index numbers of the
     * corresponding vertices.
     *
     * @param aIndexVertex0
     * @param aIndexVertex1
     * @param aIndexVertex2
     */
    public void setVertices(final int aIndexVertex0,
            final int aIndexVertex1,
            final int aIndexVertex2) {
        setIndexVertex0(aIndexVertex0);
        setIndexVertex1(aIndexVertex1);
        setIndexVertex2(aIndexVertex2);
    }

    /**
     * Read pointer to vertex 0 of triangle.
     *
     * @return
     */
    public final JVertex getVertex0() {
        // Where does the vertex array live?
        return parentMesh.getVertices().get(indexVertex0);
    }

    /**
     * Read pointer to vertex 1 of triangle.
     *
     * @return
     */
    public final JVertex getVertex1() {
        // Where does the vertex array live?
        return parentMesh.getVertices().get(indexVertex1);
    }

    /**
     * Read pointer to vertex 2 of triangle.
     *
     * @return
     */
    public final JVertex getVertex2() {
        // Where does the vertex array live?
        return parentMesh.getVertices().get(indexVertex2);
    }

    /**
     * Access a pointer to the specified vertex of this triangle.
     *
     * @param vi
     * @return
     */
    public final JVertex getVertex(int vi) {
        // Where does the vertex array live?

        switch (vi) {
            case 0:
                return parentMesh.getVertices().get(indexVertex0);
            case 1:
                return parentMesh.getVertices().get(indexVertex1);
            case 2:
                return parentMesh.getVertices().get(indexVertex2);
            default:
                return null;
        }
    }

    /**
     * Access the index of the specified vertex of this triangle.
     *
     * @param vi
     * @return
     */
    public final int getVertexIndex(int vi) {
        switch (vi) {
            case 0:
                return indexVertex0;
            case 1:
                return indexVertex1;
            case 2:
                return indexVertex2;
        }
        return 0;
    }

    /**
     * Read index number of vertex 0 (defines a location in my owning mesh's
     * vertex array)
     *
     * @return
     */
    public final int getIndexVertex0() {
        return (indexVertex0);
    }

    /**
     * Read index number of vertex 1 (defines a location in my owning mesh's
     * vertex array).
     *
     * @return
     */
    public final int getIndexVertex1() {
        return (indexVertex1);
    }

    /**
     * Read index number of vertex 2 (defines a location in my owning mesh's
     * vertex array)
     *
     * @return
     */
    public final int getIndexVertex2() {
        return (indexVertex2);
    }

    /**
     * Read the index of this triangle (defines a location in my owning mesh's
     * triangle array).
     *
     * @return
     */
    public final int getIndex() {
        return (index);
    }

    /**
     * Retrieve a pointer to the mesh that owns this triangle.
     *
     * @return
     */
    public final JMesh getParent() {
        return (parentMesh);
    }

    /**
     * Set pointer to mesh parent of triangle.
     *
     * @param parent
     */
    public void setParent(JMesh parent) {
        parentMesh = parent;
    }

    /**
     * Is this triangle allocated to an existing mesh?
     *
     * @return
     */
    public boolean isAllocated() {
        return (allocated);
    }

    /**
     * Check if a ray intersects this triangle. The ray is described by its
     * origin (\e a_origin) and its direction (\e a_direction). \n
     *
     * If a collision occurs, this information is stored in the collision
     * recorder \e a_recorder. \n
     *
     * This is one of the most performance-critical routines in CHAI, so we have
     * code here for a couple different approaches that may become useful in
     * different scenarios.
     *
     * @param aSegmentPointA
     * @param aSegmentPointB
     * @param a_recorder
     * @param aSettings
     * @return
     */
    public final boolean computeCollision(JVector3d aSegmentPointA,
            JVector3d aSegmentPointB,
            JCollisionRecorder aRecorder,
            JCollisionSettings aSettings) {


        // temp variables
        boolean hit = false;
        JVector3d collisionPoint = new JVector3d();
        JVector3d collisionNormal = new JVector3d();
        double collisionDistanceSq = JConstants.CHAI_LARGE;

        // Get the position of the triangle's vertices
        JVector3d vertex0 = parentMesh.getVertex(indexVertex0, true).getPosition();
        JVector3d vertex1 = parentMesh.getVertex(indexVertex1, true).getPosition();
        JVector3d vertex2 = parentMesh.getVertex(indexVertex2, true).getPosition();

        // If getCollisionRadius() == 0, we search for a possible intersection between
        // the segment AB and the triangle defined by its three vertices V0, V1, V2.
        if (aSettings.getCollisionRadius() == 0) {
            // check for collision between segment and triangle only
            if (JMaths.jIntersectionSegmentTriangle(aSegmentPointA,
                    aSegmentPointB,
                    vertex0,
                    vertex1,
                    vertex2,
                    collisionPoint,
                    collisionNormal)) {
                hit = true;
                collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, collisionPoint);
            }
        } // If getCollisionRadius() > 0, we search for a possible intersection between
        // the segment AB and the shell of the current triangle which is described
        // by its three vertices and getCollisionRadius().
        else {
            JVector3d t_collisionPoint = new JVector3d(), t_collisionNormal = new JVector3d();
            double t_collisionDistanceSq;
            JVector3d normal = JMaths.jComputeSurfaceNormal(vertex0, vertex1, vertex2);

            JVector3d offset = new JVector3d();
            normal.mulr(aSettings.getCollisionRadius(), offset);
            JVector3d tVertex0 = new JVector3d(), tVertex1 = new JVector3d(), tVertex2 = new JVector3d();

            // check for collision between segment and triangle upper shell
            vertex0.addr(offset, tVertex0);
            vertex1.addr(offset, tVertex1);
            vertex2.addr(offset, tVertex2);
            if (JMaths.jIntersectionSegmentTriangle(aSegmentPointA,
                    aSegmentPointB,
                    tVertex0,
                    tVertex1,
                    tVertex2,
                    collisionPoint,
                    collisionNormal)) {
                hit = true;
                collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, collisionPoint);
            }

            // check for collision between segment and triangle lower shell
            vertex0.subr(offset, tVertex0);
            vertex1.subr(offset, tVertex1);
            vertex2.subr(offset, tVertex2);
            if (JMaths.jIntersectionSegmentTriangle(aSegmentPointA,
                    aSegmentPointB,
                    tVertex0,
                    tVertex1,
                    tVertex2,
                    t_collisionPoint,
                    t_collisionNormal)) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between sphere located at vertex 0
            JVector3d t_p = new JVector3d(), t_n = new JVector3d();
            if (JMaths.jIntersectionSegmentSphere(aSegmentPointA,
                    aSegmentPointB,
                    vertex0,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between sphere located at vertex 1
            if (JMaths.jIntersectionSegmentSphere(aSegmentPointA,
                    aSegmentPointB,
                    vertex1,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between sphere located at vertex 2
            if (JMaths.jIntersectionSegmentSphere(aSegmentPointA,
                    aSegmentPointB,
                    vertex2,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between segment and triangle edge01 shell
            if (JMaths.jIntersectionSegmentToplessCylinder(aSegmentPointA,
                    aSegmentPointB,
                    vertex0,
                    vertex1,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between segment and triangle edge02 shell
            if (JMaths.jIntersectionSegmentToplessCylinder(aSegmentPointA,
                    aSegmentPointB,
                    vertex0,
                    vertex2,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }

            // check for collision between segment and triangle edge12 shell
            if (JMaths.jIntersectionSegmentToplessCylinder(aSegmentPointA,
                    aSegmentPointB,
                    vertex1,
                    vertex2,
                    aSettings.getCollisionRadius(),
                    t_collisionPoint,
                    t_collisionNormal,
                    t_p,
                    t_n) > 0) {
                hit = true;
                t_collisionDistanceSq = JMaths.jDistanceSq(aSegmentPointA, t_collisionPoint);
                if (t_collisionDistanceSq < collisionDistanceSq) {
                    collisionPoint.copyFrom(t_collisionPoint);
                    collisionNormal.copyFrom(t_collisionNormal);
                    collisionDistanceSq = t_collisionDistanceSq;
                }
            }
        }

        // report collision
        if (hit) {
            // before reporting the new collision, we need to check if
            // the collision settings require us to verify the side of the
            // triangle which has been hit.
            boolean hit_confirmed;
            if (aSettings.isCheckBothSidesOfTriangles()) {
                // settings specify that a collision can occur on both sides
                // of the triangle, so the new collision is reported.
                hit_confirmed = true;
            } else {
                // we need check on which side of the triangle the collision occurred
                // and see it needs to be reported.
                JVector3d segmentAB = new JVector3d();
                aSegmentPointB.subr(aSegmentPointA, segmentAB);

                JVector3d v01 = new JVector3d(), v02 = new JVector3d(), triangleNormal = new JVector3d();
                vertex2.subr(vertex0, v02);
                vertex1.subr(vertex0, v01);
                v01.crossr(v02, triangleNormal);

                double value = JMaths.jCosAngle(segmentAB, triangleNormal);
                if (value <= 0.0) {
                    hit_confirmed = true;
                } else {
                    hit_confirmed = false;
                }
            }


            // here we finally report the new collision to the collision event handler.
            if (hit_confirmed) {
                // we verify if anew collision needs to be created or if we simply
                // need to update the nearest collision.
                if (aSettings.isCheckForNearestCollisionOnly()) {
                    // no new collision event is create. We just check if we need
                    // to update the nearest collision
                    if (collisionDistanceSq < aRecorder.getNearestCollision().getSquareDistance()) {
                        // report basic collision data
                        aRecorder.getNearestCollision().setObject(parentMesh);
                        aRecorder.getNearestCollision().setTriangle(this);
                        aRecorder.getNearestCollision().setLocalPosition(collisionPoint);
                        aRecorder.getNearestCollision().setLocalNormal(collisionNormal);
                        aRecorder.getNearestCollision().setSquareDistance(collisionDistanceSq);
                        aRecorder.getNearestCollision().setAdjustedSegmentAPoint(aSegmentPointA);

                        // report advanced collision data
                        if (!aSettings.isReturnMinimalCollisionData()) {
                            aRecorder.getNearestCollision().setGlobalPosition(JMaths.jAdd(parentMesh.getGlobalPosition(), JMaths.jMul(parentMesh.getGlobalRotation(), aRecorder.getNearestCollision().getLocalPosition())));
                            aRecorder.getNearestCollision().setGlobalNormal(JMaths.jMul(parentMesh.getGlobalRotation(), aRecorder.getNearestCollision().getLocalNormal()));
                        }

                    }
                } else {
                    JCollisionEvent newCollisionEvent = new JCollisionEvent();

                    // report basic collision data
                    newCollisionEvent.setObject(parentMesh);
                    newCollisionEvent.setTriangle(this);
                    newCollisionEvent.setLocalPosition(collisionPoint);
                    newCollisionEvent.setLocalNormal(collisionNormal);
                    newCollisionEvent.setSquareDistance(collisionDistanceSq);
                    newCollisionEvent.setAdjustedSegmentAPoint(aSegmentPointA);

                    // report advanced collision data
                    if (!aSettings.isReturnMinimalCollisionData()) {
                        newCollisionEvent.setGlobalPosition(JMaths.jAdd(parentMesh.getGlobalPosition(), JMaths.jMul(parentMesh.getGlobalRotation(), newCollisionEvent.getLocalPosition())));
                        newCollisionEvent.setGlobalNormal(JMaths.jMul(parentMesh.getGlobalRotation(), newCollisionEvent.getLocalNormal()));
                    }

                    // add new collision even to collision list
                    aRecorder.getCollisions().add(newCollisionEvent);

                    // check if this new collision is a candidate for "nearest one"
                    if (collisionDistanceSq < aRecorder.getNearestCollision().getSquareDistance()) {
                        aRecorder.getNearestCollision().copyFrom(newCollisionEvent);
                    }
                }
            }

            // return result
            return (hit_confirmed);
        } else {
            return (false);
        }

        //return false;
    }

    /**
     * Compute and return the area of this triangle.
     *
     * @return
     */
    public double computeArea() {
        // A = 0.5 * | u x v |
        JVector3d u = JMaths.jSub(getVertex(1).getPosition(), getVertex(0).getPosition());
        JVector3d v = JMaths.jSub(getVertex(2).getPosition(), getVertex(0).getPosition());
        return (0.5 * (JMaths.jCross(u, v).length()));
    }

    /**
     * @param mAllocated the mAllocated to set
     */
    public void setAllocated(boolean mAllocated) {
        this.allocated = mAllocated;
    }

    /**
     * @param mIndex the mIndex to set
     */
    public void setIndex(int mIndex) {
        this.index = mIndex;
    }

    @Override
    public int compareTo(JTriangle aTriangle) {

        sortTriangle(aTriangle);

        // Do the comparison
        if (indexVertex0 < aTriangle.getIndexVertex0()) {
            return -1;
        } else if (indexVertex0 > aTriangle.getIndexVertex0()) {
            return 1;
        } else if (indexVertex1 < aTriangle.getIndexVertex1()) {
            return -1;
        } else if (indexVertex1 > aTriangle.getIndexVertex1()) {
            return 1;
        } else if (indexVertex2 < aTriangle.getIndexVertex2()) {
            return -1;
        }
        return 0;
    }

    public static void sortTriangle(JTriangle t) {
        int tmp;
        if (t.getIndexVertex0() > t.getIndexVertex1()) {
            tmp = t.getIndexVertex1();
            t.setIndexVertex1(t.getIndexVertex2());
            t.setIndexVertex1(tmp);
        }
        if (t.getIndexVertex0() > t.getIndexVertex2()) {
            tmp = t.getIndexVertex2();
            t.setIndexVertex2(t.getIndexVertex1());
            t.setIndexVertex1(tmp);
        } else {
            return;
        }
        if (t.getIndexVertex0() > t.getIndexVertex1()) {
            tmp = t.getIndexVertex1();
            t.setIndexVertex1(t.getIndexVertex0());
            t.setIndexVertex1(tmp);
        }
    }

    /**
     * @param mIndexVertex0 the mIndexVertex0 to set
     */
    public void setIndexVertex(int vertex, int vertexIndex) {
        switch (vertex) {
            case 0:
                indexVertex0 = vertexIndex;
                break;
            case 1:
                indexVertex1 = vertexIndex;
                break;
            case 2:
                indexVertex2 = vertexIndex;
                break;
        }
    }

    /**
     * @param mIndexVertex0 the mIndexVertex0 to set
     */
    public void setIndexVertex0(int mIndexVertex0) {
        this.indexVertex0 = mIndexVertex0;
    }

    /**
     * @param mIndexVertex1 the mIndexVertex1 to set
     */
    public void setIndexVertex1(int mIndexVertex1) {
        this.indexVertex1 = mIndexVertex1;
    }

    /**
     * @param mIndexVertex2 the mIndexVertex2 to set
     */
    public void setIndexVertex2(int mIndexVertex2) {
        this.indexVertex2 = mIndexVertex2;
    }

    /**
     * @return the mTag
     */
    public int getTag() {
        return tag;
    }

    /**
     * @param mTag the mTag to set
     */
    public void setTag(int mTag) {
        this.tag = mTag;
    }

    /**
     * @return the neighbors
     */
    public ArrayList<JTriangle> getNeighbors() {
        return neighbors;
    }

    /**
     * @param neighbors the neighbors to set
     */
    public void setNeighbors(ArrayList<JTriangle> neighbors) {
        this.neighbors = neighbors;
    }
}
