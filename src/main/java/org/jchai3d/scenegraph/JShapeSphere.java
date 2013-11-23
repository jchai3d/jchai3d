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
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import javax.media.opengl.glu.GLUquadric;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * Implementation of a virtual sphere shape.
 *
 * @author Francois Conti (original author)
 * @author Jairo Melo (java implementation)
 */
public class JShapeSphere extends JGenericShape {

    /**
     * radius of sphere.
     */
    protected double radius;

    /**
     * Constructor of cShapeSphere.
     */
    public JShapeSphere(double aRadius) {
        super();
        // initialize radius of sphere
        radius = JMaths.jAbs(aRadius);

        // set material properties
        material.setShininess(100);


        material.getAmbient().set(0.3f, 0.3f, 0.3f, 1.0f);
        material.getDiffuse().set(0.1f, 0.7f, 0.8f, 1.0f);
        material.getSpecular().set(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Render object in OpenGL.
     */
    public void render(JChaiRenderMode aRenderMode) {


        //-----------------------------------------------------------------------
        // Conditions for object to be rendered
        //-----------------------------------------------------------------------

        if (((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY)
                && (isTransparencyEnabled() == true))
                || ((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY)
                && (isTransparencyEnabled() == false))
                || ((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY)
                && (isTransparencyEnabled() == false))) {
            return;
        }

        //-----------------------------------------------------------------------
        // Rendering code here
        //-----------------------------------------------------------------------

        // render material properties
        if (materialEnabled) {
            material.render();
        }

        // allocate a new OpenGL quadric object for rendering a sphere

        GLUquadric sphere = glu.gluNewQuadric();

        // set rendering style
        glu.gluQuadricDrawStyle(sphere, GLU.GLU_FILL);

        // set normal-rendering mode
        glu.gluQuadricNormals(sphere, GLU.GLU_SMOOTH);

        // render texture property if defined
        if ((texture != null) && (textureMappingEnabled)) {
            texture.render();

            // generate texture coordinates
            glu.gluQuadricTexture(sphere, true);
        }

        // render a sphere
        glu.gluSphere(sphere, radius, 36, 36);

        // delete our quadric object
        glu.gluDeleteQuadric(sphere);

        // turn off texture rendering if it has been used
        GLContext.getCurrent().getGL().glDisable(GL.GL_TEXTURE_2D);
    }

    /**
     * Update bounding box of current object.
     */
    @Override
    public void updateBoundaryBox() {
        boundaryBoxMin.set(-radius, -radius, -radius);
        boundaryBoxMax.set(radius, radius, radius);
    }

    /**
     * Object scaling.
     */
    @Override
    public void scaleObject(JVector3d aScaleFactors) {
        radius = aScaleFactors.getX() * radius;
    }

    /**
     * Update the geometric relationship between the tool and the current object.
     */
    @Override
    public void computeLocalInteraction(JVector3d aToolPos, JVector3d aToolVel, int aIDN) {

        // compute distance from center of sphere to tool
        double distance = aToolPos.length();

        // from the position of the tool, search for the nearest point located
        // on the surface of the sphere
        if (distance > 0) {
            interactionProjectedPoint.copyFrom(JMaths.jMul(radius / distance, aToolPos));
        } else {
            interactionProjectedPoint.copyFrom(aToolPos);
        }

        // check if tool is located inside or outside of the sphere
        interactionInside = (distance <= radius);
    }

    /**
     * Set radius of sphere.
     */
    public void setRadius(double aRadius) {
        radius = JMaths.jAbs(aRadius);
        updateBoundaryBox();
    }

    /**
     * Get radius of sphere.
     */
    double getRadius() {
        return radius;
    }
}
