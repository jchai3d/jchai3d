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

import javax.media.opengl.GL2;

/**
 * JColorb describes a color composed of 4 \e bytes.
 * @author jairo
 */
public class JColorb {
    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------

    //! Color in \e GLubyte format [R,G,B,A].
    private byte[] mColor = new byte[4];

    /* acesso as rotinas graficas opengl */
    private GL2 gl;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    //-----------------------------------------------------------------------
    /*!
    Constructor of cColorb.
     */
    //-----------------------------------------------------------------------
    public JColorb() {
        // initialize color components R,G,B,A
        mColor[0] = (byte) 0xff;
        mColor[1] = (byte) 0xff;
        mColor[2] = (byte) 0xff;
        mColor[3] = (byte) 0xff;
    }

    /**
     * Constructor of cColorb. Define a color by passing its RGBA components.
    as parameters.
     * 
     * @param aRed
     * @param aGreen
     * @param aBlue
     * @param aAlpha
     */
    public JColorb(final byte aRed, final byte aGreen, final byte aBlue,
            final byte aAlpha) {
        mColor[0] = aRed;
        mColor[1] = aGreen;
        mColor[2] = aBlue;
        mColor[3] = aAlpha;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    /**
     * Set a color by passing its RGBA components as parameters.
     * 
     * @param aRed
     * @param aGreen
     * @param aBlue
     * @param aAlpha
     */
    public void set(final byte aRed, final byte aGreen, final byte aBlue,
            final byte aAlpha) {
        mColor[0] = aRed;
        mColor[1] = aGreen;
        mColor[2] = aBlue;
        mColor[3] = aAlpha;
    }

    /**
     * Set color by copying three floats from an external array, each
    describing an RGB component. Alpha is set to \e 0xff.
     * 
     * @param aColorRGB
     */
    public void setMem3(final byte[] aColorRGB) {
        mColor[0] = aColorRGB[0];
        mColor[1] = aColorRGB[1];
        mColor[2] = aColorRGB[2];
        mColor[3] = (byte) 0xff;
    }

    /**
     *Set color by copying four floats from an external array, each
    describing an RGBA component.
     * 
     * @param aColorRGBA
     */
    public void setMem4(final byte[] aColorRGBA) {
        mColor[0] = aColorRGBA[0];
        mColor[1] = aColorRGBA[1];
        mColor[2] = aColorRGBA[2];
        mColor[3] = aColorRGBA[3];
    }

    /**
     * Set the \e red component.
     * @param aRed
     */
    void setR(final byte aRed) {
        mColor[0] = aRed;
    }

    /**
     * Read the \e red component.
     * 
     * @return
     */
    public final float getR() {
        return (mColor[0]);
    }

    /**
     * Set the \e green component.
     * @param aGreen
     */
    public void setG(final byte aGreen) {
        mColor[1] = aGreen;
    }

    /**
     * Read the \e green component.
     * 
     * @return
     */
    public final float getG() {
        return (mColor[1]);
    }

    /**
     * Set the \e blue component.
     * 
     * @param aBlue
     */
    public void setB(final byte aBlue) {
        mColor[2] = aBlue;
    }

    /**
     * Read the \e blue component.
     * 
     * @return
     */
    public final float getB() {
        return (mColor[2]);
    }

    /**
     * Set the \e alpha component.
     * @param aAlpha
     */
    public void setA(final byte aAlpha) {
        mColor[3] = aAlpha;
    }

    /**
     * Read the \e alpha component.
     * 
     * @return
     */
    public final float getA() {
        return (mColor[3]);
    }

    /**
     * Render this color in OpenGL (sets it to be the current color).
    Does not confirm that GL color-tracking is enabled.
     */
    public final void render() {
        gl.glColor4bv(mColor, 0);
    }

    /**
     * Return a pointer to the raw color array.
     * @return
     */
    public final byte[] pColor() {
        return (mColor);
    }

    /**
     * Convert float to byte
     * @return
     */
    public JColorf getjColorf() {
        return new JColorf(mColor[0], mColor[1], mColor[2], mColor[3]);
    }
}

