/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class PerspectiveCamera extends Camera{

    double fov;

    public PerspectiveCamera(double fov) {
        super();
        this.fov = fov;
    }

    public PerspectiveCamera() {
        this(45);
    }
    
    
    @Override
    public void updateView() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        
        gl.glMatrixMode(GL2.GL_PROJECTION);
        gl.glLoadIdentity();
        
        double aspect = renderingArea.height > 0 ? renderingArea.width / renderingArea.height : 1.0;
        
        glu.gluPerspective(fov, aspect, near, far);
        
        glu.gluLookAt(
                position.x, position.y, position.z,
                lookAtPoint.x, lookAtPoint.y, lookAtPoint.z,
                upVector.x, upVector.y, upVector.z);
        
        gl.glMatrixMode(GL2.GL_MODELVIEW);
        gl.glLoadIdentity();
    }
    
}
