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

import javax.media.opengl.GLContext;
import org.jchai3d.math.JMaths;

/**
 * 
 * JColorf describes a color composed of 4 \e GLfloats.
 * 
 * @author Jairo Simão Santana Melo <jairossmunb@gmail.com>
 * @author Marcos Ramos <marcos.9306@gmail.com>
 */
public class JColorf{

    //-----------------------------------------------------------------------
    // MEMBERS:
    //-----------------------------------------------------------------------
    //! Color in \e GLfloat format [R,G,B,A].
    public float[] color = new float[4];

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    public JColorf() {
        // initialize color components R,G,B,A
        color[0] = 1.0f;
        color[1] = 1.0f;
        color[2] = 1.0f;
        color[3] = 1.0f;
    }

    /**
     * Constructor of cColorf. Define a color by passing its RGBA components
    as parameters.
     * 
     * @param aRed the red component of this color
     * @param aGreen the green component of this color
     * @param aBlue the blue component of this color
     * @param aAlpha the alpha component of this color
     */
    public JColorf(final float aRed, final float aGreen, final float aBlue,
            final float aAlpha) {
        color[0] = JMaths.jClamp(aRed, 0.0f, 1.0f);
        color[1] = JMaths.jClamp(aGreen, 0.0f, 1.0f);
        color[2] = JMaths.jClamp(aBlue, 0.0f, 1.0f);
        color[3] = JMaths.jClamp(aAlpha, 0.0f, 1.0f);
    }
    /**
     * Constructor of cColorf. Define a color by passing its RGB components
    as parameters. The alpha component will be setted as 1.0
     * 
     * @param aRed the red component of this color
     * @param aGreen the green component of this color
     * @param aBlue the blue component of this color
     */
    public JColorf(final float aRed, final float aGreen, final float aBlue) {
        color[0] = JMaths.jClamp(aRed, 0.0f, 1.0f);
        color[1] = JMaths.jClamp(aGreen, 0.0f, 1.0f);
        color[2] = JMaths.jClamp(aBlue, 0.0f, 1.0f);
        color[3] = 1.0f;
    }

    public JColorf(String s) {
        String[] tmp = s.trim().split(" ");
        color[0] = JMaths.jClamp(Float.parseFloat(tmp[0]), 0f, 1f);
        color[1] = JMaths.jClamp(Float.parseFloat(tmp[1]), 0f, 1f);
        color[2] = JMaths.jClamp(Float.parseFloat(tmp[2]), 0f, 1f);
        if(tmp.length == 4) {
            color[3] = JMaths.jClamp(Float.parseFloat(tmp[3]), 0f, 1f);
        }
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
    public void set(final float aRed, final float aGreen, final float aBlue,
            final float aAlpha) {
        color[0] = JMaths.jClamp(aRed, 0.0f, 1.0f);
        color[1] = JMaths.jClamp(aGreen, 0.0f, 1.0f);
        color[2] = JMaths.jClamp(aBlue, 0.0f, 1.0f);
        color[3] = JMaths.jClamp(aAlpha, 0.0f, 1.0f);
    }


    /**
     * Access the nth component of this color (we provide both const
    and non-const versions so you can use this operator as an l-value
    or an r-value).
     */
    /*inline GLfloat operator[](const unsigned int n) const
    {
    if (n<4) return m_color[n];
    else return 0.0f;
    }*/
    /**
     * Access the nth component of this color (we provide both const
    and non-const versions so you can use this operator as an l-value
    or an r-value).
     */
    /*inline GLfloat& operator[](const unsigned int n)
    {
    if (n<4) return m_color[n];
    else return m_color[0];
    }*/
    /**
     * Set the \e red component.
     * @param aRed
     */
    public void setR(final float aRed) {
        color[0] = JMaths.jClamp(aRed, 0.0f, 1.0f);
    }

    /**
     * Read the \e red component.
     * 
     * @return
     */
    public final float getR() {
        return (color[0]);
    }

    /**
     * Set the \e green component.
     * 
     * @param aGreen
     */
    public void setG(final float aGreen) {
        color[1] = JMaths.jClamp(aGreen, 0.0f, 1.0f);
    }

    /**
     * Read the \e green component.
     * 
     * @return
     */
    public final float getG() {
        return (color[1]);
    }

    /**
     * Set the \e blue component.
     * 
     * @param aBlue
     */
    public void setB(final float aBlue) {
        color[2] = JMaths.jClamp(aBlue, 0.0f, 1.0f);
    }

    /**
     * Read the \e blue component.
     * 
     * @return
     */
    public final float getB() {
        return (color[2]);
    }

    /**
     * Set the \e alpha component.
     * 
     * @param aAlpha
     */
    public void setA(final float aAlpha) {
        color[3] = JMaths.jClamp(aAlpha, 0.0f, 1.0f);
    }

    /**
     * Read the \e alpha component.
     * 
     * @return
     */
    public final float getA() {
        return (color[3]);
    }

    /**
     * Render this color in OpenGL (sets it to be the current color).

    Does not confirm that GL color-tracking is enabled.
     */
    public final void render() {
        GLContext.getCurrent().getGL().getGL2().glColor4fv(color, 0);
    }

    /**
     * Returns a pointer to the raw color array.
     * 
     * @return
     */
    public final float[] getComponents() {
        return (color);
    }

    public void copyFrom(JColorf c) {
        color[0] = c.color[0];
        color[1] = c.color[1];
        color[2] = c.color[2];
        color[3] = c.color[3];
    }

    /**
     * Convert byte to float
     * @return
     */
    public JColorb getjColorf() {
        return new JColorb((byte) color[0], (byte) color[1], (byte) color[2], (byte) color[3]);
    }

    @Override
    public String toString() {
        return "JColorf["+color[0]+", "+color[1]+", "+color[2]+", "+color[3]+"]";
    }


}
