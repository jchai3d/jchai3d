/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JMaterial;
import org.jchai3d.graphics.JTexture2D;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class Appearance {
    
    /**
     * 
     */
    public static final int POLYGON_MODE_FILL = GL2.GL_FILL;
    
    /**
     * 
     */
    public static final int POLYGON_MODE_LINE = GL2.GL_LINE;
    
    /**
     * 
     */
    public static final int POLYGON_MODE_POINT = GL2.GL_POINTS;
 
    /**
     * The texture of this appearance.
     */
    JTexture2D texture;
    
    /**
     * Enable/disable texture mapping
     */
    boolean textureEnabled;
    
    /**
     * Material of this appearance
     */
    JMaterial material;
    
    /**
     * Enable/disable the material of this appearance
     */
    boolean materialEnabled;
    
    /**
     * Color for ALL vertices of the geometry. If you want to render per-vertex
     * color, see {@link org.jchai3d.math.Geometry}
     */
    JColorf color;
    
    /**
     * Enable/disable the color of this appearance
     */
    boolean colorEnabled;
    
    /**
     * This transparency level of this appearance.
     */
    float transparencyLevel;
    
    /**
     * Sets how OpenGL should draw the polygons. You can use {@link #POLYGON_MODE_FILL}, 
     * {@link #POLYGON_MODE_LINE} or {@link #POLYGON_MODE_POINT}. But if you want, you can use
     * {@link javax.media.opengl.GL#GL_FILL}, {@link javax.media.opengl.GL#GL_LINE}
     * or {@link javax.media.opengl.GL#GL_POINT}
     */
    int polygonMode;
    
    /**
     * The width of the line of this appearance.
     */
    int lineWidth;
    
    /**
     * The line pattern of line to be used in OpenGL2.
     */
    int linePattern;
}
