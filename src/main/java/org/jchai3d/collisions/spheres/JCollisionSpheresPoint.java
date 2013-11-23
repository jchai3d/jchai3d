/**
 * This file is part of the JCHAI 3D visualization and haptics libraries.
 * Copyright (C) 2010 by JCHAI 3D. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License("GPL") version 2 as published by
 * the Free Software Foundation.
 *
 * For using the JCHAI 3D libraries with software that can not be combined with
 * the GNU GPL, and for taking advantage of the additional benefits of our
 * support services, please contact CHAI 3D about acquiring a Professional
 * Edition License.
 *
 * project <https://sourceforge.net/projects/jchai3d>
 */
package org.jchai3d.collisions.spheres;

import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheresPoint {

    protected JVector3d position;
    protected JPointMap edgeMap;

    public JCollisionSpheresPoint() {
        this(0, 0, 0);
    }

    public JCollisionSpheresPoint(double a_x,
            double a_y,
            double a_z) {
        position = new JVector3d(a_x, a_y, a_z);
    }

    public JCollisionSpheresPoint(JVector3d pos) {
        position = new JVector3d(pos);
    }
}
