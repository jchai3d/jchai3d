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
package org.jchai3d.widgets;

import com.jogamp.opengl.util.awt.ImageUtil;
import com.jogamp.opengl.util.texture.Texture;
import com.jogamp.opengl.util.texture.TextureData;
import com.jogamp.opengl.util.texture.TextureIO;
import com.jogamp.opengl.util.texture.awt.AWTTextureIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.scenegraph.JChaiRenderMode;
import org.jchai3d.scenegraph.JGenericObject;

/**
 *
 * The JBitmap class displays a 2D image into the scene.
 *
 * @author João Cerqueira
 * @author Marcos da Silva Ramos
 * @version 1.0.0
 */
public class JBitmap extends JGenericObject {

    /**
     *
     */
    protected boolean markForUpdate;
    /**
     * Image loader.
     */
    protected TextureData data;
    /**
     * Image stream.
     */
    protected InputStream imageInputStream;
    /**
     * zoom factors.
     */
    protected float horizontalZoom, verticalZoom;

    /**
     * Constructor of JBitmap.
     */
    public JBitmap() {

        /**
         * initialize zoom factors
         */
        horizontalZoom = 1.0f;
        verticalZoom = 1.0f;

        /**
         * do not used transparency
         */
        transparencyEnabled = false;

        markForUpdate = false;
    }

    /**
     * Render texture in JOpenGL2.
     */
    @Override
    public void render(final JChaiRenderMode aRenderMode) {

        GL2 gl = GLContext.getCurrent().getGL().getGL2();

        if (markForUpdate) {
            updateBitmap();
        }

        gl.glDisable(GL2.GL_LIGHTING);

        /**
         * transparency is used
         */
        if (transparencyEnabled) {
            gl.glEnable(GL2.GL_BLEND);
            gl.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
            gl.glDepthMask(false);
        } /**
         * transparency is not used
         */
        else {
            gl.glDisable(GL2.GL_BLEND);
            gl.glDepthMask(true);
        }

        if (data != null) {
            gl.glRasterPos2i(1, 1);
            gl.glPixelZoom(horizontalZoom, verticalZoom);
            gl.glPixelStorei(GL2.GL_UNPACK_ALIGNMENT, 1);
            gl.glPixelStorei(GL2.GL_UNPACK_ROW_LENGTH, 0);

            gl.glDrawPixels(data.getWidth(), data.getHeight(),
                    GL2.GL_RGBA, GL2.GL_UNSIGNED_BYTE,
                    data.getBuffer());

            gl.glPixelZoom(1.0f, 1.0f);

        }

        /**
         * restore JOpenGL state
         */
        gl.glEnable(GL2.GL_LIGHTING);
        gl.glDisable(GL2.GL_BLEND);
        gl.glDepthMask(true);
    }

    /**
     * Loads a image file to this JBitmap object.
     */
    public void load(String path) throws IOException {
        load(new File(path));
    }

    /**
     * Loads a image file to this JBitmap object.
     */
    public void load(File file) throws IOException {
        load(new FileInputStream(file));
    }

    public void load(InputStream stream) {
        if (stream != null) {
            this.imageInputStream = stream;
            markForUpdate = true;
        }
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

        markForUpdate = false;
    }

    /**
     * Set zoom factors.
     */
    public void setZoom(float aZoomHorizontal, float aZoomVertical) {
        horizontalZoom = aZoomHorizontal;
        verticalZoom = aZoomVertical;
    }

    /**
     * Get zoom factor along horizontal axis.
     */
    public float getHorizontalZoom() {
        return (horizontalZoom);
    }

    /**
     * Get zoom factor along vertical axis.
     */
    public float getVerticalZoom() {
        return (verticalZoom);
    }
}
