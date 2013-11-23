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

import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public abstract class JCollisionSpheresGenericShape implements Comparable<JCollisionSpheresGenericShape> {

    JCollisionSpheresLeaf sphere;
    
    public static int split = 0;

    public JCollisionSpheresGenericShape() {
        sphere = null;
    }
    
    //! Return center.
    public abstract JVector3d getCenter();

    //! Return radius.
    public abstract double getRadius();

    //! Determine whether this primitive intersects the given primitive.
    public abstract boolean computeCollision(JCollisionSpheresGenericShape other,
            JCollisionRecorder recorder,
            JCollisionSettings settings);

    //! Return pointer to bounding sphere of this primitive shape.
    public JCollisionSpheresLeaf getSphere() {
        return sphere;
    }

    //! Set pointer for the bounding sphere of this primitive shape.
    public void setSphere(JCollisionSpheresLeaf sphere) {
        this.sphere = sphere;
    }

    public int compareTo(JCollisionSpheresGenericShape other) {
        if((getCenter().get(split) < other.getCenter().get(split))) {
            return -1;
        }
        else if((getCenter().get(split) > other.getCenter().get(split))) {
            return 1;
        }
        return 0;
    }
}
