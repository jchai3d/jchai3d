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
 *   version   1.0.0
 */
package org.jchai3d.graphics;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.imageio.ImageIO;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import com.jogamp.opengl.glu.GLU;

/**
 * JTexture2D describes a 2D bitmap texture used for OpenGL texture-mapping
 *
 * @author Francois Conti (originak author)
 * @author Dan Morris (original author)
 * @author Marcos da Silva Ramos<marco.9306@gmail.com> (Java adaptation)
 */
public class JTexture2D {

    /**
     * If \b true, texture bitmap has not yet been sent to video card.
     */
    protected boolean updateTextureFlag;
    /**
     * OpenGL texture ID number.
     */
    protected int textureID;
    /**
     * Texture wrap parameter along S (\e GL_REPEAT or \e GL_CLAMP).
     */
    protected int wrapSmode;
    /**
     * Texture wrap parameter along T (\e GL_REPEAT or \e GL_CLAMP).
     */
    protected int wrapTmode;
    /**
     * Texture magnification function. (\e GL_NEAREST or \e GL_LINEAR).
     */
    protected int magnificationFunction;
    /**
     * Texture minifying function. (\e GL_NEAREST or \e GL_LINEAR).
     */
    protected int minifyingFunction;
    /**
     * If \b true, we use GLU to build mipmaps.
     */
    protected boolean mipMapEnabled;
    /**
     * If \b true, we use spherical mapping.
     */
    protected boolean sphericalMappingEnabled;
    /**
     * OpenGL texture mode (\e GL_MODULATE, \e GL_DECAL, \e GL_BLEND, \e
     * GL_REPLACE).
     */
    protected int environmentMode;
    /**
     *
     */
    protected TextureData data;
    /**
     *
     */
    protected InputStream imageInputStream;
    /**
     * Environmental color.
     */
    protected JColorf color;

    /**
     *
     */
    public JTexture2D() {
        reset();
    }

    /**
     * Loads a image file to this JBitmap object.
     */
    public boolean load(String path) throws IOException {
        return load(new File(path));
    }

    /**
     * Loads a image file to this JBitmap object.
     */
    public boolean load(File file) throws IOException {
        return load(new FileInputStream(file));
    }

    public boolean load(InputStream stream) {
        if (stream != null) {
            this.imageInputStream = stream;
            updateTextureFlag = true;
            return true;
        }
        return false;
    }

    private void updateBitmap() {
        try {
           
            BufferedImage img = ImageIO.read(imageInputStream);
            data = AWTTextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), img, false);
            
            if (data.getMustFlipVertically()) {
                ImageUtil.flipImageVertically(img);
                data = AWTTextureIO.newTextureData(GLContext.getCurrentGL().getGLProfile(), img, false);
            }
        } catch (IOException ex) {
            //TODO this is a very, very bad practice...
            throw new RuntimeException(ex.getMessage());
        }

        updateTextureFlag = false;
    }

    /**
     *
     */
    public void render() {

        GL2 gl = GLContext.getCurrent().getGL().getGL2();

        // Only check residency in memory if we weren't going to
        // update the texture anyway...
        if (!updateTextureFlag) {
            byte[] texture_is_resident = new byte[1];
            gl.glAreTexturesResident(1, new int[]{textureID}, 0, texture_is_resident, 0);

            if (texture_is_resident[0] == 0) {
                updateTextureFlag = true;
            }
        }

        // is texture being rendered for the first time?
        if (updateTextureFlag) {
            update();
            updateTextureFlag = false;
        }

        // enable texturing
        gl.glEnable(GL2.GL_TEXTURE_2D);

        // enable or disable spherical mapping
        if (sphericalMappingEnabled) {
            gl.glEnable(GL2.GL_TEXTURE_GEN_S);
            gl.glEnable(GL2.GL_TEXTURE_GEN_T);
            gl.glTexGeni(GL2.GL_S, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
            gl.glTexGeni(GL2.GL_T, GL2.GL_TEXTURE_GEN_MODE, GL2.GL_SPHERE_MAP);
        } else {
            gl.glDisable(GL2.GL_TEXTURE_GEN_S);
            gl.glDisable(GL2.GL_TEXTURE_GEN_T);
        }

        // Sets the wrap parameter for texture coordinate s to either
        // GL2.GL_CLAMP or GL2.GL_REPEAT.
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_S, wrapSmode);
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_WRAP_T, wrapTmode);

        // Set the texture magnification function to either GL2.GL_NEAREST or GL2.GL_LINEAR.
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MAG_FILTER, magnificationFunction);

        // Set the texture minifying function to either GL2.GL_NEAREST or GL2.GL_LINEAR.
        gl.glTexParameteri(GL2.GL_TEXTURE_2D, GL2.GL_TEXTURE_MIN_FILTER, minifyingFunction);

        // set the environment mode (GL2.GL_MODULATE, GL2.GL_DECAL, GL2.GL_BLEND, GL2.GL_REPLACE)
        gl.glTexEnvi(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_MODE, environmentMode);

        // make this the current texture
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureID);

        // set the environmental color
        gl.glTexEnvfv(GL2.GL_TEXTURE_ENV, GL2.GL_TEXTURE_ENV_COLOR, color.color, 0);
    }

    /**
     * Call this to force texture re-initialization.
     */
    public void markForUpdate() {
        updateTextureFlag = true;
    }

    /**
     * Reset internal variables. This function should be called only by
     * constructors.
     */
    private void reset() {
        // id number provided by OpenGL once texture is stored in graphics
        // card memory
        textureID = -1;

        // texture has not yet been rendered
        updateTextureFlag = true;

        // Tile the texture in X. (GL_REPEAT or GL_CLAMP)
        wrapSmode = GL2.GL_REPEAT;

        // Tile the texture in Y. (GL_REPEAT or GL_CLAMP)
        wrapTmode = GL2.GL_REPEAT;

        // set the magnification function. (GL_NEAREST or GL_LINEAR)
        magnificationFunction = GL2.GL_NEAREST;

        // set the minifying function. (GL_NEAREST or GL_LINEAR)
        minifyingFunction = GL2.GL_NEAREST;

        // set environmental mode (GL_MODULATE, GL_DECAL, GL_BLEND, GL_REPLACE)
        environmentMode = GL2.GL_MODULATE;

        // set environmental color
        color = new JColorf(1f, 1f, 1f, 0f);

        // set spherical mode
        sphericalMappingEnabled = false;

        // use mipmaps
        mipMapEnabled = false;
    }

    /**
     * Initialize GL texture.
     */
    private void update() {
        
        // update bitmap, loading from file if necessary
        updateBitmap();
        
        GL gl = GLContext.getCurrent().getGL();
        if (textureID != -1) {
            // Deletion can make for all kinds of new hassles, particularly
            // when re-initializing a whole display context, since opengl
            // automatically starts re-assigning texture ID's.
            gl.glDeleteTextures(1, new int[]{textureID}, 0);
            textureID = -1;
        }

        // Generate a texture ID and bind to it
        gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, data.getAlignment());
        int[] tmp = new int[1];
        gl.glGenTextures(1, tmp, 0);
        textureID = tmp[0];
        gl.glBindTexture(GL2.GL_TEXTURE_2D, textureID);

        if (mipMapEnabled) {
            GLU glu = new GLU();
            glu.gluBuild2DMipmaps(
                    GL2.GL_TEXTURE_2D,
                    data.getInternalFormat(),
                    data.getWidth(),
                    data.getHeight(),
                    data.getPixelFormat(),
                    data.getPixelType(),
                    data.getBuffer());

        } else {
            gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, data.getAlignment());
            ////gl.glPixelStorei(GL2.GL_UNPACK_ROW_LENGTH, 0);
            //gl.glPixelStorei(GL2.GL_UNPACK_SKIP_ROWS, 0);
            //gl.glPixelStorei(GL2.GL_UNPACK_SKIP_PIXELS, 0);

            gl.glTexImage2D(
                    GL2.GL_TEXTURE_2D,
                    0,
                    data.getInternalFormat(),
                    data.getWidth(),
                    data.getHeight(),
                    data.getBorder(),
                    data.getPixelFormat(),
                    data.getPixelType(),
                    data.getBuffer());
        }
    }

    /**
     * @return the updateTextureFlag
     */
    public boolean isUpdateTextureFlag() {
        return updateTextureFlag;
    }

    /**
     * @param updateTextureFlag the updateTextureFlag to set
     */
    public void setUpdateTextureFlag(boolean updateTextureFlag) {
        this.updateTextureFlag = updateTextureFlag;
    }

    /**
     * @return the textureID
     */
    public int getTextureID() {
        return textureID;
    }

    /**
     * @return the wrapSmode
     */
    public int getWrapSmode() {
        return wrapSmode;
    }

    /**
     * @param wrapSmode the wrapSmode to set
     */
    public void setWrapSmode(int wrapSmode) {
        this.wrapSmode = wrapSmode;
    }

    /**
     * @return the wrapTmode
     */
    public int getWrapTmode() {
        return wrapTmode;
    }

    /**
     * @param wrapTmode the wrapTmode to set
     */
    public void setWrapTmode(int wrapTmode) {
        this.wrapTmode = wrapTmode;
    }

    /**
     * @return the magnificationFunction
     */
    public int getMagnificationFunction() {
        return magnificationFunction;
    }

    /**
     * @param magnificationFunction the magnificationFunction to set
     */
    public void setMagnificationFunction(int magnificationFunction) {
        this.magnificationFunction = magnificationFunction;
    }

    /**
     * @return the minifyingFunction
     */
    public int getMinifyingFunction() {
        return minifyingFunction;
    }

    /**
     * @param minifyingFunction the minifyingFunction to set
     */
    public void setMinifyingFunction(int minifyingFunction) {
        this.minifyingFunction = minifyingFunction;
    }

    /**
     * @return the mipMapEnabled
     */
    public boolean isMipMapEnabled() {
        return mipMapEnabled;
    }

    /**
     * @param mipMapEnabled the mipMapEnabled to set
     */
    public void setMipMapEnabled(boolean mipMapEnabled) {
        this.mipMapEnabled = mipMapEnabled;
    }

    /**
     * @return the sphericalMappingEnabled
     */
    public boolean isSphericalMappingEnabled() {
        return sphericalMappingEnabled;
    }

    /**
     * @param sphericalMappingEnabled the sphericalMappingEnabled to set
     */
    public void setSphericalMappingEnabled(boolean sphericalMappingEnabled) {
        this.sphericalMappingEnabled = sphericalMappingEnabled;
    }

    /**
     * @return the environmentMode
     */
    public int getEnvironmentMode() {
        return environmentMode;
    }

    /**
     * @param environmentMode the environmentMode to set
     */
    public void setEnvironmentMode(int environmentMode) {
        this.environmentMode = environmentMode;
    }

    /**
     * @return the data
     */
    public TextureData getTextureData() {
        return data;
    }

    /**
     * @return the color
     */
    public JColorf getColor() {
        return color;
    }

    /**
     * @param color the color to set
     */
    public void setColor(JColorf color) {
        this.color = color;
    }
}
