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

import java.nio.FloatBuffer;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.glu.GLU;
import com.jogamp.opengl.glu.GLUquadric;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * Drawing Macros.
 * 
 * @author jairo
 */
public class JDraw3D {

    private static GLU glu;
    public static final float ARROW_CYLINDER_PORTION = 0.75f;
    public static final float ARRROW_CONE_PORTION = (1.0f - 0.75f);

    /**
     * Draw an X-Y-Z Frame. The red arrow corresponds to the X-Axis,
    green to the Y-Axis, and blue to the Z-Axis. \n

    The scale parameter determines the size of the arrows.
     * @param aScale
     * @param aModifyMaterialState
     */
    public static void jDrawFrame(final double aScale,
            final boolean aModifyMaterialState) {
        jDrawFrame(aScale, aScale, aModifyMaterialState);
    }
    private static FloatBuffer triVerticesBuffer;
    private static FloatBuffer triNormalsBuffer;
    private static FloatBuffer quadVerticesBuffer;
    private static FloatBuffer quadNormalsBuffer;

    static {

        float[] triangle_vertices = {
            0.000000f, 0.040000f, -0.800000f, 0.028284f, 0.028284f, -0.800000f,
            0.000000f, 0.000000f, -1.000000f, 0.028284f, 0.028284f, -0.800000f,
            0.040000f, 0.000000f, -0.800000f, 0.000000f, 0.000000f, -1.000000f,
            0.040000f, 0.000000f, -0.800000f, 0.028284f, -0.028284f, -0.800000f,
            0.000000f, 0.000000f, -1.000000f, 0.028284f, -0.028284f, -0.800000f,
            0.000000f, -0.040000f, -0.800000f, 0.000000f, 0.000000f, -1.000000f,
            0.000000f, -0.040000f, -0.800000f, -0.028284f, -0.028284f, -0.800000f,
            0.000000f, 0.000000f, -1.000000f, -0.028284f, -0.028284f, -0.800000f,
            -0.040000f, 0.000000f, -0.800000f, 0.000000f, 0.000000f, -1.000000f,
            -0.040000f, 0.000000f, -0.800000f, -0.028284f, 0.028284f, -0.800000f,
            0.000000f, 0.000000f, -1.000000f, -0.028284f, 0.028284f, -0.800000f,
            0.000000f, 0.040000f, -0.800000f, 0.000000f, 0.000000f, -1.000000f
        };

        triVerticesBuffer = FloatBuffer.allocate(triangle_vertices.length);
        triVerticesBuffer.put(triangle_vertices);
        triVerticesBuffer.rewind();

        // Triangle normals:
        float[] triangle_normals = {
            0.000000f, 0.980581f, -0.196116f, 0.693375f, 0.693375f, -0.196116f,
            0.357407f, 0.862856f, -0.357407f, 0.693375f, 0.693375f, -0.196116f,
            0.980581f, 0.000000f, -0.196116f, 0.862856f, 0.357407f, -0.357407f,
            0.980581f, 0.000000f, -0.196116f, 0.693375f, -0.693375f, -0.196116f,
            0.862856f, -0.357407f, -0.357407f, 0.693375f, -0.693375f, -0.196116f,
            0.000000f, -0.980581f, -0.196116f, 0.357407f, -0.862856f, -0.357407f,
            0.000000f, -0.980581f, -0.196116f, -0.693375f, -0.693375f, -0.196116f,
            -0.357407f, -0.862856f, -0.357407f, -0.693375f, -0.693375f, -0.196116f,
            -0.980581f, 0.000000f, -0.196116f, -0.862856f, -0.357407f, -0.357407f,
            -0.980581f, 0.000000f, -0.196116f, -0.693375f, 0.693375f, -0.196116f,
            -0.862856f, 0.357407f, -0.357407f, -0.693375f, 0.693375f, -0.196116f,
            0.000000f, 0.980581f, -0.196116f, -0.357407f, 0.862856f, -0.357407f
        };

        triNormalsBuffer = FloatBuffer.allocate(triangle_normals.length);
        triNormalsBuffer.put(triangle_normals);
        triNormalsBuffer.rewind();





        float[] quad_vertices = {
            0.000000f, 0.010000f, 0.000000f, 0.007000f, 0.007000f, 0.000000f,
            0.007000f, 0.007000f, -0.800000f, 0.000000f, 0.010000f, -0.800000f,
            0.000000f, -0.010000f, 0.000000f, -0.007000f, -0.007000f, 0.000000f,
            -0.007000f, -0.007000f, -0.800000f, 0.000000f, -0.010000f, -0.800000f,
            -0.007000f, -0.007000f, 0.000000f, -0.010000f, 0.000000f, 0.000000f,
            -0.010000f, 0.000000f, -0.800000f, -0.007000f, -0.007000f, -0.800000f,
            -0.010000f, 0.000000f, 0.000000f, -0.007000f, 0.007000f, 0.000000f,
            -0.007000f, 0.007000f, -0.800000f, -0.010000f, 0.000000f, -0.800000f,
            -0.007000f, 0.007000f, 0.000000f, 0.000000f, 0.010000f, 0.000000f,
            0.000000f, 0.010000f, -0.800000f, -0.007000f, 0.007000f, -0.800000f,
            0.007000f, 0.007000f, 0.000000f, 0.010000f, 0.000000f, 0.000000f,
            0.010000f, 0.000000f, -0.800000f, 0.007000f, 0.007000f, -0.800000f,
            0.010000f, 0.000000f, 0.000000f, 0.007000f, -0.007000f, 0.000000f,
            0.007000f, -0.007000f, -0.800000f, 0.010000f, 0.000000f, -0.800000f,
            0.007000f, -0.007000f, 0.000000f, 0.000000f, -0.010000f, 0.000000f,
            0.000000f, -0.010000f, -0.800000f, 0.007000f, -0.007000f, -0.800000f,
            -0.007000f, 0.007000f, -0.800000f, -0.028284f, 0.028284f, -0.800000f,
            -0.040000f, 0.000000f, -0.800000f, -0.010000f, 0.000000f, -0.800000f,
            -0.010000f, 0.000000f, -0.800000f, -0.040000f, 0.000000f, -0.800000f,
            -0.028284f, -0.028284f, -0.800000f, -0.007000f, -0.007000f, -0.800000f,
            -0.007000f, -0.007000f, -0.800000f, -0.028284f, -0.028284f, -0.800000f,
            0.000000f, -0.040000f, -0.800000f, 0.000000f, -0.010000f, -0.800000f,
            0.000000f, -0.010000f, -0.800000f, 0.000000f, -0.040000f, -0.800000f,
            0.028284f, -0.028284f, -0.800000f, 0.007000f, -0.007000f, -0.800000f,
            0.028284f, -0.028284f, -0.800000f, 0.040000f, 0.000000f, -0.800000f,
            0.010000f, 0.000000f, -0.800000f, 0.007000f, -0.007000f, -0.800000f,
            0.040000f, 0.000000f, -0.800000f, 0.028284f, 0.028284f, -0.800000f,
            0.007000f, 0.007000f, -0.800000f, 0.010000f, 0.000000f, -0.800000f,
            0.007000f, 0.007000f, -0.800000f, 0.028284f, 0.028284f, -0.800000f,
            0.000000f, 0.040000f, -0.800000f, 0.000000f, 0.010000f, -0.800000f,
            0.000000f, 0.010000f, -0.800000f, 0.000000f, 0.040000f, -0.800000f,
            -0.028284f, 0.028284f, -0.800000f, -0.007000f, 0.007000f, -0.800000f
        };
        quadVerticesBuffer = FloatBuffer.allocate(quad_vertices.length);
        quadVerticesBuffer.put(quad_vertices);
        quadVerticesBuffer.rewind();

        // Quad normals:
        float[] quad_normals = {
            0.000000f, 1.000000f, 0.000000f, 0.707107f, 0.707107f, 0.000000f,
            0.707107f, 0.707107f, 0.000000f, 0.000000f, 1.000000f, 0.000000f,
            0.000000f, -1.000000f, 0.000000f, -0.707107f, -0.707107f, 0.000000f,
            -0.707107f, -0.707107f, 0.000000f, 0.000000f, -1.000000f, 0.000000f,
            -0.707107f, -0.707107f, 0.000000f, -1.000000f, 0.000000f, 0.000000f,
            -1.000000f, 0.000000f, 0.000000f, -0.707107f, -0.707107f, 0.000000f,
            -1.000000f, 0.000000f, 0.000000f, -0.707107f, 0.707107f, 0.000000f,
            -0.707107f, 0.707107f, 0.000000f, -1.000000f, 0.000000f, 0.000000f,
            -0.707107f, 0.707107f, 0.000000f, 0.000000f, 1.000000f, 0.000000f,
            0.000000f, 1.000000f, 0.000000f, -0.707107f, 0.707107f, 0.000000f,
            0.707107f, 0.707107f, 0.000000f, 1.000000f, 0.000000f, 0.000000f,
            1.000000f, 0.000000f, 0.000000f, 0.707107f, 0.707107f, 0.000000f,
            1.000000f, 0.000000f, 0.000000f, 0.707107f, -0.707107f, 0.000000f,
            0.707107f, -0.707107f, 0.000000f, 1.000000f, 0.000000f, 0.000000f,
            0.707107f, -0.707107f, 0.000000f, 0.000000f, -1.000000f, 0.000000f,
            0.000000f, -1.000000f, 0.000000f, 0.707107f, -0.707107f, 0.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f,
            0.000000f, 0.000000f, 1.000000f, 0.000000f, 0.000000f, 1.000000f
        };
        quadNormalsBuffer = FloatBuffer.allocate(quad_normals.length);
        quadNormalsBuffer.put(quad_normals);
        quadNormalsBuffer.rewind();

        glu = new GLU();
    }

    /**
     * //! Draw an x-y-z frame.
     * @param aAxisLengthScale
     * @param aAxisThicknessScale
     * @param aModifyMaterialState
     */
    public static void jDrawFrame(final double aAxisLengthScale,
            final double aAxisThicknessScale,
            final boolean aModifyMaterialState) {
        // Triangle vertices:
        int nTriangles = 8;

        // Quad vertices:
        int nQuads = 16;




        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        // set material properties
        float[] fnull = {0, 0, 0, 0};
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, fnull, 0);
        gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, fnull, 0);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glEnable(GL2.GL_COLOR_MATERIAL);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        // enable vertex and normal arrays
        gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);

        if (aModifyMaterialState) {
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);
        }

        for (int k = 0; k < 3; k++) {
            gl.glPushMatrix();

            // Rotate to the appropriate axis
            if (k == 0) {
                gl.glRotatef(-90.0f, 0, 1, 0);
                gl.glColor3f(1.0f, 0.0f, 0.0f);
            } else if (k == 1) {
                gl.glRotatef(90.0f, 1, 0, 0);
                gl.glColor3f(0.0f, 1.0f, 0.0f);
            } else {
                gl.glRotatef(180.0f, 1, 0, 0);
                gl.glColor3f(0.0f, 0.0f, 1.0f);
            }

            // scaling
            gl.glScaled(aAxisThicknessScale, aAxisThicknessScale, aAxisLengthScale);

            // render frame object

            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, triVerticesBuffer);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, triNormalsBuffer);

            gl.glDrawArrays(GL2.GL_TRIANGLES, 0, nTriangles * 3);

            gl.glVertexPointer(3, GL2.GL_FLOAT, 0, quadVerticesBuffer);
            gl.glNormalPointer(GL2.GL_FLOAT, 0, quadNormalsBuffer);

            gl.glDrawArrays(GL2.GL_QUADS, 0, nQuads * 4);

            gl.glPopMatrix();
        }

        // disable vertex and normal arrays
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
    }

    /**
     * Draw a line-based box with sides parallel to the x-y-z axes.
     * 
     * @param aXMin
     * @param aXMax
     * @param aYMin
     * @param aYMax
     * @param aZMin
     * @param aZMax
     */
    public static void jDrawWireBox(final double aXMin, final double aXMax,
            final double aYMin, final double aYMax,
            final double aZMin, final double aZMax) {

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        // render lines for each edge of the box
        gl.glBegin(GL2.GL_LINES);
        gl.glVertex3d(aXMin, aYMin, aZMin);
        gl.glVertex3d(aXMax, aYMin, aZMin);
        gl.glVertex3d(aXMin, aYMax, aZMin);
        gl.glVertex3d(aXMax, aYMax, aZMin);
        gl.glVertex3d(aXMin, aYMin, aZMax);
        gl.glVertex3d(aXMax, aYMin, aZMax);
        gl.glVertex3d(aXMin, aYMax, aZMax);
        gl.glVertex3d(aXMax, aYMax, aZMax);

        gl.glVertex3d(aXMin, aYMin, aZMin);
        gl.glVertex3d(aXMin, aYMax, aZMin);
        gl.glVertex3d(aXMax, aYMin, aZMin);
        gl.glVertex3d(aXMax, aYMax, aZMin);
        gl.glVertex3d(aXMin, aYMin, aZMax);
        gl.glVertex3d(aXMin, aYMax, aZMax);
        gl.glVertex3d(aXMax, aYMin, aZMax);
        gl.glVertex3d(aXMax, aYMax, aZMax);

        gl.glVertex3d(aXMin, aYMin, aZMin);
        gl.glVertex3d(aXMin, aYMin, aZMax);
        gl.glVertex3d(aXMax, aYMin, aZMin);
        gl.glVertex3d(aXMax, aYMin, aZMax);
        gl.glVertex3d(aXMin, aYMax, aZMin);
        gl.glVertex3d(aXMin, aYMax, aZMax);
        gl.glVertex3d(aXMax, aYMax, aZMin);
        gl.glVertex3d(aXMax, aYMax, aZMax);
        gl.glEnd();
    }

    /**
     * Render a sphere given a radius.
     * @param aRadius
     * @param aNumSlices
     * @param aNumStacks
     */
    public static void jDrawSphere(final double aRadius,
            final int aNumSlices,
            final int aNumStacks) {
        // allocate a new OpenGL quadric object for rendering a sphere
        GLUquadric quadObj;
        quadObj = glu.gluNewQuadric();


        // set rendering style
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);

        // set normal-rendering mode
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);

        // render a sphere
        glu.gluSphere(quadObj, aRadius, aNumSlices, aNumStacks);

        // delete our quadric object
        glu.gluDeleteQuadric(quadObj);
    }

    /**
     * //! Draw a pretty arrow on the z-axis using a cone and a cylinder (using GLUT)
     * @param aArrowStart
     * @param aArrowTip
     * @param aWidth
     */
    public static void jDrawArrow(final JVector3d aArrowStart,
            final JVector3d aArrowTip,
            final double aWidth) {

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        
        gl.glPushMatrix();

        // We don't really care about the up vector, but it can't
        // be parallel to the arrow...
        JVector3d up = new JVector3d(0, 1, 0);
        //JVector3d arrow = aArrowTip-aArrowStart;
        JVector3d arrow = new JVector3d(0, 0, 0);
        arrow.normalize();
        double d = Math.abs(JMaths.jDot(up, arrow));
        if (d > .9) {
            up = new JVector3d(1, 0, 0);
        }

        JMatrixGL.jLookAt(gl, aArrowStart, aArrowTip, up);

        double distance = JMaths.jDistance(aArrowTip, aArrowStart);


        // This flips the z axis around
        gl.glRotatef(180, 1, 0, 0);

        // create a new OpenGL quadratic object
        GLUquadric quadObj;
        quadObj = glu.gluNewQuadric();

        // set rendering style
        glu.gluQuadricDrawStyle(quadObj, GLU.GLU_FILL);

        // set normal-rendering mode
        glu.gluQuadricNormals(quadObj, GLU.GLU_SMOOTH);

        // render a cylinder and a cone
        gl.glRotatef(180, 1, 0, 0);
        glu.gluDisk(quadObj, 0, aWidth, 10, 10);
        gl.glRotatef(180, 1, 0, 0);

        glu.gluCylinder(quadObj, aWidth, aWidth, distance * ARROW_CYLINDER_PORTION, 10, 10);
        gl.glTranslated(0, 0, ARROW_CYLINDER_PORTION * distance);

        gl.glRotatef(180, 1, 0, 0);
        glu.gluDisk(quadObj, 0, aWidth * 2.0, 10, 10);
        gl.glRotatef(180, 1, 0, 0);

        glu.gluCylinder(quadObj, aWidth * 2.0, 0.0, distance * ARRROW_CONE_PORTION, 10, 10);

        // delete our quadric object
        glu.gluDeleteQuadric(quadObj);

        gl.glPopMatrix();
    }
}
