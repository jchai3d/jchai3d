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
package org.jchai3d.scenegraph;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLContext;
import org.jchai3d.collisions.JCollisionBrute;
import org.jchai3d.collisions.spheres.JCollisionSpheres;
import org.jchai3d.collisions.aabb.JCollisionAABB;
import org.jchai3d.files.JMeshLoader;
import org.jchai3d.graphics.JColorf;
import org.jchai3d.graphics.JTriangle;
import org.jchai3d.graphics.JVertex;
import org.jchai3d.math.JConstants;
import org.jchai3d.math.JMaths;
import org.jchai3d.math.JVector3d;

/**
 *
 * JMesh represents a collection of vertices, triangls, materials, and texture
 * properties that can be rendered graphically and haptically.
 *
 *
 * @author Francois Conti (original author)
 * @author Dan Morris (original author)
 * @author Chris Sewell (original author)
 * @author Jairo Melo (java implementation)
 * @author Marcos da Silva Ramos (java implementation)
 * @version 1.0.0
 */
public class JMesh extends JGenericObject {

    /*
     * Parent world.
     */
    protected JWorld parentWorld;
    /**
     * If <b> true</b>, then normals are displayed.
     */
    protected boolean normalsVisible;
    /**
     * If <b> true</b>, normals are displayed only for vertices that are used in triangles.
     */
    protected boolean normalsVisibleForTriangleVerticesOnly;
    /**
     * Color used to render lines representing normals.
     */
    protected JColorf normalsColor;
    /**
     * Length of each normal (for graphic rendering of normals).
     */
    protected double normalsLength;
    /**
     * Should we use a display list to render this mesh?
     */
    protected boolean displayListEnabled;
    /**
     * Should we use vertex arrays to render this mesh?
     */
    protected boolean vertexArrayEnabled;
    /**
     * The openGL display list used to draw this mesh, if display lists are enabled.
     */
    protected int displayList;
    /**
     * Array of vertices.
     */
    protected ArrayList<JVertex> vertices;
    /**
     * List of free slots in the vertex array.
     */
    protected ArrayList<Integer> freeVertices;
    /**
     * Array of triangles.
     */
    protected ArrayList<JTriangle> triangles;
    /**
     * List of free slots in the triangle array.
     */
    protected ArrayList<Integer> freeTriangles;

    /**
     * 
     * @param world 
     */
    public JMesh(JWorld world) {
        this();
        if (world != null) {
            this.parentWorld = world;
        }
    }

    /**
     * Constructor of JMesh.
     */
    public JMesh() {
        super();

        parent = null;

        // should normals be displayed?
        normalsVisible = false;

        // if normals are displayed, this value defines their length.
        normalsLength = 0.1;

        // if normals are displayed, this defines their color
        normalsColor = new JColorf();
        normalsColor.set(1.0f, 0.0f, 0.0f, 0.0f);

        // should the frame (X-Y-Z) be displayed?
        //
        // Not by default... this is really a debugging tool.
        frameVisible = false;

        // set default collision detector
        triangles = new ArrayList<JTriangle>();
        collisionDetector = new JCollisionBrute(triangles);

        // by default, only triangl vertices have their normals rendered
        // if normal-rendering is enabled
        normalsVisibleForTriangleVerticesOnly = false;

        // Display lists disabled by default
        displayListEnabled = false;
        displayList = -1;

        // Vertex array disabled by default
        vertexArrayEnabled = false;

        vertices = new ArrayList<JVertex>();
        freeTriangles = new ArrayList<Integer>();
        freeVertices = new ArrayList<Integer>();
    }

    /**
     * Get parent world.
     */
    public final JWorld getParentWorld() {
        return (parentWorld);
    }

    /**
     * Set parent world.
     */
    public void setParentWorld(JWorld aWorld) {
        parentWorld = aWorld;
    }

    /**
     * Load a 3D object file (CHAI currently supports .obj and .3ds files).
     */
    public boolean loadFromFile(final File file) throws FileNotFoundException, IOException, URISyntaxException {
        return (JMeshLoader.loadMeshFromFile(this, file));
    }

    /**
     * Create a new vertex and add it to the vertex list.
     */
    /**
     * Create a new vertex and add it to the vertex list.
     *
     * @param aX
     * @param aY
     * @param aZ
     * @return
     */
    public int newVertex(final double aX, final double aY, final double aZ) {
        int index;

        // check if there is any available vertex on the free list
        if (freeVertices.size() > 0) {
            index = getFreeVertices().get(0);
            freeVertices.remove(getFreeVertices().get(0));
        } // No vertex is available on the free list so create a new one from the array
        else {
            // allocate new vertex
            index = vertices.size();
            JVertex newVertex = new JVertex(aX, aY, aZ);
            newVertex.setIndex(index);
            vertices.add(newVertex);
        }

        // return the index at which I inserted this vertex in my vertex array
        return index;
    }

    /**
     * Create a new vertex and add it to the vertex list.
     */
    public int newVertex(final JVector3d aPos) {
        return (newVertex(aPos.getX(), aPos.getY(), aPos.getZ()));
    }

    /**
     * Create a new vertex for each supplied position and add it to the vertex
     * list.
     *
     * @param aVertexPositions
     * @param aNumVertices
     */
    public void addVertices(ArrayList<JVector3d> aVertexPositions) {
        for (int i = 0; i < aVertexPositions.size(); i++) {
            newVertex(aVertexPositions.get(i));
        }
    }

    /**
     *
     * @param vertex
     */
    public void addVertex(JVertex vertex) {
        vertices.add(vertex);
    }

    /**
     * Remove the vertex at the specified position in my vertex array
     *
     * @param aIndex
     * @return
     */
    public boolean removeVertex(final int aIndex) {
        // get vertex to be removed
        JVertex vertex = getVertices().get(aIndex);

        // check if it has not already been removed
        if (!vertex.isAllocated()) {
            return false;
        }

        // deactivate vertex
        vertex.setAllocated(false);

        // add vertex to free list
        getFreeVertices().add(aIndex);

        // return success
        return (true);
    }

    /**
     * Returns the specified vertex... if aIncludeChildren is false, I just
     * index into my vertex array (no boundary checking, since this is called
     * often).
     *
     * If aIncludeChildren is true, I start counting through my own vertex
     * array, then each of my children... in the process, I'm going to call
     * getNumVertices(true) on each of my children, so this is a recursive and
     * unbounded (though generally fast) version of this method.
     *
     * @param aIndex
     * @param aIncludeChildren
     * @return
     */
    public final JVertex getVertex(int aIndex, boolean aIncludeChildren) {
        // The easy case...
        if (aIncludeChildren == false) {
            return (vertices.get(aIndex));
        }

        // Now we have to possibly search through children...

        // First do a sanity check to make sure this is a reasonable
        // vertex...
        if (aIndex >= getNumVertices(true)) {
            return null;
        }

        // Get number of vertices of current object
        int numVertices = vertices.size();

        if (aIndex < numVertices) {
            return (vertices.get(aIndex));
        }

        // Okay, this vertex must live in a child mesh... subtract
        // my own vertices from the number we have to search through
        aIndex -= numVertices;

        int i, numChildren;
        numChildren = childrens.size();
        for (i = 0; i < numChildren; i++) {
            JGenericObject nextObject = childrens.get(i);

            // check if nextObject is a mesh.
            JMesh nextMesh = (JMesh) (nextObject);
            if (nextMesh != null) {
                // How many vertices does he have?
                numVertices = nextMesh.getNumVertices(true);

                // If this index lives in This_ child...
                if (aIndex < numVertices) {
                    return nextMesh.getVertex(aIndex, true);
                } // Otherwise keep going...
                else {
                    aIndex -= numVertices;
                }

            } // ...if this child was a mesh
        } // ...for each child

        // We didn't find this vertex... this should never happen, since we
        // sanity-checked at the beginning of this method...
        return null;
    }

    /**
     * Read the number of stored vertices, optionally including those of my children.
     */
    public final int getNumVertices(boolean aIncludeChildren) {
        // get number of vertices of current object
        int numVertices = getVertices().size();

        // apply computation to children if specified
        if (aIncludeChildren) {
            int i, numItems;
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                // check if nextObject is a mesh.
                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    numVertices = numVertices + nextMesh.getNumVertices(aIncludeChildren);
                }
            }
        }

        // return result
        return (numVertices);
    }

    /**
     * Access my vertex list directly (use carefully).
     */
    public ArrayList<JVertex> pVertices() {
        return (vertices);
    }

    /**
     * Access the first vertex list in any of my children (use carefully)
     *
     * @return
     */
    public ArrayList<JVertex> pVerticesNonEmpty() {
        if (vertices.size() > 0) {
            return pVertices();
        } else {
            int i, numChildren;
            numChildren = getChildrens().size();
            for (i = 0; i < numChildren; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                // check if nextObject is a mesh.
                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    ArrayList<JVertex> pv = nextMesh.pVerticesNonEmpty();
                    if (pv != null) {
                        return pv;
                    }
                } // ...if this child was a mesh
            } // ...for each child
        }
        return null;
    }

    //-----------------------------------------------------------------------
    // METHODS - TRIANGLES
    //-----------------------------------------------------------------------
    /**
     * Create a new triangl by passing vertex indices. /** Create a new triangl
     * and three new vertices by passing vertex indices
     *
     * @param aIndexVertex0
     * @param aIndexVertex1
     * @param aIndexVertex2
     * @return
     */
    public int newTriangle(int aIndexVertex0,
            int aIndexVertex1, int aIndexVertex2) {
        int index;

        // check if there is an available slot on the free triangl list
        if (freeTriangles.size() > 0) {
            index = freeTriangles.get(0);
            triangles.get(index).setAllocated(true);
            freeTriangles.remove(0);
            triangles.get(index).setVertices(aIndexVertex0, aIndexVertex1, aIndexVertex2);
            triangles.get(index).setParent(this);
        } // No triangl is available from the free list so create a new one from the array
        else {
            // allocate new triangl
            index = triangles.size();
            JTriangle newTriangle = new JTriangle(this,
                    aIndexVertex0,
                    aIndexVertex1,
                    aIndexVertex2);
            newTriangle.setIndex(index);
            newTriangle.setAllocated(true);
            triangles.add(newTriangle);
        }


        vertices.get(aIndexVertex0).setAllocated(true);
        vertices.get(aIndexVertex0).setTriangleCount(vertices.get(aIndexVertex0).getTriangleCount() + 1);
        vertices.get(aIndexVertex1).setAllocated(true);
        vertices.get(aIndexVertex1).setTriangleCount(vertices.get(aIndexVertex1).getTriangleCount() + 1);
        vertices.get(aIndexVertex2).setAllocated(true);
        vertices.get(aIndexVertex2).setTriangleCount(vertices.get(aIndexVertex2).getTriangleCount() + 1);

        // return the index at which I inserted this triangl in my triangl array
        return (index);
    }

    /**
     * Create a new triangl and three new vertices by passing vertex positions
     *
     * @param aVertex0
     * @param aVertex1
     * @param aVertex2
     * @return
     */
    public int newTriangle(final JVector3d aVertex0, final JVector3d aVertex1,
            final JVector3d aVertex2) {
        // create three new vertices
        int indexVertex0 = newVertex(aVertex0);
        int indexVertex1 = newVertex(aVertex1);
        int indexVertex2 = newVertex(aVertex2);

        // create new triangl
        int indexTriangle = newTriangle(indexVertex0, indexVertex1, indexVertex2);

        // return index of new triangl.
        return (indexTriangle);
    }

    /**
     * Remove a triangl from my triangl array.
     */
    public boolean removeTriangle(final int aIndex) {
        // get triangl to be removed
        JTriangle triangle = triangles.get(aIndex);

        // check if it has not already been removed
        if (triangle.isAllocated() == false) {
            return (false);
        }

        // deactivate triangl
        triangle.setAllocated(false);

        getVertices().get(triangle.getIndexVertex0()).setTriangleCount(getVertices().get(triangle.getIndexVertex0()).getTriangleCount() + 1);
        getVertices().get(triangle.getIndexVertex1()).setTriangleCount(getVertices().get(triangle.getIndexVertex1()).getTriangleCount() + 1);
        getVertices().get(triangle.getIndexVertex2()).setTriangleCount(getVertices().get(triangle.getIndexVertex2()).getTriangleCount() + 1);

        // add triangl to free list
        getFreeTriangles().add(aIndex);

        // return success
        return (true);
    }

    /**
     * Returns the specified triangl... if aIncludeChildren is false, I just
     * index into my triangl array (no boundary checking, since this is called
     * often).
     *
     * If aIncludeChildren is true, I start counting through my own triangl
     * array, then each of my children... in the process, I'm going to call
     * getNumTriangles(true) on each of my children, so this is a recursive and
     * unbounded (though generally fast) version of this method.
     *
     * @param aIndex
     * @param aIncludeChildren
     * @return
     */
    public JTriangle getTriangle(int aIndex, boolean aIncludeChildren) {
        // The easy case...
        if (aIncludeChildren == false) {
            return (triangles.get(aIndex));
        }

        // Now we have to possibly search through children...

        // First do a sanity check to make sure this is a reasonable
        // triangl...
        if (aIndex >= getNumTriangles(true)) {
            return null;
        }

        // Get number of triangls of current object
        int numTriangles = triangles.size();

        if (aIndex < numTriangles) {
            return (triangles.get(aIndex));
        }

        // Okay, this triangl must live in a child mesh... subtract
        // my own triangls from the number we have to search through
        aIndex -= numTriangles;

        int i, numChildren;
        numChildren = getChildrens().size();
        for (i = 0; i < numChildren; i++) {
            JGenericObject nextObject = getChildrens().get(i);

            // check if nextObject is a mesh.
            JMesh nextMesh = (JMesh) (nextObject);
            if (nextMesh != null) {
                // How many triangls does he have?
                numTriangles = nextMesh.getNumTriangles(true);

                // If this index lives in This_ child...
                if (aIndex < numTriangles) {
                    return nextMesh.getTriangle(aIndex, true);
                } // Otherwise keep going...
                else {
                    aIndex -= numTriangles;
                }

            } // ...if this child was a mesh
        } // ...for each child

        // We didn't find this triangl... this should never happen, since we
        // sanity-checked at the beginning of this method...
        return null;
    }

    /**
     * Returns the number of triangls contained in this mesh, optionally
     * including its children.
     *
     * @param aIncludeChildren
     * @return
     */
    public final int getNumTriangles(boolean aIncludeChildren) {
        // get the number of triangl of current object
        int numTriangles = triangles.size();

        // optionally count the number of triangls in my children
        if (aIncludeChildren) {
            int i, numItems;
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                // check if nextobject is a mesh.
                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    numTriangles += nextMesh.getNumTriangles(aIncludeChildren);
                }
            }
        }

        // return result
        return (numTriangles);
    }

    /**
     * Clear all triangls and vertices of mesh.
     */
    public void clear() {
        // clear all triangls
        triangles.clear();

        // clear all vertices
        vertices.clear();

    }

    /**
     * Access my triangl array directly (use carefully). public
     * ArrayList<JTriangle> pTriangles() { return (triangles); }
     *
     * /** Set the alpha value at each vertex and in all of my material colors.
     */
    @Override
    public void setTransparencyLevel(final float aLevel,
            final boolean aApplyToTextures,
            final boolean aAffectChildren) {
        // if the transparency level is equal to 1.0, then do not apply transparency
        // otherwise enable it.

        super.setTransparencyLevel(aLevel, aApplyToTextures, aAffectChildren);

        // convert transparency level to cColorb format
        //byte level = (byte) (255.0f * aLevel);

        // apply the new value to all vertex colors
        int numItems = vertices.size();
        for (int i = 0; i < numItems; i++) {
            vertices.get(i).getColor().setA(aLevel);
        }
    }

    /**
     * Set color of each vertex, optionally propagating the operation to my children.
     */
    public void setVertexColor(final JColorf aColor, final boolean aAffectChildren) {

        // apply color to all vertex colors
        int i, numItems;
        numItems = getVertices().size();
        for (i = 0; i < numItems; i++) {
            vertices.get(i).setColor(aColor);
        }

        // update changes to children
        if (aAffectChildren) {
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.setVertexColor(aColor, aAffectChildren);
                }
            }
        }
    }

    /**
     * This enables the use of display lists for mesh rendering. This should
     * significantly speed up rendering for large meshes, but it means that any
     * changes you make to any cMesh options or any vertex positions will not
     * take effect until you invalidate the existing display list (by calling
     * invalidateDisplayList()).
     *
     * In general, if you aren't having problems with rendering performance,
     * don't bother with this; you don't want to worry about having to
     * invalidate display lists every time you change a tiny option.
     *
     * @param aUseDisplayList
     * @param aAffectChildren
     */
    public void useDisplayList(final boolean aUseDisplayList, final boolean aAffectChildren) {
        // update changes to object
        setDisplayListEnabled(aUseDisplayList);

        // propagate changes to children
        if (aAffectChildren) {
            int i, numItems;
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.useDisplayList(aUseDisplayList, aAffectChildren);
                }
            }
        }
    }

    /**
     * This enables the use of vertex arrays for mesh rendering. This mode can
     * be faster than the classical approach, however crashes sometime occur on
     * certain types of graphic cards.
     *
     * In general, if you aren't having problems with rendering performance,
     * don't bother with this.
     *
     * @param aUseVertexArrays
     * @param aAffectChildren
     */
    public void useVertexArrays(final boolean aUseVertexArrays, final boolean aAffectChildren) {
        // update changes to object
        vertexArrayEnabled = aUseVertexArrays;

        // propagate changes to children
        if (aAffectChildren) {
            int i, numItems;
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.useVertexArrays(aUseVertexArrays, aAffectChildren);
                }
            }
        }
    }

    /**
     * Ask whether I'm currently rendering with a display list.
     */
    public final boolean isDisplayListEnabled() {
        return displayListEnabled;
    }

    /**
     * Invalidate any existing display lists.
     */
    public void invalidateDisplayList(final boolean aAffectChildren) {

        // Delete my display list if necessary
        if (displayList != -1) {
            GLContext.getCurrent().getGL().getGL2().glDeleteLists(displayList, 1);
            displayList = -1;
        }

        // Propagate the operation to my children
        if (aAffectChildren) {
            int i, numItems;
            numItems = getChildrens().size();
            for (i = 0; i < numItems; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.invalidateDisplayList(aAffectChildren);
                }
            }
        }
    }

    /**
     * Enable or disable the rendering of vertex normals, optionally propagating
     * the operation to my children.
     */
    public void setShowNormals(final boolean aShowNormals, final boolean aAffectChildren, final boolean aTrianglesOnly) {
        setNormalsVisible(aShowNormals);
        setNormalsVisibleForTriangleVerticesOnly(aTrianglesOnly);

        // propagate changes to children
        if (aAffectChildren) {
            for (int i = 0; i < getChildrens().size(); i++) {
                ((JMesh) getChildrens().get(i)).setShowNormals(aShowNormals, aAffectChildren, false);
            }
        }
    }

    /**
     * Returns whether rendering of normals is enabled.
     */
    public final boolean getShowNormals() {
        return isNormalsVisible();
    }

    /**
     * Set graphic properties for normal-rendering, optionally propagating the
     * operation to my children.
     */
    public void setNormalsProperties(final double aLength, final JColorf aColor, final boolean aAffectChildren) {

        setNormalsLength(aLength);
        setNormalsColor(aColor);

        // propagate changes to children
        if (aAffectChildren) {
            for (int i = 0; i < getChildrens().size(); i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.setNormalsProperties(aLength, aColor, aAffectChildren);
                }
            }
        }
    }

    /**
     * Are vertex colors currently enabled?
     */
    public final boolean getColorsEnabled() {
        return vertexColorsEnabled;
    }

    /**
     * Re-initializes textures and display lists.
     */
    @Override
    public void onDisplayReset(final boolean aAffectChildren) {

        invalidateDisplayList(false);
        if (texture != null) {
            texture.markForUpdate();
        }

        // Use the superclass method to call the same function on the rest of the
        // scene graph...
        super.onDisplayReset(aAffectChildren);
    }

    //-----------------------------------------------------------------------
    // METHODS - COLLISION DETECTION:
    //-----------------------------------------------------------------------
    /**
     * Set up a brute force collision detector for this mesh and (optionally)
     * for its children.
     */
    public void createBruteForceCollisionDetector(boolean aAffectChildren, boolean aUseNeighbors) {
        // delete previous collision detector
        if (collisionDetector != null) {
            collisionDetector = null;
        }

        // create AABB collision detector
        collisionDetector = new JCollisionBrute(triangles);
        collisionDetector.initialize(0.0);

        // create neighbor lists
        if (aUseNeighbors) {
            createTriangleNeighborList(false);
        }

        // update children if required
        if (aAffectChildren) {
            int i;
            for (i = 0; i < getChildrens().size(); i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.createBruteForceCollisionDetector(aAffectChildren,
                            aUseNeighbors);
                }
            }
        }
    }

    /**
     * Set up an AABB collision detector for this mesh and (optionally) its children.
     */
    public void createAABBCollisionDetector(double aRadius, boolean aAffectChildren, boolean aUseNeighbors) {

        // create AABB collision detector
        collisionDetector = new JCollisionAABB(triangles, aUseNeighbors);
        collisionDetector.initialize(aRadius);

        // create neighbor lists
        if (aUseNeighbors) {
            createTriangleNeighborList(false);
        }

        // update children if required
        if (aAffectChildren) {
            int i;
            int n = getChildrens().size();
            for (i = 0; i < n; i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) nextObject;
                if (nextMesh != null) {
                    nextMesh.createAABBCollisionDetector(aRadius,
                            aAffectChildren,
                            aUseNeighbors);
                }
            }
        }
    }

    /**
     * Set up a sphere tree collision detector for this mesh and (optionally)
     * its children.
     */
    public void createSphereTreeCollisionDetector(double aRadius, boolean aAffectChildren, boolean aUseNeighbors) {

        // delete previous collision detector
        if (collisionDetector != null) {
            collisionDetector = null;
        }

        // create sphere tree collision detector
        collisionDetector = new JCollisionSpheres(triangles, aUseNeighbors);
        collisionDetector.initialize(aRadius);

        // create list of neighbors
        if (aUseNeighbors) {
            createTriangleNeighborList(false);
        }

        // update children if required
        if (aAffectChildren) {
            for (int i = 0; i < childrens.size(); i++) {
                JMesh nextMesh = (JMesh) childrens.get(i);
                if (nextMesh != null) {
                    nextMesh.createSphereTreeCollisionDetector(aRadius,
                            aAffectChildren,
                            aUseNeighbors);
                }
            }
        }
    }

    /**
     * Create a lists for neighbor triangls for each triangl of the mesh.
     */
    public void createTriangleNeighborList(boolean aAffectChildren) {
        ArrayList<JTriangle> sort0 = new ArrayList<JTriangle>();
        int i;
        int n = triangles.size();

        for (i = 0; i < n; i++) {
            sort0.add(triangles.get(i));
        }

        ArrayList<JTriangle> sort1 = new ArrayList<JTriangle>(sort0);
        ArrayList<JTriangle> sort2 = new ArrayList<JTriangle>(sort1);

        // Sort each list by the appropriate vertex index
        Collections.sort(sort0, triangleSort0());
        Collections.sort(sort1, triangleSort1());
        Collections.sort(sort2, triangleSort2());

        // Create neighbors for each triangle
        for (i = 0; i < n; i++) {
            // Clear the neighbor array
            triangles.get(i).neighbors.clear();
            // Include each triangle as its own neighbor
            triangles.get(i).neighbors.add(triangles.get(i));
        }

        // Find pairs of triangles for which vertex #0 of one is shared with
        // vertex #0 of the other...
        findNeighbors(sort0, sort0, 0, 0);

        // Find pairs of triangles for which vertex #0 of one is shared with
        // vertex #1 of the other...
        findNeighbors(sort0, sort1, 0, 1);

        // Find pairs of triangles for which vertex #0 of one is shared with
        // vertex #2 of the other...
        findNeighbors(sort0, sort2, 0, 2);

        // Find pairs of triangles for which vertex #1 of one is shared with
        // vertex #1 of the other...
        findNeighbors(sort1, sort1, 1, 1);

        // Find pairs of triangles for which vertex #1 of one is shared with
        // vertex #2 of the other...
        findNeighbors(sort1, sort2, 1, 2);

        // Find pairs of triangles for which vertex #2 of one is shared with
        // vertex #2 of the other...
        findNeighbors(sort2, sort2, 2, 2);

        // update children if required
        if (aAffectChildren) {
            for (i = 0; i < childrens.size(); i++) {

                JMesh nextMesh = (JMesh) childrens.get(i);
                if (nextMesh != null) {
                    nextMesh.createTriangleNeighborList(true);
                }
            }
        }

    }

    /**
     * Search for triangl neighbors.
     */
    public void findNeighbors(ArrayList<JTriangle> search1, ArrayList<JTriangle> search2, final int v1, final int v2) {
        int i = 0;
        int j = 0;

        // In this loop, we want to find triangles from search1 that have vertex #v1
        // (v1 = 0, 1, or 2) equal to vertex #v2 (v2 = 0, 1, or 2) of a triangle from
        // search2.  Since both lists are sorted, we use a merge join.
        // If the x coordinate of vertex #v1 of triangle #i in search1 is the same as
        // the x-coordinate of vertex #v2 of triangle #j in search2 (matching x-
        // coordinates is a necessary but not sufficient condition that the vertices
        // are shared)...
        while ((i < search1.size()) && (j < search2.size())) {
            if (Math.abs((search1).get(i).getVertex(v1).getPosition().getX()
                    - (search2).get(j).getVertex(v2).getPosition().getX()) < 0.0000001) {
                // Keep matching triangles from search1 with triangle #j in search2 as
                // long as the specified vertices' x-coordinates are shared.
                while ((i < search1.size())
                        && (Math.abs((search1).get(i).getVertex(v1).getPosition().getX()
                        - (search2).get(j).getVertex(v2).getPosition().getX()) < 0.0000001)) {
                    int jj = j;

                    // Keep matching triangles from search2 with triangle #i in search1 as
                    // long as the specified vertices' x-coordinates are shared.
                    while ((jj < search2.size())
                            && (Math.abs((search1).get(i).getVertex(v1).getPosition().getX()
                            - (search2).get(jj).getVertex(v2).getPosition().getX()) < 0.0000001)) {
                        // If vertex #v1 of triangle #i in search1 is in fact equal (in x, y, and
                        // z coordinates) to vertex #v2 of triangle #jj in search2, we have
                        // found a pair of neighbors.
                        if (((search1).get(i) != (search2).get(jj))
                                && (JMaths.jEqualPoints((search1).get(i).getVertex(v1).getPosition(), (search2).get(jj).getVertex(v2).getPosition()))) {
                            boolean found = false;
                            int ii;

                            // Check if triangle #jj from search2 has already been inserted into
                            // the neighbors list for triangle #i from search1.
                            for (ii = 0; (!found) && (ii < (search1).get(i).getNeighbors().size()); ii++) {
                                if ((((search1).get(i).getNeighbors())).get(ii) == (search2).get(jj)) {
                                    found = true;
                                }
                            }

                            // If not, add it now.
                            if (!found) {
                                (search1).get(i).getNeighbors().add((search2).get(jj));
                            }

                            // Check if triangle #i from search1 has already been inserted into
                            // the neighbors list for triangle #jj from search2.
                            found = false;
                            for (ii = 0; (!found) && (ii < (search2).get(jj).getNeighbors().size()); ii++) {
                                if ((((search2).get(jj).getNeighbors())).get(ii) == (search1).get(i)) {
                                    found = true;
                                }
                            }

                            // If not, add it now.
                            if (!found) {
                                (search2).get(jj).getNeighbors().add((search1).get(i));
                            }
                        }
                        jj++;
                    }
                    i++;
                }
            } // Since this is a merge join, we increment the counter for the search2 to
            // "catch up to" search1 if the x-coordiante of vertex #v2 of triangle #j
            // from search2 is smaller than that of vertex #v1 of triangle #i from search1...
            else if ((search1).get(i).getVertex(v1).getPosition().getX()
                    > (search2).get(j).getVertex(v2).getPosition().getX()) {
                j++;
            } // And vice versa.
            else if ((search1).get(i).getVertex(v1).getPosition().getX()
                    < (search2).get(j).getVertex(v2).getPosition().getX()) {
                i++;
            }
        }
    }

    /**
     * Compute all triangl normals, optionally propagating the operation to my children.
     */
    public void computeAllNormals(final boolean aAffectChildren) {
        int nvertices = getVertices().size();
        int ntriangls = triangles.size();


        // If we have vertices and we have triangls, compute normals
        // for all triangls
        if (ntriangls != 0 && ntriangls != 0) {

            // used to grab positions of vertices
            ArrayList<JVertex> vertexVector = pVertices();
            // set all normals to zero
            for (int i = 0; i < nvertices; i++) {
                vertexVector.get(i).setNormal(0, 0, 0);
            }


            // compute normals for all triangls.
            for (int i = 0; i < ntriangls; i++) {

                // compute normal ArrayList
                JVector3d normal = new JVector3d(),
                        v01 = new JVector3d(),
                        v02 = new JVector3d();

                JVector3d vertex0 = vertexVector.get(triangles.get(i).getIndexVertex0()).getPosition();
                JVector3d vertex1 = vertexVector.get(triangles.get(i).getIndexVertex1()).getPosition();
                JVector3d vertex2 = vertexVector.get(triangles.get(i).getIndexVertex2()).getPosition();

                vertex1.subr(vertex0, v01);
                vertex2.subr(vertex0, v02);
                v01.crossr(v02, normal);
                double length = normal.length();
                if (length > 0.0000001) {
                    normal.div(length);
                    vertexVector.get(triangles.get(i).getIndexVertex0()).getNormal().add(normal);
                    vertexVector.get(triangles.get(i).getIndexVertex1()).getNormal().add(normal);
                    vertexVector.get(triangles.get(i).getIndexVertex2()).getNormal().add(normal);
                    vertexVector.get(triangles.get(i).getIndexVertex0()).setTriangleCount(vertexVector.get(triangles.get(i).getIndexVertex0()).getTriangleCount() + 1);
                    vertexVector.get(triangles.get(i).getIndexVertex1()).setTriangleCount(vertexVector.get(triangles.get(i).getIndexVertex1()).getTriangleCount() + 1);
                    vertexVector.get(triangles.get(i).getIndexVertex2()).setTriangleCount(vertexVector.get(triangles.get(i).getIndexVertex2()).getTriangleCount() + 1);
                }
            }


            // optionally propagate changes to children
            if (aAffectChildren) {
                int i, numItems;
                numItems = getChildrens().size();
                for (i = 0; i < numItems; i++) {
                    JGenericObject nextObject = getChildrens().get(i);

                    // check if nextobject is a mesh. if yes, apply changes
                    JMesh nextMesh = (JMesh) (nextObject);
                    if (nextMesh != null) {
                        nextMesh.computeAllNormals(aAffectChildren);
                    }
                }
            }

            if (nvertices == 0) {
                return;
            }

            // normalize all normals (After_ doing child-normal computations, in case
            // I share vertices with my children)
            for (int i = 0; i < vertexVector.size(); i++) {
                /*
                 * if (curv.mNTriangles) { curv.mNormal /=
                 * (double)(curv.mNTriangles); }
                 */
                if (vertexVector.get(i).getNormal().lengthsq() > JConstants.CHAI_SMALL) {
                    vertexVector.get(i).getNormal().normalize();
                }
            }
        }
    }

    /**
     * Extrude each vertex of the mesh by some amount along its normal.
     */
    public void extrude(final double aExtrudeDistance, final boolean aAffectChildren,
            final boolean aUpdateCollisionDetector) {
        // update this object
        int vertexcount = vertices.size();
        for (int i = 0; i < vertexcount; i++) {
            vertices.get(i).getLocalPosition().add(JMaths.jMul(aExtrudeDistance, localPosition));
        }

        // This is an O(N) operation, as is the extrusion, so it seems okay to call
        // this by default...
        updateBoundaryBox();

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < getChildrens().size(); i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.extrude(aExtrudeDistance, aAffectChildren, aUpdateCollisionDetector);
                }
            }
        }

        if (aUpdateCollisionDetector == true && collisionDetector != null) {
            collisionDetector.initialize(0);
        }
    }

    /*
     * !
     * Shifts all vertex positions by the specified amount. \n Use setPos() if
     * you want to move the whole mesh for rendering.
     */
    public void offsetVertices(final JVector3d aOffset,
            final boolean aAffectChildren,
            final boolean aUpdateCollisionDetector) {

        // update this object
        int vertexcount = vertices.size();

        for (int i = 0; i < vertexcount; i++) {
            vertices.get(i).getLocalPosition().add(aOffset);
        }

        boundaryBoxMin.add(aOffset);
        boundaryBoxMax.add(aOffset);

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < getChildrens().size(); i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) (nextObject);
                if (nextMesh != null) {
                    nextMesh.offsetVertices(aOffset, aAffectChildren, aUpdateCollisionDetector);
                }
            }
        }

        if (aUpdateCollisionDetector == true && collisionDetector != null) {
            collisionDetector.initialize(0);
        }
    }

    /**
     * Scale vertices and normals by the specified scale factors and re-normalize.
     */
    @Override
    public void scaleObject(final JVector3d aScaleFactors) {
        int i, numItems;
        numItems = vertices.size();

        for (i = 0; i < numItems; i++) {
            vertices.get(i).getLocalPosition().elementMul(aScaleFactors);
            vertices.get(i).getNormal().elementMul(aScaleFactors);
            vertices.get(i).getNormal().normalize();
        }

        boundaryBoxMax.elementMul(aScaleFactors);
        boundaryBoxMin.elementMul(aScaleFactors);
    }

    /**
     * Simple method used to create a new (empty) mesh of my type.
     */
    public final JMesh createMesh() {
        return new JMesh(parentWorld);
    }

    /**
     * Render triangles, material and texture properties.
     */
    public void renderMesh(JChaiRenderMode aRenderMode) {

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        //-----------------------------------------------------------------------
        // INITIALIZATION
        //-----------------------------------------------------------------------
        // check if object contains any triangls or vertices
        if ((vertices.isEmpty()) || (triangles.isEmpty())) {
            return;
        }

        // we are not currently creating a display list
        boolean creatingDisplayList = false;

        //-----------------------------------------------------------------------
        // DISPLAY LIST
        //-----------------------------------------------------------------------
        // Should we render with a display list?
        if (displayListEnabled) {
            // If the display list doesn't exist, create it
            if (displayList == -1) {
                displayList = gl.glGenLists(1);

                if (displayList == -1) {
                    return;
                }

                // On some machines, GL2.GL_COMPILE_AND_EXECUTE totally blows for some reason,
                // so even though it's more complex on the first rendering pass, we use
                // GL2.GL_COMPILE (and Repeat_ the first rendering pass)
                gl.glNewList(displayList, GL2.GL_COMPILE);

                // the list has been created
                creatingDisplayList = true;

                // Go ahead and render; we'll create this list now...
                // we'll make another (recursive) call to renderMesh()
                // at the end of this function.
            } // Otherwise all we have to do is call the display list
            else {
                gl.glCallList(displayList);

                // All done...
                return;
            }
        }
        //-----------------------------------------------------------------------
        // RENDERING WITH VERTEX ARRAYS OR CLASSIC OPENGL CALLS
        //-----------------------------------------------------------------------

        gl.glEnable(GL2.GL_CULL_FACE);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL2.GL_INDEX_ARRAY);
        gl.glDisableClientState(GL2.GL_EDGE_FLAG_ARRAY);

        if (isVertexArraysEnabled()) {
            gl.glEnableClientState(GL2.GL_NORMAL_ARRAY);
            gl.glEnableClientState(GL2.GL_VERTEX_ARRAY);
        }

        /////////////////////////////////////////////////////////////////////////
        // RENDER MATERIAL
        /////////////////////////////////////////////////////////////////////////
        if (materialEnabled) {
            material.render();
        }


        /////////////////////////////////////////////////////////////////////////
        // RENDER VERTEX COLORS
        /////////////////////////////////////////////////////////////////////////
        if (vertexColorsEnabled) {
            // Clear the effects of material properties...
            if (!materialEnabled) {
                float[] fnull = {0, 0, 0, 0};
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_SPECULAR, FloatBuffer.wrap(fnull));
                gl.glMaterialfv(GL2.GL_FRONT_AND_BACK, GL2.GL_EMISSION, FloatBuffer.wrap(fnull));
            }

            // enable vertex colors
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);


            if (vertexArrayEnabled) {
                gl.glEnableClientState(GL2.GL_COLOR_ARRAY);
            }
        } else {
            gl.glDisable(GL2.GL_COLOR_MATERIAL);
            gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        }

        /////////////////////////////////////////////////////////////////////////
        // FOR OBJECTS WITH NO DEFINED COLOR/MATERIAL SETTINGS
        /////////////////////////////////////////////////////////////////////////
        // A default color for objects that don't have vertex colors or
        // material properties (otherwise they're invisible)...
        if ((!vertexColorsEnabled) && (!materialEnabled)) {
            gl.glEnable(GL2.GL_COLOR_MATERIAL);
            gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
            gl.glColor4f(1, 1, 1, 1);
        }

        /////////////////////////////////////////////////////////////////////////
        // TEXTURE
        /////////////////////////////////////////////////////////////////////////
        if ((texture != null) && (textureMappingEnabled)) {
            gl.glEnable(GL2.GL_TEXTURE_2D);
            if (vertexArrayEnabled) {
                gl.glEnableClientState(GL2.GL_TEXTURE_COORD_ARRAY);
            }
            texture.render();
        }


        /////////////////////////////////////////////////////////////////////////
        // RENDER TRIANGLES WITH VERTEX ARRAYS
        /////////////////////////////////////////////////////////////////////////
        if (vertexArrayEnabled) {
            // specify pointers
            //gl.glVertexP

            // variables
            int i;
            int numItems = triangles.size();

            // begin rendering triangls
            gl.glBegin(GL2.GL_TRIANGLES);

            // render all active triangls
            for (i = 0; i < numItems; i++) {
                boolean allocated = triangles.get(i).isAllocated();
                if (allocated) {
                    int index0 = triangles.get(i).getIndexVertex0();
                    int index1 = triangles.get(i).getIndexVertex1();
                    int index2 = triangles.get(i).getIndexVertex2();
                    gl.glArrayElement(index0);
                    gl.glArrayElement(index1);
                    gl.glArrayElement(index2);
                }
            }

            // finalize rendering list of triangls
            gl.glEnd();
        } /////////////////////////////////////////////////////////////////////////
        // RENDER TRIANGLES USING CLASSIC OPENGL COMMANDS
        /////////////////////////////////////////////////////////////////////////
        else {
            // variables
            int i;
            int numItems = triangles.size();

            // begin rendering triangls
            gl.glBegin(GL2.GL_TRIANGLES);

            // render all active triangles
            if ((!textureMappingEnabled) && (!vertexColorsEnabled)) {
                for (i = 0; i < numItems; i++) {
                    // get pointers to vertices
                    JVertex v0 = triangles.get(i).getVertex0();
                    JVertex v1 = triangles.get(i).getVertex1();
                    JVertex v2 = triangles.get(i).getVertex2();

                    // render vertex 0
                    gl.glNormal3d(v0.getNormal().getX(), v0.getNormal().getY(), v0.getNormal().getZ());
                    gl.glVertex3d(v0.getLocalPosition().getX(), v0.getLocalPosition().getY(), v0.getLocalPosition().getZ());

                    // render vertex 1
                    gl.glNormal3d(v1.getNormal().getX(), v1.getNormal().getY(), v1.getNormal().getZ());
                    gl.glVertex3d(v1.getLocalPosition().getX(), v1.getLocalPosition().getY(), v1.getLocalPosition().getZ());

                    // render vertex 2
                    gl.glNormal3d(v2.getNormal().getX(), v2.getNormal().getY(), v2.getNormal().getZ());
                    gl.glVertex3d(v2.getLocalPosition().getX(), v2.getLocalPosition().getY(), v2.getLocalPosition().getZ());
                }
            } else if ((textureMappingEnabled) && (!vertexColorsEnabled)) {
                for (i = 0; i < numItems; i++) {
                    // get pointers to vertices
                    JVertex v0 = triangles.get(i).getVertex0();
                    JVertex v1 = triangles.get(i).getVertex1();
                    JVertex v2 = triangles.get(i).getVertex2();

                    // render vertex 0
                    gl.glNormal3d(v0.getNormal().getX(), v0.getNormal().getY(), v0.getNormal().getZ());
                    gl.glTexCoord2d(v0.getTexCoord().getX(), v0.getTexCoord().getY());
                    gl.glVertex3d(v0.getLocalPosition().getX(), v0.getLocalPosition().getY(), v0.getLocalPosition().getZ());

                    // render vertex 1
                    gl.glNormal3d(v1.getNormal().getX(), v1.getNormal().getY(), v1.getNormal().getZ());
                    gl.glTexCoord2d(v1.getTexCoord().getX(), v1.getTexCoord().getY());
                    gl.glVertex3d(v1.getLocalPosition().getX(), v1.getLocalPosition().getY(), v1.getLocalPosition().getZ());

                    // render vertex 2
                    gl.glNormal3d(v2.getNormal().getX(), v2.getNormal().getY(), v2.getNormal().getZ());
                    gl.glTexCoord2d(v2.getTexCoord().getX(), v2.getTexCoord().getY());
                    gl.glVertex3d(v2.getLocalPosition().getX(), v2.getLocalPosition().getY(), v2.getLocalPosition().getZ());
                }
            } else if ((!textureMappingEnabled) && (vertexColorsEnabled)) {
                for (i = 0; i < numItems; i++) {
                    // get pointers to vertices
                    JVertex v0 = triangles.get(i).getVertex0();
                    JVertex v1 = triangles.get(i).getVertex1();
                    JVertex v2 = triangles.get(i).getVertex2();

                    // render vertex 0
                    gl.glNormal3d(v0.getNormal().getX(), v0.getNormal().getY(), v0.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v0.getColor().getComponents()));
                    gl.glVertex3d(v0.getLocalPosition().getX(), v0.getLocalPosition().getY(), v0.getLocalPosition().getZ());

                    // render vertex 1
                    gl.glNormal3d(v1.getNormal().getX(), v1.getNormal().getY(), v1.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v1.getColor().getComponents()));
                    gl.glVertex3d(v1.getLocalPosition().getX(), v1.getLocalPosition().getY(), v1.getLocalPosition().getZ());

                    // render vertex 2
                    gl.glNormal3d(v2.getNormal().getX(), v2.getNormal().getY(), v2.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v2.getColor().getComponents()));
                    gl.glVertex3d(v2.getLocalPosition().getX(), v2.getLocalPosition().getY(), v2.getLocalPosition().getZ());

                }
            } else if ((textureMappingEnabled) && (vertexColorsEnabled)) {
                for (i = 0; i < numItems; i++) {
                    // get pointers to vertices
                    JVertex v0 = triangles.get(i).getVertex0();
                    JVertex v1 = triangles.get(i).getVertex1();
                    JVertex v2 = triangles.get(i).getVertex2();

                    gl.glNormal3d(v0.getNormal().getX(), v0.getNormal().getY(), v0.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v0.getColor().getComponents()));
                    gl.glTexCoord2d(v0.getTexCoord().getX(), v0.getTexCoord().getY());
                    gl.glVertex3d(v0.getLocalPosition().getX(), v0.getLocalPosition().getY(), v0.getLocalPosition().getZ());

                    // render vertex 1
                    gl.glNormal3d(v1.getNormal().getX(), v1.getNormal().getY(), v1.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v1.getColor().getComponents()));
                    gl.glTexCoord2d(v1.getTexCoord().getX(), v1.getTexCoord().getY());
                    gl.glVertex3d(v1.getLocalPosition().getX(), v1.getLocalPosition().getY(), v1.getLocalPosition().getZ());

                    // render vertex 2
                    gl.glNormal3d(v2.getNormal().getX(), v2.getNormal().getY(), v2.getNormal().getZ());
                    gl.glColor4fv(FloatBuffer.wrap(v1.getColor().getComponents()));
                    gl.glTexCoord2d(v2.getTexCoord().getX(), v2.getTexCoord().getY());
                    gl.glVertex3d(v2.getLocalPosition().getX(), v2.getLocalPosition().getY(), v2.getLocalPosition().getZ());
                }
            }

            // finalize rendering list of triangls
            gl.glEnd();
        }

        //-----------------------------------------------------------------------
        // FINALIZE
        //-----------------------------------------------------------------------

        // restore OpenGL settings to reasonable defaults
        gl.glDisable(GL2.GL_BLEND);
        gl.glDepthMask(true);
        gl.glDisable(GL2.GL_COLOR_MATERIAL);
        gl.glColorMaterial(GL2.GL_FRONT_AND_BACK, GL2.GL_AMBIENT_AND_DIFFUSE);
        gl.glDisable(GL2.GL_TEXTURE_2D);
        gl.glPolygonMode(GL2.GL_FRONT_AND_BACK, GL2.GL_FILL);

        // Turn off any array variables I might have turned on...
        gl.glDisableClientState(GL2.GL_NORMAL_ARRAY);
        gl.glDisableClientState(GL2.GL_VERTEX_ARRAY);
        gl.glDisableClientState(GL2.GL_COLOR_ARRAY);
        gl.glDisableClientState(GL2.GL_TEXTURE_COORD_ARRAY);

        // If we've gotten this far and we're using a display list for rendering,
        // we must be capturing it right now...
        if ((displayListEnabled) && (displayList != -1) && (creatingDisplayList)) {
            // finalize list
            gl.glEndList();

            // Recursively make a call to actually render this object if
            // we didn't use compileAnd_execute
            renderMesh(aRenderMode);
        }
    }

    /**
     * Compute the center of mass of this mesh, based on vertex positions.
     */
    public JVector3d getCenterOfMass(final boolean aIncludeChildren) {
        JVector3d com = new JVector3d(0, 0, 0);
        long nVertices = getVertices().size();

        if (nVertices != 0) {
            for (int curVertex = 0; curVertex < nVertices; curVertex++) {
                JVector3d p = getVertices().get(curVertex).getPosition();
                com.operatorAdd(p);
            }
            com.operatorDiv(nVertices);
        }

        if (aIncludeChildren == true) {
            return com;
        }

        long totalVertices = getNumVertices(true);

        double contribution;
        if (nVertices == 0) {
            contribution = 0.0;
        } else {
            contribution = ((double) nVertices) / ((double) totalVertices);
        }

        //com *= contribution;
        com.operatorMul(contribution);

        int i, numChildren;
        numChildren = getChildrens().size();
        for (i = 0; i < numChildren; i++) {
            JGenericObject nextObject = getChildrens().get(i);

            // check if nextObject is a mesh.
            JMesh nextMesh = (JMesh) (nextObject);
            if (nextMesh != null) {
                // How many vertices does he have?
                long nLocalVertices = nextMesh.getNumVertices(true);

                // What's his contribution to the com?
                JVector3d localCenter = nextMesh.getCenterOfMass(true);
                double localContribution;
                if (nLocalVertices == 0) {
                    localContribution = 0;
                } else {
                    localContribution = (((double) nLocalVertices) / ((double) totalVertices));
                }
                //com = com + (localCenter * localContribution);
                com.operatorAdd(com);
                localCenter.mul(localContribution);
                com.operatorAdd(localCenter);
            } // ...if this child was a mesh
        } // ...for each child

        return com;
    }

    /**
     * Reverse all normals on this model.
     */
    public void reverseAllNormals(final boolean aAffectChildren) {

        // reverse normals for this object
        if (getVertices().size() > 0) {
            ArrayList<JVertex> vertexVector = pVertices();

            for (int i = 0; i < vertexVector.size(); i++) {
                vertexVector.get(i).getNormal().mul(-1.0);
            }
        }

        // propagate changes to my children
        if (aAffectChildren) {
            for (int i = 0; i < getChildrens().size(); i++) {
                JGenericObject nextObject = getChildrens().get(i);

                JMesh nextMesh = (JMesh) nextObject;
                if (nextMesh != null) {
                    nextMesh.reverseAllNormals(true);
                }
            }
        }
    }

    /**
     * Remove redundant triangls from this model.
     */
    public void removeRedundantTriangles(final boolean aAffectChildren) {

        // remove redundant triangles from this mesh

        // move everything from a ArrayList into a set, sorted by triangles,
        // and remove degenerate triangles.
        ArrayList<JTriangle> sortedTris = new ArrayList();
        int ntris = triangles.size();
        for (int i = 0; i < ntris; i++) {
            JTriangle t = triangles.get(i);

            // Remove degenerate triangles
            if (t.getIndexVertex0() == t.getIndexVertex1()
                    || t.getIndexVertex0() == t.getIndexVertex2()
                    || t.getIndexVertex1() == t.getIndexVertex2()) {
                continue;
            }

            // Put this triangle in the sorted list
            sortedTris.add(t);
        }

        // clear the ArrayList
        triangles.clear();

        // move everything back from the set to the ArrayList
        ntris = sortedTris.size();

        for (int i = 0; i < ntris; i++) {
            triangles.add(sortedTris.get(i));
        }

        // clear the set before recursing
        sortedTris.clear();

        // propagate changes to my children
        if (aAffectChildren == false) {
            return;
        }

        for (int i = 0; i < getChildrens().size(); i++) {
            JGenericObject nextObject = getChildrens().get(i);
            JMesh nextMesh = (JMesh) (nextObject);
            if (nextMesh != null) {
                nextMesh.removeRedundantTriangles(true);
            }
        }
    }

    /**
     * Render the mesh itself.
     */
    @Override
    public void render(JChaiRenderMode aRenderMode) {


        // Only render normals on one pass, no matter what the transparency
        // options are...
        if ((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY)
                || (aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_RENDER_ALL)) {
            // render normals
            if (normalsVisible) {
                renderNormals(normalsVisibleForTriangleVerticesOnly);
            }
        }

        /////////////////////////////////////////////////////////////////////////
        // Conditions for mesh to be rendered
        /////////////////////////////////////////////////////////////////////////

        if (((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_NON_TRANSPARENT_ONLY) && (isTransparencyEnabled() == true))
                || ((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_FRONT_ONLY) && (isTransparencyEnabled() == false))
                || ((aRenderMode == JChaiRenderMode.CHAI_RENDER_MODE_TRANSPARENT_BACK_ONLY) && (isTransparencyEnabled() == false))) {
            return;
        }

        // render triangle mesh
        renderMesh(aRenderMode);
    }

    /**
     * Draw a small line for each vertex normal.
     */
    protected void renderNormals(final boolean aTrianglesOnly) {

        // check if any normals to render
        if (vertices.isEmpty()) {
            return;
        }

        GL2 gl = GLContext.getCurrent().getGL().getGL2();
        // disable lighting
        gl.glDisable(GL2.GL_LIGHTING);

        // set line width
        gl.glLineWidth(1.0F);

        // set color
        gl.glColor4fv(FloatBuffer.wrap(getNormalsColor().getComponents()));

        // Where does our vertex array live?
        ArrayList<JVertex> vertexVector = pVertices();

        if (aTrianglesOnly) {

            gl.glBegin(GL2.GL_LINES);

            // render vertex normals
            for (int i = 0; i < triangles.size(); i++) {
                JTriangle nextTriangle = triangles.get(i);
                JVector3d vertex0 = vertexVector.get(nextTriangle.getIndexVertex0()).getLocalPosition();
                JVector3d vertex1 = vertexVector.get(nextTriangle.getIndexVertex1()).getLocalPosition();
                JVector3d vertex2 = vertexVector.get(nextTriangle.getIndexVertex2()).getLocalPosition();

                JVector3d normal0 = vertexVector.get(nextTriangle.getIndexVertex0()).getNormal();
                JVector3d normal1 = vertexVector.get(nextTriangle.getIndexVertex1()).getNormal();
                JVector3d normal2 = vertexVector.get(nextTriangle.getIndexVertex2()).getNormal();
                JVector3d normalPos = new JVector3d(), normal = new JVector3d();

                // render normal 0 of triangle
                gl.glVertex3d(vertex0.getX(), vertex0.getY(), vertex0.getZ());
                normal0.mulr(getNormalsLength(), normal);
                vertex0.addr(normal, normalPos);
                gl.glVertex3d(normalPos.getX(), normalPos.getY(), normalPos.getZ());

                // render normal 1 of triangle
                gl.glVertex3d(vertex1.getX(), vertex1.getY(), vertex1.getZ());
                normal1.mulr(getNormalsLength(), normal);
                vertex1.addr(normal, normalPos);
                gl.glVertex3d(normalPos.getX(), normalPos.getY(), normalPos.getZ());

                // render normal 2 of triangle
                gl.glVertex3d(vertex2.getX(), vertex2.getY(), vertex2.getZ());
                normal2.mulr(getNormalsLength(), normal);
                vertex2.addr(normal, normalPos);
                gl.glVertex3d(normalPos.getX(), normalPos.getY(), normalPos.getZ());
            }

            gl.glEnd();

        } else {

            int nvertices = getVertices().size();
            gl.glBegin(GL2.GL_LINES);
            for (int i = 0; i < nvertices; i++) {

                if (!vertices.get(i).isAllocated()) {
                    continue;
                }
                JVector3d v = getVertices().get(i).getLocalPosition();
                JVector3d n = getVertices().get(i).getNormal();

                // render normal 0 of triangle
                gl.glVertex3d(v.getX(), v.getY(), v.getZ());
                n.mul(getNormalsLength());
                n.add(v);
                gl.glVertex3d(n.getX(), n.getY(), n.getZ());
            }
            gl.glEnd();
        }


        // enable lighting
        gl.glEnable(GL2.GL_LIGHTING);
    }

    /**
     * Update the gl.global position of each of my vertices.
     */
    public void updateGlobalPositions() {
        updateGlobalPositions(false);
    }

    /**
     * Update the gl.global position of each of my vertices.
     */
    @Override
    public void updateGlobalPositions(final boolean aFrameOnly) {
        if (aFrameOnly) {
            return;
        }

        int i, numVertices;
        numVertices = getVertices().size();
        for (i = 0; i < numVertices; i++) {
            vertices.get(i).computeGlobalPosition(globalPosition, globalRotation);
        }
    }

    /**
     * Update my boundary box dimensions based on my vertices.
     */
    @Override
    public void updateBoundaryBox() {

        if (triangles.isEmpty()) {
            boundaryBoxMin.zero();
            boundaryBoxMax.zero();
            return;
        }

        double xMin = JConstants.CHAI_LARGE;
        double yMin = JConstants.CHAI_LARGE;
        double zMin = JConstants.CHAI_LARGE;
        double xMax = -JConstants.CHAI_LARGE;
        double yMax = -JConstants.CHAI_LARGE;
        double zMax = -JConstants.CHAI_LARGE;

        // Where does our vertex array live?
        ArrayList<JVertex> vertexVector = pVertices();
        if (vertexVector == null) {
            return;
        }

        // loop over all my triangles
        for (int i = 0; i < triangles.size(); i++) {
            // get next triangle
            JTriangle nextTriangle = triangles.get(i);

            if (nextTriangle.isAllocated()) {
                JVector3d tVertex0 = vertexVector.get(nextTriangle.getIndexVertex0()).getLocalPosition();
                xMin = JMaths.jMin(tVertex0.getX(), xMin);
                yMin = JMaths.jMin(tVertex0.getY(), yMin);
                zMin = JMaths.jMin(tVertex0.getZ(), zMin);
                xMax = JMaths.jMax(tVertex0.getX(), xMax);
                yMax = JMaths.jMax(tVertex0.getY(), yMax);
                zMax = JMaths.jMax(tVertex0.getZ(), zMax);

                JVector3d tVertex1 = vertexVector.get(nextTriangle.getIndexVertex1()).getLocalPosition();
                xMin = JMaths.jMin(tVertex1.getX(), xMin);
                yMin = JMaths.jMin(tVertex1.getY(), yMin);
                zMin = JMaths.jMin(tVertex1.getZ(), zMin);
                xMax = JMaths.jMax(tVertex1.getX(), xMax);
                yMax = JMaths.jMax(tVertex1.getY(), yMax);
                zMax = JMaths.jMax(tVertex1.getZ(), zMax);

                JVector3d tVertex2 = vertexVector.get(nextTriangle.getIndexVertex2()).getLocalPosition();
                xMin = JMaths.jMin(tVertex2.getX(), xMin);
                yMin = JMaths.jMin(tVertex2.getY(), yMin);
                zMin = JMaths.jMin(tVertex2.getZ(), zMin);
                xMax = JMaths.jMax(tVertex2.getX(), xMax);
                yMax = JMaths.jMax(tVertex2.getY(), yMax);
                zMax = JMaths.jMax(tVertex2.getZ(), zMax);
            }
        }


        if (triangles.size() > 0) {
            boundaryBoxMin.set(xMin, yMin, zMin);
            boundaryBoxMax.set(xMax, yMax, zMax);
        } else {
            boundaryBoxMin.zero();
            boundaryBoxMax.zero();
        }
    }

    /**
     * @return the showNormals
     */
    public boolean isNormalsVisible() {
        return normalsVisible;
    }

    /**
     * @param showNormals the showNormals to set
     */
    public void setNormalsVisible(boolean showNormals) {
        setNormalsVisible(showNormals, true);
    }

    /**
     * @param showNormals the showNormals to set
     */
    public void setNormalsVisible(boolean showNormals, boolean affectChildren) {
        this.normalsVisible = showNormals;


        if (affectChildren) {
            for (JGenericObject child : childrens) {
                if (child instanceof JMesh) {
                    ((JMesh) child).setNormalsVisible(showNormals, true);
                }
            }
        }
    }

    /**
     * @return the showNormalsForTriangleVerticesOnly
     */
    public boolean isNormalsVisibleForTriangleVerticesOnly() {
        return normalsVisibleForTriangleVerticesOnly;
    }

    /**
     * @param showNormalsForTriangleVerticesOnly the
     * showNormalsForTriangleVerticesOnly to set
     */
    public void setNormalsVisibleForTriangleVerticesOnly(boolean showNormalsForTriangleVerticesOnly) {
        this.normalsVisibleForTriangleVerticesOnly = showNormalsForTriangleVerticesOnly;
    }

    /**
     * @return the showNormalsColor
     */
    public JColorf getNormalsColor() {
        return normalsColor;
    }

    /**
     * @param showNormalsColor the showNormalsColor to set
     */
    public void setNormalsColor(JColorf showNormalsColor) {
        this.normalsColor = showNormalsColor;
    }

    /**
     * @return the showNormalsLength
     */
    public double getNormalsLength() {
        return normalsLength;
    }

    /**
     * @param showNormalsLength the showNormalsLength to set
     */
    public void setNormalsLength(double showNormalsLength) {
        this.normalsLength = showNormalsLength;
    }

    /**
     * @return the useDisplayList
     */
    public boolean isUseDisplayList() {
        return displayListEnabled;
    }

    /**
     * @param useDisplayList the useDisplayList to set
     */
    public void setDisplayListEnabled(boolean useDisplayList) {
        setDisplayListEnabled(useDisplayList, true);
    }

    /**
     * @param useDisplayList the useDisplayList to set
     */
    public void setDisplayListEnabled(boolean useDisplayList, boolean affectChildren) {
        this.displayListEnabled = useDisplayList;
        if (affectChildren) {
            for (JGenericObject child : childrens) {
                if (child instanceof JMesh) {
                    ((JMesh) child).setDisplayListEnabled(useDisplayList, true);
                }
            }
        }
    }

    /**
     * @return the useVertexArrays
     */
    public boolean isVertexArraysEnabled() {
        return vertexArrayEnabled;
    }

    /**
     * @param useVertexArrays the useVertexArrays to set
     */
    public void setVertexArraysEnabled(boolean useVertexArrays) {
        setVertexArraysEnabled(useVertexArrays, true);
    }

    /**
     * @param useVertexArrays the useVertexArrays to set
     */
    public void setVertexArraysEnabled(boolean useVertexArrays, boolean affectChildren) {
        this.vertexArrayEnabled = useVertexArrays;
        if (affectChildren) {
            for (JGenericObject child : childrens) {
                if (child instanceof JMesh) {
                    ((JMesh) child).setVertexArraysEnabled(useVertexArrays, true);
                }
            }
        }
    }

    /**
     * @return the displayList
     */
    public int getDisplayList() {
        return displayList;
    }

    /**
     * @param displayList the displayList to set
     */
    public void setDisplayList(int displayList) {
        this.displayList = displayList;
    }

    /**
     * @return the vertices
     */
    public ArrayList<JVertex> getVertices() {
        return vertices;
    }

    /**
     * @param vertices the vertices to set
     */
    public void setVertices(ArrayList<JVertex> vertices) {
        this.vertices = vertices;
    }

    /**
     * @return the freeVertices
     */
    public ArrayList<Integer> getFreeVertices() {
        return freeVertices;
    }

    /**
     * @param freeVertices the freeVertices to set
     */
    public void setFreeVertices(ArrayList<Integer> freeVertices) {
        this.freeVertices = freeVertices;
    }

    /**
     * @return the triangles
     */
    public ArrayList<JTriangle> getTriangles() {
        return triangles;
    }

    /**
     * @param triangles the triangles to set
     */
    public void setTriangles(ArrayList<JTriangle> triangles) {
        if (triangles != null && !triangles.isEmpty()) {
            this.triangles.clear();
            this.triangles.ensureCapacity(triangles.size());
            for (int i = 0; i < triangles.size(); i++) {
                JTriangle t = triangles.get(i);
                t.setParent(this);
                this.triangles.add(t);
            }
        }
    }

    /**
     * @return the freeTriangles
     */
    public ArrayList<Integer> getFreeTriangles() {
        return freeTriangles;
    }

    /**
     * @param freeTriangles the freeTriangles to set
     */
    public void setFreeTriangles(ArrayList<Integer> freeTriangles) {
        this.freeTriangles = freeTriangles;
    }

    /**
     *
     * @return
     */
    private static Comparator<JTriangle> triangleSort0() {
        return new TriangleSort(0);
    }

    /**
     *
     * @return
     */
    private static Comparator<JTriangle> triangleSort1() {
        return new TriangleSort(1);
    }

    /**
     *
     * @return
     */
    private static Comparator<JTriangle> triangleSort2() {
        return new TriangleSort(2);
    }
}

/**
 *
 * On CHAI3D original code, there is a function that compares two triangles.
 *
 * In JCHAI3D, we need to create a comparator to do this job.
 *
 * The compare method compare two triangles and verify if the x coordinates of
 * t1 is less, equals or greater then t2.
 *
 * @author Marcos da Silva Ramos
 */
class TriangleSort implements Comparator<JTriangle> {

    /**
     * The vertex to be compared
     */
    int vertex;

    /**
     *
     * @param vertex
     */
    public TriangleSort(int vertex) {
        this.vertex = vertex;
    }

    /**
     * Compare the x component of the given vertices from two triangles.
     *
     * @param t1 the first triangle
     * @param t2 the second triangle
     * @return positive value, t1.vertex.x < t2.vertex.x; zero, if t1.vertex.x
     * == t2.vertex.x; negative value if t1.vertex.x > t2.vertex.x
     */
    public int compare(JTriangle t1, JTriangle t2) {
        return (int) (t2.getVertex(vertex).getPosition().x - t1.getVertex(vertex).getPosition().x);
    }
}
