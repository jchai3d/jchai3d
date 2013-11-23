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
package org.jchai3d.scenegraph;

import com.jogamp.opengl.util.gl2.GLUT;
import javax.media.opengl.glu.GLU;

/**
 * <p>Since that in Java we need to have objects for noth GLU and GLUT, this
 * class is the superclass for all class that use any of this objects</p>
 *
 * @author Marcos Ramos
 */
public class JGenericShape extends JGenericObject {

    /**
     * Reference to the JOGL GLU
     */
    GLU glu;
    
    /**
     * Reference to the JOGL GLUT
     */
    GLUT glut;

    /**
     * Creates a new JGenricShape and initializes both glu and glut objects
     */
    public JGenericShape() {
        super();
        glu = new GLU();
        glut = new GLUT();
    }
}
