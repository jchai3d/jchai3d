/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.graphics.JColorf;

/**
 * The Light class is the superclass for every light source class implementation
 * under JCHAI3D.
 *
 * TODO: the light id should be assigned for each GL Context!!
 * @author Marcos da Silva Ramos
 */
public abstract class Light extends RenderableNode {

    /**
     * Array of available light IDs.
     */
    private static int[] lightIDs;
    /**
     * Maximum number of OpenGL lights
     */
    public static final int MAX_LIGHTS = GL2.GL_MAX_LIGHTS;
    
    /**
     * ID for invalid lights.
     */
    public static final int INVALID_ID = -1;
    
    static {
        lightIDs = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            lightIDs[i] = GL2.GL_LIGHT0 + i;
        }
    }

    /**
     * Returns the next available OpenGL light ID.
     *
     * @return the next available light id, or {@link Light#INVALID_ID} if
     * there's no available id.
     */
    public static int getNextAvailableLightID() {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (lightIDs[i] != -1) {
                final int id = lightIDs[i];
                lightIDs[i] = INVALID_ID;
                return id;
            }
        }
        return INVALID_ID;
    }

    /**
     * Releases the specified light id, turning it available for another Light
     * object.
     *
     * @param id the OpenGL light id to be released.
     */
    public static void releaseLightID(int id) {
        int k = 0;
        for (int i = GL2.GL_LIGHT0; i < GL2.GL_LIGHT0 + MAX_LIGHTS; i++) {
            if (i == id && lightIDs[k] == INVALID_ID) {
                lightIDs[k] = i;
                return;
            }
            k++;
        }
    }
    /**
     * The internal OpenGL ID for this light object.
     */
    protected int lightID;
    
    /**
     * Enable/disable the local viewpoint model for this light. Disabled
     * by default.
     */
    protected boolean localViewpointEnabled;
    
    /**
     * Enabled/disable two sided lighting for this light in OpenGL2. Disabled
     * by default.
     */
    protected boolean twoSidedLightingEnabled;
    /**
     * The ambient component of this light.
     */
    protected JColorf ambientColor;
    /**
     * The diffuse component of this light.
     */
    protected JColorf diffuseColor;
    /**
     * The specular component of this light.
     */
    protected JColorf specularColor;
    
    /**
     * The constant attenuation factor
     */
    protected float constantAttenuation;
    
    /**
     * The linear attenuation factor
     */ 
    protected float linearAttenuation;
    
    /**
     * The quadratic attenuation factor
     */
    protected float quadraticAttenuation;

    /**
     * Default constructor
     */
    public Light() throws ScenegraphException {
        super();
        lightID = getNextAvailableLightID();
        if (lightID == INVALID_ID) {
            throw new ScenegraphException("No OpenGL light ID available.");
        }
        enabled = true;
        markForUpdate();
    }
    
    public abstract void updateLight();

    /**
     * 
     */
    @Override
    public void renderNode() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_LOCAL_VIEWER, localViewpointEnabled ? GL2.GL_TRUE : GL2.GL_FALSE);
        gl.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE, twoSidedLightingEnabled ? GL2.GL_TRUE : GL2.GL_FALSE);
        gl.glLightfv(lightID, GL2.GL_AMBIENT, ambientColor.color, 0);
        gl.glLightfv(lightID, GL2.GL_DIFFUSE, diffuseColor.color, 0);
        gl.glLightfv(lightID, GL2.GL_SPECULAR, specularColor.color, 0);
    }

    /**
     * Returns this light OpenGL internal ID.
     *
     * @return the lightID
     */
    public int getLightID() {
        return lightID;
    }
}
