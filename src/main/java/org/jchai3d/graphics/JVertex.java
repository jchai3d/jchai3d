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

import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * JVertex defines a point in 3 dimensional space and the associated
rendering properties (position, color, texture coordinate,
and surface normal)
 *
 * @author jairo
 */
public class JVertex {

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    //! Local position of this vertex.
    private JVector3d localPosition;
    //! Global position of this vertex in world coordinates.
    private JVector3d globalPosition;
    //! Surface normal.
    private JVector3d normal;
    //! Texture coordinate (uvw).
    private JVector3d texCoord;
    //! Color.
    private JColorf color;
    //! My index in the vertex list of the mesh that owns me.
    private int index;
    //! Is this vertex allocated?
    private boolean allocated;
    //! How many triangles use this vertex?
    private int trianglesCount;
    //! User data.
    private int tag;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    public JVertex(final double aX, final double aY, final double aZ) {
        localPosition = new JVector3d(aX, aY, aZ);
        globalPosition = new JVector3d(aX, aY, aZ);
        normal = new JVector3d(aX, aY, aZ);
        this.texCoord = new JVector3d();
        color = new JColorf();
        index = -1;
        allocated = false;
        trianglesCount = 0;
    }

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    public JVertex(JVector3d position, JVector3d textureCoord, JVector3d normal) {
        localPosition = position;
        globalPosition = position;
        this.normal = normal;
        this.texCoord = textureCoord;
        color = new JColorf();
        index = -1;
        allocated = false;
        trianglesCount = 0;
    }

    //-----------------------------------------------------------------------
    // MWTHODS:
    //-----------------------------------------------------------------------
    /**
     * Set the position coordinates of vertex.
     *
     * @param aX
     * @param aY
     * @param aZ
     */
    public void setPosition(final double aX, final double aY, final double aZ) {
        // set local position
        localPosition.set(aX, aY, aZ);
    }

    /**
     * Set local position of vertex.
     *
     * @param aPos
     */
    public void setPosition(final JVector3d aPos) {
        localPosition.copyFrom(aPos);
    }

    /**
     * Translate vertex by defining a translation passed as parameter.
     *
     * @param aTranslation
     */
    public void translate(final JVector3d aTranslation) {
        localPosition.add(aTranslation);
    }

    /**
     * Read local position of vertex.
     *
     * @return
     */
    public final JVector3d getPosition() {
        return (localPosition);
    }

    /**
     * Read global position. This value is only correct if the
    computeGlobalPositions() method is called from the parent world.
     *
     * @return
     */
    public final JVector3d getGlobalPosition() {
        return (globalPosition);
    }

    /**
     * Set normal vector of vertex.
     *
     * @param aNormal
     */
    public void setNormal(final JVector3d aNormal) {
        normal.copyFrom(aNormal);
    }

    /**
     *Set normal vector of vertex by passing its X,Y and Z components
    as parameters.
     *
     * @param aX
     * @param aY
     * @param aZ
     */
    public void setNormal(final double aX, final double aY, final double aZ) {
        if (normal == null) {
            normal = new JVector3d(aX, aY, aZ);
        } else {

            normal.set(aX, aY, aZ);
        }
    }

    /**
     * Set normal vector of vertex.
     *
     * @return
     */
    public final JVector3d getNormal() {
        return (normal);
    }

    /**
     * Set texture coordinate of vertex.
     *
     * @param aTexCoord
     */
    public void setTexCoord(final JVector3d aTexCoord) {
        texCoord.copyFrom(aTexCoord);
    }

    /**
     * Set texture coordinate by passing its coordinates as parameters.
     * @param aTx
     * @param aTy
     */
    public void setTexCoord(final double aTx, final double aTy) {
        texCoord.set(aTx, aTy, 0.0);
    }

    /**
     * Read texture coordinate of vertex.
     * @return
     */
    public final JVector3d getTexCoord() {
        return (texCoord);
    }

    /**
     * Set color of vertex.
     * @param aColor
     */
    public void setColor(final JColorf aColor) {
        color.copyFrom(aColor);
    }

    /**
     * Set color of vertex.
     * @param aRed
     * @param aGreen
     * @param aBlue
     * @param aAlpha
     */
    public void setColor(final float aRed, final float aGreen,
            final float aBlue, final float aAlpha) {
        color.set(aRed, aGreen, aBlue, aAlpha);
    }

    /**
     * Set color of vertex
     * @param aColor
     */
    public void setColor(final JColorb aColor) {
        setColor(aColor.getjColorf());
    }

    /**
     *Compute the global position of vertex given the global position
    and global rotation matrix of the parent object.
     */
    public void computeGlobalPosition(final JVector3d aGlobalPos, final JMatrix3d aGlobalRot) {
        aGlobalRot.mulr(localPosition, globalPosition);
        globalPosition.add(aGlobalPos);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int mIndex) {
        this.index = mIndex;
    }

    public boolean isAllocated() {
        return allocated;
    }

    public void setAllocated(boolean mAllocated) {
        this.allocated = mAllocated;
    }

    /**
     * @return the mNTriangles
     */
    public int getTriangleCount() {
        return trianglesCount;
    }

    /**
     * @param mNTriangles the mNTriangles to set
     */
    public void setTriangleCount(int mNTriangles) {
        this.trianglesCount = mNTriangles;
    }

    /**
     * @return the mLocalPos
     */
    public JVector3d getLocalPosition() {
        return localPosition;
    }

    /**
     * @param mLocalPos the mLocalPos to set
     */
    public void setLocalPosition(JVector3d mLocalPos) {
        this.localPosition.copyFrom(mLocalPos);
    }

    /**
     * @return the mColor
     */
    public JColorf getColor() {
        return color;
    }

}
