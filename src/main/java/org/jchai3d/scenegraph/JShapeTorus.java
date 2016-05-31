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

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GLContext;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * JShapeTorus describes a simple torus potential field.
 *
 * @author Francois Conti (original author)
 * @author Jairo Melo (java implementation)
 */
public class JShapeTorus extends JGenericShape {

    /**
     * Inside radius of torus.
     */
    protected double innerRadius;
    /**
     * Outside radius of torus.
     */
    protected double outerRadius;
    /**
     * Resolution of the GLU graphical model.
     */
    protected int resolution;

    /**
     * Creates a new torus with the given inside and outside radius, with
     * resolution set as 64.
     *
     * @param insideRadius the inside radius of this torus
     * @param outsideRadius the outside radius of this torus
     */
    public JShapeTorus(double insideRadius, double outisideRadius) {
        this(insideRadius, outisideRadius, 64);
    }

    /**
     * Creates a new torus with the given resolution,inside and outside radius
     *
     * @param insideRadius the inside radius of this torus
     * @param outsideRadius the outside radius of this torus
     * @param resolution the resolution of this torus
     */
    public JShapeTorus(double insideRadius, double outsideRadius, int resolution) {
        super();
        // initialize radius of sphere
        setSize(insideRadius, outsideRadius);

        // resolution of the graphical model
        this.resolution = resolution;

        // set material properties
        material.setShininess(100);


        material.getAmbient().set(0.3f, 0.3f, 0.3f, 1.0f);
        material.getDiffuse().set(0.7f, 0.7f, 0.7f, 1.0f);
        material.getSpecular().set(1.0f, 1.0f, 1.0f, 1.0f);
        material.setStiffness(100.0);
    }

    /**
     * Render object in OpenGL.
     *
     * @param insideRadius the inside radius of this torus
     * @param outsideRadius the outside radius of this torus
     */
    public void render(JChaiRenderMode aRenderMode) {

        if (((aRenderMode
                == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY)
                && (isTransparencyEnabled() == true)) || ((aRenderMode
                == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY)
                && (isTransparencyEnabled() == false)) || ((aRenderMode
                == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY)
                && (isTransparencyEnabled() == false))) {
            return;
        }

        // render material properties
        if (materialEnabled) {
            material.render();
        }


        // render texture property if defined
        if ((texture != null)
                && (textureMappingEnabled)) {
            texture.render();
        }
        // draw sphere
        glut.glutSolidTorus(innerRadius, outerRadius, resolution,
                resolution);

        // turn off texture rendering if it has been used
        GLContext.getCurrent().getGL().glDisable(GL.GL_TEXTURE_2D);
    }

    /**
     * Update bounding box of current object.
     */
    public void updateBoundaryBox() {
        double a = (outerRadius + innerRadius);
        boundaryBoxMin.set(-outerRadius, -outerRadius, -(outerRadius
                - innerRadius));
        boundaryBoxMax.set(outerRadius, outerRadius,
                (outerRadius - innerRadius));
        boundaryBoxMin.set(-a, -a, -innerRadius);
        boundaryBoxMax.set(a, a, innerRadius);
    }

    /**
     * object scaling.
     */
    public void scaleObject(JVector3d aScaleFactors) {
        outerRadius = aScaleFactors.getX() * outerRadius;
        innerRadius =
                aScaleFactors.getX() * innerRadius;
    }

    /**
     * Update the geometric relationship between the tool and the current
     * object.
     */
    public void computeLocalInteraction(JVector3d aToolPos, JVector3d aToolVel, int aIDN) {
        JVector3d toolProjection = aToolPos;
        toolProjection.setZ(0);

        // search for the nearest point on the torus medial axis
        if (aToolPos.lengthsq() > JConstants.CHAI_SMALL) {
            JVector3d pointAxisTorus = JMaths.jMul(outerRadius, JMaths.jNormalize(toolProjection));

            // compute eventual penetration of tool inside the torus 
            JVector3d vectTorusTool = JMaths.jSub(aToolPos, pointAxisTorus);

            double distance = vectTorusTool.length();

            // tool is located inside or outside the torus?
            interactionInside =
                    ((distance < innerRadius) && (distance > 0.001));

            // compute surface point 
            double dist = vectTorusTool.length();
            if (dist
                    > 0) {
                vectTorusTool.mul(1 / dist);
            }
            vectTorusTool.mul(innerRadius);
            pointAxisTorus.addr(vectTorusTool, interactionProjectedPoint);
        } else {
            interactionInside = (false);
            interactionProjectedPoint.copyFrom(aToolPos);
        }
    }

    /**
     * Set inside and outside radius of torus.
     */
    public void setSize(double aInnerRadius, double aOuterRadius) {
        innerRadius =
                JMaths.jAbs(aInnerRadius);
        outerRadius = JMaths.jAbs(aOuterRadius);
    }

    /**
     * Get inside radius of torus.
     */
    public double getInnerRadius() {
        return (innerRadius);
    }

    /**
     * Get inside radius of torus.
     */
    public double getOuterRadius() {
        return (outerRadius);
    }
}
