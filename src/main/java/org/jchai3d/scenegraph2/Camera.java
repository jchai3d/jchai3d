package org.jchai3d.scenegraph2;

import javax.media.opengl.GL;
import javax.media.opengl.GLContext;
import javax.media.opengl.glu.GLU;
import org.jchai3d.math.JVector3d;

/**
 *
 * The Camera class is the abstract class for all classes that deal with the
 * projection matrix.
 *
 * @author Marcos da Silva Ramos
 *
 */
public abstract class Camera {

    /**
     * Constant value for {@link Camera#renderingAreaMode}
     */
    public static final int VIEWPORT_FILL = 0x1;
    /**
     * Constant value for {@link Camera#renderingAreaMode}
     */
    public static final int VIEWPORT_AREA = 0x2;
    /**
     * Near clipping plane
     */
    protected double near;
    /**
     * Far clipping plane
     */
    protected double far;
    /**
     * Position of the viewpoint.
     */
    protected JVector3d position;
    /**
     * Direction of the viewpoint.
     */
    protected JVector3d lookAtPoint;
    /**
     * Orientation of the viewpoint.
     */
    protected JVector3d upVector;
    /**
     * Utility toolkit
     */
    protected GLU glu;
    /**
     * Rendering area of this camera
     */
    protected RenderingArea renderingArea;
    /**
     * Possible values are {@link Camera#VIEWPORT_FILL} or {@link Camera#VIEWPORT_AREA}
     */
    protected int renderingAreaMode;

    /**
     * Default constructor
     */
    public Camera() {
        glu = new GLU();
        position = new JVector3d();
        lookAtPoint = new JVector3d(0, 0, -1);
        near = -1;
        far = 1;
        renderingArea = new RenderingArea();
        renderingAreaMode = VIEWPORT_FILL;
    }

    /**
     * Subclasses of Camera should only update the projection matrix when they
     * are changed(e.g.: the position of the viewpoint was changed).
     */
    public abstract void updateView();
    
    /**
     * <p>Defines a viewport and call the {@link Camera#updateView()} implementation
     * to properly setup the projection matrix.</p>
     * <p><b>This method should not be called by user since the ViewportAdapter will
     * call it.</b></p>
     */
    public void display() {
        GL gl = GLContext.getCurrent().getGL();
        
        gl.glViewport(renderingArea.x, renderingArea.y, renderingArea.width, renderingArea.height);
        
        updateView();
    }
    
    /**
     * 
     */
    public void reshape(int x, int y, int w, int h) {
        
        if(renderingAreaMode == VIEWPORT_FILL) {
            this.renderingArea.set(x, y, w, h);
        }
    }

    /**
     * @return the near
     */
    public double getNear() {
        return near;
    }

    /**
     * @param near the near to set
     */
    public void setNear(double near) {
        this.near = near;
    }

    /**
     * @return the far
     */
    public double getFar() {
        return far;
    }

    /**
     * @param far the far to set
     */
    public void setFar(double far) {
        this.far = far;
    }

    /**
     * @return the position
     */
    public JVector3d getPosition() {
        return position;
    }

    /**
     * @param position the position to set
     */
    public void setPosition(JVector3d position) {
        this.position = position;
    }

    /**
     * @return the lookAtPoint
     */
    public JVector3d getLookAtPoint() {
        return lookAtPoint;
    }

    /**
     * @param lookAtPoint the lookAtPoint to set
     */
    public void setLookAtPoint(JVector3d lookAtPoint) {
        this.lookAtPoint = lookAtPoint;
    }

    /**
     * @return the upVector
     */
    public JVector3d getUpVector() {
        return upVector;
    }

    /**
     * @param upVector the upVector to set
     */
    public void setUpVector(JVector3d upVector) {
        this.upVector = upVector;
    }

    /**
     * @return the renderingArea
     */
    public RenderingArea getRenderingArea() {
        return renderingArea;
    }

    /**
     * @param renderingArea the renderingArea to set
     */
    public void setRenderingArea(RenderingArea renderingArea) {
        this.renderingArea = renderingArea;
    }

    /**
     * @return the renderingAreaMode
     */
    public int getRenderingAreaMode() {
        return renderingAreaMode;
    }

    /**
     * <p>Possible values are {@link Camera#VIEWPORT_FILL} or {@link Camera#VIEWPORT_AREA}.</p>
     *
     * If the rendering area mode is set as {@link Camera#VIEWPORT_FILL}, then
     * this camera will fill all viewport rendering area. Otherwise, if you set
     * as {@link Camera#VIEWPORT_AREA} you should also specify a RenderingArea
     * (with {@link Camera#setRenderingArea(org.jchai3d.scenegraph2.RenderingArea)}
     * that will specify the rectangle on wich you want to render the view.
     *
     *
     * @param renderingAreaMode {@link Camera#VIEWPORT_FILL} or {@link Camera#VIEWPORT_AREA}.
     * If different values are set, then no modifications will be made.
     */
    public void setRenderingAreaMode(int renderingAreaMode) {
        if (renderingAreaMode == VIEWPORT_AREA || renderingAreaMode == VIEWPORT_FILL) {
            this.renderingAreaMode = renderingAreaMode;
        }
    }
}
