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
package org.jchai3d.display;

import java.nio.DoubleBuffer;
import java.nio.IntBuffer;
import com.jogamp.opengl.*;
import com.jogamp.opengl.glu.GLU;
import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;
import org.jchai3d.scenegraph.JCamera;
import org.jchai3d.scenegraph.JChaiRenderMode;
import org.jchai3d.scenegraph.JGenericObject;
import org.jchai3d.widgets.JLabel;


/**
 * This class implements GLEventListener, so it is the bridge between a
 * GLDrawable object and the JCHAI3D.
 *
 * author Jairo Simão version 1.0.0
 */
public class JViewport implements GLEventListener {

    //---------------------------------------------------------------------
    // GENERAL PROPERTIES:
    //---------------------------------------------------------------------
    //opengl
    private GLAutoDrawable drawable;
    //!  Virtual camera connected to this viewport.
    protected JCamera camera;
    //! Status of viewport.
    protected boolean enabled;
    //! Stereo status.
    protected boolean stereoEnabled;
    //! GL Status.
    protected boolean glReady;
    /**
     *
     */
    protected ForceRenderArea forceRenderArea;
    /**
     *
     */
    private boolean startSimulation = false;

    /*
     * !
     * The rectangle to which we're rendering within the GL window, equal to the
     * window size by default. The _positive_ y axis goes _up_.
     */
    protected ForceRenderArea activeRenderingArea;

    /*
     * !
     * If non-zero, this object will get rendered immediately before the GL
     * buffer is swapped out, after all other.
     */
    protected JGenericObject postRenderCallback;

    /*
     * !
     * The most recent viewport to initiate rendering; useful for finding global
     * opengl state information
     */
    protected static JViewport lastActiveViewport;
    /**
     * Last collision events with mouse.
     */
    protected JCollisionRecorder collisionRecorder;
    /**
     * It's useful to store the last viewport transformation, for gluProject'ing
     * things.
     */
    int[] glViewport = new int[4];
    /**
     * The shading model of this viewport
     */
    private int shadingMode;
    /**
     *
     */
    protected GLU glu;
    /**
     *
     */
    protected JLabel labelFPS;
    /**
     *
     */
    protected long lastTick;

    /**
     *
     */
    public JViewport() {
        shadingMode = GL2.GL_SMOOTH;
        activeRenderingArea = new ForceRenderArea();
        forceRenderArea = new ForceRenderArea();
        glu = new GLU();
    }

    //---------------------------------------------------------------------
    // PROTECTED METHODS:
    //---------------------------------------------------------------------
    //! Clean up the current rendering context.
    protected boolean cleanup() {
        boolean status = true;

        // delete display context
        drawable.getContext().destroy();
        /*
         * int result = ReleaseDC(m_winHandle, m_glDC); if (result == 0) status
         * = false;
         *
         * result = wglDeleteContext(m_glContext); if (result == 0) status =
         * false;
         */

        glReady = false;
        return status;
    }

    //! Render the scene in OpenGL.  Nukes the contents of the GL buffers.
    protected boolean renderView(final int aImageIndex) {

        // Make sure the viewport is really ready for rendering
        if ((glReady == false) || (enabled == false) || (camera == null)) {
            return false;
        }

        // If we're using the whole window, see whether the window has
        // changed size...
        if (forceRenderArea.getLeft() == -1) {
            //int width = drawable.getWidth();
            //int height = drawable.getHeight();
            int width = drawable.getSurfaceWidth();
            int height = drawable.getSurfaceHeight();

            if ((activeRenderingArea.getLeft() != 0)
                    || (activeRenderingArea.getBottom() != 0)
                    || (activeRenderingArea.getRight() != (int) width)
                    || (activeRenderingArea.getTop() != (int) height)) {
                update(false);
            }
        } // Otherwise the user is telling us to use a particular rectangle; see
        // whether that rectangle has changed...
        else {
            if ((activeRenderingArea.getLeft() != forceRenderArea.getLeft())
                    || (activeRenderingArea.getRight() != forceRenderArea.getRight())
                    || (activeRenderingArea.getTop() != forceRenderArea.getTop())
                    || (activeRenderingArea.getBottom() != forceRenderArea.getBottom())) {
                update(false);
            }
        }

        // Activate display context
        //
        // Note that in the general case, this is not strictly necessary,
        // but if a user is using multiple viewports, we don't want him
        // to worry about the current rendering context, so we incur a bit
        // of overhead here.
        //if (!wglMakeCurrent(m_glDC, m_glContext))
        //{

        // Try once to re-initialize the context...
        //if (!(update()))
        // And return an error if this doesn't work out...
        //return(false);
        //}

        // Set up rendering to the appropriate buffer
        /*
         * if (aImageIndex == JCamera.CHAI_STEREO_RIGHT) {
         * gl.glDrawBuffer(GL.GL_BACK_RIGHT); } else if (aImageIndex ==
         * JCamera.CHAI_STEREO_LEFT) { gl.glDrawBuffer(GL.GL_BACK_LEFT); } else
         * { gl.glDrawBuffer(GL.GL_BACK); }
         */

        // set viewport size
        int width = activeRenderingArea.getRight() - activeRenderingArea.getLeft();
        int height = activeRenderingArea.getTop() - activeRenderingArea.getBottom();

        // cria área de redesenho gráfico
        GLContext.getCurrent().getGL().glViewport(activeRenderingArea.getLeft(), activeRenderingArea.getBottom(),
                width, height);

        //correção
        GLContext.getCurrent().getGL().glGetIntegerv(GL.GL_VIEWPORT, IntBuffer.wrap(glViewport));

        // render world
        camera.renderView(width, height, aImageIndex);

        if (postRenderCallback != null) {
            postRenderCallback.renderSceneGraph(JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL);
        }

        // Swap buffers
        // If stereo is enabled, we only swap after the _right_ image is drawn
        if (stereoEnabled == false || aImageIndex == JCamera.CHAI_STEREO_RIGHT) {
            drawable.swapBuffers();
        }

        // deactivate display context (not necessary)
        // wglMakeCurrent(m_glDC, 0);

        // operation succeeded
        return true;
    }

    //---------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //---------------------------------------------------------------------
    //! Constructor of cViewport
    public JViewport(
            JCamera aCamera,
            final boolean aStereoEnabled) {

        // If no viewport has been created at all, creation is enough to make this
        // the active viewport
        if (lastActiveViewport == null) {
            lastActiveViewport = this;
        }

        // set the camera through which this viewport should be rendered
        camera = aCamera;

        // stereo status
        stereoEnabled = aStereoEnabled;

        // No post-render callback by default (see setPostRenderCallback() for details)
        postRenderCallback = null;

        //variaveis de armazenamento de posição
        forceRenderArea = new ForceRenderArea();
        activeRenderingArea = new ForceRenderArea();

        // inicializado variaveis
        forceRenderArea.setLeft(-1);
        forceRenderArea.setRight(-1);
        forceRenderArea.setTop(-1);
        forceRenderArea.setBottom(-1);

        if (drawable != null) {
            // This actually creates the context...
            update(false);
        }
    }

    //---------------------------------------------------------------------
    // GENERAL SEETINGS
    //---------------------------------------------------------------------
    //! Get height of active viewport area.
    public final int getHeight() {

        //return (drawable.getHeight());
        return (drawable.getSurfaceHeight());
    }

    //! Get width of active viewport area.
    public final int getWidth() {
        //return (drawable.getWidth());
        return (drawable.getSurfaceWidth());
    }

    //! Set the camera through which this viewport will be rendered.
    public void setCamera(JCamera aCamera) {
        // set camera
        camera = aCamera;
    }

    //! Get the camera through which this viewport is being rendered.
    public final JCamera getCamera() {
        return (camera);
    }

    //! Enable or disable rendering of this viewport.
    public void setEnabled(final boolean aEnabled) {
        enabled = aEnabled;
    }

    //! Get the rendering status of this viewport.
    public final boolean getEnabled() {
        return (enabled);
    }

    public final boolean getMGlReady() {
        return glReady;
    }

    //---------------------------------------------------------------------
    // STEREO DISPLAY SUPPORT
    //---------------------------------------------------------------------
    //! Enable or disable stereo rendering.
    public void setStereoOn(boolean aStereoEnabled) {
        // check if new mode is not already active
        if (aStereoEnabled == stereoEnabled) {
            return;
        }

        // update stereo rendering state
        stereoEnabled = aStereoEnabled;
    }

    //! Is stereo rendering enabled?
    public final boolean getStereoOn() {
        return (stereoEnabled);
    }

    //---------------------------------------------------------------------
    // MOUSE SELECTION
    //---------------------------------------------------------------------
    //! Tell the viewport to figure out whether the (x,y) viewport coordinate is within a visible object.
    public boolean select(final int aWindowPosX,
            final int aWindowPosY,
            JCollisionSettings aCollisionSettings) {
        // check if camera is valid
        if (camera == null) {
            return false;
        }

        // compute width and height of rendering area in scene
        //int width = drawable.getWidth();
        //int height = drawable.getHeight();
        int width = drawable.getSurfaceWidth();
        int height = drawable.getSurfaceHeight();

        // apply some default collision settings in none have been specified
        JCollisionSettings collisionSettings = new JCollisionSettings();

        if (aCollisionSettings == null) {
            collisionSettings.setCheckVisibleObjectsOnly(true);
            collisionSettings.setCheckBothSidesOfTriangles(true);
            collisionSettings.setCollisionRadius(0.0);
        } else {
            collisionSettings = aCollisionSettings;
        }

        // search for intersection between ray and objects in world
        boolean result = camera.select(aWindowPosX,
                aWindowPosY,
                width,
                height,
                collisionRecorder,
                collisionSettings);

        // return result. True if and object has been hit, else false.
        return (result);

    }

    //! Get last selected mesh.
    public JGenericObject getLastSelectedObject() {
        return (collisionRecorder.getNearestCollision().getObject());
    }

    //! Get last selected triangle.
    public JTriangle getLastSelectedTriangle() {
        return (collisionRecorder.getNearestCollision().getTriangle());
    }

    //! Get last selected point position.
    public JVector3d getLastSelectedPoint() {
        return (collisionRecorder.getNearestCollision().getGlobalPosition());
    }

    //! Get last selected point normal.
    public JVector3d getLastSelectedPointNormal() {
        return (collisionRecorder.getNearestCollision().getGlobalNormal());
    }

    //! Get distance to last selected object.
    public double getLastSelectedDistance() {
        return (JMaths.jSqr(collisionRecorder.getNearestCollision().getSquareDistance()));
    }

    //---------------------------------------------------------------------
    // RENDERING
    //---------------------------------------------------------------------
    //! Call this method to render the scene in OpenGL
    public boolean render(final int imageIndex) {
        boolean result;

        lastActiveViewport = this;

        // The default rendering option tells the viewport to decide
        // whether it's rendering in stereo, and - if so - to render
        // a full stereo pair.
        if (imageIndex == JCamera.CHAI_STEREO_DEFAULT) {
            // render mono mode
            if (stereoEnabled) {
                result = renderView(JCamera.CHAI_STEREO_LEFT);
                if (!result) {
                    return (false);
                }

                result = renderView(JCamera.CHAI_STEREO_RIGHT);
                return (result);
            } // render stereo mode
            else {
                result = renderView(JCamera.CHAI_MONO);
                return (result);
            }
        } else {
            result = renderView(imageIndex);
            return (result);
        }
    }

    /*
     * !
     * Clients should call this when the scene associated wit this viewport may
     * need re-initialization, e.g. after a switch to or from fullscreen.
     */
    public void onDisplayReset() {
        if (camera != null) {
            camera.onDisplayReset(true);
        }
    }

    //! Reconfigures the display context.
    public boolean update(boolean resizeOnly) {
        // Clean up the old rendering context if necessary
        if ((resizeOnly == false)) {
            cleanup();
        }

        // declare variables
        int formatIndex;

        // viewport is not yet enabled
        enabled = false;

        // gl display not yet ready
        glReady = false;

        // check display handle
        if (drawable == null) {
            return (false);
        }

        // Find out the rectangle to which we should be rendering

        // If we're using the entire window...
        if (forceRenderArea.getLeft() == -1) {
            //if (GetWindowRect(m_winHandle, &m_activeRenderingArea) == 0) { return (false); }

            // Convert from screen to window coordinates
            activeRenderingArea.setRight(activeRenderingArea.getRight() - activeRenderingArea.getLeft());
            activeRenderingArea.setLeft(0);

            activeRenderingArea.setBottom(activeRenderingArea.getBottom() - activeRenderingArea.getTop());
            activeRenderingArea.setTop(0);

            // Convert from y-axis-down to y-axis-up, since that's how we store
            // our rendering area.
            int height = activeRenderingArea.getBottom();
            activeRenderingArea.setTop(height - activeRenderingArea.getTop());
            activeRenderingArea.setBottom(height - activeRenderingArea.getBottom());
        } // Otherwise use whatever rectangle the user wants us to use...
        else {
            activeRenderingArea = forceRenderArea;
        }

        if (resizeOnly == false) {
            // find pixel format supported by the device context. If error return false.
        /*
             * formatIndex = ChoosePixelFormat(m_glDC, &m_pixelFormat); if
             * (formatIndex == 0) { return(false); }
             */
            // sets the specified device context's pixel format. If error return false
        /*
             * if (!SetPixelFormat(m_glDC, formatIndex, &m_pixelFormat)) {
             * return(false); }
             */

            /*
             * formatIndex = GetPixelFormat (m_glDC); DescribePixelFormat
             * (m_glDC, formatIndex, sizeof(PIXELFORMATDESCRIPTOR),
             * &m_pixelFormat);
             */
            // if stereo was enabled but can not be displayed, switch over to mono.
        /*
             * if (((m_pixelFormat.dwFlags & PFD_STEREO) == 0) &&
             * m_stereoEnabled) { m_stereoEnabled = false; }
             */
            // create display context
            //wglMakeCurrent(m_glDC, m_glContext);
        }

        // OpenGL is now ready for rendering
        glReady = true;

        // store this current view as the last active one
        lastActiveViewport = this;

        // enable viewport
        enabled = true;

        if (resizeOnly == false) {
            onDisplayReset();
        }

        // return success
        return (true);

    }

    //! Set post-render callback... the object you supply here will be rendered _after_ all other rendering.
    public void setPostRenderCallback(JGenericObject aPostRenderCallback) {
        postRenderCallback = aPostRenderCallback;
    }

    //! Get post-render callback.
    public final JGenericObject getPostRenderCallback() {
        return (postRenderCallback);
    }

    //---------------------------------------------------------------------
    // SUB AREA RENDERING
    //---------------------------------------------------------------------
    //! Return the last activated viewport. (Last Viewport for which the render() function was called).
    public static JViewport getLastActiveViewport() {
        return lastActiveViewport;
    }

    /*
     * !
     * Project a world-space point from 3D to 2D, using my viewport xform, my
     * camera's projection matrix, and his world's modelview matrix.
     */
    public JVector3d projectPoint(JVector3d aPoint) {
        JVector3d result = new JVector3d();

        int[] viewport = glViewport;
        double[] projection = camera.projectionMatrix;
        double[] modelview = camera.getParentWorld().worldModelView;
        double[] winpos = new double[4];

        glu.gluProject(
                (double) aPoint.getX(),
                (double) aPoint.getY(),
                (double) aPoint.getZ(),
                modelview,
                0,
                projection,
                0,
                viewport,
                0,
                winpos,
                0);
            
        /*
         * posicao do ponto na janela
         */
        result.setX(winpos[0]);
        result.setY(winpos[1]);
        result.setZ(winpos[2]);

        return (result);
    }

    public GLAutoDrawable getMWinHandle() {
        return drawable;
    }

    /**
     * Informação do opengl referente a placa grafica
     *
     * @return
     *
     * public void getInfoGLCapabilities() {
     *
     * infoOpengl = new String();
     *
     * infoOpengl += "INIT GL IS: " + this.gl.getClass().getName(); infoOpengl
     * += "\n";
     *
     * infoOpengl += "GLCAPABILITIES: " + mWinHandle.getChosenGLCapabilities();
     * infoOpengl += "\n";
     *
     * infoOpengl += "GL_VENDOR: " + this.gl.glGetString(GL.GL_VENDOR);
     * infoOpengl += "\n";
     *
     * infoOpengl += "GL_RENDERER: " + this.gl.glGetString(GL.GL_RENDERER);
     * infoOpengl += "\n";
     *
     * infoOpengl += "GL_VERSION: " + this.gl.glGetString(GL.GL_VERSION);
     * infoOpengl += "\n";
     *
     * //infoOpengl += "GL_SHADING_LANGUAGE_VERSION: " +
     * this.mGlContext.glGetString(GL.GL_SHADING_LANGUAGE_VERSION); //infoOpengl
     * += "\n";
     *
     * infoOpengl += "GL_EXTENSIONS: " + this.gl.glGetString(GL.GL_EXTENSIONS);
     * infoOpengl += "\n";
     *
     * System.out.println(getInfoOpengl());
    }
     */
    
    public void init(GLAutoDrawable aWinHandle) {

        // Use debug pipeline
        aWinHandle.setGL(aWinHandle.getGL());

        /*
         * janela de redesenho grafico
         */
        this.drawable = aWinHandle;

        /*
         * recupera o processamento opengl
         */
        //GL gl = GLContext.getCurrent().getGL();
        GL gl = aWinHandle.getGL();


        // Enable VSync
        gl.setSwapInterval(1);

        // Setup the drawing area and shading mode
        gl.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        
        
        // try setting this to GL_FLAT and see what happens.
        //gl.glShadeModel(GL.GL_SMOOTH);
        


        /*
         * teste
         */
        glReady = true;
        enabled = true;

        updateFPS();
    }

    
    public void display(GLAutoDrawable aWinHandle) {
        /**
         * Updates FPS information
         */
        if (labelFPS.isVisible()) {
            updateFPS();
        }
        //aWinHandle.getGL().glShadeModel(GL.GL_SMOOTH);
        /*
         * Render the entire view
         */
        render(JCamera.CHAI_STEREO_DEFAULT);


        startSimulation = true;
    }

    
    public void reshape(GLAutoDrawable aWinHandle, int x, int y, int width, int height) {
        activeRenderingArea.setLeft(x);
        activeRenderingArea.setBottom(y);
        activeRenderingArea.setRight(width);
        activeRenderingArea.setTop(height);

        forceRenderArea.setLeft(x);
        forceRenderArea.setBottom(y);
        forceRenderArea.setRight(width);
        forceRenderArea.setTop(height);

    }

    public void displayChanged(GLAutoDrawable aWinHandle, boolean modeChanged, boolean deviceChanged) {
        /*
         * janela de redesenho grafico
         */
        this.drawable = aWinHandle;
    }

    /**
     * @return the startSimulation
     */
    public boolean isStartSimulation() {
        return startSimulation;
    }

    /**
     * @return the shadingMode
     */
    public int getShadingMode() {
        return shadingMode;
    }

    /**
     * @param shadingMode the shadingMode to set
     */
    public void setShadingMode(int shadingMode) {
        this.shadingMode = shadingMode;
    }

    private void createFPSLabel() {
        labelFPS = new JLabel();
        labelFPS.setVisible(false);
        camera.front2Dscene.addChild(labelFPS);
    }

    private void updateFPS() {

        if (labelFPS == null) {
            createFPSLabel();
        }
        long cur = System.currentTimeMillis();
        double diff = (cur - lastTick) / 1000.0;
        labelFPS.setString("FPS: " + (1 / diff));
        lastTick = cur;
    }

    public void setFPSLabelPosition(int x, int y) {
        if (labelFPS == null) {
            createFPSLabel();
        }

        labelFPS.setPosition(x, y, 0);
    }

    public void setFPSLabelVisible(boolean v) {
        if (labelFPS == null) {
            createFPSLabel();
        }
        labelFPS.setVisible(v);
    }

    
    public void dispose(GLAutoDrawable glad) {
        
    }
}
class ForceRenderArea {

    private int left;
    private int bottom;
    private int right;
    private int top;

    public int getBottom() {
        return bottom;
    }

    public void setBottom(int bottom) {
        this.bottom = bottom;
    }

    public int getLeft() {
        return left;
    }

    public void setLeft(int left) {
        this.left = left;
    }

    public int getRight() {
        return right;
    }

    public void setRight(int right) {
        this.right = right;
    }

    public int getTop() {
        return top;
    }

    public void setTop(int top) {
        this.top = top;
    }
}
