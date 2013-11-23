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
 */
package org.jchai3d.scenegraph;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * JShapeLine describes a simple line potential field
 *
 * @author Francois Conti (original author)
 * @author Jairo Melo (java implementation)
 */
public class JShapeLine extends JGenericObject {

    /**
     * Point A of line.
     */
    protected JVector3d pointA;
    /**
     * Point A of line.
     */
    protected JVector3d pointB;
    /**
     * Color of point A of line.
     */
    protected JColorf pointAColor;
    /**
     * Color of point B of line.
     */
    protected JColorf pointBColor;

    /**
     * <p>Default constructor of JShapeLine.</p> <p>Both points are located in
     * the origin.</p>
     */
    public JShapeLine() {
        // initialize line with start and end points.
        pointA = new JVector3d();
        pointB = new JVector3d();

        // set color properties
        pointAColor = new JColorf(1.0f, 1.0f, 1.0f, 1.0f);
        pointBColor = new JColorf(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Constructor of JShapeLine
     *
     * @param aPointA
     * @param aPointB
     */
    public JShapeLine(JVector3d aPointA, JVector3d aPointB) {
        this();
        // initialize line with start and end points.
        pointA.copyFrom(aPointA);
        pointB.copyFrom(aPointB);
    }

    /**
     * Render sphere in OpenGL
     *
     * @see JGenericObject#render(org.jchai3d.scenegraph.JChaiRenderMode)
     */
    @Override
    public void render(JChaiRenderMode renderMode) {
        //-----------------------------------------------------------------------
        // Conditions for object to be rendered
        //-----------------------------------------------------------------------
        if ((renderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY)
                || (renderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY)) {
            return;
        }


        //-----------------------------------------------------------------------
        // Rendering code here
        //-----------------------------------------------------------------------

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        gl.glDisable(GL2.GL_LIGHTING);

        // draw line
        gl.glBegin(GL2.GL_LINES);
        gl.glColor4fv(getPointAColor().getComponents(), 0);
        gl.glVertex3dv(new double[]{getPointA().getX(), getPointA().getY(), getPointA().getZ()}, 0);
        gl.glColor4fv(getPointBColor().getComponents(), 0);
        gl.glVertex3dv(new double[]{getPointB().getX(), getPointB().getY(), getPointB().getZ()}, 0);
        gl.glEnd();

        gl.glEnable(GL2.GL_LIGHTING);
    }

    /**
     * Update bounding box of current object.
     */
    @Override
    public void updateBoundaryBox() {
        boundaryBoxMin.set(
                JMaths.jMin(getPointA().getX(), getPointB().getX()),
                JMaths.jMin(getPointA().getY(), getPointB().getY()),
                JMaths.jMin(getPointA().getZ(), getPointB().getZ()));

        boundaryBoxMax.set(
                JMaths.jMax(getPointA().getX(), getPointB().getX()),
                JMaths.jMax(getPointA().getY(), getPointB().getY()),
                JMaths.jMax(getPointA().getZ(), getPointB().getZ()));
    }

    /**
     * Object scaling.
     */
    @Override
    public void scaleObject(JVector3d aScaleFactors) {
        getPointA().setX(aScaleFactors.getX() * getPointA().getX());
        getPointA().setY(aScaleFactors.getY() * getPointA().getY());
        getPointA().setZ(aScaleFactors.getZ() * getPointA().getZ());

        getPointB().setX(aScaleFactors.getX() * getPointA().getX());
        getPointB().setY(aScaleFactors.getY() * getPointA().getY());
        getPointB().setZ(aScaleFactors.getZ() * getPointA().getZ());
    }

    /**
     * From the position of the tool, search for the nearest point located at
     * the surface of the current object. Decide if the point is located inside
     * or outside of the object
     *
     * @param toolPosition Position of the tool.
     * @param toolVelocity Velocity of the tool.
     * @param idn Identification number of the force algorithm.
     *
     */
    @Override
    public void computeLocalInteraction(JVector3d toolPosition, JVector3d toolVelocity, int idn) {
        // the tool can never be inside the line
        interactionInside = (false);

        // if both point are equal
        interactionProjectedPoint.copyFrom(JMaths.jProjectPointOnSegment(toolPosition, getPointA(), getPointB()));
    }

    /**
     * Sets both points of this line
     *
     * @param posA the point A
     * @param posB the point B
     */
    public void setPoints(JVector3d posA, JVector3d posB) {
        pointA.copyFrom(posA);
        pointB.copyFrom(posB);
    }

    /**
     * @return the pointA
     */
    public JVector3d getPointA() {
        return pointA;
    }

    /**
     * @param pointA the pointA to set
     */
    public void setPointA(JVector3d pointA) {
        this.pointA = pointA;
    }

    /**
     * @return the pointB
     */
    public JVector3d getPointB() {
        return pointB;
    }

    /**
     * @param pointB the pointB to set
     */
    public void setPointB(JVector3d pointB) {
        this.pointB = pointB;
    }

    /**
     * @return the pointAColor
     */
    public JColorf getPointAColor() {
        return pointAColor;
    }

    /**
     * @param pointAColor the pointAColor to set
     */
    public void setPointAColor(JColorf pointAColor) {
        this.pointAColor = pointAColor;
    }

    /**
     * @return the pointBColor
     */
    public JColorf getPointBColor() {
        return pointBColor;
    }

    /**
     * @param pointBColor the pointBColor to set
     */
    public void setPointBColor(JColorf pointBColor) {
        this.pointBColor = pointBColor;
    }
}
