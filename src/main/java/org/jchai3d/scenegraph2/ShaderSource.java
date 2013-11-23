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
 * The ShaderSource encapsulates OpenGL commands to create GLSL source shaders.
 *
 * TODO: delete this shader from OpenGL
 * TODO: load .vert and .frag shader sources
 * 
 * @author Marcos da Silva Ramos
 */
public class ShaderSource {

    /**
     *
     */
    public static final int VERTEX_SHADER = GL2.GL_VERTEX_SHADER;
    /**
     *
     */
    public static final int FRAGMENT_SHADER = GL2.GL_FRAGMENT_SHADER;
    /**
     *
     */
    protected int shaderID;
    /**
     *
     */
    protected int type;
    /**
     *
     */
    protected String sourceLines[];

    /**
     *
     * @param type
     * @param source
     * @throws ScenegraphException
     */
    public ShaderSource(int type, String source) throws ScenegraphException {
        this(type);

        if (source == null) {
            throw new ScenegraphException("Null source.");
        }

        this.sourceLines = sourceToLines(source);
    }

    /**
     * Creates a new ShaderSource object, with the given type and empty source
     * code.
     *
     * @param type the type of this shader. Can be {@link ShaderSource#FRAGMENT_SHADER}
     * or {@link ShaderSource#VERTEX_SHADER}
     * @throws ScenegraphException if the specified type is not supported by
     * this implementation.
     */
    public ShaderSource(int type) throws ScenegraphException {
        if (!isValidType(type)) {
            throw new ScenegraphException("Specified shader type not supported.");
        }
        this.type = type;
    }

    /**
     * Initializes this shader source by creating, uploading and compiling it
     * into OpenGL2.
     */
    public void init() {
        // get current OpenGL context for this thread
        GL2 gl = GLContext.getCurrent().getGL().getGL2();

        // generates this shader's id
        this.shaderID = gl.glCreateShader(type);

        // upload shader to OpenGL
        gl.glShaderSource(shaderID, sourceLines.length, sourceLines, linesSizeArray(), 0);

        // compile the source
        gl.glCompileShader(this.shaderID);
    }

    /**
     * Utility to get the sizes of each line.
     *
     * @return an array with the sizes from each line of {@link ShaderSource#sourceLines}
     */
    private int[] linesSizeArray() {
        int[] s = new int[sourceLines.length];
        for (int i = 0; i < s.length; i++) {
            s[i] = sourceLines[i].length();
        }
        return s;
    }

    /**
     *
     */
    private String[] sourceToLines(String source) {
        return source.split(System.getProperty("line.separator"));
    }

    /**
     *
     * @param id
     * @return
     */
    private boolean isValidType(int id) {
        switch (id) {
            case FRAGMENT_SHADER:
            case VERTEX_SHADER:
                return true;
            default:
                return false;
        }
    }
}
