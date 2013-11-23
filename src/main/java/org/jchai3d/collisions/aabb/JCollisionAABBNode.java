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
package org.jchai3d.collisions.aabb;

import org.jchai3d.collisions.JCollisionRecorder;
import org.jchai3d.collisions.JCollisionSettings;
import org.jchai3d.math.JVector3d;

/**
 *
 * JCollisionAABBNode is an abstract class that contains methods to set up
 * internal and leaf nodes of an AABB tree and to use them to detect for
 * collision with a line.
 *
 * @author Chris Sewell (original author)
 * @author Francois Conti (original author)
 * @author Marcos da Silva Ramos (Java adaptation)
 * 
 */
public abstract class JCollisionAABBNode {

    /**
     * The bounding box for this node.
     */
    protected JCollisionAABBBox bbox;
    /**
     * The depth of this node in the collision tree.
     */
    protected int depth;
    /**
     * Parent node of this node.
     */
    protected JCollisionAABBNode parent;
    /**
     * Child node of this node.
     */
    protected JCollisionAABBNode child;
    /**
     * The node type, used only for proper deletion right now.
     */
    protected JAABBNodeType nodeType;

    /**
     * Constructor of cCollisionAABBNode.
     */
    public JCollisionAABBNode() {
        parent = null;
        depth = 0;
        nodeType = (JAABBNodeType.AABB_NODE_GENERIC);
        bbox = new JCollisionAABBBox();
    }

    /**
     * Constructor of cCollisionAABBNode.
     */
    public JCollisionAABBNode(JAABBNodeType nodeType, int depth) {
        this();
        parent = null;
        this.nodeType = (nodeType);
        this.depth = depth;
    }

    //-----------------------------------------------------------------------
    // METHODS:
    //-----------------------------------------------------------------------
    //! Create a bounding box for the portion of the model at or below the node.
    public void fitBBox() {
        fitBBox(0.0);
    }

    //! Create a bounding box for the portion of the model at or below the node.
    public abstract void fitBBox(double a_radius);

    //! Draw the edges of the bounding box for this node, if at the depth -1.
    public void render() {
        render(-1);
    }

    public abstract void render(int depth);

    //! Determine whether line intersects mesh bounded by subtree rooted at node.
    public abstract boolean computeCollision(JVector3d segmentPointA,
            JVector3d segmentDirection,
            JCollisionAABBBox lineBox,
            JCollisionRecorder recorder,
            JCollisionSettings settings);

    //! Return true if this node contains the specified triangle tag.
    public abstract boolean containsTriangle(int tag);

    //! Set the parent of this node.
    public abstract void setParent(JCollisionAABBNode parent, boolean recusive);
    
    public void setChild(JCollisionAABBNode child) {
        this.child = child;
        this.child.setParent(this, false);
    }
}
