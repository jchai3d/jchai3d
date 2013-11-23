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

import org.jchai3d.graphics.JDraw3D;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 * JCollisionAABBox contains the properties and methods of an axis-aligned
 * bounding box, as used in the AABB collision detection algorithm.
 *
 * @author Chris Sewell (original author)
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (Java adaptation)
 */
public class JCollisionAABBBox {

    /**
     * The center of the bounding box.
     */
    JVector3d center;
    /**
     * The extent (half the width) of the bounding box.
     */
    JVector3d extent;
    /**
     * The minimum point (along each axis) of the bounding box.
     */
    JVector3d min;
    /**
     * The maximum point (along each axis) of the bounding box.
     */
    JVector3d max;

    /**
     * Default Constructor of cCollisionAABBBox.
     */
    public JCollisionAABBBox() {
        min = new JVector3d();
        max = new JVector3d();
        center = new JVector3d();
        extent = new JVector3d();
    }

    /*
     * Constructor of JCollisionAABBBox.
     */
    public JCollisionAABBBox(JVector3d min, JVector3d max) {
        setValue(min, max);
    }

    /**
     * Return the center of the bounding box.
     */
    public JVector3d getCenter() {
        return (center);
    }

    /**
     * Return the extent (half the width) of the bounding box.
     */
    public JVector3d getExtent() {
        return (extent);
    }

    /**
     * Set the center of the bounding box.
     */
    public void setCenter(JVector3d a_center) {
        center.copyFrom(a_center);
    }

    /**
     * Set the extent (half the width) of the bounding box.
     */
    public void setExtent(JVector3d a_extent) {
        extent.copyFrom(a_extent);
    }

    /**
     * Set the center and extent of the box based on two points.
     */
    public void setValue(JVector3d min, JVector3d max) {
        extent = JMaths.jMul(0.5, JMaths.jSub(max, min));
        center = JMaths.jAdd(min, extent);
        this.min.copyFrom(min);
        this.max.copyFrom(max);
        
    }

    /**
     *
     * Test whether this box contains the given point.
     *
     * @return Returns whether this box contains this point
     */
    public boolean contains(JVector3d point) {
        // check that each of the point's coordinates are within the box's range
        return (point.x > min.x
                && point.y > min.y
                && point.z > min.z
                && point.x < max.x
                && point.y < max.y
                && point.z < max.z);
    }

    /**
     * Set the bounding box to bound the two given bounding boxes.
     *
     * @param boxA The first bounding box to be enclosed.
     * @param boxB The other bounding box to be enclosed.
     */
    public void enclose(JCollisionAABBBox boxA, JCollisionAABBBox boxB) {
        // find the minimum coordinate along each axis
        JVector3d lower = new JVector3d(
                JMaths.jMin(boxA.getLowerX(), boxB.getLowerX()),
                JMaths.jMin(boxA.getLowerY(), boxB.getLowerY()),
                JMaths.jMin(boxA.getLowerZ(), boxB.getLowerZ()));

        // find the maximum coordinate along each axis
        JVector3d upper = new JVector3d(
                JMaths.jMax(boxA.getUpperX(), boxB.getUpperX()),
                JMaths.jMax(boxA.getUpperY(), boxB.getUpperY()),
                JMaths.jMax(boxA.getUpperZ(), boxB.getUpperZ()));

        // set the center and extent of this box to enclose the two extreme points
        setValue(lower, upper);
    }

    /**
     * Modify the bounding box as needed to bound the given point.
     *
     * @param a_point The point to be bounded.
     */
    public void enclose(JVector3d point) {
        // decrease coordinates as needed to include given point
        JVector3d lower = new JVector3d(JMaths.jMin(getLowerX(), point.x),
                JMaths.jMin(getLowerY(), point.y),
                JMaths.jMin(getLowerZ(), point.z));

        // increase coordinates as needed to include given point
        JVector3d upper = new JVector3d(JMaths.jMax(getUpperX(), point.x),
                JMaths.jMax(getUpperY(), point.y),
                JMaths.jMax(getUpperZ(), point.z));

        // set the center and extent of this box to enclose the given point
        setValue(lower, upper);
    }

    /**
     * Modify the bounding box to bound another box
     *
     * @param box The box to be bounded.
     */
    public void enclose(JCollisionAABBBox box) {
        enclose(this, box);
    }

    /**
     * Initialize a bounding box to center at origin and infinite extent.
     */
    public void setEmpty() {
        double CHAI_INFINITY = JConstants.CHAI_LARGE;
        center.zero();
        extent = new JVector3d(-CHAI_INFINITY, -CHAI_INFINITY, -CHAI_INFINITY);
        min.set(CHAI_INFINITY, CHAI_INFINITY, CHAI_INFINITY);
        max.set(-CHAI_INFINITY, -CHAI_INFINITY, -CHAI_INFINITY);
    }

    /**
     * Return the smallest coordinate along X axis.
     *
     * @return the smallest coordinate along X axis.
     */
    public double getLowerX() {
        return (min.x);
    }

    /**
     * Return the largest coordinate along X axis.
     *
     * @return the largest coordinate along X axis.
     */
    public double getUpperX() {
        return (max.x);
    }

    /**
     * Return the smallest coordinate along Y axis.
     *
     * @return the smallest coordinate along Y axis.
     */
    public double getLowerY() {
        return (min.y);
    }

    /**
     * Return the largest coordinate along Y axis.
     *
     * @return the largest coordinate along X axis.
     */
    public double getUpperY() {
        return (max.y);
    }

    /**
     * Return the smallest coordinate along Z axis.
     *
     * @return the smallest coordinate along Z axis.
     */
    public double getLowerZ() {
        return (min.z);
    }

    /**
     * Return the largest coordinate along Z axis.
     *
     * @return the largest coordinate along Z axis.
     */
    public double getUpperZ() {
        return (max.z);
    }

    /*
     * Return the length of the longest axis of the bounding box. //public
     * double size() {
     *
     * }
     */
    /**
     * Return the index of the longest axis of the bounding box.
     *
     * @return Return the index of the longest axis of the box.
     */
    public int longestAxis() {
        // if extent of x axis is greatest, return index 0
        if ((extent.x >= extent.y) && (extent.x >= extent.z)) {
            return 0;
        }
        
        // if extent of axis y is greatest, return index 1
        if ((extent.y >= extent.x) && (extent.y >= extent.z)) {
            return 1;
        }
        
        // return 2 otherwise
        return 2;
    }

    /**
     * Draw the edges of the bounding box.
     */
    public void render() {
        JDraw3D.jDrawWireBox(min.x, max.x, min.y, max.y, min.z, max.z);
    }
}
