/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.jchai3d.scenegraph2;

import java.util.ArrayList;
import javax.media.opengl.GL;
import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;

/**
 *
 * @author Marcos da Silva Ramos
 */
public class ShaderProgram {
    
    ArrayList <ShaderSource> shaders;

    int programID;
    
    public ShaderProgram() {
        shaders = new ArrayList<ShaderSource>();
        programID = -1;
    }
    
    /**
     * Initializes this shader program by creating it, initializing all shader
     * program attached to it and also attaching those shaders to this
     * program in OpenGL2.
     */
    public void init() {
        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        programID = gl.glCreateProgram();
        
        // upload and compile each source, also, attach it to this program
        // under OpenGL2.
        for(ShaderSource shader: shaders) {
            shader.init();
            gl.glAttachShader(shader.shaderID, programID);
        }
        
        // link and start using shaders
        gl.glLinkProgram(programID);
        gl.glUseProgram(programID);
    }
    
    //TODO delete shader program and communication with a running shader
}
