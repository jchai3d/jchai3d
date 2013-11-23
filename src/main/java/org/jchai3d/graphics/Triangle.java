package org.jchai3d.graphics;

import org.jchai3d.math.TriangleVertex;
import org.jchai3d.math.geom.Geometry;

/**
 * The Triangle class holds three indexes to vertices that are stored in a 
 * geometry.
 * 
 * @author Marcos da Silva Ramos
 */
public class Triangle {
    
    /**
     * Index for the vertex 0 in geometry
     */
    protected int vertexIndex0;
    
    /**
     * Index for the vertex 1 in geometry
     */
    protected int vertexIndex1;
    
    /**
     * Index for the vertex 2 in geometry
     */
    protected int vertexIndex2;
    
    /**
     * Referenced {@link Geometry} object that hold this triangle.
     */
    protected Geometry geometry;

    /**
     * Creates a new triangle with given vertex indices and referenced geometry.
     * 
     * @param vertexIndex0 the index of vertex 0 in geometry
     * @param vertexIndex1 the index of vertex 1 in geometry
     * @param vertexIndex2 the index of vertex 2 in geometry
     * @param geometry a referenced geometry for this triangle
     */
    public Triangle(int vertexIndex0, int vertexIndex1, int vertexIndex2, Geometry geometry) {
        this.vertexIndex0 = vertexIndex0;
        this.vertexIndex1 = vertexIndex1;
        this.vertexIndex2 = vertexIndex2;
        this.geometry = geometry;
    }

    /**
     * Creates a new triangle with no geometry and all indices set as -1.
     */
    public Triangle() {
        setGeometry(null);
    }
    
    /**
     * Access the vertex 0 in the Geometry of this triangle.
     * @return a reference to the vertex 0 of this triangle in the geometry
     * object.
     */
    public TriangleVertex getVertex0() {
        return geometry.getVertex(vertexIndex0);
    }
    
    /**
     * Access the vertex 1 in the Geometry of this triangle.
     * @return a reference to the vertex 1 of this triangle in the geometry
     * object.
     */
    public TriangleVertex getVertex1() {
        return geometry.getVertex(vertexIndex0);
    }
    
    /**
     * Access the vertex 2 in the Geometry of this triangle.
     * @return a reference to the vertex 2 of this triangle in the geometry
     * object.
     */
    public TriangleVertex getVertex2() {
        return geometry.getVertex(vertexIndex0);
    }

    /**
     * @return the vertexIndex0
     */
    public int getVertexIndex0() {
        return vertexIndex0;
    }

    /**
     * @param vertexIndex0 the vertexIndex0 to set
     */
    public void setVertexIndex0(int vertexIndex0) {
        this.vertexIndex0 = vertexIndex0;
    }

    /**
     * @return the vertexIndex1
     */
    public int getVertexIndex1() {
        return vertexIndex1;
    }

    /**
     * @param vertexIndex1 the vertexIndex1 to set
     */
    public void setVertexIndex1(int vertexIndex1) {
        this.vertexIndex1 = vertexIndex1;
    }

    /**
     * @return the vertexIndex2
     */
    public int getVertexIndex2() {
        return vertexIndex2;
    }

    /**
     * @param vertexIndex2 the vertexIndex2 to set
     */
    public void setVertexIndex2(int vertexIndex2) {
        this.vertexIndex2 = vertexIndex2;
    }

    /**
     * @return the geometry
     */
    public Geometry getGeometry() {
        return geometry;
    }

    /**
     * @param geometry the geometry to set
     */
    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
        
        if(this.geometry == null) {
            this.vertexIndex0 = -1;
            this.vertexIndex1 = -1;
            this.vertexIndex2 = -1;
        }
    }
    
    
}
