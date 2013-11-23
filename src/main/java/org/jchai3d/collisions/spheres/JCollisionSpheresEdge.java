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

import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * @author Usuário
 */
public class JCollisionSpheresEdge {

    //! The two vertices of the edge.
    JCollisionSpheresPoint[] end;
    //! The center of the edge.
    JVector3d center;
    //! The distance between the vertices.
    JVector3d difference;
    //! The 2-norm of the edge.
    double squaredDistance;
    
    public JCollisionSpheresEdge(JCollisionSpheresPoint a, JCollisionSpheresPoint b) {
        initialize(a, b);
    }
    
    //! Initialization.
    void initialize(JCollisionSpheresPoint a, JCollisionSpheresPoint b) {
        // set the endpoints of the new edge
        end = new JCollisionSpheresPoint[2];
        end[0] = a;
        end[1] = b;

        // insert the edge into the edge maps of both endpoints
        end[0].edgeMap.add(end[1], this);
        end[1].edgeMap.add(end[0], this);

        // calculate the vector between the endpoints
        difference = JMaths.jSub((end[1]).position, (end[0]).position);

        // calculate the squared distance of the edge
        squaredDistance = difference.lengthsq();

        // calculate the center of the edge
        double lambda = 0.5;
        center = new JVector3d();
        center.x = (end[0]).position.x + lambda * ((end[1]).position.x - (end[0]).position.x);
        center.y = (end[0]).position.y + lambda * ((end[1]).position.y - (end[0]).position.y);
        center.z = (end[0]).position.z + lambda * ((end[1]).position.z - (end[0]).position.z);
    }

    //! Return the center of the edge.
    public JVector3d getCenter() {
        return center;
    }

    //! Return the radius of the edge.
    public double getRadius() {
        if (squaredDistance <= 0.0) {
            return 0.0;
        }
        return Math.sqrt(squaredDistance) / 2;
    }
}
