package org.jchai3d.scenegraph2;

import java.util.ArrayList;
import javax.media.opengl.GLAutoDrawable;
import javax.media.opengl.GLEventListener;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class ViewportAdapter implements GLEventListener {

    private World world;
    private ArrayList<Camera> cameras;

    public ViewportAdapter(World world) {
        this.world = world;
        //this.cameras = world.cameras;
    }

    /**
     * @see GLEventListener#init(javax.media.opengl.GLAutoDrawable) 
     */
    @Override
    public void init(GLAutoDrawable drawable) {
        world.init();
    }

    /**
     * @see GLEventListener#display(javax.media.opengl.GLAutoDrawable) 
     */
    @Override
    public void display(GLAutoDrawable drawable) {
        for (Camera camera : cameras) {
            camera.display();
            world.display();
        }
    }

    /**
     * @see GLEventListener#reshape(javax.media.opengl.GLAutoDrawable) 
     */
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        synchronized (cameras) {
            for (Camera camera : cameras) {
                camera.reshape(x, y, width, height);
            }
        }
    }

    /**
     * @see GLEventListener#displayChanged(javax.media.opengl.GLAutoDrawable) 
     */
    public void displayChanged(GLAutoDrawable drawable, boolean modeChanged, boolean deviceChanged) {
    }

    @Override
    public void dispose(GLAutoDrawable glad) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
