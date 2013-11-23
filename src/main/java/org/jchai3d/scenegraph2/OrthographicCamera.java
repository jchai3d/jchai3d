package org.jchai3d.scenegraph2;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class OrthographicCamera extends Camera{
    
    /**
     * 
     */
    protected double top;
    
    /**
     * 
     */
    protected double right;
    
    /**
     * 
     */
    protected double bottom;
    
    /**
     * 
     */
    protected double left;

    /**
     * Creates a new orthographic camera object. By default, it will create
     * a 2D orthographic viewpoint.
     */
    public OrthographicCamera() {
        super();
        top = 1;
        right = 1;
        bottom = -1;
        left = -1;
        far = 1;
        near = -1;
    }
    
    @Override
    public void updateView() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        
        gl.glMatrixMode(GL2.GL_PROJECTION_MATRIX);
        
        gl.glLoadIdentity();
        
        gl.glOrtho(left, right, bottom, top, right, right);
        
        glu.gluLookAt(
                getPosition().x, getPosition().y, getPosition().z,
                getLookAtPoint().x, getLookAtPoint().y, getLookAtPoint().z,
                getUpVector().x, getUpVector().y, getUpVector().z);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
}
