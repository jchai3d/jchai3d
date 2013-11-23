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

import javax.media.opengl.GL2;
import javax.media.opengl.GLContext;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.scenegraph.JChaiRenderMode;
import org.jchai3d.scenegraph.JGenericObject;

/**
 *
 * @author João Cerqueira
 * @author Marcos da Silva Ramos
 * @version 1.0.0
 */
public class JLabel extends JGenericObject {

    /**
     * Default constructor of JLabel.
     * 
     * Creates a new JLabel object with empty text, default
     * font set to helvetica and size of 12.
     */
    public JLabel() {

        string = "";
        font = JFont.createFont();
        font.setFontFace("helvetica12");
        fontColor = new JColorf(1.0f, 1.0f, 1.0f, 1.0f);
    }

    /**
     * Creates a new JLabel object with the given string,
     * font set to helvetica and size of 12.
     * 
     * @param string  the string that this JLabel object will display
     */
    public JLabel(String string) {
        this();
        this.string = string;
    }
    /**
     * Font type.
     */
    private JFont font;
    /**
     * Font color.
     */
    private JColorf fontColor;
    /**
     * String to be displayed.
     */
    private String string;

    /**
     * Render object in JOpenGL.
     */
    @Override
    public void render(final JChaiRenderMode renderMode) {
        //aRenderMode = 0;

        GL2 gl = GLContext.getCurrent().getGL().getGL2();

        /**
         * disable lighting properties
         */
        gl.glDisable(GL2.GL_LIGHTING);

        /**
         * render font color
         */
        gl.glColor4fv(fontColor.color, 0);

        /**
         * draw fonts
         */
        gl.glRasterPos2f(0, 0);

        /**
         * render string
         */
        font.renderString(string);

        /**
         * enable lighting properties
         */
        gl.glEnable(GL2.GL_LIGHTING);
    }

    /**
     * @return the font
     */
    public JFont getFont() {
        return font;
    }

    /**
     * @param font the font to set
     */
    public void setFont(JFont font) {
        this.font = font;
    }

    /**
     * @return the fontColor
     */
    public JColorf getFontColor() {
        return fontColor;
    }

    /**
     * @param fontColor the fontColor to set
     */
    public void setFontColor(JColorf fontColor) {
        this.fontColor = fontColor;
    }

    /**
     * @return the string
     */
    public String getString() {
        return string;
    }

    /**
     * @param string the string to set
     */
    public void setString(String string) {
        this.string = string;
    }
}
