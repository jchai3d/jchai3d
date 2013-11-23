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

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.GL;
import javax.media.opengl.GLContext;

/**
 *
 * @author João Cerqueira
 * @author Marcos da Silva Ramos
 * @version 1.0.0
 */
public abstract class JFont {

    public static final String CHAI_DEFAULT_FONT_FACE = "Arial";
    public static final float CHAI_DEFAULT_FONT_SIZE = 12.0f;
    //! The point size of the Font.
    float pointSize;
    //! The Font face name.
    String fontFace;
    //! The width of each character in our Font.
    int charWidths[];
    protected GLUT glut;

    //-----------------------------------------------------------------------
    // CONSTRUCTOR & DESTRUCTOR:
    //-----------------------------------------------------------------------
    //! Constructor of cFont.
    public JFont() {
        fontFace = CHAI_DEFAULT_FONT_FACE;
        pointSize = CHAI_DEFAULT_FONT_SIZE;

        /*
         * cria contexto glut
         */
        glut = new GLUT();
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    //! Use this to obtain an actual, non-public Font object.
    public static JFont createFont() {
        return new JGLUTBitmapFont();
    }

    //! Use this to copy data from an existing Font object.
    public static JFont createFont(JFont oldFont) {
        JFont font = createFont();

        if (font == null || oldFont == null) {
            return null;
        }
        font.fontFace = oldFont.fontFace;
        font.pointSize = oldFont.pointSize;
        font.charWidths = oldFont.charWidths;



        return font;
    }

    //! Renders a single-line string.
    public abstract int renderString(String str);

    //! Change the Font face; may require re-initializing the Font.
    public void setFontFace(String aFaceName) {
        if (aFaceName != null) {
            fontFace = aFaceName.toLowerCase();
        }

    }

    //! Get the current Font face.
    public String getFontFace() {
        return fontFace;
    }

    //! Change the Font size; may require re-initializing the font.
    public void setPointSize(float aPointSize) {
        pointSize = aPointSize;
    }

    //! Get the current Font size.
    public float getPointSize() {
        return pointSize;
    }

    //! Get the width of a particular character.
    public int getCharacterWidth(char aChar) {
        if (aChar < 32) {
            return -1;
        }
        int w = charWidths[aChar - 32];
        return w;
    }
}

class JGLUTBitmapFont extends JFont {

    enum GlutFontFamilies {

        GLUT_FONT_FAMILY_COURIER,
        GLUT_FONT_FAMILY_TIMES,
        GLUT_FONT_FAMILY_HELVETICA
    };
    // Constants needed for GLUT fonts
    int[] glutBitmapFonts = {
        GLUT.BITMAP_9_BY_15,
        GLUT.BITMAP_8_BY_13,
        GLUT.BITMAP_TIMES_ROMAN_10,
        GLUT.BITMAP_TIMES_ROMAN_24,
        GLUT.BITMAP_HELVETICA_10,
        GLUT.BITMAP_HELVETICA_12,
        GLUT.BITMAP_HELVETICA_18
    };
    String[] glutBitmapFontNames = {
        "GLUT_BITMAP_9_BY_15",
        "GLUT_BITMAP_8_BY_13",
        "GLUT_BITMAP_TIMES_ROMAN_10",
        "GLUT_BITMAP_TIMES_ROMAN_24",
        "GLUT_BITMAP_HELVETICA_10",
        "GLUT_BITMAP_HELVETICA_12",
        "GLUT_BITMAP_HELVETICA_18"
    };
    int[] glutStrokeFonts = {
        GLUT.STROKE_ROMAN,
        GLUT.STROKE_MONO_ROMAN
    };
    String[] glutStrokeFontNames = {
        "GLUT_STROKE_ROMAN",
        "GLUT_STROKE_MONO_ROMAN"
    };

    protected int getBestFontMatch() {
        int toReturn = -1;

        GlutFontFamilies family;

        // First, which font family are we?
        if (fontFace.equals("times")) {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_TIMES;
        } else if (fontFace.equals("roman")) {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_TIMES;
        } else if (fontFace.equals("helvetica")) {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_HELVETICA;
        } else if (fontFace.equals("arial")) {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_HELVETICA;
        } else if (fontFace.equals("courier")) {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_COURIER;
        } else {
            family = GlutFontFamilies.GLUT_FONT_FAMILY_HELVETICA;
        }

        // Now what size?
        switch (family) {
            case GLUT_FONT_FAMILY_HELVETICA:

                if (pointSize <= 10) {
                    toReturn = 4;
                } else if (pointSize <= 15) {
                    toReturn = 5;
                } else {
                    toReturn = 6;
                }
                break;

            case GLUT_FONT_FAMILY_TIMES:

                if (pointSize <= 16) {
                    toReturn = 2;
                } else {
                    toReturn = 3;
                }
                break;

            case GLUT_FONT_FAMILY_COURIER:

                if (pointSize <= 14) {
                    toReturn = 0;
                } else {
                    toReturn = 1;
                }
                break;

            default:
                toReturn = 0;
                break;
        }

        return toReturn;
    }

    public int renderString(String aStr) {
        int index = getBestFontMatch();
        GL gl = GLContext.getCurrent().getGL();
        glut.glutBitmapString(glutBitmapFonts[index], aStr);
        gl.glGetError();
        return 0;
    }

    public int getCharacterWidth(char aChar) {
        int fontIndex = getBestFontMatch();
        return glut.glutBitmapWidth(glutBitmapFonts[fontIndex], aChar);
    }
}
