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

import java.nio.DoubleBuffer;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JMatrix3d;
import org.jchai3d.math.JVector3d;

/**
 * <p>JCamera describes a virtual Camera located inside the world. Its job in
 * life is to set up the OpenGL projection matrix for the current OpenGL
 * rendering context. The default camera looks down the negative x-axis. OpenGL
 * folks may wonder why we chose the negative x-axis... it turns out that's a
 * better representation of the standard conventions used in general
 * robotics.</p>
 *
 * @author Francois Conti (original author)
 * @author Dan Morris(original author)
 * @author Igor Gabriel (java implementation)
 * @author João Cerqueira (java implementation)
 * @author Jairo Simão (java implementation)
 * @version 1.0.0
 */
public class JCamera extends JGenericObject {
    //-----------------------------------------------------------------------
    // CONSTANTS:
    //-----------------------------------------------------------------------

    public static final int CHAI_MAX_CLIP_PLANES = 6;
    /**
     * Constant specifying specific stereo rendering frames (Monoscopic View).
     */
    public static int CHAI_MONO = 0;
    /**
     * Constant specifying specific stereo rendering frames (Stereoscopic View -
     * Left Eye).
     */
    public static int CHAI_STEREO_LEFT = -1;
    /**
     * Constant specifying specific stereo rendering frames (Monoscopic View -
     * Right Eye).
     */
    public static int CHAI_STEREO_RIGHT = 1;

    /*
     * This constant is used to tell the _viewport_ that he should decide which
     * frame(s) to render, and send MONO, LEFT, and/or RIGHT to the camera.
     */
    public static int CHAI_STEREO_DEFAULT = -1000;
    public static int TWOD_GL_LIGHT_INDEX = 0;
    public static float TWOD_LIGHT_AMBIENT = 0.5f;
    public static float TWOD_LIGHT_DIFFUSE = 0.5f;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------
    public JCamera(JWorld aParentWorld) {
        // set default values for clipping planes
        setClippingPlanes(0.1, 1000.0);

        // set default field of view angle
        setFieldViewAngle(45);

        // set parent world
        parentWorld = aParentWorld;

        // position and orient camera, looking down the negative x-axis
        // (the robotics convention)
        set(
                new JVector3d(0, 0, 0), // Local Position of camera.
                new JVector3d(-1, 0, 0), // Local Look At position
                new JVector3d(0, 0, 1) // Local Up Vector
                );

        // set default stereo parameters
        stereoFocalLength = 5.0;
        stereoEyeSeparation = 0.5;

        // disable multipass transparency rendering by default
        multipassTransparencyEnabled = false;

        performingDisplayReset = false;

        clippingPlanes = new JClippingPlane[JCamera.CHAI_MAX_CLIP_PLANES];
        // gera clip planes
        for (int i = 0; i < clippingPlanes.length; i++) {
            clippingPlanes[i] = new JClippingPlane();
        }

        front2Dscene = new JGenericObject();

        glu = new GLU();
    }
    //-----------------------------------------------------------------------
    // MEMBERS
    //-----------------------------------------------------------------------
    /**
     * Parent world.
     */
    protected JWorld parentWorld;
    /**
     * Distance to near clipping plane.
     */
    private double nearClippingPlaneDistance;
    /**
     * Distance to far clipping plane.
     */
    private double farClippingPlaneDistance;
    /**
     * Other clipping planes.
     */
    protected JClippingPlane[] clippingPlanes;
    /**
     * Field of view angle in degrees.
     */
    protected double fieldOfViewAngle;
    // Stereo Parameters:
    /**
     * Focal length.
     */
    protected double stereoFocalLength;
    /**
     * Eye separation.
     */
    protected double stereoEyeSeparation;
    /**
     * If true, three rendering passes are performed to approximate back-front
     * sorting (see long comment)
     */
    protected boolean multipassTransparencyEnabled;
    /**
     * Some apps may have the camera as a child of the world, which would cause
     * recursion when resetting the display
     */
    protected boolean performingDisplayReset;
    /**
     * last width size of the window.
     */
    protected int lastDisplayWidth;
    /**
     * last height size of the window.
     */
    protected int lastDisplayHeight;
    /**
     * It's useful to store the last projection matrix, for gluProject'ing
     * things.
     */
    public double[] projectionMatrix = new double[16];
    /**
     * Front plane scenegraph which can be used to attach widgets.
     */
    public JGenericObject front2Dscene;
    /**
     * Black plane scenegraph which can be used to attach widgets.
     */
    public JGenericObject back2Dscene;
    private GLU glu;

    /**
     * Get pointer to parent world.
     */
    public JWorld getParentWorld() {
        return parentWorld;
    }

    public void setParentWorld(JWorld world) {
        parentWorld = world;
    }

    /**
     * Check for collision detection between an x-y position (typically a mouse
     * click) and an object in the scene
     *
     * @param aWindowPosX X coordinate position of mouse click.
     * @param aWindowPosY Y coordinate position of mouse click.
     * @param aWindowWidth Width of window display (pixels)
     * @param aWindowHeight Height of window display (pixels)
     * @param aCollisionRecorder Recorder used to store all collisions between
     * mouse and objects
     * @param aCollisionSettings Settings related to collision detection
     * @return Returns \b true if an object has been hit, else false
     */
    public boolean select(final int aWindowPosX,
            final int aWindowPosY,
            final int aWindowWidth,
            final int aWindowHeight,
            JCollisionRecorder aCollisionRecorder,
            JCollisionSettings aCollisionSettings) {
        // clear collision recorder
        aCollisionRecorder.clear();

        // update my m_globalPos and m_globalRot variables
        parentWorld.computeGlobalPositions(true, new JVector3d(0.0, 0.0, 0.0), JMaths.jIdentity3d());

        // make sure we have a legitimate field of view
        if (Math.abs(fieldOfViewAngle) < 0.001f) {
            return false;
        }

        // compute the ray that leaves the eye point at the appropriate angle
        //
        // m_fieldViewAngle / 2.0 would correspond to the _top_ of the window
        double distCam = (aWindowHeight / 2.0f) / JMaths.jTanDeg(fieldOfViewAngle / 2.0f);

        JVector3d selectRay = new JVector3d();
        selectRay.set(-distCam,
                (aWindowPosX - (aWindowWidth / 2.0f)),
                ((aWindowHeight / 2.0f) - aWindowPosY));
        selectRay.normalize();

        selectRay = JMaths.jMul(globalRotation, selectRay);

        // create a point that's way out along that ray
        JVector3d selectPoint = JMaths.jAdd(getGlobalPosition(), JMaths.jMul(100000, selectRay));

        // search for intersection between the ray and objects in the world
        boolean result = parentWorld.computeCollisionDetection(
                getGlobalPosition(), selectPoint,
                aCollisionRecorder,
                aCollisionSettings);

        // return result
        return result;
    }

    /**
     * Set the position and orientation of the camera. Three vectors are
     * required:
     *
     * [iPosition] which describes the position in local coordinates of the
     * camera
     *
     * [iLookAt] which describes a point at which the camera is looking
     *
     * [iUp] to orient the camera around its rolling axis. [iUp] always points
     * to the top of the image.
     *
     * These vectors are used in the usual gluLookAt sense.
     *
     * @param aLocalPosition The position of the camera in local coordinates
     * @param aLocalLookAt The Point in local space at which the camera looks
     * @param aLocalUp A vector giving the rolling orientation (points toward
     * the top of the image)
     */
    public boolean set(final JVector3d aLocalPosition,
            final JVector3d aLocalLookAt,
            final JVector3d aLocalUp) {

        // copy new values to temp variables

        JVector3d pos = new JVector3d(aLocalPosition);
        JVector3d lookAt = new JVector3d(aLocalLookAt);
        JVector3d up = new JVector3d(aLocalUp);
        JVector3d Cy = new JVector3d();

        // check validity of vectors
        if (pos.distancesq(lookAt) < JConstants.CHAI_SMALL) {
            return (false);
        }
        if (up.lengthsq() < JConstants.CHAI_SMALL) {
            return (false);
        }

        // compute new rotation matrix
        pos.sub(lookAt);
        pos.normalize();
        up.normalize();
        up.crossr(pos, Cy);
        if (Cy.lengthsq() < JConstants.CHAI_SMALL) {
            return (false);
        }
        Cy.normalize();
        pos.crossr(Cy, up);

        // update frame with new values

        //setPosition(aLocalPosition);
        localPosition.copyFrom(aLocalPosition);
        JMatrix3d localRot = new JMatrix3d();
        localRot.setCol(pos, Cy, up);

        //setRotation(localRot);
        localRotation.copyFrom(localRot);

        // return success
        return (true);
    }

    /**
     * Get the camera "look at" position vector for this camera.
     *
     * @return
     */
    public final JVector3d getLookVector() {
        return localRotation.getCol0();
    }

    /**
     * Get the "up" vector for this camera.
     *
     * @return
     */
    public final JVector3d getUpVector() {
        return localRotation.getCol2();
    }

    /**
     * Get the "right direction" vector for this camera.
     *
     * @return
     */
    public final JVector3d getRightVector() {
        return localRotation.getCol1();
    }

    /**
     * Set the positions of the near and far clip planes
     *
     * @param aDistanceNear Distance to near clipping plane
     * @param aDistanceFar Distance to far clipping plane
     */
    public void setClippingPlanes(final double aDistanceNear, final double aDistanceFar) {
        /**
         * check values of near and far clipping planes
         */
        if ((aDistanceNear > 0.0)
                && (aDistanceFar > 0.0)
                && (aDistanceFar > aDistanceNear)) {
            nearClippingPlaneDistance = aDistanceNear;
            farClippingPlaneDistance = aDistanceFar;
        }
    }

    /**
     * Get near clipping plane.
     *
     * @return
     */
    double getNearClippingPlane() {
        return (nearClippingPlaneDistance);

    }

    public void setNearClippingPlaneDistance(double near) {
        this.nearClippingPlaneDistance = near;
    }

    public void setFarClippingPlaneDistance(double far) {
        this.farClippingPlaneDistance = far;
    }

    /**
     * Get far clipping plane.
     *
     * @return
     */
    double getFarClippingPlane() {
        return (farClippingPlaneDistance);

    }

    /**
     * This call automatically adjusts the front and back clipping planes to
     * optimize usage of the z-buffer.
     */
    public void adjustClippingPlanes() {
        /**
         * check if world is valid
         */
        JWorld world = getParentWorld();
        if (world == null) {
            return;
        }

        /**
         * compute size of the world
         */
        world.computeBoundaryBox(true);

        /**
         * compute a distance slightly larger the world size
         */
        JVector3d max = world.getBoundaryBoxMax();
        JVector3d min = world.getBoundaryBoxMin();
        double distance = 2.0 * JMaths.jDistance(min, max);

        /**
         * update clipping plane:
         */
        setClippingPlanes(distance / 1000.0, distance);
    }

    /**
     * Enable or disable one of the (six) arbitrary clipping planes.
     *
     * @param index Which clip plane (0 -> CHAI_MAX_CLIP_PLANES-1) does this
     * refer to?
     * @param enable Should I turn this clip plane on or off, or should I leave
     * it alone? // 0 : disabled // 1 : enabled // -1 : don't touch
     * @param peqn What is the plane equation for this clipping plane?
     */
    public void enableClipPlane(final int index, final int enable, final double peqn) {
        /**
         * Verify valid arguments
         */
        if (index >= CHAI_MAX_CLIP_PLANES) {
            return;
        }

        clippingPlanes[index].enabled = enable;

        if (peqn > 0) {
            for (int i = index; i < 4 * Double.MAX_VALUE; i++) {
                clippingPlanes[index].peqn[index] = peqn;
                //memcpy(mClipPlanes[index].peqn, peqn, 4 * sizeof(double));

            }
        }
    }

    //-----------------------------------------------------------------------
    // METHODS - FIELD OF VIEW & OPTICS:
    //-----------------------------------------------------------------------
    /**
     * Set the field of view angle in \e degrees
     *
     * @param a_fieldViewAngle Field of view angle in \e degrees (should be
     * between 0 and 180)
     */
    public void setFieldViewAngle(double aFieldViewAngle) {
        fieldOfViewAngle = JMaths.jClamp(aFieldViewAngle, 0.0, 180.0);
    }

    // Read field of view angle (in degrees).
    public double getFieldViewAngle() {
        return (fieldOfViewAngle);
    }

    /**
     * Set stereo focal length.
     */
    public int setStereoFocalLength(double aStereoFocalLength) {
        stereoFocalLength = aStereoFocalLength;

        // Prevent 0 or negative focal lengths
        if (stereoFocalLength < JConstants.CHAI_SMALL) {
            stereoFocalLength = JConstants.CHAI_SMALL;
        }

        return 0;
    }

    /**
     * Get stereo focal length.
     */
    public double getStereoFocalLength() {
        return (stereoFocalLength);
    }

    /**
     * Set stereo eye separation
     *
     * @param aStereoEyeSeparation
     * @return
     */
    public int setStereoEyeSeparation(double aStereoEyeSeparation) {
        stereoEyeSeparation = aStereoEyeSeparation;
        return 0;
    }

    /**
     * Get stereo eye separation.
     */
    public double getStereoEyeSeparation() {
        return (stereoEyeSeparation);
    }

    /**
     * Render a 2d scene within this camera's view.
     */
    void render2dSceneGraph(JGenericObject aGraph, int aWidth, int aHeight) {
        int i;

        GL2 gl = GLContext.getCurrent().getGL().getGL2();

        gl.glPushAttrib(GL2.GL_LIGHTING_BIT);
        gl.glPushAttrib(GL2.GL_ENABLE_BIT);

        // render widgets over the 3d scene
        gl.glDisable(GL2.GL_LIGHTING);

        // disable 3d clipping planes
        for (i = 0; i < CHAI_MAX_CLIP_PLANES; i++) {
            gl.glDisable(GL2.GL_CLIP_PLANE0 + i);
        }

        // set up an orthographic projection matrix
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        // No real depth clipping...
        gl.glOrtho(0, aWidth, 0, aHeight, -100000.0, 100000.0);

        // Now actually render the 2d scene graph with the mv matrix active...
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPushMatrix();
        gl.glLoadIdentity();

        gl.glEnable(GL2.GL_BLEND);
        gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);

        // Disable depth-testing
        gl.glDisable(GL2.GL_DEPTH_TEST);
        gl.glDepthMask(false);

        // We want to allow lighting to work in our 2d world, to allow
        // materials to work, but we want only one light in front of the
        // camera, so we re-initialize lighting here.
        //
        // Lighting state will be restored when we popAttrib later on.
        gl.glEnable(GL2.GL_LIGHTING);

        for (i = 0; i < 8; i++) {
            gl.glDisable(GL2.GL_LIGHT0 + i);
        }
        gl.glEnable(GL2.GL_LIGHT0 + TWOD_GL_LIGHT_INDEX);
        JLight light = new JLight(null);
        light.setEnabled(true);
        light.glLightID = GL2.GL_LIGHT0 + TWOD_GL_LIGHT_INDEX;
        light.setPosition(0, 0, 1000.0f);
        light.setDirectionalLight(true);
        light.getAmbientColor().set(TWOD_LIGHT_AMBIENT, TWOD_LIGHT_AMBIENT, TWOD_LIGHT_AMBIENT, 1.0f);
        light.getDiffuseColor().set(TWOD_LIGHT_DIFFUSE, TWOD_LIGHT_DIFFUSE, TWOD_LIGHT_DIFFUSE, 1.0f);
        light.getSpecularColor().set(0, 0, 0, 1.0f);
        light.renderLightSource();

        // render widget scene graph
        aGraph.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL);

        // Put OpenGL back into a useful state
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_BLEND);
        gl.glDepthMask(true);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glPopMatrix();

        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glPopMatrix();

        gl.glPopAttrib();
        gl.glPopAttrib();
    }

    //-----------------------------------------------------------------------
    // METHODS - RENDERING AND IMAGING:
    //-----------------------------------------------------------------------
    /**
     * Render the camera in OpenGL (i.e. set up the projection matrix)...
     */
    public void renderView(final int aWindowWidth, final int aWindowHeight, final int aImageIndex) {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        /**
         * store most recent size of display
         */
        lastDisplayWidth = aWindowWidth;
        lastDisplayHeight = aWindowHeight;

        /**
         * set background color
         */
        JColorf color = getParentWorld().getBackgroundColor();
        gl.glClearColor(color.getR(), color.getG(), color.getB(), color.getA());

        /**
         * clear the color and depth buffers
         */
        gl.glClear(GL2.GL_COLOR_BUFFER_BIT | GL2.GL_DEPTH_BUFFER_BIT);

        /**
         * compute global pose
         */
        computeGlobalCurrentObjectOnly(true);

        /**
         * check window size
         */
        if (aWindowHeight == 0) {
            return;
        }

        /**
         * render the 'back' 2d object layer; it will set up its own projection
         * matrix
         */
        if (back2Dscene != null && back2Dscene.getNumChildren() > 0) {
            render2dSceneGraph(back2Dscene, aWindowWidth, aWindowHeight);
        }

        /**
         * set up perspective projection
         */
        double glAspect = ((double) aWindowWidth / (double) aWindowHeight);

        /**
         * set the perspective up for monoscopic rendering
         */
        if (aImageIndex == CHAI_MONO || aImageIndex == CHAI_STEREO_DEFAULT) {


            /**
             * Set up the projection matrix
             */
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            glu.gluPerspective(
                    fieldOfViewAngle, // Field of View Angle.
                    glAspect, nearClippingPlaneDistance, farClippingPlaneDistance);     // Distance to Far clipping plane.

            /**
             * Now set up the view matrix
             */
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();

            /**
             * render pose
             */
            JVector3d lookAt = globalRotation.getCol0();

            JVector3d lookAtPos = new JVector3d();

            globalPosition.subr(lookAt, lookAtPos);

            JVector3d up = globalRotation.getCol2();

            glu.gluLookAt(getGlobalPosition().getX(), getGlobalPosition().getY(), getGlobalPosition().getZ(),
                    lookAtPos.getX(), lookAtPos.getY(), lookAtPos.getZ(),
                    up.getX(), up.getY(), up.getZ());



        } // set the perspective up for stereoscopic rendering
        else {

            // Based on Paul Bourke's stereo rendering tutorial:
            //
            // http://astronomy.swin.edu.au/~pbourke/opengl/stereogl/

            double radians = ((JConstants.CHAI_PI / 180.0) * fieldOfViewAngle / 2.0f);
            double wd2 = nearClippingPlaneDistance * Math.tan(radians);
            double ndfl = nearClippingPlaneDistance / stereoFocalLength;

            // compute the look, up, and cross vectors
            JVector3d lookv = globalRotation.getCol0();
            lookv.mul(-1.0);

            JVector3d upv = globalRotation.getCol2();
            JVector3d offsetv = JMaths.jCross(lookv, upv);

            offsetv.mul(stereoEyeSeparation / 2.0);

            if (aImageIndex == CHAI_STEREO_LEFT) {
                offsetv.mul(-1.0);
            }

            // decide whether to offset left or right
            double stereo_multiplier = (aImageIndex == CHAI_STEREO_LEFT) ? 1.0f : -1.0f;

            double left = -1.0 * glAspect * wd2 + stereo_multiplier * 0.5 * stereoEyeSeparation * ndfl;
            double right = glAspect * wd2 + stereo_multiplier * 0.5 * stereoEyeSeparation * ndfl;
            double top = wd2;
            double bottom = -1.0 * wd2;

            // Set up the projection matrix
            gl.glMatrixMode(GL2.GL_PROJECTION);
            gl.glLoadIdentity();

            gl.glFrustum(left, right, bottom, top, nearClippingPlaneDistance, farClippingPlaneDistance);

            // Now set up the view matrix
            gl.glMatrixMode(GL2.GL_MODELVIEW);
            gl.glLoadIdentity();

            // compute the offset we should apply to the current camera position
            JVector3d pos = JMaths.jAdd(getGlobalPosition(), offsetv);

            // compute the shifted camera position
            JVector3d lookAtPos = new JVector3d();
            pos.addr(lookv, lookAtPos);

            // set up the view matrix
            glu.gluLookAt(pos.getX(), pos.getY(), pos.getZ(),
                    lookAtPos.getX(), lookAtPos.getY(), lookAtPos.getZ(),
                    upv.getX(), upv.getY(), upv.getZ());

        }

        for (int i = 0; i < CHAI_MAX_CLIP_PLANES; i++) {
            if (clippingPlanes[i].enabled == 1) {
                gl.glEnable(GL2.GL_CLIP_PLANE0 + i);
                gl.glClipPlane(GL2.GL_CLIP_PLANE0 + i, DoubleBuffer.wrap(clippingPlanes[i].getPeqn()));
            } else if (clippingPlanes[i].enabled == 0) {
                gl.glDisable(GL2.GL_CLIP_PLANE0 + i);
            } else if (clippingPlanes[i].enabled == -1) {
                // Don't touch
            }
        }

        // Back up the projection matrix for future reference
        gl.glGetDoublev(GL2.GL_PROJECTION_MATRIX, projectionMatrix, 0);

        // Set up reasonable default OpenGL state
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_BLEND);
        gl.glDepthMask(true);
        gl.glEnable(GL2.GL_DEPTH_TEST);

        // optionally perform multiple rendering passes for transparency
        if (multipassTransparencyEnabled) {
            parentWorld.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY);
            parentWorld.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY);
            parentWorld.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY);
        } else {
            parentWorld.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL);
        }

        // render the 'front' 2d object layer; it will set up its own
        // projection matrix
        if (front2Dscene != null && front2Dscene.getNumChildren() > 0) {
            render2dSceneGraph(front2Dscene, aWindowWidth, aWindowHeight);
        }
    }

    /**
     * Copy output image data to image structure.
     */
    /*
     * public void copyImageData(JImageLoader aImage) {
     *
     *
     * //check image structure
     *
     *
     *
     * if (aImage == null) { return; }
     *
     *
     * //* check size
     *
     * if ((lastDisplayWidth != aImage.getWidth()) || (lastDisplayHeight !=
     * aImage.getHeight())) { // TODO: VERIFICAR ESSA TRETA
     * //aImage->allocate(m_lastDisplayWidth, m_lastDisplayHeight); }
     *
     * /
     * //* copy pixel data if required
     *
     * Buffer buffer = aImage.toBuffer();
     *
     * GLContext.getCurrent().getGL().glReadPixels(0, 0, lastDisplayWidth,
     * lastDisplayHeight, GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE, buffer); }
     */
    /**
     * Enable or disable additional rendering passes for transparency (see full
     * comment).
     */
    public void enableMultipassTransparency(boolean enable) {
        multipassTransparencyEnabled = enable;
    }

    /**
     * Resets textures and displays for the world associated with this camera.
     */
    @Override
    public void onDisplayReset(final boolean aAffectChildren) {
        if (performingDisplayReset) {
            return;
        }

        performingDisplayReset = true;

        parentWorld.onDisplayReset(true);
        front2Dscene.onDisplayReset(true);
        back2Dscene.onDisplayReset(true);

        // This will pass the call on to any children I might have...
        onDisplayReset(aAffectChildren);

        performingDisplayReset = false;
    }
}

class JClippingPlane {
    //-----------------------------------------------------------------------
    // CONSTRUCTOR:
    //-----------------------------------------------------------------------

    /**
     * Constructor of JClippingPlane JClippingPlane() { enabled = -1; peqn[0] =
     * peqn[1] = peqn[2] = peqn[3] = 0.0f; }
     * //-----------------------------------------------------------------------
     * // MEMBERS:
     * //-----------------------------------------------------------------------
     * /** Is this clip plane enabled? 0 : disabled 1 : enabled -1 : don't touch
     */
    int enabled;
    /**
     * The plane equation
     */
    double[] peqn = new double[4];

    public double[] getPeqn() {
        return peqn;
    }

    public void setPeqn(double[] peqn) {
        this.peqn = peqn;
    }
}
